package nl.topicus.eduarte.krd.bron.jobs;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.quartz.JobInfo;
import nl.topicus.cobra.util.TimeUtil;
import nl.topicus.eduarte.entities.examen.Examendeelname;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis;
import nl.topicus.eduarte.entities.jobs.JobRunClass;
import nl.topicus.eduarte.entities.landelijk.Schooljaar;
import nl.topicus.eduarte.entities.opleiding.SoortOnderwijsTax;
import nl.topicus.eduarte.entities.personen.Deelnemer;
import nl.topicus.eduarte.jobs.EduArteJob;
import nl.topicus.eduarte.krd.bron.BronControllerBve;
import nl.topicus.eduarte.krd.bron.BronControllerVo;
import nl.topicus.eduarte.krd.bron.BronUtils;
import nl.topicus.eduarte.krd.dao.helpers.BronAanleverpuntDataAccessHelper;
import nl.topicus.eduarte.krd.dao.helpers.ExamendeelnameDataAccessHelper;
import nl.topicus.eduarte.krd.entities.bron.BronAanleverpunt;
import nl.topicus.eduarte.krd.entities.bron.BronExamenverzameling;
import nl.topicus.eduarte.krd.entities.bron.BronExamenverzamelingenAanmakenJobRun;
import nl.topicus.eduarte.krd.entities.bron.meldingen.aanlevering.BronAanleverMelding;
import nl.topicus.eduarte.krd.entities.bron.meldingen.aanlevering.BronBveAanleverRecord;
import nl.topicus.eduarte.krd.entities.bron.meldingen.aanlevering.BronExamenresultaatVOMelding;
import nl.topicus.eduarte.krd.entities.bron.meldingen.aanlevering.BronVakGegegevensVOMelding;
import nl.topicus.eduarte.krd.zoekfilters.ExamendeelnameZoekFilter;
import nl.topicus.eduarte.zoekfilters.OrganisatieEenheidLocatieAuthorizationContext;
import nl.topicus.onderwijs.duo.bron.BronOnderwijssoort;

import org.apache.wicket.security.checks.AlwaysGrantedSecurityCheck;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

@JobInfo(name = BronExamenverzamelingenAanmakenJob.JOB_NAME)
@JobRunClass(BronExamenverzamelingenAanmakenJobRun.class)
public class BronExamenverzamelingenAanmakenJob extends EduArteJob
{
	public static final String JOB_NAME = "Examenverzamelingen aanmaken";

	private Schooljaar schooljaar;

	private BronOnderwijssoort bronOnderwijssoort;

	private Boolean alleenGewijzigdeDeelnames;

	private List<SoortOnderwijsTax> soortOnderwijsTaxList;

	private List<Long> examendeelnames;

	private int aantalVerzamelingen = 0;

	private final BronControllerBve controllerBve = new BronControllerBve();

	private final BronControllerVo controllerVo = new BronControllerVo();

	private BronExamenverzamelingenAanmakenJobRun run;

	@Override
	protected void executeJob(JobExecutionContext context) throws JobExecutionException
	{
		extractData(context);
		run = new BronExamenverzamelingenAanmakenJobRun();
		run.setGestartDoor(getMedewerker());
		run.setRunStart(TimeUtil.getInstance().currentDateTime());
		run.setSamenvatting("Aanmaken van examenverzamelingen");
		run.save();
		try
		{
			processJob();
			run.setSamenvatting(aantalVerzamelingen + " examenverzamelingen aangemaakt voor "
				+ bronOnderwijssoort.toString() + " " + schooljaar.getAfkorting() + " "
				+ soortOnderwijsTaxList);
		}
		catch (Exception e)
		{
			run
				.setSamenvatting("Probleem opgetreden tijdens het aanmaken van de examenverzamelingen");
			throw new JobExecutionException(e);
		}
		run.setRunEinde(TimeUtil.getInstance().currentDateTime());
		run.update();
		run.commit();
		return;
	}

	private void processJob() throws InterruptedException
	{
		setStatus("Aanmaken examenverzameling");
		if (examendeelnames != null && !examendeelnames.isEmpty())
		{
			BronExamenverzameling verzameling = createExamenVerzameling(null);
			createMeldingen(null, verzameling, examendeelnames);
		}
		else
		{
			for (SoortOnderwijsTax soortOnderwijs : soortOnderwijsTaxList)
			{
				BronExamenverzameling verzameling = createExamenVerzameling(soortOnderwijs);
				List<Long> examendeelnameIds = getExamendeelnameIds(soortOnderwijs);
				createMeldingen(soortOnderwijs, verzameling, examendeelnameIds);
			}
		}
	}

	private void createMeldingen(SoortOnderwijsTax soortOnderwijs,
			BronExamenverzameling verzameling, List<Long> examendeelnameIds)
			throws InterruptedException
	{
		List<BronExamenresultaatVOMelding> voMeldingen =
			new ArrayList<BronExamenresultaatVOMelding>();
		List<BronAanleverMelding> vavoMeldingen = new ArrayList<BronAanleverMelding>();
		int counter = 0;
		for (Long deelnameId : examendeelnameIds)
		{
			Examendeelname deelname = getExamendeelname(deelnameId);

			// deelname kan niet verwijderd worden als deze niet bekend is in BRON
			if (deelname.getExamenstatus().isVerwijderd()
				&& !deelname.getBronStatus().isGemeldAanBron())
			{
				run
					.info("Er is geen melding aangemaakt voor deelnemer: "
						+ deelname.getVerbintenis().getDeelnemer().getDeelnemernummer()
						+ " omdat de examenstatus 'verwijderd' is, en de examendeelname niet gemeld is aan BRON");
			}
			else
			{
				if (deelname.getExamenjaar() == null)
					repareerExamenjaar(deelname);
				if (BronOnderwijssoort.VOORTGEZETONDERWIJS == bronOnderwijssoort)
					maakVoMeldingAan(verzameling, voMeldingen, deelname);
				else if (BronOnderwijssoort.VAVO == bronOnderwijssoort)
					maakVavoMeldingAan(verzameling, vavoMeldingen, deelname);
				else
					throw new IllegalArgumentException(
						"Kan geen examenverzamelingen maken voor andere onderwijssoorten dan VO en VAVO");
			}
			if (deelname.isGewijzigd())
			{
				deelname.setGewijzigd(false);
				deelname.saveOrUpdate();
			}
			counter++;
			if (soortOnderwijsTaxList.size() > 1)
				setProgress(counter, examendeelnameIds.size() * soortOnderwijsTaxList.size());
			else
				setProgress(counter, examendeelnameIds.size());
			flushAndClearHibernate();
		}

		if (!voMeldingen.isEmpty() || !vavoMeldingen.isEmpty())
		{
			if (BronOnderwijssoort.VOORTGEZETONDERWIJS == bronOnderwijssoort)
			{
				verwerkVoMeldingen(verzameling, voMeldingen);
			}
			if (BronOnderwijssoort.VAVO == bronOnderwijssoort)
			{
				verwerkVavoMeldingen(verzameling, vavoMeldingen);
			}
			verzameling.saveOrUpdate();
			aantalVerzamelingen++;
		}
		else
		{
			verzameling.delete();
			if (soortOnderwijs != null)
				run.error("Voor " + soortOnderwijs.toString()
					+ " zijn geen examendeelnames gevonden die voldeden aan de criteria");
		}
		flushAndClearHibernate();
	}

	private void verwerkVavoMeldingen(BronExamenverzameling verzameling,
			List<BronAanleverMelding> vavoMeldingen)
	{
		verzameling.setAantalMeldingen(vavoMeldingen.size());
		verzameling.setVavoExamenMeldingen(vavoMeldingen);
		for (BronAanleverMelding vavoMelding : vavoMeldingen)
		{
			vavoMelding.saveOrUpdate();
			BronUtils.updateStatussenNaAanmaken(vavoMelding);
			for (BronBveAanleverRecord record : vavoMelding.getMeldingen())
				record.saveOrUpdate();
		}
	}

	private void verwerkVoMeldingen(BronExamenverzameling verzameling,
			List<BronExamenresultaatVOMelding> voMeldingen)
	{
		verzameling.setAantalMeldingen(voMeldingen.size());
		verzameling.setVoExamenMeldingen(voMeldingen);
		for (BronExamenresultaatVOMelding voMelding : voMeldingen)
		{
			voMelding.saveOrUpdate();
			BronUtils.updateStatussenNaAanmaken(voMelding);
			for (BronVakGegegevensVOMelding vakMelding : voMelding.getVakgegevens())
				vakMelding.saveOrUpdate();
		}
	}

	private void maakVavoMeldingAan(BronExamenverzameling verzameling,
			List<BronAanleverMelding> vavoMeldingen, Examendeelname deelname)
	{
		if (deelname.getVerbintenis().getBronStatus().isBekendInBron())
		{
			BronAanleverMelding melding = controllerBve.createExamenMeldingen(deelname);
			if (melding != null)
			{
				melding.setExamenverzameling(verzameling);
				vavoMeldingen.add(melding);
				run.info("Voor VAVO melding aangemaakt voor deelnemer: "
					+ deelname.getVerbintenis().getDeelnemer().getDeelnemernummer());
			}
			else
				run.info("Er is geen melding aangemaakt voor deelnemer: "
					+ deelname.getVerbintenis().getDeelnemer().getDeelnemernummer());
		}
		else
			run.info("Er is geen melding aangemaakt voor deelnemer: "
				+ deelname.getVerbintenis().getDeelnemer().getDeelnemernummer()
				+ " omdat er geen inschrijving bekend is bij BRON");
	}

	private void maakVoMeldingAan(BronExamenverzameling verzameling,
			List<BronExamenresultaatVOMelding> voMeldingen, Examendeelname deelname)
	{
		BronExamenresultaatVOMelding melding = controllerVo.createExamenMeldingen(deelname);
		if (melding != null)
		{
			melding.setExamenverzameling(verzameling);
			voMeldingen.add(melding);
			run.info("Voor VO melding aangemaakt voor deelnemer: "
				+ deelname.getVerbintenis().getDeelnemer().getDeelnemernummer());
		}
		else
			run.info("Er is geen melding aangemaakt voor deelnemer: "
				+ deelname.getVerbintenis().getDeelnemer().getDeelnemernummer());
	}

	private Examendeelname getExamendeelname(Long deelnameId)
	{
		ExamendeelnameDataAccessHelper examendeelnameHelper =
			DataAccessRegistry.getHelper(ExamendeelnameDataAccessHelper.class);
		Examendeelname deelname = examendeelnameHelper.get(Examendeelname.class, deelnameId);
		return deelname;
	}

	private List<Long> getExamendeelnameIds(SoortOnderwijsTax soortOnderwijs)
	{
		ExamendeelnameDataAccessHelper examendeelnameHelper =
			DataAccessRegistry.getHelper(ExamendeelnameDataAccessHelper.class);
		ExamendeelnameZoekFilter filter = createFilter();
		filter.setSoortOnderwijsTax(Arrays.asList(soortOnderwijs));
		List<Long> examendeelnameIds = examendeelnameHelper.getExamenDeelnameIds(filter);
		return examendeelnameIds;
	}

	private BronExamenverzameling createExamenVerzameling(SoortOnderwijsTax soortOnderwijs)
	{
		BronExamenverzameling verzameling = new BronExamenverzameling();
		verzameling.setAanleverpunt(getBronAanleverpunt());
		verzameling.setSchooljaar(schooljaar);
		verzameling.setBronOnderwijssoort(bronOnderwijssoort);
		verzameling.setSoortOnderwijs(soortOnderwijs);
		verzameling.saveOrUpdate();
		return verzameling;
	}

	private ExamendeelnameZoekFilter createFilter()
	{
		ExamendeelnameZoekFilter filter = new ExamendeelnameZoekFilter();
		filter.setAuthorizationContext(new OrganisatieEenheidLocatieAuthorizationContext(
			new AlwaysGrantedSecurityCheck()));
		filter.setSchooljaar(schooljaar);
		filter.setBronOnderwijssoort(bronOnderwijssoort);
		filter.setAlleenGewijzigde(alleenGewijzigdeDeelnames);
		return filter;
	}

	/**
	 * Niet altijd is het examenjaar ingevuld voordat de examenbatch aangemaakt wordt. In
	 * deze methode wordt het examenjaar gezet op de meest waarschijnlijke datum. Mantis
	 * 60564 was de oorspronkelijke melding waarin dit naar voren kwam.
	 */
	private void repareerExamenjaar(Examendeelname deelname)
	{
		TimeUtil timeUtil = TimeUtil.getInstance();

		Verbintenis verbintenis = deelname.getVerbintenis();
		Deelnemer deelnemer = verbintenis.getDeelnemer();

		if (deelname.getDatumUitslag() != null)
		{
			deelname.setExamenjaar(timeUtil.getYear(deelname.getDatumUitslag()));
		}
		else if (verbintenis.getGeplandeEinddatum() != null)
		{
			deelname.setExamenjaar(timeUtil.getYear(verbintenis.getGeplandeEinddatum()));
		}
		else if (deelname.getDatumLaatsteStatusovergang() != null)
		{
			deelname.setExamenjaar(timeUtil.getYear(deelname.getDatumLaatsteStatusovergang()));
		}
		else
		{
			deelname.setExamenjaar(timeUtil.getYear(timeUtil.currentDate()));
		}
		run.error("Examendeelname van deelnemer " + deelnemer.getDeelnemernummer()
			+ " had geen examenjaar, aangepast naar " + deelname.getExamenjaar());
		deelname.saveOrUpdate();
	}

	private void extractData(JobExecutionContext context)
	{
		schooljaar = getValue(context, "schooljaar");
		bronOnderwijssoort = getValue(context, "bronOnderwijssoort");
		alleenGewijzigdeDeelnames = getValue(context, "alleenGewijzigdeDeelnames");
		soortOnderwijsTaxList = getValue(context, "soortOnderwijsTax");
		examendeelnames = getValue(context, "examendeelnames");
	}

	private BronAanleverpunt getBronAanleverpunt()
	{
		List<BronAanleverpunt> aanleverpunten =
			DataAccessRegistry.getHelper(BronAanleverpuntDataAccessHelper.class)
				.getBronAanleverpunten();
		if (aanleverpunten.size() > 0)
			return aanleverpunten.get(0);
		return null;
	}
}
