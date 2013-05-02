package nl.topicus.eduarte.krd.jobs;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.entities.TransientIdObject;
import nl.topicus.cobra.modelsv2.DefaultModelManager;
import nl.topicus.cobra.modelsv2.ModelFactory;
import nl.topicus.cobra.quartz.CobraScheduler;
import nl.topicus.cobra.quartz.JobInfo;
import nl.topicus.cobra.util.HibernateObjectCopyManager;
import nl.topicus.cobra.util.TimeUtil;
import nl.topicus.eduarte.dao.helpers.CriteriumDataAccessHelper;
import nl.topicus.eduarte.dao.helpers.OpleidingDataAccessHelper;
import nl.topicus.eduarte.entities.criteriumbank.Criterium;
import nl.topicus.eduarte.entities.jobs.JobRunClass;
import nl.topicus.eduarte.entities.jobs.logging.JobRunDetail;
import nl.topicus.eduarte.entities.landelijk.Cohort;
import nl.topicus.eduarte.entities.opleiding.Opleiding;
import nl.topicus.eduarte.jobs.EduArteJob;
import nl.topicus.eduarte.krd.entities.CriteriaKopierenJobRun;
import nl.topicus.eduarte.krd.entities.CriteriaKopierenJobRunDetail;
import nl.topicus.eduarte.web.pages.shared.KopieerSettings;

import org.apache.wicket.model.IModel;
import org.quartz.JobExecutionContext;

@JobInfo(name = CriteriaKopierenJob.JOB_NAME)
@JobRunClass(CriteriaKopierenJobRun.class)
public class CriteriaKopierenJob extends EduArteJob
{
	public static final String JOB_NAME = "Criteria kopiëren";

	private CriteriaKopierenJobRun jobToRollBack;

	private IModel<List<Criterium>> criteria;

	private Cohort broncohort;

	private Cohort doelcohort;

	private int copyCount = 0;

	private int skipCount = 0;

	@Override
	protected void executeJob(JobExecutionContext context) throws InterruptedException
	{
		extractData(context);
		if (jobToRollBack != null)
		{
			jobRunRollBack();
		}
		else
		{
			CriteriaKopierenJobRun run = new CriteriaKopierenJobRun();
			run.setGestartDoor(getMedewerker());
			run.setRunStart(TimeUtil.getInstance().currentDateTime());
			run.setSamenvatting("Criteria kopiëren naar cohort " + doelcohort);
			run.save();

			Set<Opleiding> opleidingen = getValue(context, "opleidingen");
			List<Long> opleidingIds = new ArrayList<Long>();
			for (Opleiding curOpleiding : opleidingen)
				opleidingIds.add(curOpleiding.getId());

			kopieerCriteria(run, opleidingIds);

			run.setRunEinde(TimeUtil.getInstance().currentDateTime());
			run.setSamenvatting(run.getSamenvatting() + ": " + copyCount + " gekopiëerd, "
				+ skipCount + " overgeslagen.");
			run.update();
			run.commit();
		}
	}

	private void kopieerCriteria(CriteriaKopierenJobRun run, List<Long> opleidingIds)
			throws InterruptedException
	{
		int count = 0;
		for (Long curOpleidingId : opleidingIds)
		{
			Opleiding curOpleiding =
				DataAccessRegistry.getHelper(OpleidingDataAccessHelper.class).get(Opleiding.class,
					curOpleidingId);
			List<Criterium> criteriaToCopy;
			if (criteria == null)
			{
				criteriaToCopy = getCriteria(curOpleiding, broncohort);
			}
			else
			{
				criteriaToCopy = criteria.getObject();
				criteria.detach();
			}

			if (criteriaToCopy != null && !criteriaToCopy.isEmpty())
			{
				setStatus("Kopiëren van criteria van " + curOpleiding.getNaam());
				kopieerCriteria(run, criteriaToCopy, curOpleiding);
			}
			setProgress(count, opleidingIds.size());
			count = flushAndClearHibernateAndIncCount(count);
		}
	}

	private List<Criterium> getCriteria(Opleiding opleiding, Cohort cohort)
	{
		return opleiding.getLokaleCriteria(cohort);
	}

	private void kopieerCriteria(CriteriaKopierenJobRun run, List<Criterium> criteriaToCopy,
			Opleiding doelOpleiding)
	{
		HibernateObjectCopyManager copyManager =
			new HibernateObjectCopyManager(getCriteriumClasses());
		for (Criterium bronCriterium : criteriaToCopy)
		{
			// Controleer of dit criterium al bestaat bij de doelopleiding.
			if (doelOpleiding.getCriterium(bronCriterium.getNaam(), doelcohort) == null)
			{
				Criterium copy = copyManager.copyObject(bronCriterium);
				copy.setCohort(doelcohort);
				copy.setOpleiding(doelOpleiding);
				copy.setVerbintenisgebied(doelOpleiding.getVerbintenisgebied());
				ModelFactory.getCompoundChangeRecordingModel(copy,
					new DefaultModelManager(getCriteriumClasses())).saveObject();

				CriteriaKopierenJobRunDetail detail = new CriteriaKopierenJobRunDetail(run);
				detail.setUitkomst("Criterium gekopiëerd: " + copy.getNaam());
				detail.setCriteriumId(copy.getId());
				copyCount++;
				detail.save();
			}
			else
			{
				CriteriaKopierenJobRunDetail detail = new CriteriaKopierenJobRunDetail(run);
				detail.setUitkomst("Criterium " + bronCriterium.getNaam()
					+ " overgeslagen omdat een criterium met deze naam al bestaat");
				skipCount++;
				detail.save();
			}
		}
	}

	@SuppressWarnings("unchecked")
	private Class< ? extends TransientIdObject>[] getCriteriumClasses()
	{
		return new Class[] {Criterium.class};
	}

	private void jobRunRollBack()
	{
		for (JobRunDetail curDetail : jobToRollBack.getDetails())
		{
			if (curDetail instanceof CriteriaKopierenJobRunDetail)
			{
				CriteriaKopierenJobRunDetail curCopyDetail =
					(CriteriaKopierenJobRunDetail) curDetail;
				if (curCopyDetail.getCriteriumId() != null)
				{
					Criterium deleteCriterium =
						DataAccessRegistry.getHelper(CriteriumDataAccessHelper.class).get(
							Criterium.class, curCopyDetail.getCriteriumId());
					if (deleteCriterium != null)
					{
						setStatus("Verwijderen van bestaand criterium " + deleteCriterium.getNaam());
						ModelFactory.getCompoundChangeRecordingModel(deleteCriterium,
							new DefaultModelManager(getCriteriumClasses())).deleteObject();
					}
				}
			}
		}
		jobToRollBack.setTeruggedraaid(true);
		jobToRollBack.commit();
	}

	private void extractData(JobExecutionContext context)
	{
		if (hasValue(context, CobraScheduler.ROLL_BACK_KEY))
		{
			this.jobToRollBack = getValue(context, CobraScheduler.ROLL_BACK_KEY);
		}
		else
		{
			KopieerSettings settings = getValue(context, "kopieerSettings");
			broncohort = settings.getBronCohort();
			doelcohort = settings.getDoelCohort();
			List<Criterium> criteriaList = getValue(context, "criteria");
			if (criteriaList != null)
				criteria = ModelFactory.getListModel(criteriaList);
		}
	}
}
