package nl.topicus.eduarte.resultaten.jobs;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.dao.helpers.SessionDataAccessHelper;
import nl.topicus.cobra.quartz.JobInfo;
import nl.topicus.cobra.util.TimeUtil;
import nl.topicus.eduarte.app.resultaat.ResultaatVersionKey;
import nl.topicus.eduarte.dao.helpers.ResultaatDataAccessHelper;
import nl.topicus.eduarte.dao.helpers.ResultaatstructuurDataAccessHelper;
import nl.topicus.eduarte.dao.helpers.SettingsDataAccessHelper;
import nl.topicus.eduarte.dao.helpers.ToetsDataAccessHelper;
import nl.topicus.eduarte.entities.jobs.JobRunClass;
import nl.topicus.eduarte.entities.jobs.logging.JobRunDetail;
import nl.topicus.eduarte.entities.personen.Deelnemer;
import nl.topicus.eduarte.entities.resultaatstructuur.Resultaat;
import nl.topicus.eduarte.entities.resultaatstructuur.Resultaatstructuur;
import nl.topicus.eduarte.entities.resultaatstructuur.Toets;
import nl.topicus.eduarte.entities.resultaatstructuur.Resultaatstructuur.Status;
import nl.topicus.eduarte.entities.settings.ResultaatControleSetting;
import nl.topicus.eduarte.jobs.EduArteJob;
import nl.topicus.eduarte.resultaten.entities.ResultatenHerberekenenJobRun;
import nl.topicus.eduarte.web.components.resultaat.ResultaatKey;
import nl.topicus.eduarte.web.components.resultaat.StructuurUpdateRecalculationManager;
import nl.topicus.eduarte.zoekfilters.ResultaatZoekFilter;

import org.apache.wicket.model.IModel;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@JobInfo(name = ResultatenHerberekenJob.JOB_NAME)
@JobRunClass(ResultatenHerberekenenJobRun.class)
public class ResultatenHerberekenJob extends EduArteJob
{
	public static final String JOB_NAME = "Resultaten herberekenen";

	@SuppressWarnings("hiding")
	private static final Logger log = LoggerFactory.getLogger(ResultatenHerberekenJob.class);

	private int count = 0;

	private Long onderwijsproductId;

	private Resultaatstructuur resultaatstructuur;

	private StructuurUpdateRecalculationManager recalcManager;

	private Boolean maakBeschikbaar;

	@Override
	protected void executeJob(JobExecutionContext context) throws JobExecutionException,
			InterruptedException
	{
		final List<Toets> toetsen = extractData(context);

		ResultatenHerberekenenJobRun run = new ResultatenHerberekenenJobRun();
		run.setGestartDoor(getMedewerker());
		run.setRunStart(TimeUtil.getInstance().currentDateTime());
		run.setSamenvatting("Resultaten herberekenen voor "
			+ resultaatstructuur.getOnderwijsproduct().getCode());
		run.setResultaatstructuurId(resultaatstructuur.getId());
		run.save();

		resultaatstructuur.setStatus(maakBeschikbaar ? Status.BESCHIKBAAR : Status.IN_ONDERHOUD);
		resultaatstructuur.saveOrUpdate();

		recalcManager = new StructuurUpdateRecalculationManager()
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void recalculate(ResultaatKey recalcKey,
					Map<ResultaatKey, IModel<List<List<Resultaat>>>> resultaten)
			{
				setStatus("Herbereken van " + recalcKey);
				super.recalculate(recalcKey, resultaten);

				count++;
				try
				{
					setProgress(count, getRecalculationSize());
				}
				catch (InterruptedException e)
				{
					throw new RecalculationInterruptedException(e);
				}
			}
		};

		try
		{
			Set<ResultaatVersionKey> keys = getResultaatKeys(toetsen);
			log.info("Entering mutex with " + keys.size() + " keys");
			getEduarteApp().getResultaatMutex().execute(keys, 3600 * 1000, new Runnable()
			{
				@Override
				public void run()
				{
					recalcResultaten(toetsen);
				}
			});
			log.info("Left mutex");
		}
		catch (RecalculationInterruptedException e)
		{
			setStatusToError(run, "Afgebroken");
			throw e.getCause();
		}
		catch (Exception e)
		{
			setStatusToError(run, "Fout: " + e.getClass().getSimpleName() + ": " + e.getMessage());
			throw new JobExecutionException(e);
		}

		run.setRunEinde(TimeUtil.getInstance().currentDateTime());
		run.setSamenvatting(run.getSamenvatting() + ": " + count + " toetsafnames verwerkt.");
		run.update();
		run.commit();
	}

	private void recalcResultaten(List<Toets> toetsen)
	{
		log.info("Inside lock: recalculating");
		recalcManager.recalculate(toetsen);

		flushHibernate();
		log.info("Inside lock: verifying");
		if (DataAccessRegistry.getHelper(SettingsDataAccessHelper.class).getSetting(
			ResultaatControleSetting.class).getValue().isActief())
		{
			Set<Toets> checkToetsen = new HashSet<Toets>();
			for (Toets curToets : toetsen)
			{
				// refetch to prevent lazy init, the session may be cleared by now
				Resultaatstructuur structuur =
					curToets.getResultaatstructuur().reget(Resultaatstructuur.class);
				checkToetsen.addAll(structuur.getToetsen());
			}
			ResultaatZoekFilter resultaatFilter =
				new ResultaatZoekFilter(new ArrayList<Toets>(checkToetsen));
			DataAccessRegistry.getHelper(ResultaatDataAccessHelper.class).verifyResultaten(
				resultaatFilter);
		}
	}

	private Set<ResultaatVersionKey> getResultaatKeys(List<Toets> toetsen)
	{
		Set<ResultaatVersionKey> ret = new HashSet<ResultaatVersionKey>();
		for (Toets curToets : toetsen)
		{
			List<Deelnemer> deelnemers =
				DataAccessRegistry.getHelper(ToetsDataAccessHelper.class)
					.getDeelnemersMetResultaten(curToets);
			for (Deelnemer curDeelnemer : deelnemers)
			{
				ret.add(new ResultaatVersionKey(curDeelnemer, curToets.getResultaatstructuur()));
			}
		}
		return ret;
	}

	private void setStatusToError(ResultatenHerberekenenJobRun run, String message)
	{
		DataAccessRegistry.getHelper(SessionDataAccessHelper.class).clearSession();
		DataAccessRegistry.getHelper(SessionDataAccessHelper.class).batchRollback();

		ResultatenHerberekenenJobRun errorRun = new ResultatenHerberekenenJobRun();
		errorRun.setGestartDoor(getMedewerker());
		errorRun.setRunStart(run.getRunStart());
		errorRun.setResultaatstructuurId(resultaatstructuur.getId());
		errorRun.setRunEinde(TimeUtil.getInstance().currentDateTime());
		errorRun.setSamenvatting(run.getSamenvatting() + ": " + message);
		errorRun.save();

		JobRunDetail detail = new JobRunDetail(errorRun);
		detail.setUitkomst("Door een fout is de herberekening afgebroken.");
		detail.save();

		detail = new JobRunDetail(errorRun);
		detail.setUitkomst(message);
		detail.save();

		resultaatstructuur =
			DataAccessRegistry.getHelper(ResultaatstructuurDataAccessHelper.class).get(
				resultaatstructuur.getId());
		resultaatstructuur.setStatus(Status.FOUTIEF);
		resultaatstructuur.update();
		resultaatstructuur.commit();
	}

	private List<Toets> extractData(JobExecutionContext context)
	{
		resultaatstructuur = getValue(context, "resultaatstructuur");
		onderwijsproductId = resultaatstructuur.getOnderwijsproduct().getId();
		maakBeschikbaar = getValue(context, "maakBeschikbaar");
		return getValue(context, "toetsen");
	}

	public Long getOnderwijsproductId()
	{
		return onderwijsproductId;
	}
}
