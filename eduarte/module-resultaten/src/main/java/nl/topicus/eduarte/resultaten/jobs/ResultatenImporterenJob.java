package nl.topicus.eduarte.resultaten.jobs;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.modelsv2.ModelFactory;
import nl.topicus.cobra.quartz.JobInfo;
import nl.topicus.cobra.quartz.ThreePartSegment;
import nl.topicus.cobra.util.StringUtil;
import nl.topicus.cobra.util.TimeUtil;
import nl.topicus.cobra.web.components.file.SimpleCSVFile;
import nl.topicus.eduarte.dao.helpers.MedewerkerDataAccessHelper;
import nl.topicus.eduarte.entities.inschrijving.OnderwijsproductAfname;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis;
import nl.topicus.eduarte.entities.jobs.JobRunClass;
import nl.topicus.eduarte.entities.jobs.logging.JobRun;
import nl.topicus.eduarte.entities.onderwijsproduct.Onderwijsproduct;
import nl.topicus.eduarte.entities.personen.Deelnemer;
import nl.topicus.eduarte.entities.personen.Medewerker;
import nl.topicus.eduarte.entities.resultaatstructuur.Resultaat;
import nl.topicus.eduarte.entities.resultaatstructuur.ResultaatFormatException;
import nl.topicus.eduarte.entities.resultaatstructuur.Resultaatstructuur;
import nl.topicus.eduarte.entities.resultaatstructuur.Toets;
import nl.topicus.eduarte.entities.resultaatstructuur.Resultaat.Resultaatsoort;
import nl.topicus.eduarte.resultaten.entities.ResultatenImporterenJobRun;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

@JobInfo(name = ResultatenImporterenJob.JOB_NAME)
@JobRunClass(ResultatenImporterenJobRun.class)
public class ResultatenImporterenJob extends AbstractResultatenInlezenJob
{
	public static final String JOB_NAME = "Resultaten importeren";

	private static class ImportResultaat
	{
		private Integer score;

		private Object resultaat;

		private Medewerker medewerker;

		private Date datumBehaald;

		public ImportResultaat(Integer score, Object resultaat, Medewerker medewerker,
				Date datumBehaald)
		{
			this.score = score;
			this.resultaat = resultaat;
			this.medewerker = medewerker;
			this.datumBehaald = datumBehaald;
		}

		public Integer getScore()
		{
			return score;
		}

		public Object getResultaat()
		{
			return resultaat;
		}

		public Medewerker getMedewerker()
		{
			return medewerker;
		}

		public Date getDatumBehaald()
		{
			return datumBehaald;
		}
	}

	private String filename;

	@Override
	protected void executeJob(JobExecutionContext context) throws JobExecutionException
	{
		ResultatenImporterenJobRun run = new ResultatenImporterenJobRun();
		run.setGestartDoor(getMedewerker());
		run.setRunStart(TimeUtil.getInstance().currentDateTime());
		run.setSamenvatting("Resultaten importeren");
		run.save();
		try
		{
			SimpleCSVFile file = getValue(context, "bestand");
			filename = file.getBestandsnaam();

			setStatus("Leest bestand " + filename);
			if (!valideerBestand(run, file.getLines()))
				return;
			Map<ResultaatPogingKey, Object> cijfers = verwerkBestand(run, file.getLines());

			if (!cijfers.isEmpty())
				importeerCijfers(cijfers);

			run.setRunEinde(TimeUtil.getInstance().currentDateTime());
			run.setSamenvatting(run.getSamenvatting() + ": " + cijfers.size()
				+ " resultaten ingelezen");
			run.update();
			run.commit();
		}
		catch (Exception e)
		{
			setStatusToError(run, "Fout: " + e.getClass().getSimpleName() + ": " + e.getMessage());
			throw new JobExecutionException(e);
		}
	}

	@Override
	protected JobRun createJobRun()
	{
		return new ResultatenImporterenJobRun();
	}

	private boolean valideerBestand(ResultatenImporterenJobRun run, List<String> lines)
	{
		if (lines.isEmpty())
		{
			setStatusToError(run, "Bestand " + filename + " is leeg");
			return false;
		}
		boolean isValid = true;
		for (int i = 0; i < lines.size(); i++)
		{
			String line = lines.get(i);
			String[] velden = line.split(";", -1);
			if (velden.length != 7)
			{
				run.error(String.format("Regel %d: bevat niet 7 velden", i + 1));
				isValid = false;
				continue;
			}
			else if (!StringUtil.isNumeric(velden[0]))
			{
				run.error(String.format("Regel %d: %s is geen getal", i + 1, velden[0]));
				isValid = false;
				continue;
			}
			else if (!"-".equals(velden[4]) && !StringUtil.isNumeric(velden[4]))
			{
				run.error(String.format("Regel %d: %s is geen getal of '-'", i + 1, velden[4]));
				isValid = false;
				continue;
			}
			else if (TimeUtil.getInstance().parseDateString(velden[6], "yyyy-mm-dd") == null)
			{
				run.error(String.format("Regel %d: %s is geen datum in het formaat jjjj-mm-dd",
					i + 1, velden[6]));
				isValid = false;
				continue;
			}
		}

		if (!isValid)
		{
			setStatusToError(run, "Bestand " + filename
				+ " bevat ongeldige records. Klik hier voor meer informatie");
		}
		return isValid;
	}

	private Map<ResultaatPogingKey, Object> verwerkBestand(ResultatenImporterenJobRun run,
			List<String> lines) throws InterruptedException
	{
		Map<ResultaatPogingKey, Object> ret = new HashMap<ResultaatPogingKey, Object>();
		setStatus("Importeert bestand " + filename);
		int counter = 0;
		for (String line : lines)
		{
			setProgress(counter, lines.size(), ThreePartSegment.FIRST_PART);
			counter = flushAndClearHibernateAndIncCount(counter);
			String[] velden = line.split(";", -1);
			Deelnemer deelnemer = getDeelnemer(Integer.parseInt(velden[0]));
			if (deelnemer == null)
			{
				run
					.error(String
						.format("Regel %d: %s geen deelnemer gevonden", counter, velden[0]));
				continue;
			}
			Onderwijsproduct onderwijsproduct = getOnderwijsproduct(velden[1]);
			if (onderwijsproduct == null)
			{
				run.error(String.format("Regel %d: %s geen onderwijsproduct gevonden", counter,
					velden[1]));
				continue;
			}
			if (getMedewerker(velden[2]) == null)
			{
				run.error(String.format("Regel %d: geen medewerker gevonden met afkoring %s",
					counter, velden[2]));
				continue;
			}

			Date datumBehaald = TimeUtil.getInstance().parseDateString(velden[6], "yyyy-mm-dd");
			List<Resultaatstructuur> resultaatstructuren =
				getResultaatstructuren(deelnemer, onderwijsproduct, datumBehaald);
			if (resultaatstructuren.isEmpty())
			{
				run.error(String.format("Regel %d: Kan geen resultaatstructuren vinden", counter));
				continue;
			}

			boolean toetsGevonden = false;
			for (Resultaatstructuur structuur : resultaatstructuren)
			{
				if (!structuur.isBeschikbaar())
				{
					run.error(String.format("Regel %d: Resultaatstructuur %s is niet beschikbaar",
						counter, structuur.toString()));
					continue;
				}
				List<Toets> toetsen = findToetsen(structuur, velden[3]);
				for (Toets toets : toetsen)
				{
					if (verwerkToets(run, deelnemer, toets, counter, velden, ret))
						toetsGevonden = true;
				}
			}
			if (!toetsGevonden)
			{
				run.error(String.format("Regel %d: Kan geen toetsen vinden met de code %s",
					counter, velden[3]));
				continue;
			}
		}
		flushAndClearHibernate();
		return ret;
	}

	private boolean verwerkToets(ResultatenImporterenJobRun run, Deelnemer deelnemer, Toets toets,
			int counter, String[] velden, Map<ResultaatPogingKey, Object> ret)
	{
		if (toets.getHeeftResultaten(deelnemer))
		{
			run.error(String.format("Regel %d: Er zijn al resultaten ingevoerd", counter));
			return false;
		}

		int pogingNr = "-".equals(velden[4]) ? determinePoging(toets) : Integer.parseInt(velden[4]);

		if (!toets.isInvulbaar(pogingNr))
		{
			run.error(String.format("Regel %d: toets heeft geen resultaat voor poging %d", counter,
				pogingNr));
			return false;
		}

		try
		{
			Object converted = toets.convertWaarde(velden[5], pogingNr);
			Object cijfer = null;
			Integer score = null;
			if (converted instanceof Integer)
				score = (Integer) converted;
			else
				cijfer = converted;
			ret.put(new ResultaatPogingKey(ModelFactory.getModel(toets), ModelFactory
				.getModel(deelnemer), pogingNr), new ImportResultaat(score, cijfer,
				getMedewerker(velden[2]), TimeUtil.getInstance().parseDateString(velden[6],
					"yyyy-mm-dd")));
			return true;
		}
		catch (ResultaatFormatException e)
		{
			run.error(String.format("Regel %d: %s", counter, e.getMessage()));
			return false;
		}
	}

	private int determinePoging(Toets toets)
	{
		if (toets.isVariant())
			return toets.getVariantVoorPoging();
		return 0;
	}

	private List<Toets> findToetsen(Resultaatstructuur structuur, String referentieCode)
	{
		List<Toets> ret = new ArrayList<Toets>();
		for (Toets curToets : structuur.getToetsen())
		{
			if (referentieCode.equals(curToets.getReferentieCode() + "_"
				+ curToets.getReferentieVersie()))
				ret.add(curToets);
		}
		return ret;
	}

	private List<Resultaatstructuur> getResultaatstructuren(Deelnemer deelnemer,
			Onderwijsproduct onderwijsproduct, Date datumBehaald)
	{
		List<Resultaatstructuur> ret = new ArrayList<Resultaatstructuur>();
		List<OnderwijsproductAfname> afnames =
			getOnderwijsproductAfnames(deelnemer, onderwijsproduct);
		if (!afnames.isEmpty())
		{
			for (OnderwijsproductAfname curAfname : afnames)
			{
				Resultaatstructuur structuur =
					getResultaatstructuur(onderwijsproduct, curAfname.getCohort());
				if (structuur != null)
					ret.add(structuur);
			}
			return ret;
		}

		List<Verbintenis> verbintenissen =
			new ArrayList<Verbintenis>(deelnemer.getVerbintenissen());
		Iterator<Verbintenis> it = verbintenissen.iterator();
		while (it.hasNext())
		{
			if (!it.next().isActief(datumBehaald))
				it.remove();
		}
		if (verbintenissen.isEmpty())
			verbintenissen = deelnemer.getVerbintenissen();

		for (Verbintenis curVerbintenis : verbintenissen)
		{
			Resultaatstructuur structuur =
				getResultaatstructuur(onderwijsproduct, curVerbintenis.getCohort());
			if (structuur != null)
				ret.add(structuur);
		}
		return ret;
	}

	public Medewerker getMedewerker(String afkorting)
	{
		return DataAccessRegistry.getHelper(MedewerkerDataAccessHelper.class).batchGetByAfkorting(
			afkorting);
	}

	@Override
	protected void createCijfer(ResultaatPogingKey key, Object cijfer)
	{
		ImportResultaat importResultaat = (ImportResultaat) cijfer;
		Resultaat resultaat = new Resultaat();
		resultaat.setToets(key.getToets());
		resultaat.setActueel(true);
		resultaat.setBerekening("Dit resultaat is geimporteerd via een resultatenimport");
		if (importResultaat.getScore() == null)
			resultaat.setCijferOfWaarde(importResultaat.getResultaat());
		else
			resultaat.setScore(importResultaat.getScore());
		resultaat.setDeelnemer(key.getDeelnemer());
		resultaat.setGeldend(true);
		resultaat.setHerkansingsnummer(key.getPogingNr() - 1);
		resultaat.setIngevoerdDoor(importResultaat.getMedewerker());
		resultaat.setSoort(Resultaatsoort.Ingevoerd);
		resultaat.setDatumBehaald(importResultaat.getDatumBehaald());
		resultaat.save();
	}
}
