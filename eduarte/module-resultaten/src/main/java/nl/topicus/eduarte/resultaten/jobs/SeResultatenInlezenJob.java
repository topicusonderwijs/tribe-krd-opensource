package nl.topicus.eduarte.resultaten.jobs;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nl.topicus.cobra.modelsv2.ModelFactory;
import nl.topicus.cobra.quartz.JobInfo;
import nl.topicus.cobra.quartz.ThreePartSegment;
import nl.topicus.cobra.util.StringUtil;
import nl.topicus.cobra.util.TimeUtil;
import nl.topicus.cobra.web.components.file.SimpleCSVFile;
import nl.topicus.eduarte.entities.inschrijving.OnderwijsproductAfname;
import nl.topicus.eduarte.entities.jobs.JobRunClass;
import nl.topicus.eduarte.entities.jobs.logging.JobRun;
import nl.topicus.eduarte.entities.onderwijsproduct.Onderwijsproduct;
import nl.topicus.eduarte.entities.personen.Deelnemer;
import nl.topicus.eduarte.entities.resultaatstructuur.Resultaat;
import nl.topicus.eduarte.entities.resultaatstructuur.ResultaatFormatException;
import nl.topicus.eduarte.entities.resultaatstructuur.Resultaatstructuur;
import nl.topicus.eduarte.entities.resultaatstructuur.Toets;
import nl.topicus.eduarte.entities.resultaatstructuur.Resultaat.Resultaatsoort;
import nl.topicus.eduarte.entities.resultaatstructuur.Toets.SoortToets;
import nl.topicus.eduarte.resultaten.entities.SeResultatenInlezenJobRun;
import nl.topicus.eduarte.web.components.resultaat.ResultatenModel;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

@JobInfo(name = SeResultatenInlezenJob.JOB_NAME)
@JobRunClass(SeResultatenInlezenJobRun.class)
public class SeResultatenInlezenJob extends AbstractResultatenInlezenJob
{
	public static final String JOB_NAME = "SE resultaten inlezen";

	private Date datumBehaald;

	private String filename;

	@Override
	protected void executeJob(JobExecutionContext context) throws JobExecutionException
	{
		SeResultatenInlezenJobRun run = new SeResultatenInlezenJobRun();
		run.setGestartDoor(getMedewerker());
		run.setRunStart(TimeUtil.getInstance().currentDateTime());
		run.setSamenvatting("SE resultaten inlezen");
		run.save();
		try
		{
			datumBehaald = getValue(context, "datum");
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
		return new SeResultatenInlezenJobRun();
	}

	private boolean valideerBestand(SeResultatenInlezenJobRun run, List<String> lines)
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
			if (velden == null)
			{
				run.error(String.format("Regel %d: bevat geen velden", i + 1));
				isValid = false;
				continue;
			}
			else if (velden.length != 3)
			{
				run.error(String.format("Regel %d: bevat niet 3 velden", i + 1));
				isValid = false;
				continue;
			}
			else if (!StringUtil.isNumeric(velden[0]))
			{
				run.error(String.format("Regel %d: %s is geen getal", i + 1, velden[0]));
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

	private Map<ResultaatPogingKey, Object> verwerkBestand(SeResultatenInlezenJobRun run,
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
			if (velden != null && velden.length == 3)
			{
				Deelnemer deelnemer = getDeelnemer(Integer.parseInt(velden[0]));
				if (deelnemer == null)
				{
					run.error(String.format("Regel %d: %s geen deelnemer gevonden", counter,
						velden[0]));
					continue;
				}
				Onderwijsproduct onderwijsproduct = getOnderwijsproduct(velden[1]);
				if (onderwijsproduct == null)
				{
					run.error(String.format("Regel %d: %s geen onderwijsproduct gevonden", counter,
						velden[1]));
					continue;
				}
				OnderwijsproductAfname afname =
					getOnderwijsproductAfname(deelnemer, onderwijsproduct);
				if (afname == null)
				{
					run.error(String.format(
						"Regel %d: Deelnemer heeft het onderwijsproduct niet afgenomen", counter));
					continue;
				}
				Resultaatstructuur structuur =
					getResultaatstructuur(afname.getOnderwijsproduct(), afname.getCohort());
				if (structuur == null)
				{
					run.error(String.format("Regel %d: Geen resultaatstructuur gevonden", counter));
					continue;
				}
				if (!structuur.isBeschikbaar())
				{
					run.error(String.format("Regel %d: Resultaatstructuur is niet beschikbaar",
						counter));
					continue;
				}
				Toets toets = structuur.getToets(SoortToets.Schoolexamen);
				if (toets == null)
				{
					run.error(String.format(
						"Regel %d: Kan geen toets van het soort schoolexamen vinden", counter));
					continue;
				}
				if (toets.getHeeftResultaten(deelnemer))
				{
					run.error(String
						.format("Regel %d: Er zijn al SE resultaten ingevoerd", counter));
					continue;
				}
				try
				{
					Object cijfer = toets.convertWaarde(velden[2], -1);
					if (cijfer instanceof Integer)
						throw new ResultaatFormatException("Toets heeft een scoreschaal");
					ret.put(new ResultaatPogingKey(ModelFactory.getModel(toets), ModelFactory
						.getModel(deelnemer), ResultatenModel.RESULTAAT_NR), cijfer);
				}
				catch (ResultaatFormatException e)
				{
					run.error(String.format("Regel %d: %s", counter, e.getMessage()));
					continue;
				}
			}
		}
		flushAndClearHibernate();
		return ret;
	}

	@Override
	protected void createCijfer(ResultaatPogingKey key, Object cijfer)
	{
		Resultaat resultaat = new Resultaat();
		resultaat.setToets(key.getToets());
		resultaat.setActueel(true);
		resultaat.setBerekening("Dit resultaat is geimporteerd via een SE-cijfer import");
		resultaat.setCijferOfWaarde(cijfer);
		resultaat.setDeelnemer(key.getDeelnemer());
		resultaat.setGeldend(true);
		resultaat.setHerkansingsnummer(0);
		resultaat.setIngevoerdDoor(getMedewerker());
		resultaat.setSoort(Resultaatsoort.Ingevoerd);
		resultaat.setDatumBehaald(datumBehaald);
		resultaat.save();
	}
}
