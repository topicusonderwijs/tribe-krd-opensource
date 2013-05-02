package nl.topicus.eduarte.app.util.importeren.medewerkers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.dao.helpers.SessionDataAccessHelper;
import nl.topicus.cobra.types.personalia.Geslacht;
import nl.topicus.cobra.util.JavaUtil;
import nl.topicus.cobra.util.StringUtil;
import nl.topicus.cobra.util.TimeUtil;
import nl.topicus.eduarte.dao.helpers.LocatieDataAccessHelper;
import nl.topicus.eduarte.dao.helpers.MedewerkerDataAccessHelper;
import nl.topicus.eduarte.dao.helpers.OrganisatieEenheidDataAccessHelper;
import nl.topicus.eduarte.entities.adres.SoortContactgegeven;
import nl.topicus.eduarte.entities.adres.TypeContactgegeven;
import nl.topicus.eduarte.entities.organisatie.Locatie;
import nl.topicus.eduarte.entities.organisatie.OrganisatieEenheid;
import nl.topicus.eduarte.entities.personen.Medewerker;
import nl.topicus.eduarte.entities.personen.OrganisatieMedewerker;
import nl.topicus.eduarte.entities.personen.Persoon;
import nl.topicus.eduarte.entities.personen.PersoonContactgegeven;

import org.hibernate.HibernateException;

/**
 * @author idserda
 */
public class MedewerkersImporterenFile implements Serializable
{
	private static final long serialVersionUID = 1L;

	private static final int AANTAL_VELDEN = 11;

	private final List<String> lines = new ArrayList<String>();

	private List<String> meldingen;

	private int aantalVerwerkt;

	private int aantalToegevoegd;

	private int aantalBijgewerkt;

	private int aantalOngeldigeRegels;

	private int regelnummer;

	private boolean opslaan;

	public MedewerkersImporterenFile(BufferedReader reader) throws IOException
	{
		// header ophalen
		String line = reader.readLine();

		while ((line = reader.readLine()) != null)
		{
			lines.add(line);
		}
	}

	public void importeerMedewerkers(boolean save)
	{
		this.opslaan = save;

		SessionDataAccessHelper sessionHelper =
			DataAccessRegistry.getHelper(SessionDataAccessHelper.class);
		meldingen = new ArrayList<String>();
		aantalOngeldigeRegels = 0;
		aantalToegevoegd = 0;
		aantalBijgewerkt = 0;
		aantalVerwerkt = 0;
		regelnummer = 2;
		for (String line : lines)
		{
			verwerkLine(line);
			regelnummer++;
			if (regelnummer % 50 == 0 && opslaan)
			{
				// Doe een flush en een clear.
				sessionHelper.getHibernateSessionProvider().getSession().flush();
				sessionHelper.clearSession();
			}
		}
	}

	private void verwerkLine(String line)
	{
		String[] velden = line.split(";", -1);
		aantalVerwerkt++;
		if (validateVelden(velden))
		{
			String afkorting = getAfkorting(velden);
			Medewerker medewerker = getNieuweOfBestaandeMedewerker(afkorting);

			if (opslaan)
			{
				String geboortenaam = getGeboortenaam(velden);
				String geboortenaamVoorvoegesel = getGeboortenaamVoorvoegesel(velden);
				String roepnaam = getRoepnaam(velden);
				String voorletters = getVoorletters(velden);
				String geslacht = getGeslacht(velden);
				String telefoonnummer = getTelefoonnummer(velden);
				String email = getEmail(velden);
				String organisatieEenhedenAfkortingen = getOrganisatieEenhedenAfkortingen(velden);
				String locatieAfkortingen = getLocatieAfkortingen(velden);
				String datumInDienst = getDatumInDienst(velden);

				Persoon persoon = medewerker.getPersoon();

				persoon.setAchternaam(geboortenaam);
				persoon.setOfficieleAchternaam(geboortenaam);
				persoon.setVoorvoegsel(geboortenaamVoorvoegesel);
				persoon.setOfficieleVoorvoegsel(geboortenaamVoorvoegesel);

				persoon.setRoepnaam(roepnaam);
				persoon.setVoorletters(voorletters);
				persoon.setGeslacht(Geslacht.parse(geslacht));

				setOrgansatieEenhedenMedewerkers(medewerker, organisatieEenhedenAfkortingen,
					locatieAfkortingen, TimeUtil.getInstance().parseDateString(datumInDienst));

				medewerker.setAfkorting(afkorting);
				medewerker.setBegindatum(TimeUtil.getInstance().parseDateString(datumInDienst));
				medewerker.setPersoon(persoon);

				persoon.save();

				voegTelefoonnummerToeAanPersoon(persoon, telefoonnummer);
				voegEmailadresToeAanPersoon(persoon, email);

				medewerker.save();
				// Onderstaande flush is nodig om foutmeldingen te voorkomen.
				medewerker.flush();
			}
		}
		else
		{
			aantalOngeldigeRegels++;
			meldingen.add("Ongeldige medewerkergegevens gevonden op regel " + regelnummer);
		}
	}

	private boolean validateVelden(String[] velden)
	{
		if (velden.length != AANTAL_VELDEN)
		{
			voegOngeldigeMeldingToe("Ongeldige aantal velden: regel " + regelnummer);
			return false;
		}

		if (!heeftSoortContactgegevenTypeTelefoon())
		{
			voegOngeldigeMeldingToe("Er kan geen soort contactgegeven worden gevonden met type 'Telefoon'");
			return false;
		}
		if (!heeftSoortContactgegevenTypeEmail())
		{
			voegOngeldigeMeldingToe("Er kan geen soort contactgegeven worden gevonden met type 'Email'");
			return false;
		}

		if (StringUtil.isEmpty(getAfkorting(velden)))
		{
			voegOngeldigeMeldingToe("Geen afkorting: regel " + regelnummer);
			return false;
		}

		if (StringUtil.isEmpty(getGeboortenaam(velden)))
		{
			voegOngeldigeMeldingToe("Geen achternaam: regel " + regelnummer);
			return false;
		}

		if (StringUtil.isNotEmpty(getGeslacht(velden))
			&& Geslacht.parse(getGeslacht(velden)) == null)
		{
			voegOngeldigeMeldingToe("Ongeldige geslacht: regel " + regelnummer);
			return false;
		}

		if (StringUtil.isNotEmpty(getOrganisatieEenhedenAfkortingen(velden))
			&& !validateOrganisatieEenheden(getOrganisatieEenhedenAfkortingen(velden)))
		{
			voegOngeldigeMeldingToe("Ongeldige organisatie-eenheid: regel " + regelnummer);
			return false;
		}

		if (StringUtil.isNotEmpty(getLocatieAfkortingen(velden))
			&& !validateLocaties(getLocatieAfkortingen(velden)))
		{
			voegOngeldigeMeldingToe("Ongeldige locatie: regel " + regelnummer);
			return false;
		}

		if (StringUtil.isEmpty(getDatumInDienst(velden)))
		{
			voegOngeldigeMeldingToe("Geen datum in dienst: regel " + regelnummer);
			return false;
		}
		else
		{
			Date datum = TimeUtil.getInstance().parseDateString(getDatumInDienst(velden));
			if (datum == null)
			{
				voegOngeldigeMeldingToe("Ongeldige datum in dienst: regel " + regelnummer);
				return false;
			}
		}

		if (validateOrganisatieEenheidLocatie(getOrganisatieEenhedenAfkortingen(velden),
			getLocatieAfkortingen(velden), getDatumInDienst(velden)))
		{
			voegOngeldigeMeldingToe("Ongeldige organisatie-eenheid-locatie koppelingen: regel "
				+ regelnummer);
			return false;
		}

		return true;
	}

	private boolean validateOrganisatieEenheden(String afkortingenCommaSeperated)
	{
		String[] organisatieEenheidAfkortingen = afkortingenCommaSeperated.split(",", -1);
		for (String organisatieEenheidAfkorting : organisatieEenheidAfkortingen)
		{
			organisatieEenheidAfkorting = organisatieEenheidAfkorting.trim();

			OrganisatieEenheid organisatieEenheid =
				DataAccessRegistry.getHelper(OrganisatieEenheidDataAccessHelper.class).get(
					organisatieEenheidAfkorting);

			if (organisatieEenheid == null)
			{
				return false;
			}
		}
		return true;
	}

	private boolean validateLocaties(String afkortingenCommaSeperated)
	{
		String[] locatieAfkortingen = afkortingenCommaSeperated.split(",", -1);
		for (String locatieAfkorting : locatieAfkortingen)
		{
			locatieAfkorting = locatieAfkorting.trim();

			if (!StringUtil.isEmpty(locatieAfkorting))
			{
				Locatie locatie =
					DataAccessRegistry.getHelper(LocatieDataAccessHelper.class).get(
						locatieAfkorting);

				if (locatie == null)
				{
					return false;
				}
			}
		}
		return true;
	}

	private boolean validateOrganisatieEenheidLocatie(String organisatieEenheden, String locaties,
			String datumInDienst)
	{
		if (StringUtil.isEmpty(organisatieEenheden))
			return !StringUtil.isEmpty(locaties);
		if (StringUtil.isEmpty(locaties))
			return false;

		String[] organisatieEenheidAfkortingen = organisatieEenheden.split(",", -1);
		String[] locatieAfkortingen = locaties.split(",", -1);

		if (organisatieEenheidAfkortingen.length != locatieAfkortingen.length)
			return true;

		Date datum = TimeUtil.getInstance().parseDateString(datumInDienst);

		for (int count = 0; count < organisatieEenheidAfkortingen.length; count++)
		{
			OrganisatieEenheid organisatieEenheid =
				DataAccessRegistry.getHelper(OrganisatieEenheidDataAccessHelper.class).get(
					organisatieEenheidAfkortingen[count].trim());
			Locatie locatie =
				DataAccessRegistry.getHelper(LocatieDataAccessHelper.class).get(
					locatieAfkortingen[count].trim());
			if (locatie != null && !organisatieEenheid.isVerbondenAanLocatie(locatie, datum))
				return true;
		}
		return false;
	}

	private Medewerker getNieuweOfBestaandeMedewerker(String afkorting)
	{
		Medewerker medewerker = getBestaandeMedewerker(afkorting);

		if (medewerker == null)
		{
			aantalToegevoegd++;
			medewerker = new Medewerker();
			Persoon persoon = new Persoon();
			medewerker.setPersoon(persoon);
		}
		else
		{
			aantalBijgewerkt++;
		}

		return medewerker;
	}

	private void setOrgansatieEenhedenMedewerkers(Medewerker medewerker,
			String organisatieEenheidAfkortingenList, String locatieAfkortingenList, Date begindatum)
	{
		if (!StringUtil.isEmpty(organisatieEenheidAfkortingenList))
		{
			String[] organisatieEenheidAfkortingen =
				organisatieEenheidAfkortingenList.split(",", -1);
			String[] locatieAfkortingen =
				StringUtil.isEmpty(locatieAfkortingenList) ? null : locatieAfkortingenList.split(
					",", -1);
			for (int count = 0; count < organisatieEenheidAfkortingen.length; count++)
			{
				OrganisatieEenheid organisatieEenheid =
					DataAccessRegistry.getHelper(OrganisatieEenheidDataAccessHelper.class).get(
						organisatieEenheidAfkortingen[count].trim());
				Locatie locatie =
					locatieAfkortingen == null ? null : DataAccessRegistry.getHelper(
						LocatieDataAccessHelper.class).get(locatieAfkortingen[count].trim());

				OrganisatieMedewerker koppeling =
					getBestaandeKoppeling(medewerker, organisatieEenheid, locatie);
				if (koppeling == null)
				{
					koppeling = new OrganisatieMedewerker(medewerker);
					medewerker.getOrganisatieEenheidLocatieKoppelingen().add(koppeling);
				}
				koppeling.setOrganisatieEenheid(organisatieEenheid);
				koppeling.setLocatie(locatie);
				koppeling.setBegindatum(begindatum);
				koppeling.setEinddatum(null);
				koppeling.saveOrUpdate();
			}
		}
	}

	private void voegTelefoonnummerToeAanPersoon(Persoon persoon, String telefoonnummer)
	{
		if (StringUtil.isNotEmpty(telefoonnummer)
			&& !persoonHeeftTelefoonnummer(persoon, telefoonnummer))
		{
			PersoonContactgegeven contactGegeven = new PersoonContactgegeven();
			contactGegeven.setPersoon(persoon);
			contactGegeven.setContactgegeven(telefoonnummer);
			contactGegeven.setSoortContactgegeven(getSoortContactgegevenTelefoon());
			contactGegeven.setGeheim(false);
			contactGegeven.setVolgorde(persoon.getContactgegevens().size());

			persoon.getContactgegevens().add(contactGegeven);
			contactGegeven.save();
		}
	}

	private void voegEmailadresToeAanPersoon(Persoon persoon, String email)
	{
		if (StringUtil.isNotEmpty(email) && !persoonHeeftEmail(persoon, email))
		{
			PersoonContactgegeven contactGegeven = new PersoonContactgegeven();
			contactGegeven.setPersoon(persoon);
			contactGegeven.setContactgegeven(email);
			contactGegeven.setSoortContactgegeven(getSoortContactgegevenEmail());
			contactGegeven.setGeheim(false);
			contactGegeven.setVolgorde(persoon.getContactgegevens().size());

			persoon.getContactgegevens().add(contactGegeven);
			contactGegeven.save();
		}
	}

	private boolean persoonHeeftTelefoonnummer(Persoon persoon, String telefoonnummer)
	{
		for (PersoonContactgegeven contactGegeven : persoon
			.getContactgegevens(getSoortContactgegevenTelefoon()))
		{
			if (telefoonnummer.equals(contactGegeven.getContactgegeven()))
				return true;
		}
		return false;
	}

	private boolean persoonHeeftEmail(Persoon persoon, String email)
	{
		for (PersoonContactgegeven contactGegeven : persoon
			.getContactgegevens(getSoortContactgegevenEmail()))
		{
			if (email.equals(contactGegeven.getContactgegeven()))
				return true;
		}
		return false;
	}

	private boolean heeftSoortContactgegevenTypeTelefoon()
	{
		return getSoortContactgegevenTelefoon() != null;
	}

	private boolean heeftSoortContactgegevenTypeEmail()
	{
		return getSoortContactgegevenEmail() != null;
	}

	private SoortContactgegeven getSoortContactgegevenTelefoon()
	{
		return SoortContactgegeven.getSoortContactgegeven(TypeContactgegeven.Telefoon);
	}

	private SoortContactgegeven getSoortContactgegevenEmail()
	{
		return SoortContactgegeven.getSoortContactgegeven(TypeContactgegeven.Email);
	}

	private Medewerker getBestaandeMedewerker(String afkorting)
	{
		Medewerker medewerker = null;

		try
		{
			medewerker =
				DataAccessRegistry.getHelper(MedewerkerDataAccessHelper.class).batchGetByAfkorting(
					afkorting);
		}
		catch (HibernateException e)
		{
			return null;
		}

		return medewerker;
	}

	private OrganisatieMedewerker getBestaandeKoppeling(Medewerker medewerker,
			OrganisatieEenheid organisatieEenheid, Locatie locatie)
	{
		for (OrganisatieMedewerker organisatieMedewerker : medewerker
			.getOrganisatieEenheidLocatieKoppelingen())
		{
			if (organisatieEenheid.equals(organisatieMedewerker.getOrganisatieEenheid())
				&& JavaUtil.equalsOrBothNull(locatie, organisatieMedewerker.getLocatie()))
			{
				return organisatieMedewerker;
			}
		}
		return null;
	}

	public List<String> getAlleResultaten()
	{
		List<String> alleResultaten = new ArrayList<String>();
		alleResultaten.add("Aantal regels verwerkt: " + getAantalVerwerkt());
		alleResultaten.add("Aantal ongeldige regels: " + getOngeldigeRegels());
		alleResultaten.add("Aantal nieuwe medewerkers aangemaakt: " + getAantalToegevoegd());
		alleResultaten.add("Aantal medewerkers bijgewerkt: " + getAantalBijgewerkt());
		alleResultaten.addAll(getMeldingen());
		return alleResultaten;
	}

	public int getAantalVerwerkt()
	{
		return aantalVerwerkt;
	}

	public int getAantalToegevoegd()
	{
		return aantalToegevoegd;
	}

	public int getAantalBijgewerkt()
	{
		return aantalBijgewerkt;
	}

	public int getOngeldigeRegels()
	{
		return aantalOngeldigeRegels;
	}

	public List<String> getMeldingen()
	{
		return meldingen;
	}

	private void voegOngeldigeMeldingToe(String msg)
	{
		meldingen.add(msg);
	}

	private String getAfkorting(String[] velden)
	{
		return velden[0].trim();
	}

	private String getGeboortenaam(String[] velden)
	{
		return velden[1].trim();
	}

	private String getGeboortenaamVoorvoegesel(String[] velden)
	{
		return velden[2].trim();
	}

	private String getRoepnaam(String[] velden)
	{
		return velden[3].trim();
	}

	private String getVoorletters(String[] velden)
	{
		return velden[4].trim();
	}

	private String getGeslacht(String[] velden)
	{
		return velden[5].trim();
	}

	private String getTelefoonnummer(String[] velden)
	{
		return velden[6].trim();
	}

	private String getEmail(String[] velden)
	{
		return velden[7].trim();
	}

	private String getOrganisatieEenhedenAfkortingen(String[] velden)
	{
		return velden[8].trim();
	}

	private String getLocatieAfkortingen(String[] velden)
	{
		return velden[9].trim();
	}

	private String getDatumInDienst(String[] velden)
	{
		return velden[10].trim();
	}
}