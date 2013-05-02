package nl.topicus.eduarte.web.pages.shared.jobs;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import nl.topicus.cobra.web.components.datapanel.CustomDataPanelContentDescription;
import nl.topicus.cobra.web.components.form.AutoFieldSet;
import nl.topicus.cobra.web.components.form.modifier.FieldModifier;
import nl.topicus.cobra.web.components.menu.main.MainMenuItem;
import nl.topicus.cobra.web.components.panels.bottomrow.AbstractBottomRowButton;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.cobra.web.components.panels.bottomrow.PageLinkButton;
import nl.topicus.eduarte.dao.helpers.JobRunDataAccessHelper;
import nl.topicus.eduarte.entities.jobs.logging.JobRun;
import nl.topicus.eduarte.entities.jobs.logging.JobRunDetail;
import nl.topicus.eduarte.jobs.EduArteJob;
import nl.topicus.eduarte.web.components.panels.EduArteDataPanel;
import nl.topicus.eduarte.web.components.panels.datapanel.table.JobRunDetailTable;
import nl.topicus.eduarte.web.components.panels.filter.JobRunDetailZoekFilterPanel;
import nl.topicus.eduarte.web.components.panels.filter.JobRunFilterPanel;
import nl.topicus.eduarte.web.pages.SecurePage;
import nl.topicus.eduarte.zoekfilters.JobRunDetailZoekFilter;
import nl.topicus.eduarte.zoekfilters.JobRunZoekFilter;

import org.apache.wicket.Component;
import org.apache.wicket.Page;
import org.apache.wicket.markup.html.link.IPageLink;
import org.apache.wicket.markup.html.navigation.paging.IPageable;
import org.apache.wicket.util.lang.Bytes;
import org.quartz.JobDataMap;

public abstract class AbstractJobBeheerPage<T extends JobRun> extends SecurePage
{
	private AbstractJobBeheerPanel<T> jobPanel;

	Class< ? extends EduArteJob> jobClass;

	public AbstractJobBeheerPage(MainMenuItem selectedMainMenu,
			Class< ? extends EduArteJob> jobClass, String message)
	{
		super(selectedMainMenu);
		this.jobClass = jobClass;
		add(jobPanel = new AbstractJobBeheerPanel<T>("jobPanel", jobClass, message)
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected JobDataMap createDataMap()
			{
				return AbstractJobBeheerPage.this.createDataMap();
			}

			@Override
			public Collection<String> addExtraFieldSetProperties()
			{
				return AbstractJobBeheerPage.this.addExtraFieldSetProperties();
			}

			@Override
			public void addExtraBottomRowButtons(BottomRowPanel panel, T run)
			{
				AbstractJobBeheerPage.this.addExtraBottomRowButtons(panel, run);
			}

			@Override
			public CustomDataPanelContentDescription<JobRunDetail> getDetailContent()
			{
				return AbstractJobBeheerPage.this.getDetailContent();
			}

			@Override
			protected JobRunFilterPanel createJobRunFilterPanel(String id,
					JobRunZoekFilter<T> filter, EduArteDataPanel<T> taken)
			{
				return AbstractJobBeheerPage.this.createJobRunFilterPanel(id, filter, taken);
			}

			@Override
			protected JobRunZoekFilter<T> createJobRunFilter()
			{
				return AbstractJobBeheerPage.this.createJobRunFilter();
			}

			@Override
			protected Class< ? extends JobRunDataAccessHelper<T, ? >> getAccessHelper()
			{
				return AbstractJobBeheerPage.this.getAccessHelper();
			}

			@Override
			public AutoFieldSet< ? > addExtraFieldSet(T jobRun)
			{
				return AbstractJobBeheerPage.this.addExtraFieldSet(jobRun);
			}

			@Override
			protected List< ? extends FieldModifier> getFieldModifiers()
			{
				return AbstractJobBeheerPage.this.getFieldModifiers();
			}
		});
		createComponents();
	}

	protected List<FieldModifier> getFieldModifiers()
	{
		return Collections.emptyList();
	}

	@SuppressWarnings("unused")
	protected AutoFieldSet< ? > addExtraFieldSet(T jobRun)
	{
		return null;
	}

	protected JobRunFilterPanel createJobRunFilterPanel(String id, JobRunZoekFilter<T> filter,
			EduArteDataPanel<T> taken)
	{
		return new JobRunFilterPanel(id, filter, taken);
	}

	protected Component createDetailsFilterPanel(String id,
			JobRunDetailZoekFilter< ? extends JobRunDetail> filter, IPageable pageable)
	{
		return new JobRunDetailZoekFilterPanel(id, filter, pageable);
	}

	@SuppressWarnings("unchecked")
	protected JobRunZoekFilter<T> createJobRunFilter()
	{
		JobRunZoekFilter<T> filter = new JobRunZoekFilter(EduArteJob.getJobRunClass(jobClass));
		filter.addOrderByProperty("runStart");
		filter.setAscending(false);
		return filter;
	}

	@SuppressWarnings("unchecked")
	protected Class< ? extends JobRunDataAccessHelper<T, ? >> getAccessHelper()
	{
		return (Class) JobRunDataAccessHelper.class;
	}

	/**
	 * Zet de maximale upload grootte voor een multipart form.
	 */
	protected void setMaxSize(Bytes size)
	{
		jobPanel.setMaxSize(size);
	}

	protected abstract JobDataMap createDataMap();

	public Collection<String> addExtraFieldSetProperties()
	{
		return new ArrayList<String>();
	}

	public CustomDataPanelContentDescription<JobRunDetail> getDetailContent()
	{
		return new JobRunDetailTable();
	}

	@SuppressWarnings("unused")
	protected void addExtraBottomRowButtons(BottomRowPanel panel, T run)
	{
	}

	@Override
	protected void fillBottomRow(BottomRowPanel panel)
	{
		if (jobPanel.isSchedulable())
			panel.addButton(new PageLinkButton(panel, "Tijdinstellingen", new IPageLink()
			{
				private static final long serialVersionUID = 1L;

				@Override
				public Page getPage()
				{
					return new JobScheduleBeheerPage(jobPanel.getJobClass(),
						AbstractJobBeheerPage.this);
				}

				@Override
				public Class< ? extends Page> getPageIdentity()
				{
					return JobScheduleBeheerPage.class;
				}
			}));
		panel.addButton(createTaakStartenButton(panel));
	}

	protected AbstractBottomRowButton createTaakStartenButton(BottomRowPanel panel)
	{
		return jobPanel.createTaakStartenButton(panel, getTaakStartenButtonTekst());
	}

	public final void startJob(JobDataMap datamap)
	{
		jobPanel.startJob(datamap);
	}

	public AbstractJobBeheerPanel<T> getJobPanel()
	{
		return jobPanel;
	}

	protected String getTaakStartenButtonTekst()
	{
		return "Taak nu starten";
	}
}