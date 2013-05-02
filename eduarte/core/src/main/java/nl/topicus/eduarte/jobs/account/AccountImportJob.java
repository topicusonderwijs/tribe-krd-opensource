package nl.topicus.eduarte.jobs.account;

import java.util.ArrayList;
import java.util.List;

import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.dao.helpers.SessionDataAccessHelper;
import nl.topicus.cobra.mail.EmailDao;
import nl.topicus.cobra.mail.VelocityEmailBuilder;
import nl.topicus.cobra.quartz.JobInfo;
import nl.topicus.cobra.util.PasswordGenerator;
import nl.topicus.cobra.util.StringUtil;
import nl.topicus.cobra.util.TimeUtil;
import nl.topicus.eduarte.app.mail.EduArteMailDao;
import nl.topicus.eduarte.app.util.importeren.accounts.AccountsImporterenFile;
import nl.topicus.eduarte.app.util.importeren.accounts.IdentificerendeKolom;
import nl.topicus.eduarte.app.util.importeren.accounts.ImportSettings;
import nl.topicus.eduarte.dao.helpers.AccountDataAccessHelper;
import nl.topicus.eduarte.dao.helpers.MedewerkerDataAccessHelper;
import nl.topicus.eduarte.dao.helpers.RolDataAccessHelper;
import nl.topicus.eduarte.entities.jobs.AccountsImportJobRun;
import nl.topicus.eduarte.entities.jobs.JobRunClass;
import nl.topicus.eduarte.entities.jobs.logging.JobRunDetail;
import nl.topicus.eduarte.entities.personen.Medewerker;
import nl.topicus.eduarte.entities.security.authentication.Account;
import nl.topicus.eduarte.entities.security.authentication.AccountRol;
import nl.topicus.eduarte.entities.security.authentication.MedewerkerAccount;
import nl.topicus.eduarte.entities.security.authorization.Rol;
import nl.topicus.eduarte.jobs.EduArteJob;

import org.quartz.JobExecutionContext;

/**
 * Achtergrond taak voor het importeren van een vasco licentie bestand waarmee de Vasco
 * tokens in de database worden geladen.
 */
@JobInfo(name = AccountImportJob.JOB_NAME)
@JobRunClass(AccountsImportJobRun.class)
public class AccountImportJob extends EduArteJob
{
	public static final String JOB_NAME = "Accounts importeren";

	private static final int AANTAL_VELDEN = 5;

	private ImportSettings settings;

	private List<String> meldingen;

	private int aantalVerwerkt = 0;

	private int aantalToegevoegd = 0;

	private int aantalBijgewerkt = 0;

	private int aantalOngeldigeRegels = 0;

	private int aantalOnbekendeMedewerkers = 0;

	private int regelnummer = 2;

	@Override
	protected void executeJob(JobExecutionContext context) throws InterruptedException
	{
		AccountsImportJobRun run = new AccountsImportJobRun();
		run.setGestartDoor(getMedewerker());
		run.setRunStart(getDatumTijdOpgestart());
		run.setSamenvatting("Het importeren van accounts is gestart");
		run.saveOrUpdate();

		this.settings = getImportSetting(context);
		AccountsImporterenFile file = getValue(context, "bestand");
		importeerAccounts(file.getLines());

		for (String melding : meldingen)
		{
			JobRunDetail detail = new JobRunDetail(run);
			detail.setUitkomst(melding);
			run.getDetails().add(detail);
			detail.save();
		}

		run
			.setSamenvatting(String
				.format(
					"Het importeren van accounts is voltooid. Er zijn %d regels verwerkt, %d bijgewerkt, %d toegevoegd en %d ongeldige regels",
					aantalVerwerkt, aantalBijgewerkt, aantalToegevoegd, aantalOngeldigeRegels));
		run.setRunEinde(TimeUtil.getInstance().currentDateTime());
		run.saveOrUpdate();
		run.commit();
	}

	private ImportSettings getImportSetting(JobExecutionContext context)
	{
		ImportSettings ret = new ImportSettings();
		ret.setEmailVersturen((Boolean) getValue(context, "emailVersturen"));
		ret
			.setIdentificerendeKolom((IdentificerendeKolom) getValue(context,
				"identificerendeKolom"));
		ret.setWachtwoordGenereren((Boolean) getValue(context, "wachtwoordGenereren"));
		return ret;
	}

	public void importeerAccounts(List<String> lines) throws InterruptedException
	{
		SessionDataAccessHelper sessionHelper =
			DataAccessRegistry.getHelper(SessionDataAccessHelper.class);
		meldingen = new ArrayList<String>();
		for (String line : lines)
		{
			verwerkLine(line);
			regelnummer++;
			if (regelnummer % 50 == 0)
			{
				// Doe een flush en een clear.
				sessionHelper.getHibernateSessionProvider().getSession().flush();
				sessionHelper.clearSession();
			}
			setProgress(regelnummer, lines.size());
		}
	}

	private void verwerkLine(String line)
	{
		String[] velden = line.split(";", -1);
		if (validateVelden(velden))
		{
			// Haal de medewerker op.
			Medewerker medewerker = getMedewerker(velden);
			// Controleer of deze medewerker al een account heeft.
			if (medewerker != null)
			{
				String wachtwoord = null;
				boolean bijgewerkt = false;
				Account account =
					DataAccessRegistry.getHelper(AccountDataAccessHelper.class).get(medewerker);
				if (account == null)
				{
					// Nog geen account. Maak een nieuwe aan.
					account = new MedewerkerAccount(medewerker);
					account.setGebruikersnaam(velden[1]);
					if (settings.isWachtwoordGenereren())
					{
						wachtwoord = genereerWachtwoord();
					}
					else
					{
						wachtwoord = velden[2];
					}
					account.setWachtwoord(wachtwoord);
					account.setActief(true);
					aantalToegevoegd++;
					meldingen.add("Account toegevoegd voor: "
						+ medewerker.getPersoon().getVolledigeNaam());
				}
				else
				{
					// update gebruikersnaam en wachtwoord van bestaande account met die
					// uit het csv bestand
					String gebruikersnaam = velden[1];
					if (!account.getGebruikersnaam().equals(gebruikersnaam))
					{
						account.setGebruikersnaam(gebruikersnaam);
						bijgewerkt = true;
					}

					wachtwoord = velden[2];
					String huidigeEncryptedWachtwoord = account.getEncryptedWachtwoord();
					account.setWachtwoord(wachtwoord);
					if (!huidigeEncryptedWachtwoord.equals(account.getEncryptedWachtwoord()))
					{
						bijgewerkt = true;
					}
				}

				String rollenString = velden[4];
				if (StringUtil.isNotEmpty(rollenString))
				{
					RolDataAccessHelper rolHelper =
						DataAccessRegistry.getHelper(RolDataAccessHelper.class);
					String[] rollen = rollenString.split(",", -1);
					for (String rolnaam : rollen)
					{
						rolnaam = rolnaam.trim();
						if (!account.heeftRol(rolnaam))
						{
							Rol rol = rolHelper.getRol(rolnaam);
							if (rol != null)
							{
								AccountRol newRol = new AccountRol();
								newRol.setAccount(account);
								newRol.setRol(rol);
								account.getRoles().add(newRol);
								bijgewerkt = true;
							}
							else
							{
								meldingen.add("Onbekende rol gevonden op regel nummer: "
									+ regelnummer + ": " + rolnaam);
							}
						}
					}
				}
				if (bijgewerkt && account.isSaved())
				{
					aantalBijgewerkt++;
					meldingen.add("Account bijgewerkt van: "
						+ medewerker.getPersoon().getVolledigeNaam());
				}
				boolean nieuw = !account.isSaved();
				if (nieuw)
				{
					account.setAuthorisatieNiveau(account.berekenAuthorisatieNiveau());
				}
				account.saveOrUpdate();
				for (AccountRol curRol : account.getRoles())
					curRol.saveOrUpdate();
				if (settings.isEmailVersturen())
				{
					// Verstuur een email.
					if (StringUtil.isNotEmpty(velden[3]))
						verstuurEmail(account, wachtwoord, velden[3]);
					else if (medewerker.getPersoon().getEersteEmailAdres() != null)
						verstuurEmail(account, wachtwoord, medewerker.getPersoon()
							.getEersteEmailAdres().getContactgegeven());
				}
				aantalVerwerkt++;
			}
			else
			{
				aantalOnbekendeMedewerkers++;
				meldingen.add("Onbekende medewerker gevonden op regel nummer: " + regelnummer
					+ ": " + velden[0]);
			}
		}
		else
		{

		}
	}

	private String genereerWachtwoord()
	{
		return PasswordGenerator.generatePassword(11);
	}

	private boolean validateVelden(String[] velden)
	{
		if (velden.length != AANTAL_VELDEN)
		{
			aantalOngeldigeRegels++;
			meldingen.add("Ongeldige regel: " + regelnummer);
			return false;
		}
		if (StringUtil.isEmpty(velden[1]))
		{
			aantalOngeldigeRegels++;
			meldingen.add("Geen gebruikersnaam: " + regelnummer);
			return false;
		}
		if (!settings.isWachtwoordGenereren() && StringUtil.isEmpty(velden[2]))
		{
			aantalOngeldigeRegels++;
			meldingen.add("Geen wachtwachtwoord: " + regelnummer);
			return false;
		}
		return true;
	}

	private Medewerker getMedewerker(String[] velden)
	{
		if (StringUtil.isEmpty(velden[0]))
		{
			return null;
		}
		if (settings.getIdentificerendeKolom() == IdentificerendeKolom.Nummer)
		{
			String idString = velden[0];
			if (!StringUtil.isNumeric(idString))
			{
				return null;
			}
			Long id = Long.valueOf(idString);
			return DataAccessRegistry.getHelper(MedewerkerDataAccessHelper.class).get(
				Medewerker.class, id);
		}
		if (settings.getIdentificerendeKolom() == IdentificerendeKolom.Roostercode)
		{
			return DataAccessRegistry.getHelper(MedewerkerDataAccessHelper.class)
				.batchGetByAfkorting(velden[0]);
		}
		return null;
	}

	private void verstuurEmail(Account account, String wachtwoord, String email)
	{
		try
		{
			EmailDao dao = new EduArteMailDao();
			VelocityEmailBuilder builder = (VelocityEmailBuilder) dao.createEmailBuilder();
			builder.setTemplateName("accountgegevens.vm");
			builder.put("account", account);
			builder.put("wachtwoord", wachtwoord);

			builder.addRecipient(account.getEigenaar().getVolledigeNaam(), email);
			builder.setSubject("Accountgegevens Tribe KRD");
			dao.send(builder.buildMessage());
		}
		catch (Exception e)
		{
			meldingen.add("Fout bij het versturen van e-mail voor regel nummer " + regelnummer
				+ ": " + e.getLocalizedMessage());
		}

	}
}
