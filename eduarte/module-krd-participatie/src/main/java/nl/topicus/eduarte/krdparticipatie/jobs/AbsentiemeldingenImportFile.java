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
import nl.topicus.eduarte.dao.participatie.helpers.AbsentieMeldingDataAccessHelper;
import nl.topicus.eduarte.dao.participatie.helpers.AbsentieRedenDataAccessHelper;
import nl.topicus.eduarte.dao.participatie.helpers.WaarnemingDataAccessHelper;
import nl.topicus.eduarte.entities.participatie.AbsentieMelding;
import nl.topicus.eduarte.entities.participatie.AbsentieReden;
import nl.topicus.eduarte.entities.participatie.Waarneming;
import nl.topicus.eduarte.entities.participatie.enums.WaarnemingSoort;
import nl.topicus.eduarte.entities.personen.Deelnemer;
import nl.topicus.eduarte.krdparticipatie.entities.AbsentiemeldingenImporterenJobRun;
import nl.topicus.eduarte.krdparticipatie.entities.AbsentiemeldingenImporterenJobRunDetail;
import nl.topicus.eduarte.participatie.zoekfilters.AbsentieMeldingZoekFilter;
import nl.topicus.eduarte.participatie.zoekfilters.WaarnemingZoekFilter;

public class AbsentiemeldingenImportFile implements Serializable
{
	private static final long serialVersionUID = 1L;

	private static final int AANTAL_VELDEN = 9;

	private static final int DEELNEMER_INDEX = 0;

	private static final int BEGINDATUM_INDEX = 1;

	private static final int BEGINTIJD_INDEX = 2;

	private static final int EINDDATUM_INDEX = 3;

	private static final int EINDTIJD_INDEX = 4;

	private static final int BEGINLESUUR_INDEX = 5;

	private static final int EINDLESUUR_INDEX = 6;

	private static final int REDEN_INDEX = 7;

	private static final int OPMERKINGEN_INDEX = 8;

	private final String bestandsnaam;

	private final List<String> lines = new ArrayList<String>();

	private int aantalToegevoegd;

	private int aantalOngeldigeRegels;

	private int regelnummer;

	public AbsentiemeldingenImportFile(String bestandsnaam, BufferedReader reader)
			throws IOException
	{
		this.bestandsnaam = bestandsnaam;
		String line;
		while ((line = reader.readLine()) != null)
		{
			lines.add(line);
		}
	}

	public void importeerAbsentiemeldingen(AbsentiemeldingenImporterenJobRun jobrun,
			AbsentiemeldingenImporterenJob job) throws InterruptedException
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

	private void verwerkLine(String line, AbsentiemeldingenImporterenJobRun jobrun)
	{
		AbsentieMeldingDataAccessHelper meldingHelper =
			DataAccessRegistry.getHelper(AbsentieMeldingDataAccessHelper.class);

		WaarnemingDataAccessHelper helper =
			DataAccessRegistry.getHelper(WaarnemingDataAccessHelper.class);
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
			if (eindDatumTijd != null)
			{
				if (eindtijd != null)
					eindDatumTijd = timeutil.setTimeOnDate(eindDatumTijd, eindtijd);
				else
					eindDatumTijd = timeutil.maakEindeVanDagVanDatum(eindDatumTijd);
			}
			Integer beginLesuur =
				StringUtil.isEmpty(velden[BEGINLESUUR_INDEX]) ? null : Integer
					.valueOf(velden[BEGINLESUUR_INDEX]);
			Integer eindLesuur =
				StringUtil.isEmpty(velden[EINDLESUUR_INDEX]) ? null : Integer
					.valueOf(velden[EINDLESUUR_INDEX]);
			AbsentieReden reden = getReden(velden);
			String opmerkingen = velden[OPMERKINGEN_INDEX];

			AbsentieMeldingZoekFilter filter = new AbsentieMeldingZoekFilter();
			filter.setBeginDatumTijd(beginDatumTijd);
			filter.setEindDatumTijd(eindDatumTijd);
			filter.setDeelnemer(deelnemer);
			List<AbsentieMelding> meldingen = meldingHelper.getOverlappendeMeldingen(filter);
			if (meldingen.isEmpty())
			{

				AbsentieMelding melding = new AbsentieMelding();
				melding.setAbsentieReden(reden);
				melding.setAfgehandeld(reden.isStandaardAfgehandeld());
				melding.setBeginDatumTijd(beginDatumTijd);
				melding.setEindDatumTijd(eindDatumTijd);
				melding.setBeginLesuur(beginLesuur);
				melding.setEindLesuur(eindLesuur);
				melding.setDeelnemer(deelnemer);
				melding.setOpmerkingen(opmerkingen);
				melding.save();
				aantalToegevoegd++;

				// Koppel eventuele waarnemingen.
				WaarnemingZoekFilter waarnemingFilter = new WaarnemingZoekFilter();
				waarnemingFilter.setDeelnemer(deelnemer);
				waarnemingFilter.setBeginDatumTijd(beginDatumTijd);
				waarnemingFilter.setEindDatumTijd(eindDatumTijd);
				List<Waarneming> waarnemingen =
					helper.getOverlappendeWaarnemingen(waarnemingFilter);
				for (Waarneming waarneming : waarnemingen)
				{
					if (waarneming.getWaarnemingSoort() == WaarnemingSoort.Afwezig)
					{
						waarneming.setAbsentieMelding(melding);
						waarneming.update();
						melding.getWaarnemingen().add(waarneming);
					}
				}

				createDetail("Absentiemelding aangemaakt voor deelnemer "
					+ deelnemer.getDeelnemernummer(), jobrun, melding.getId());
			}
			else
				createDetail("Er is al een overlappende melding voor "
					+ deelnemer.getDeelnemernummer() + " op " + TimeUtil.asString(beginDatumTijd),
					jobrun);
		}
	}

	private boolean validateVelden(String[] velden, String regel,
			AbsentiemeldingenImporterenJobRun jobrun)
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
			timeutil.isoStringAsDate(velden[EINDDATUM_INDEX]);
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
		AbsentieReden reden = getReden(velden);
		if (reden == null)
		{
			aantalOngeldigeRegels++;
			createDetail("Onbekende absentiereden op regel " + regelnummer + ": " + regel, jobrun);
			return false;
		}
		if (StringUtil.isNotEmpty(velden[BEGINLESUUR_INDEX]))
		{
			if (StringUtil.isEmpty(velden[EINDLESUUR_INDEX]))
			{
				aantalOngeldigeRegels++;
				createDetail("Absentiemelding met beginlesuur maar zonder eindlesuur op regel "
					+ regelnummer + ": " + regel, jobrun);
				return false;
			}
		}
		if (StringUtil.isNotEmpty(velden[EINDLESUUR_INDEX]))
		{
			if (StringUtil.isEmpty(velden[BEGINLESUUR_INDEX]))
			{
				aantalOngeldigeRegels++;
				createDetail("Absentiemelding met eindlesuur maar zonder beginlesuur op regel "
					+ regelnummer + ": " + regel, jobrun);
				return false;
			}
		}
		if (StringUtil.isNotEmpty(velden[BEGINLESUUR_INDEX])
			|| StringUtil.isNotEmpty(velden[EINDLESUUR_INDEX]))
		{
			Date begindatum = timeutil.isoStringAsDate(velden[BEGINDATUM_INDEX]);
			Date einddatum = timeutil.isoStringAsDate(velden[EINDDATUM_INDEX]);
			begindatum = timeutil.asDate(begindatum);
			einddatum = timeutil.asDate(einddatum);
			if (einddatum != null && !begindatum.equals(einddatum))
			{
				aantalOngeldigeRegels++;
				createDetail(
					"Absentiemelding met ingevulde begin- en eindlesuur en verschillende begin- en einddata op regel "
						+ regelnummer
						+ ": "
						+ regel
						+ ". Als begin- en eindlesuur is ingevuld, moet begin- en einddatum dezelfde datum zijn.",
					jobrun);
				return false;
			}
		}

		return true;
	}

	private void createDetail(String melding, AbsentiemeldingenImporterenJobRun jobrun)
	{
		createDetail(melding, jobrun, null);
	}

	private void createDetail(String melding, AbsentiemeldingenImporterenJobRun jobrun,
			Long meldingId)
	{
		AbsentiemeldingenImporterenJobRunDetail detail =
			new AbsentiemeldingenImporterenJobRunDetail(jobrun);
		detail.setUitkomst(melding);
		detail.setAbsentiemeldingId(meldingId);
		detail.save();
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

	private AbsentieReden getReden(String[] velden)
	{
		if (StringUtil.isEmpty(velden[REDEN_INDEX]))
		{
			return null;
		}
		return DataAccessRegistry.getHelper(AbsentieRedenDataAccessHelper.class).get(
			velden[REDEN_INDEX]);
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