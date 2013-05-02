package nl.topicus.eduarte.krdparticipatie.jobs;

import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.quartz.CobraScheduler;
import nl.topicus.cobra.quartz.JobInfo;
import nl.topicus.cobra.util.TimeUtil;
import nl.topicus.eduarte.dao.participatie.helpers.WaarnemingDataAccessHelper;
import nl.topicus.eduarte.entities.jobs.JobRunClass;
import nl.topicus.eduarte.entities.jobs.logging.JobRunDetail;
import nl.topicus.eduarte.entities.participatie.Waarneming;
import nl.topicus.eduarte.jobs.EduArteJob;
import nl.topicus.eduarte.krdparticipatie.entities.KRDWaarnemingenImporterenJobRun;
import nl.topicus.eduarte.krdparticipatie.entities.KRDWaarnemingenImporterenJobRunDetail;

import org.quartz.JobExecutionContext;

@JobInfo(name = KRDWaarnemingenImporterenJob.JOB_NAME)
@JobRunClass(KRDWaarnemingenImporterenJobRun.class)
public class KRDWaarnemingenImporterenJob extends EduArteJob
{
	public static final String JOB_NAME = "Waarnemingen importeren";

	private KRDWaarnemingenImportFile file;

	private KRDWaarnemingenImporterenJobRun run;

	private KRDWaarnemingenImporterenJobRun jobToRollBack;

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
			run = new KRDWaarnemingenImporterenJobRun();
			run.setGestartDoor(getMedewerker());
			run.setRunStart(TimeUtil.getInstance().currentDateTime());
			run.setSamenvatting("Importeren is gestart");
			run.save();
			run.flush();

			file.importeerWaarnemingen(run, this);

			run.setRunEinde(TimeUtil.getInstance().currentDateTime());
			run.setSamenvatting("Er zijn " + file.getAantalToegevoegd()
				+ " waarnemingen succesvol geimporteerd. Er zijn "
				+ file.getAantalOngeldigeRegels() + " regels overgeslagen.");
			run.update();
			run.commit();
		}
	}

	private void jobRunRollBack() throws InterruptedException
	{
		int progress = 0;
		for (JobRunDetail curDetail : jobToRollBack.getDetails())
		{
			if (curDetail instanceof KRDWaarnemingenImporterenJobRunDetail)
			{
				KRDWaarnemingenImporterenJobRunDetail curImpDetail =
					(KRDWaarnemingenImporterenJobRunDetail) curDetail;
				if (curImpDetail.getWaarnemingId() != null)
				{
					Waarneming deleteWaarneming =
						DataAccessRegistry.getHelper(WaarnemingDataAccessHelper.class).get(
							Waarneming.class, curImpDetail.getWaarnemingId());
					deleteWaarneming.flush();
					deleteWaarneming.delete();
				}
			}
			progress++;
			setProgress(progress, jobToRollBack.getDetails().size());
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
			file = getValue(context, "bestand");
		}
	}

	@Override
	public void setProgress(int count, int total) throws InterruptedException
	{
		super.setProgress(count, total);
	}
}
