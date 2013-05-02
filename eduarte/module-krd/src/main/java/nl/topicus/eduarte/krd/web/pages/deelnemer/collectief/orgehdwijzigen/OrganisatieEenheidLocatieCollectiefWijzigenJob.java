package nl.topicus.eduarte.krd.web.pages.deelnemer.collectief.orgehdwijzigen;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.dao.helpers.SessionDataAccessHelper;
import nl.topicus.cobra.io.StringPrintWriter;
import nl.topicus.cobra.quartz.JobInfo;
import nl.topicus.cobra.util.TimeUtil;
import nl.topicus.eduarte.dao.helpers.SettingsDataAccessHelper;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis.VerbintenisStatus;
import nl.topicus.eduarte.entities.jobs.JobRunClass;
import nl.topicus.eduarte.entities.organisatie.Locatie;
import nl.topicus.eduarte.entities.organisatie.OrganisatieEenheid;
import nl.topicus.eduarte.entities.personen.Deelnemer;
import nl.topicus.eduarte.entities.settings.MutatieBlokkedatumSetting;
import nl.topicus.eduarte.jobs.EduArteJob;
import nl.topicus.eduarte.krd.web.pages.deelnemer.collectief.CollectieveStatusovergangJobDataMap;
import nl.topicus.eduarte.krd.web.pages.deelnemer.collectief.VerbintenisCollectiefBlokkadedatumValidator;
import nl.topicus.eduarte.krd.web.pages.deelnemer.collectief.VerbintenisCollectiefValidator;
import nl.topicus.eduarte.krd.web.pages.deelnemer.verbintenis.BlokkadedatumValidatorMode;

import org.hibernate.FlushMode;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/*
 * @author idserda
 */
@JobInfo(name = OrganisatieEenheidLocatieCollectiefWijzigenJob.JOB_NAME)
@JobRunClass(OrganisatieEenheidLocatieCollectiefWijzigenJobRun.class)
public class OrganisatieEenheidLocatieCollectiefWijzigenJob extends EduArteJob
{
	public static final String JOB_NAME = "Collectief organisatie-eenheid en locatie wijzigen";

	private OrganisatieEenheidLocatieCollectiefWijzigenJobRun run;

	private List<String> validationErrors = new ArrayList<String>();

	private List<Long> succesvol = new ArrayList<Long>();

	private List<Long> mislukt = new ArrayList<Long>();

	private OrganisatieEenheid organisatieEenheid;

	private Locatie locatie;

	private Date verbintenisBlokkadedatum;

	@Override
	protected void executeJob(JobExecutionContext context) throws JobExecutionException,
			InterruptedException
	{
		setFlushMode(FlushMode.COMMIT);
		try
		{
			run = new OrganisatieEenheidLocatieCollectiefWijzigenJobRun();
			run.setGestartDoor(getMedewerker());
			run.setRunStart(TimeUtil.getInstance().currentDateTime());

			extractData(context);

			run.setSamenvatting("Verplaatsen naar organisatie-eenheid: "
				+ organisatieEenheid.toString() + " en locatie: " + locatie.toString() + ". ");
			run.save();

			List<Verbintenis> selectie = getValue(context, "selectie");

			wijzigVerbintenis(selectie);

			run.setRunEinde(TimeUtil.getInstance().currentDateTime());
			run.setSamenvatting(run.getSamenvatting() + "Totaal: " + selectie.size()
				+ ", succesvol:  " + succesvol.size() + ", mislukt: " + mislukt.size());
			run.update();
			run.commit();
			flushAndClearHibernate();
		}
		catch (InterruptedException e)
		{
			log.error("Job was interrupted: " + e.getMessage(), e);
			throw e;
		}
		catch (Exception e)
		{
			failJob(e);
			throw new JobExecutionException(e);
		}
	}

	private void wijzigVerbintenis(List<Verbintenis> selectie) throws InterruptedException
	{
		int count = 0;
		for (Verbintenis entiteit : selectie)
		{
			if (wijzigigenMogelijk(entiteit))
			{
				updateEntiteit(entiteit);
				succesvol.add(entiteit.getId());
				createJobRunDetail(createDeelnemerOmschrijving(entiteit)
					+ " Statusovergang geslaagd voor verbintenis " + entiteit.getOmschrijving());
			}
			else
			{
				mislukt.add(entiteit.getId());
			}
			setProgress(count, selectie.size());
			count++;

			flushHibernate();
		}
	}

	private void updateEntiteit(Verbintenis entiteit)
	{
		entiteit.setOrganisatieEenheid(organisatieEenheid);
		entiteit.setLocatie(locatie);

		entiteit.saveOrUpdate();
	}

	private void extractData(JobExecutionContext context)
	{
		organisatieEenheid = getValue(context, "organisatieEenheid");
		locatie = getValue(context, "locatie");
	}

	private boolean wijzigigenMogelijk(Verbintenis entiteit)
	{
		checkVerplichteVeldenEnBron(entiteit);
		checkBlokkadeDatum(entiteit);

		if (!validationErrors.isEmpty())
		{
			createJobRunDetailMetErrors();
			return false;
		}
		else
		{
			return true;
		}
	}

	private void checkVerplichteVeldenEnBron(Verbintenis entiteit)
	{
		addErrors(new VerbintenisCollectiefValidator(entiteit, entiteit.getStatus()).validate());
	}

	private void checkBlokkadeDatum(Verbintenis verbintenis)
	{
		if (getBlokkadedatum() != null)
			addErrors(new VerbintenisCollectiefBlokkadedatumValidator(verbintenis,
				getBlokkadedatum(), BlokkadedatumValidatorMode.Bewerken).validate());
	}

	private void createJobRunDetail(String error)
	{
		OrganisatieEenheidLocatieCollectiefWijzigenJobRunDetail detail =
			new OrganisatieEenheidLocatieCollectiefWijzigenJobRunDetail(run);
		detail.setUitkomst(error);
		detail.save();
	}

	@SuppressWarnings("unused")
	private CollectieveStatusovergangJobDataMap<VerbintenisStatus> getDataMap()
	{
		return null;
	}

	private Date getBlokkadedatum()
	{
		if (verbintenisBlokkadedatum == null)
		{
			MutatieBlokkedatumSetting setting =
				DataAccessRegistry.getHelper(SettingsDataAccessHelper.class).getSetting(
					MutatieBlokkedatumSetting.class);

			if (setting != null && setting.getValue() != null)
				verbintenisBlokkadedatum = setting.getValue().getBlokkadedatumVerbintenis();
		}
		return verbintenisBlokkadedatum;
	}

	private void createJobRunDetailMetErrors()
	{
		for (String error : validationErrors)
		{
			createJobRunDetail(error);
		}
		validationErrors.clear();
	}

	@SuppressWarnings("unused")
	private void addError(String newValidationError)
	{
		validationErrors.add(newValidationError);
	}

	private void addErrors(List<String> newValidationErrors)
	{
		validationErrors.addAll(newValidationErrors);
	}

	private String createDeelnemerOmschrijving(Verbintenis verbintenis)
	{
		Deelnemer deelnemer = verbintenis.getDeelnemer();

		String msg =
			"Deelnemer: " + deelnemer.getPersoon().getAchternaam() + ", "
				+ deelnemer.getPersoon().getVoornamen() + " (" + deelnemer.getDeelnemernummer()
				+ "). ";
		return msg;
	}

	private void setFlushMode(FlushMode flushmode)
	{
		SessionDataAccessHelper sessionDAH =
			DataAccessRegistry.getHelper(SessionDataAccessHelper.class);
		sessionDAH.getHibernateSessionProvider().getSession().setFlushMode(flushmode);
	}

	void failJob(Exception e)
	{
		log.error("Organisatie-eenheid/locatie wijzigen mislukt");

		run
			.setSamenvatting("Organisatie-eenheid/locatie wijzigen mislukt, klik voor meer informatie");
		run.setRunEinde(TimeUtil.getInstance().currentDateTime());
		setStatus("Organisatie-eenheid/locatie wijzigen mislukt");

		StringPrintWriter spw = new StringPrintWriter();
		e.printStackTrace(spw);
		run.error("Organisatie-eenheid/locatie wijzigen is mislukt door een onverwachte fout:\n"
			+ spw.getString());

		run.saveOrUpdate();
		run.commit();
	}

}