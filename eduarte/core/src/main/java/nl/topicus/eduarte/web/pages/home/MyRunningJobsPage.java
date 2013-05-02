package nl.topicus.eduarte.web.pages.home;

import java.util.Calendar;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.dataproviders.GeneralFilteredSortableDataProvider;
import nl.topicus.cobra.quartz.JobDescription;
import nl.topicus.cobra.quartz.MatchAllFilter;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.web.components.datapanel.CustomDataPanel;
import nl.topicus.eduarte.app.EduArteApp;
import nl.topicus.eduarte.app.EduArteContext;
import nl.topicus.eduarte.core.principals.Always;
import nl.topicus.eduarte.dao.helpers.JobRunDataAccessHelper;
import nl.topicus.eduarte.entities.jobs.logging.RapportageJobRun;
import nl.topicus.eduarte.entities.personen.Medewerker;
import nl.topicus.eduarte.jobs.MatchMedewerkerFilter;
import nl.topicus.eduarte.jobs.rapportage.DataPanelExportJob;
import nl.topicus.eduarte.jobs.rapportage.RapportageJob;
import nl.topicus.eduarte.web.components.menu.HomeMenuItem;
import nl.topicus.eduarte.web.components.panels.EduArteDataPanel;
import nl.topicus.eduarte.web.components.panels.datapanel.table.JobDescriptionTable;
import nl.topicus.eduarte.web.components.panels.datapanel.table.RapportageJobRunTable;
import nl.topicus.eduarte.web.components.panels.sidebar.rapportage.RapportageSideBar;
import nl.topicus.eduarte.zoekfilters.JobRunZoekFilter;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.AjaxSelfUpdatingTimerBehavior;
import org.apache.wicket.util.time.Duration;

/**
 * Pagina met alle lopende taken van de huidige gebruiker. Wordt nu nog gebruikt voor
 * rapportages (pdf, rtf xls etc).
 * 
 * @author hoeve
 */
@PageInfo(title = "Rapportages", menu = "Home > Rapportages > Rapportages")
@InPrincipal(Always.class)
public class MyRunningJobsPage extends AbstractHomePage<Void>
{
	private static final long serialVersionUID = 1L;

	private CustomDataPanel<RapportageJobRun> afgerondejobs;

	private JobRunZoekFilter<RapportageJobRun> getDefaultFinishedZoekFilter()
	{
		JobRunZoekFilter<RapportageJobRun> filter =
			new JobRunZoekFilter<RapportageJobRun>(RapportageJobRun.class);
		filter.setMedewerker(EduArteContext.get().getMedewerker());
		filter.setAccount(EduArteContext.get().getAccount());

		// rapportages van de laatste 48 uur.
		Calendar datenow = Calendar.getInstance();
		datenow.add(Calendar.DAY_OF_MONTH, -2);
		filter.setBeginDatum(datenow.getTime());

		filter.addOrderByProperty("runStart");

		return filter;
	}

	@SuppressWarnings("unchecked")
	public MyRunningJobsPage()
	{
		super(HomeMenuItem.Rapportages);

		Component sideBar = this.get(ID_LAYSIDE + ":" + ID_RAPPORTAGESIDEBAR);
		if (sideBar != null && sideBar instanceof RapportageSideBar)
		{
			sideBar.setVisible(false);
		}

		Medewerker medewerker = EduArteContext.get().getMedewerker();
		final CustomDataPanel<JobDescription> lopendejobs =
			new EduArteDataPanel<JobDescription>("datapanelLopende", EduArteApp
				.get()
				.getEduarteScheduler()
				.getExecutingAndWaitingJobs(
					medewerker == null ? new MatchAllFilter() : new MatchMedewerkerFilter(
						medewerker), RapportageJob.class, DataPanelExportJob.class),
				new JobDescriptionTable().setTitle("Lopende rapportages"));
		lopendejobs.setItemsPerPage(10);
		lopendejobs.setOutputMarkupId(true);
		lopendejobs.addBehaviorToTable(new AjaxSelfUpdatingTimerBehavior(Duration.seconds(10))
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void onPostProcessTarget(AjaxRequestTarget target)
			{
				target.addComponent(lopendejobs);
			}
		});
		add(lopendejobs);

		GeneralFilteredSortableDataProvider<RapportageJobRun, JobRunZoekFilter<RapportageJobRun>> provider =
			new GeneralFilteredSortableDataProvider(getDefaultFinishedZoekFilter(),
				JobRunDataAccessHelper.class);

		afgerondejobs =
			new EduArteDataPanel<RapportageJobRun>("datapanelAfgeronde", provider,
				new RapportageJobRunTable()
				{
					private static final long serialVersionUID = 1L;

					@Override
					public void updateDataPanel(AjaxRequestTarget target)
					{
						target.addComponent(getDataPanel());
					}
				});
		afgerondejobs.setItemsPerPage(10);
		afgerondejobs.setOutputMarkupId(true);
		afgerondejobs.addBehaviorToTable(new AjaxSelfUpdatingTimerBehavior(Duration.seconds(10))
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void onPostProcessTarget(AjaxRequestTarget target)
			{
				target.addComponent(getDataPanel());
			}
		});
		add(afgerondejobs);

		createComponents();
	}

	private CustomDataPanel<RapportageJobRun> getDataPanel()
	{
		return afgerondejobs;
	}
}
