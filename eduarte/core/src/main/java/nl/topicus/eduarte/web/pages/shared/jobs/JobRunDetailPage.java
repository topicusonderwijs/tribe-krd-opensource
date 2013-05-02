/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.web.pages.shared.jobs;

import java.util.ArrayList;
import java.util.List;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.modelsv2.ModelFactory;
import nl.topicus.cobra.quartz.CobraScheduler;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.web.components.datapanel.DefaultCustomDataPanelId;
import nl.topicus.cobra.web.components.form.AutoFieldSet;
import nl.topicus.cobra.web.components.form.RenderMode;
import nl.topicus.cobra.web.components.menu.AbstractMenuBar;
import nl.topicus.cobra.web.components.panels.bottomrow.AbstractConfirmationLinkButton;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.cobra.web.components.panels.bottomrow.ButtonAlignment;
import nl.topicus.cobra.web.components.panels.bottomrow.TerugButton;
import nl.topicus.cobra.web.components.shortcut.CobraKeyAction;
import nl.topicus.eduarte.app.EduArteApp;
import nl.topicus.eduarte.core.principals.Always;
import nl.topicus.eduarte.entities.jobs.logging.JobRun;
import nl.topicus.eduarte.entities.jobs.logging.JobRunDetail;
import nl.topicus.eduarte.entities.jobs.logging.TerugdraaibareJobRun;
import nl.topicus.eduarte.jobs.EduArteJob;
import nl.topicus.eduarte.web.components.panels.EduArteDataPanel;
import nl.topicus.eduarte.web.pages.AbstractDynamicContextPage;
import nl.topicus.eduarte.web.pages.SecurePage;
import nl.topicus.eduarte.web.pages.SubpageContext;
import nl.topicus.eduarte.zoekfilters.JobRunDetailZoekFilter;

import org.apache.wicket.extensions.markup.html.repeater.data.table.filter.IFilterStateLocator;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.quartz.JobDataMap;
import org.quartz.SchedulerException;

@PageInfo(title = "Jobrun details", menu = {"Beheer", "verschillende paden"})
@InPrincipal(Always.class)
public class JobRunDetailPage<T extends JobRun> extends AbstractDynamicContextPage<T>
{
	private static final long serialVersionUID = 1L;

	private SecurePage returnPage;

	private JobDetailsInterfaceCreator<T> interfaceCreator;

	@SuppressWarnings("unchecked")
	public JobRunDetailPage(T run, SecurePage returnPage,
			JobDetailsInterfaceCreator<T> interfaceCreator)
	{
		super(ModelFactory.getCompoundModel(run), new SubpageContext(returnPage));
		this.returnPage = returnPage;
		this.interfaceCreator = interfaceCreator;

		AutoFieldSet<T> fieldset =
			new AutoFieldSet<T>("fieldset", getContextModel(), "Run details");
		fieldset.setRenderMode(RenderMode.DISPLAY);
		List<String> properties = new ArrayList<String>();
		properties.add("runStart");
		properties.add("runEinde");
		properties.add("gestartDoorOmschrijving");
		for (String property : interfaceCreator.addExtraFieldSetProperties())
		{
			properties.add(property);
		}
		fieldset.setPropertyNames(properties);
		fieldset.setSortAccordingToPropertyNames(true);
		add(fieldset);

		AutoFieldSet< ? > extraFieldSet = interfaceCreator.addExtraFieldSet(getJobRun());
		if (extraFieldSet != null)
			add(extraFieldSet);
		else
			add(new WebMarkupContainer("extrafieldset").setVisible(false));

		IDataProvider<JobRunDetail> dataProvider = interfaceCreator.createDetailsDataProvider(run);
		EduArteDataPanel<JobRunDetail> datapanel =
			new EduArteDataPanel<JobRunDetail>("details", new DefaultCustomDataPanelId(returnPage
				.getClass(), "details"), dataProvider, interfaceCreator.getDetailContent());
		add(datapanel);

		if (dataProvider instanceof IFilterStateLocator)
		{
			add(interfaceCreator
				.createDetailsFilterPanel(
					"filter",
					(JobRunDetailZoekFilter< ? extends JobRunDetail>) ((IFilterStateLocator) dataProvider)
						.getFilterState(), datapanel));
		}
		else
		{
			add(new WebMarkupContainer("filter").setVisible(false));
		}

		createComponents();
	}

	@Override
	public AbstractMenuBar createMenu(String id)
	{
		return returnPage.createMenu(id);
	}

	@Override
	protected void fillBottomRow(BottomRowPanel panel)
	{
		if (isTerugdraaibaar())
		{
			panel.addButton(new AbstractConfirmationLinkButton(panel, "Terugdraaien",
				CobraKeyAction.GEEN, ButtonAlignment.LEFT,
				"Weet u zeker dat u deze Jobrun wilt terug draaien?")
			{
				private static final long serialVersionUID = 1L;

				@Override
				protected void onClick()
				{
					JobRun jobRun = getJobRun();
					Class< ? extends EduArteJob> type = jobRun.getJobClass();
					JobDataMap datamap = new JobDataMap();
					datamap.put(CobraScheduler.ROLL_BACK_KEY, jobRun);
					try
					{
						EduArteApp.get().getEduarteScheduler().triggerJob(type, datamap);
						info("De taak is op de achtergrond gestart. De voortgang van de taak kunt u hieronder zien.");
					}
					catch (SchedulerException e)
					{
						error("Er is een fout opgetreden bij het starten van de taak.");
					}
					setResponsePage(returnPage);
				}
			});
		}
		panel.addButton(new TerugButton(panel, returnPage));
		interfaceCreator.addExtraBottomRowButtons(panel, getJobRun());
	}

	/**
	 * @return of de jobrun terugdraaibaar is (en dus niet al eerder teruggedraaid is)
	 */
	protected boolean isTerugdraaibaar()
	{
		JobRun jobRun = (JobRun) getDefaultModelObject();
		if (jobRun instanceof TerugdraaibareJobRun)
		{
			return !((TerugdraaibareJobRun) jobRun).isTeruggedraaid();

		}
		return false;
	}

	@Override
	protected void onDetach()
	{
		super.onDetach();
		returnPage.detach();
	}

	private T getJobRun()
	{
		return getContextModelObject();
	}

	public SecurePage getReturnPage()
	{
		return returnPage;
	}

	@Override
	public boolean supportsBookmarks()
	{
		return false;
	}
}
