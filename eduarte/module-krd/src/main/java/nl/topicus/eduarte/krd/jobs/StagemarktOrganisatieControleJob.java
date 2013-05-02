package nl.topicus.eduarte.krd.jobs;

import java.util.List;

import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.quartz.JobInfo;
import nl.topicus.cobra.util.TimeUtil;
import nl.topicus.eduarte.dao.helpers.ExterneOrganisatieDataAccessHelper;
import nl.topicus.eduarte.entities.jobs.JobRunClass;
import nl.topicus.eduarte.entities.organisatie.ExterneOrganisatie;
import nl.topicus.eduarte.jobs.EduArteJob;
import nl.topicus.eduarte.krd.entities.ProductregelsKopierenJobRun;
import nl.topicus.eduarte.krd.entities.StagemarktOrganisatieControleJobRun;
import nl.topicus.eduarte.zoekfilters.ExterneOrganisatieZoekFilter;

import org.quartz.JobExecutionContext;

@JobInfo(name = StagemarktOrganisatieControleJob.JOB_NAME)
@JobRunClass(ProductregelsKopierenJobRun.class)
public class StagemarktOrganisatieControleJob extends EduArteJob
{
	public static final String JOB_NAME = "Controleren gegevens stagebedrijven";

	private int gecontroleerd = 0;

	@Override
	protected void executeJob(JobExecutionContext context)
	{
		StagemarktOrganisatieControleJobRun run = new StagemarktOrganisatieControleJobRun();
		run.setGestartDoor(getMedewerker());
		run.setRunStart(TimeUtil.getInstance().currentDateTime());
		run.setSamenvatting("Controle gegevens externe organisaties gestart.");
		run.save();

		controleerExterneOrganisaties();

		run.setRunEinde(TimeUtil.getInstance().currentDateTime());
		run.setSamenvatting(run.getSamenvatting() + ": " + gecontroleerd + " gecontroleerd.");
		run.update();
		run.commit();

	}

	private void controleerExterneOrganisaties()
	{
		ExterneOrganisatieZoekFilter filter = new ExterneOrganisatieZoekFilter();
		filter.setNogControleren(Boolean.TRUE);

		List<ExterneOrganisatie> externeOrganisaties =
			getExterneOrganisatieDataAccessHelper().list(filter);

		for (ExterneOrganisatie externeOrganisatie : externeOrganisaties)
		{
			controleerExterneOrganisatie(externeOrganisatie);
		}
	}

	private void controleerExterneOrganisatie(ExterneOrganisatie externeOrganisatie)
	{
		if (externeOrganisatie.isBpvBedrijf())
		{
			// TODO controleren gegevens bij COLO
			gecontroleerd++;
		}
	}

	private ExterneOrganisatieDataAccessHelper getExterneOrganisatieDataAccessHelper()
	{
		ExterneOrganisatieDataAccessHelper helper =
			DataAccessRegistry.getHelper(ExterneOrganisatieDataAccessHelper.class);
		return helper;
	}
}
