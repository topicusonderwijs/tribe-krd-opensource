package nl.topicus.eduarte.krdparticipatie.jobs;

import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.quartz.CobraScheduler;
import nl.topicus.cobra.quartz.JobInfo;
import nl.topicus.cobra.util.TimeUtil;
import nl.topicus.eduarte.dao.participatie.helpers.AbsentieMeldingDataAccessHelper;
import nl.topicus.eduarte.entities.jobs.JobRunClass;
import nl.topicus.eduarte.entities.jobs.logging.JobRunDetail;
import nl.topicus.eduarte.entities.participatie.AbsentieMelding;
import nl.topicus.eduarte.entities.participatie.Waarneming;
import nl.topicus.eduarte.jobs.EduArteJob;
import nl.topicus.eduarte.krdparticipatie.entities.AbsentiemeldingenImporterenJobRun;
import nl.topicus.eduarte.krdparticipatie.entities.AbsentiemeldingenImporterenJobRunDetail;

import org.quartz.JobExecutionContext;

@JobInfo(name = AbsentiemeldingenImporterenJob.JOB_NAME)
@JobRunClass(AbsentiemeldingenImporterenJobRun.class)
public class AbsentiemeldingenImporterenJob extends EduArteJob
{
	public static final String JOB_NAME = "Absentiemeldingen importeren";

	private AbsentiemeldingenImportFile file;

	private AbsentiemeldingenImporterenJobRun run;

	private AbsentiemeldingenImporterenJobRun jobToRollBack;

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
			run = new AbsentiemeldingenImporterenJobRun();
			run.setGestartDoor(getMedewerker());
			run.setRunStart(TimeUtil.getInstance().currentDateTime());
			run.setSamenvatting("Importeren is gestart");
			run.save();
			run.flush();

			file.importeerAbsentiemeldingen(run, this);

			run.setRunEinde(TimeUtil.getInstance().currentDateTime());
			run.setSamenvatting("Er zijn " + file.getAantalToegevoegd()
				+ " absentiemeldingen succesvol geimporteerd. Er zijn "
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
			if (curDetail instanceof AbsentiemeldingenImporterenJobRunDetail)
			{
				AbsentiemeldingenImporterenJobRunDetail curImpDetail =
					(AbsentiemeldingenImporterenJobRunDetail) curDetail;
				if (curImpDetail.getAbsentiemeldingId() != null)
				{
					AbsentieMelding deleteMelding =
						DataAccessRegistry.getHelper(AbsentieMeldingDataAccessHelper.class).get(
							AbsentieMelding.class, curImpDetail.getAbsentiemeldingId());
					for (Waarneming waarneming : deleteMelding.getWaarnemingen())
					{
						waarneming.setAbsentieMelding(null);
						waarneming.update();
					}
					deleteMelding.flush();
					deleteMelding.delete();
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
