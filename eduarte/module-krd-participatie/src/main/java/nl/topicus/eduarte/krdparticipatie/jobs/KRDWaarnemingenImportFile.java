package nl.topicus.eduarte.krdparticipatie.jobs;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.dao.helpers.SessionDataAccessHelper;
import nl.topicus.cobra.entities.Time;
import nl.topicus.cobra.util.StringUtil;
import nl.topicus.cobra.util.TimeUtil;
import nl.topicus.eduarte.dao.helpers.DeelnemerDataAccessHelper;
import nl.topicus.eduarte.dao.helpers.OnderwijsproductDataAccessHelper;
import nl.topicus.eduarte.dao.participatie.helpers.AbsentieMeldingDataAccessHelper;
import nl.topicus.eduarte.dao.participatie.hibernate.WaarnemingHibernateDataAccessHelper;
import nl.topicus.eduarte.entities.onderwijsproduct.Onderwijsproduct;
import nl.topicus.eduarte.entities.participatie.AbsentieMelding;
import nl.topicus.eduarte.entities.participatie.Waarneming;
import nl.topicus.eduarte.entities.participatie.enums.WaarnemingSoort;
import nl.topicus.eduarte.entities.personen.Deelnemer;
import nl.topicus.eduarte.krdparticipatie.entities.KRDWaarnemingenImporterenJobRun;
import nl.topicus.eduarte.krdparticipatie.entities.KRDWaarnemingenImporterenJobRunDetail;
import nl.topicus.eduarte.participatie.zoekfilters.AbsentieMeldingZoekFilter;
import nl.topicus.eduarte.participatie.zoekfilters.WaarnemingZoekFilter;

public class KRDWaarnemingenImportFile implements Serializable
{
	private static final long serialVersionUID = 1L;

	private static final int AANTAL_VELDEN = 10;

	private static final int DEELNEMER_INDEX = 0;

	private static final int BEGINDATUM_INDEX = 1;

	private static final int BEGINTIJD_INDEX = 2;

	private static final int EINDDATUM_INDEX = 3;

	private static final int EINDTIJD_INDEX = 4;

	private static final int BEGINLESUUR_INDEX = 5;

	private static final int EINDLESUUR_INDEX = 6;

	private static final int ONDERWIJSPRODUCT_INDEX = 7;

	private static final int AANWEZIG_INDEX = 8;

	private static final int AFGEHANDELD_INDEX = 9;

	private final String bestandsnaam;

	private final List<String> lines = new ArrayList<String>();

	private int aantalToegevoegd;

	private int aantalOngeldigeRegels;

	private int regelnummer;

	public KRDWaarnemingenImportFile(String bestandsnaam, BufferedReader reader) throws IOException
	{
		this.bestandsnaam = bestandsnaam;
		String line;
		while ((line = reader.readLine()) != null)
		{
			lines.add(line);
		}
	}

	public void importeerWaarnemingen(KRDWaarnemingenImporterenJobRun jobrun,
			KRDWaarnemingenImporterenJob job) throws InterruptedException
	{
		SessionDataAccessHelper sessionHelper =
			DataAccessRegistry.getHelper(SessionDataAccessHelper.class);
		aantalOngeldigeRegels = 0;
		aantalToegevoegd = 0;
		regelnummer = 1;
		for (String line : lines)
		{
			verwerkLine(line, jobrun);
			job.setProgress(regelnummer, lines.size());
			regelnummer++;
			if (regelnummer % 50 == 0)
			{
				// Doe een flush en een clear.
				sessionHelper.getHibernateSessionProvider().getSession().flush();
				sessionHelper.clearSession();
			}
		}
	}

	private boolean waarnemingBestaat(Deelnemer deelnemer, Date beginDatumTijd, Date eindDatumTijd)
	{
		WaarnemingZoekFilter waarnemingZoekFilter = new WaarnemingZoekFilter();
		waarnemingZoekFilter.setDeelnemer(deelnemer);
		waarnemingZoekFilter.setBeginDatumTijd(beginDatumTijd);
		waarnemingZoekFilter.setEindDatumTijd(eindDatumTijd);
		waarnemingZoekFilter.setDatumTijdExactGelijk(false);
		return DataAccessRegistry.getHelper(WaarnemingHibernateDataAccessHelper.class).listCount(
			waarnemingZoekFilter) > 0;
	}

	private void verwerkLine(String line, KRDWaarnemingenImporterenJobRun jobrun)
	{
		AbsentieMeldingDataAccessHelper helper =
			DataAccessRegistry.getHelper(AbsentieMeldingDataAccessHelper.class);

		TimeUtil timeutil = TimeUtil.getInstance();
		String[] velden = line.split(";", -1);
		if (validateVelden(velden, line, jobrun))
		{
			// Haal de deelnemer op.
			Deelnemer deelnemer = getDeelnemer(velden);
			// Bepaal begin- en einddatum en -tijd.
			Date beginDatumTijd = timeutil.isoStringAsDate(velden[BEGINDATUM_INDEX]);
			Time begintijd = timeutil.parseTimeString(velden[BEGINTIJD_INDEX]);
			Date eindDatumTijd = timeutil.isoStringAsDate(velden[EINDDATUM_INDEX]);
			Time eindtijd = timeutil.parseTimeString(velden[EINDTIJD_INDEX]);
			if (begintijd != null)
				beginDatumTijd = timeutil.setTimeOnDate(beginDatumTijd, begintijd);
			if (eindtijd != null)
				eindDatumTijd = timeutil.setTimeOnDate(eindDatumTijd, eindtijd);
			else
				eindDatumTijd = timeutil.maakEindeVanDagVanDatum(eindDatumTijd);
			Integer beginLesuur =
				StringUtil.isEmpty(velden[BEGINLESUUR_INDEX]) ? null : Integer
					.valueOf(velden[BEGINLESUUR_INDEX]);
			Integer eindLesuur =
				StringUtil.isEmpty(velden[EINDLESUUR_INDEX]) ? null : Integer
					.valueOf(velden[EINDLESUUR_INDEX]);
			Onderwijsproduct onderwijsproduct = getOnderwijsproduct(velden);
			String aanwezig = velden[AANWEZIG_INDEX];
			boolean afgehandeld = "J".equalsIgnoreCase(velden[AFGEHANDELD_INDEX]);

			if (!waarnemingBestaat(deelnemer, beginDatumTijd, eindDatumTijd))
			{
				Waarneming waarneming = new Waarneming();
				waarneming.setOnderwijsproduct(onderwijsproduct);
				waarneming.setAfgehandeld(afgehandeld);
				waarneming.setBeginDatumTijd(beginDatumTijd);
				waarneming.setEindDatumTijd(eindDatumTijd);
				waarneming.setBeginLesuur(beginLesuur);
				waarneming.setEindLesuur(eindLesuur);
				waarneming.setDeelnemer(deelnemer);
				waarneming.setWaarnemingSoort(WaarnemingSoort.valueOf(aanwezig));

				if (waarneming.getWaarnemingSoort() == WaarnemingSoort.Afwezig)
				{
					AbsentieMeldingZoekFilter meldingFilter = new AbsentieMeldingZoekFilter();
					meldingFilter.setDeelnemer(deelnemer);
					meldingFilter.setBeginDatumTijd(beginDatumTijd);
					meldingFilter.setEindDatumTijd(eindDatumTijd);
					List<AbsentieMelding> meldingen =
						helper.getOverlappendeMeldingen(meldingFilter);
					AbsentieMelding melding = null;
					if (meldingen.size() == 1)
					{
						melding = meldingen.get(0);
					}
					waarneming.setAbsentieMelding(melding);
				}

				waarneming.save();
				aantalToegevoegd++;

				createDetail("Waarneming aangemaakt voor deelnemer "
					+ deelnemer.getDeelnemernummer(), jobrun, waarneming.getId());
			}
			else
				createDetail("Er is al een overlappende waarneming voor "
					+ deelnemer.getDeelnemernummer() + " op " + TimeUtil.asString(beginDatumTijd),
					jobrun);
		}
	}

	private boolean validateVelden(String[] velden, String regel,
			KRDWaarnemingenImporterenJobRun jobrun)
	{
		if (velden.length != AANTAL_VELDEN)
		{
			aantalOngeldigeRegels++;
			createDetail("Verkeerd aantal velden op regel " + regelnummer + ": " + regel, jobrun);
			return false;
		}
		if (StringUtil.isEmpty(velden[DEELNEMER_INDEX]))
		{
			aantalOngeldigeRegels++;
			createDetail("Geen deelnemernummer op regel " + regelnummer + ": " + regel, jobrun);
			return false;
		}
		TimeUtil timeutil = TimeUtil.getInstance();
		try
		{
			Date begindatum = timeutil.isoStringAsDate(velden[BEGINDATUM_INDEX]);
			if (begindatum == null)
			{
				aantalOngeldigeRegels++;
				createDetail("Geen begindatum op regel " + regelnummer + ": " + regel, jobrun);
				return false;
			}
		}
		catch (Exception e)
		{
			aantalOngeldigeRegels++;
			createDetail("Ongeldige begindatum op regel " + regelnummer + ": " + regel, jobrun);
			return false;
		}
		try
		{
			if (StringUtil.isNotEmpty(velden[BEGINTIJD_INDEX]))
			{
				Time begintijd = timeutil.parseTimeString(velden[BEGINTIJD_INDEX]);
				if (begintijd == null)
				{
					aantalOngeldigeRegels++;
					createDetail("Ongeldige begintijd op regel " + regelnummer + ": " + regel,
						jobrun);
					return false;
				}
			}
		}
		catch (Exception e)
		{
			aantalOngeldigeRegels++;
			createDetail("Ongeldige begintijd op regel " + regelnummer + ": " + regel, jobrun);
			return false;
		}
		try
		{
			Date einddatum = timeutil.isoStringAsDate(velden[EINDDATUM_INDEX]);
			if (einddatum == null)
			{
				aantalOngeldigeRegels++;
				createDetail("Geen einddatum op regel " + regelnummer + ": " + regel, jobrun);
				return false;
			}
		}
		catch (Exception e)
		{
			aantalOngeldigeRegels++;
			createDetail("Ongeldige einddatum op regel " + regelnummer + ": " + regel, jobrun);
			return false;
		}
		try
		{
			if (StringUtil.isNotEmpty(velden[EINDTIJD_INDEX]))
			{
				Time eindtijd = timeutil.parseTimeString(velden[EINDTIJD_INDEX]);
				if (eindtijd == null)
				{
					aantalOngeldigeRegels++;
					createDetail("Ongeldige eindtijd op regel " + regelnummer + ": " + regel,
						jobrun);
					return false;
				}
			}
		}
		catch (Exception e)
		{
			aantalOngeldigeRegels++;
			createDetail("Ongeldige eindtijd op regel " + regelnummer + ": " + regel, jobrun);
			return false;
		}
		try
		{
			if (StringUtil.isNotEmpty(velden[BEGINLESUUR_INDEX]))
				Integer.valueOf(velden[BEGINLESUUR_INDEX]);
		}
		catch (Exception e)
		{
			aantalOngeldigeRegels++;
			createDetail("Ongeldig beginlesuur op regel " + regelnummer + ": " + regel, jobrun);
			return false;
		}
		try
		{
			if (StringUtil.isNotEmpty(velden[EINDLESUUR_INDEX]))
				Integer.valueOf(velden[EINDLESUUR_INDEX]);
		}
		catch (Exception e)
		{
			aantalOngeldigeRegels++;
			createDetail("Ongeldig eindlesuur op regel " + regelnummer + ": " + regel, jobrun);
			return false;
		}
		Deelnemer deelnemer = getDeelnemer(velden);
		if (deelnemer == null)
		{
			aantalOngeldigeRegels++;
			createDetail("Onbekende deelnemer op regel " + regelnummer + ": " + regel, jobrun);
			return false;
		}
		if (StringUtil.isNotEmpty(velden[ONDERWIJSPRODUCT_INDEX]))
		{
			Onderwijsproduct onderwijsproduct = getOnderwijsproduct(velden);
			if (onderwijsproduct == null)
			{
				aantalOngeldigeRegels++;
				createDetail("Onbekend onderwijsproduct op regel " + regelnummer + ": " + regel,
					jobrun);
				return false;
			}
		}
		WaarnemingSoort soort = WaarnemingSoort.parse(velden[AANWEZIG_INDEX]);
		if (soort == null)
		{
			aantalOngeldigeRegels++;
			createDetail("Ongeldige aanwezigheid/afwezigheid op regel " + regelnummer + ": "
				+ regel, jobrun);
			return false;
		}
		if (!("J".equalsIgnoreCase(velden[AFGEHANDELD_INDEX]) || "N"
			.equalsIgnoreCase(velden[AFGEHANDELD_INDEX])))
		{
			aantalOngeldigeRegels++;
			createDetail("Ongeldige status afgehandeld op regel " + regelnummer + ": " + regel,
				jobrun);
			return false;
		}

		return true;
	}

	private KRDWaarnemingenImporterenJobRunDetail createDetail(String melding,
			KRDWaarnemingenImporterenJobRun jobrun)
	{
		return createDetail(melding, jobrun, null);
	}

	private KRDWaarnemingenImporterenJobRunDetail createDetail(String melding,
			KRDWaarnemingenImporterenJobRun jobrun, Long waarnemingId)
	{
		KRDWaarnemingenImporterenJobRunDetail detail =
			new KRDWaarnemingenImporterenJobRunDetail(jobrun);
		detail.setUitkomst(melding);
		detail.setWaarnemingId(waarnemingId);
		detail.save();

		return detail;
	}

	private Deelnemer getDeelnemer(String[] velden)
	{
		if (StringUtil.isEmpty(velden[0]))
		{
			return null;
		}
		String idString = velden[0];
		if (!StringUtil.isNumeric(idString))
		{
			return null;
		}
		Integer id = Integer.valueOf(idString);
		return DataAccessRegistry.getHelper(DeelnemerDataAccessHelper.class).getByDeelnemernummer(
			id);
	}

	private Onderwijsproduct getOnderwijsproduct(String[] velden)
	{
		if (StringUtil.isEmpty(velden[ONDERWIJSPRODUCT_INDEX]))
		{
			return null;
		}
		return DataAccessRegistry.getHelper(OnderwijsproductDataAccessHelper.class).get(
			velden[ONDERWIJSPRODUCT_INDEX]);
	}

	public String getBestandsnaam()
	{
		return bestandsnaam;
	}

	/**
	 * @return Returns the aantalToegevoegd.
	 */
	public int getAantalToegevoegd()
	{
		return aantalToegevoegd;
	}

	public int getAantalOngeldigeRegels()
	{
		return aantalOngeldigeRegels;
	}
}