package nl.topicus.eduarte.resultaten.jobs;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeoutException;

import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.dao.helpers.SessionDataAccessHelper;
import nl.topicus.cobra.quartz.ThreePartSegment;
import nl.topicus.cobra.util.ComponentUtil;
import nl.topicus.cobra.util.TimeUtil;
import nl.topicus.eduarte.app.resultaat.ResultaatVersionKey;
import nl.topicus.eduarte.dao.helpers.DeelnemerDataAccessHelper;
import nl.topicus.eduarte.dao.helpers.OnderwijsproductAfnameDataAccessHelper;
import nl.topicus.eduarte.dao.helpers.OnderwijsproductDataAccessHelper;
import nl.topicus.eduarte.dao.helpers.ResultaatDataAccessHelper;
import nl.topicus.eduarte.dao.helpers.ResultaatstructuurDataAccessHelper;
import nl.topicus.eduarte.dao.helpers.SettingsDataAccessHelper;
import nl.topicus.eduarte.entities.inschrijving.OnderwijsproductAfname;
import nl.topicus.eduarte.entities.jobs.logging.JobRun;
import nl.topicus.eduarte.entities.jobs.logging.JobRunDetail;
import nl.topicus.eduarte.entities.landelijk.Cohort;
import nl.topicus.eduarte.entities.onderwijsproduct.Onderwijsproduct;
import nl.topicus.eduarte.entities.personen.Deelnemer;
import nl.topicus.eduarte.entities.resultaatstructuur.Resultaat;
import nl.topicus.eduarte.entities.resultaatstructuur.Resultaatstructuur;
import nl.topicus.eduarte.entities.resultaatstructuur.Toets;
import nl.topicus.eduarte.entities.settings.ResultaatControleSetting;
import nl.topicus.eduarte.jobs.EduArteJob;
import nl.topicus.eduarte.web.components.resultaat.JobRecalculationManager;
import nl.topicus.eduarte.web.components.resultaat.ResultaatKey;
import nl.topicus.eduarte.zoekfilters.OnderwijsproductAfnameZoekFilter;
import nl.topicus.eduarte.zoekfilters.ResultaatZoekFilter;

import org.apache.wicket.model.IModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractResultatenInlezenJob extends EduArteJob
{
	@SuppressWarnings("hiding")
	private static final Logger log = LoggerFactory.getLogger(ResultatenHerberekenJob.class);

	private JobRecalculationManager recalcManager;

	protected abstract JobRun createJobRun();

	protected abstract void createCijfer(ResultaatPogingKey key, Object cijfer);

	protected Resultaatstructuur getResultaatstructuur(Onderwijsproduct product, Cohort cohort)
	{
		return DataAccessRegistry.getHelper(ResultaatstructuurDataAccessHelper.class)
			.getSummatieveResultaatstructuur(product, cohort);
	}

	protected OnderwijsproductAfname getOnderwijsproductAfname(Deelnemer deelnemer,
			Onderwijsproduct onderwijsproduct)
	{
		List<OnderwijsproductAfname> list = getOnderwijsproductAfnames(deelnemer, onderwijsproduct);
		OnderwijsproductAfname ret = null;
		for (OnderwijsproductAfname afname : list)
		{
			if (ret == null
				|| afname.getCohort().getBegindatum().after(ret.getCohort().getBegindatum()))
				ret = afname;
		}
		return ret;
	}

	protected List<OnderwijsproductAfname> getOnderwijsproductAfnames(Deelnemer deelnemer,
			Onderwijsproduct onderwijsproduct)
	{
		OnderwijsproductAfnameZoekFilter filter = new OnderwijsproductAfnameZoekFilter();
		filter.setDeelnemer(deelnemer);
		filter.setOnderwijsproduct(onderwijsproduct);
		filter.setActief(true);
		OnderwijsproductAfnameDataAccessHelper helper =
			DataAccessRegistry.getHelper(OnderwijsproductAfnameDataAccessHelper.class);
		List<OnderwijsproductAfname> list = helper.list(filter);
		return list;
	}

	protected Deelnemer getDeelnemer(int deelnemernummer)
	{
		return DataAccessRegistry.getHelper(DeelnemerDataAccessHelper.class).getByDeelnemernummer(
			deelnemernummer);
	}

	protected Onderwijsproduct getOnderwijsproduct(String code)
	{
		List<Onderwijsproduct> onderwijsproducts =
			DataAccessRegistry.getHelper(OnderwijsproductDataAccessHelper.class)
				.getOnderwijsproductByCode(code);
		if (onderwijsproducts.size() == 1)
			return onderwijsproducts.get(0);
		return null;

	}

	protected void setStatusToError(JobRun run, String message)
	{
		DataAccessRegistry.getHelper(SessionDataAccessHelper.class).clearSession();
		DataAccessRegistry.getHelper(SessionDataAccessHelper.class).batchRollback();

		JobRun errorRun = createJobRun();
		errorRun.setGestartDoor(getMedewerker());
		errorRun.setRunStart(run.getRunStart());
		errorRun.setRunEinde(TimeUtil.getInstance().currentDateTime());
		errorRun.setSamenvatting(run.getSamenvatting() + ": " + message);
		errorRun.save();

		JobRunDetail detail = new JobRunDetail(errorRun);
		detail.setUitkomst("Door een fout is het inlezen afgebroken.");
		detail.save();

		for (JobRunDetail det : run.getDetails())
		{
			det.setJobRun(errorRun);
			det.save();
		}

		detail = new JobRunDetail(errorRun);
		detail.setUitkomst(message);
		detail.save();
		detail.commit();
	}

	protected void importeerCijfers(final Map<ResultaatPogingKey, Object> cijfers)
	{
		ComponentUtil.detachQuietly(cijfers);
		recalcManager = new JobRecalculationManager()
		{
			private static final long serialVersionUID = 1L;

			private int count;

			@Override
			protected void recalculate(ResultaatKey recalcKey,
					Map<ResultaatKey, IModel<List<List<Resultaat>>>> resultaten)
			{
				setStatus("Herbereken van " + recalcKey);
				super.recalculate(recalcKey, resultaten);

				count++;
				try
				{
					setProgress(count, getRecalculationSize(), ThreePartSegment.THIRD_PART);
				}
				catch (InterruptedException e)
				{
					throw new RecalculationInterruptedException(e);
				}
			}
		};
		recalcManager.setMedewerker(getMedewerker());
		final Set<ResultaatVersionKey> versionKeys = new HashSet<ResultaatVersionKey>();
		for (ResultaatKey curKey : cijfers.keySet())
		{
			recalcManager.addRecalcuation(curKey.getToets(), curKey.getDeelnemer());
			versionKeys.add(new ResultaatVersionKey(curKey.getDeelnemer(), curKey.getToets()
				.getResultaatstructuur()));
		}

		try
		{
			log.info("Entering mutex with " + versionKeys.size() + " keys");
			getEduarteApp().getResultaatMutex().execute(versionKeys, 3600 * 1000, new Runnable()
			{
				@Override
				public void run()
				{
					log.info("Inside lock: creating");
					try
					{
						createCijfers(cijfers);
					}
					catch (InterruptedException e)
					{
						throw new RecalculationInterruptedException(e);
					}
					recalcResultaten(versionKeys);
				}
			});
			log.info("Left mutex");
		}
		catch (TimeoutException e)
		{
			throw new RuntimeException(e);
		}
	}

	protected void createCijfers(Map<ResultaatPogingKey, Object> cijfers)
			throws InterruptedException
	{
		int counter = 0;
		ComponentUtil.detachQuietly(cijfers);
		for (Map.Entry<ResultaatPogingKey, Object> curCijfer : cijfers.entrySet())
		{
			setProgress(counter, cijfers.size(), ThreePartSegment.SECOND_PART);
			counter = flushAndClearHibernateAndIncCount(counter);
			createCijfer(curCijfer.getKey(), curCijfer.getValue());
		}
		flushAndClearHibernate();
	}

	protected void recalcResultaten(Set<ResultaatVersionKey> versionKeys)
	{
		log.info("Inside lock: recalculating");
		recalcManager.detach();
		recalcManager.recalculate();

		log.info("Inside lock: verifying");
		if (DataAccessRegistry.getHelper(SettingsDataAccessHelper.class).getSetting(
			ResultaatControleSetting.class).getValue().isActief())
		{
			ResultaatstructuurDataAccessHelper helper =
				DataAccessRegistry.getHelper(ResultaatstructuurDataAccessHelper.class);
			Set<Toets> checkToetsen = new HashSet<Toets>();
			for (ResultaatVersionKey curVersionKey : versionKeys)
			{
				Resultaatstructuur structuur = helper.get(curVersionKey.getStructuurId());
				checkToetsen.addAll(structuur.getToetsen());
			}
			ResultaatZoekFilter resultaatFilter =
				new ResultaatZoekFilter(new ArrayList<Toets>(checkToetsen));
			DataAccessRegistry.getHelper(ResultaatDataAccessHelper.class).verifyResultaten(
				resultaatFilter);
		}
	}
}
