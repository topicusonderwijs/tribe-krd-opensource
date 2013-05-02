package nl.topicus.eduarte.web.pages.shared.jobs;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import nl.topicus.cobra.dataproviders.GeneralDataProvider;
import nl.topicus.cobra.dataproviders.GeneralFilteredSortableDataProvider;
import nl.topicus.cobra.quartz.CobraJob;
import nl.topicus.cobra.quartz.JobDescription;
import nl.topicus.cobra.quartz.JobDescriptionFilter;
import nl.topicus.cobra.quartz.JobInfo;
import nl.topicus.cobra.quartz.MatchAllFilter;
import nl.topicus.cobra.reflection.Property;
import nl.topicus.cobra.util.StringUtil;
import nl.topicus.cobra.web.behaviors.AppendingAttributeModifier;
import nl.topicus.cobra.web.components.datapanel.CustomDataPanelContentDescription;
import nl.topicus.cobra.web.components.datapanel.CustomDataPanelPageLinkRowFactory;
import nl.topicus.cobra.web.components.form.AutoFieldSet;
import nl.topicus.cobra.web.components.form.AutoForm;
import nl.topicus.cobra.web.components.form.RenderMode;
import nl.topicus.cobra.web.components.form.modifier.FieldAdapter;
import nl.topicus.cobra.web.components.form.modifier.FieldModifier;
import nl.topicus.cobra.web.components.panels.bottomrow.AbstractBottomRowButton;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.cobra.web.components.panels.bottomrow.OpslaanButton;
import nl.topicus.eduarte.app.EduArteApp;
import nl.topicus.eduarte.app.EduArteScheduler;
import nl.topicus.eduarte.dao.helpers.JobRunDataAccessHelper;
import nl.topicus.eduarte.dao.helpers.JobRunDetailDataAccessHelper;
import nl.topicus.eduarte.entities.jobs.logging.JobRun;
import nl.topicus.eduarte.entities.jobs.logging.JobRunDetail;
import nl.topicus.eduarte.jobs.EduArteJob;
import nl.topicus.eduarte.web.components.panels.EduArteDataPanel;
import nl.topicus.eduarte.web.components.panels.datapanel.table.JobDescriptionTable;
import nl.topicus.eduarte.web.components.panels.datapanel.table.JobRunDetailTable;
import nl.topicus.eduarte.web.components.panels.datapanel.table.JobRunTable;
import nl.topicus.eduarte.web.components.panels.filter.JobRunDetailZoekFilterPanel;
import nl.topicus.eduarte.web.components.panels.filter.JobRunFilterPanel;
import nl.topicus.eduarte.web.pages.SecurePage;
import nl.topicus.eduarte.zoekfilters.JobRunDetailZoekFilter;
import nl.topicus.eduarte.zoekfilters.JobRunZoekFilter;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.AjaxSelfUpdatingTimerBehavior;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.navigation.paging.IPageable;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.IDetachable;
import org.apache.wicket.model.Model;
import org.apache.wicket.util.lang.Bytes;
import org.apache.wicket.util.time.Duration;
import org.quartz.JobDataMap;
import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractJobBeheerPanel<T extends JobRun> extends Panel implements
		JobDetailsInterfaceCreator<T>
{
	private static final long serialVersionUID = 1L;

	private static final Logger log = LoggerFactory.getLogger(AbstractJobBeheerPanel.class);

	private Class< ? extends EduArteJob> jobClass;

	private AutoFieldSet<JobDataMap> inputFields;

	private Form<JobDataMap> form;

	private JobRunTable<T> jobRunTable;

	public AbstractJobBeheerPanel(String id, Class< ? extends EduArteJob> jobClass, String message)
	{
		super(id);
		this.jobClass = jobClass;

		JobDataMap datamap = createDataMap();
		form = new Form<JobDataMap>("form", new Model<JobDataMap>(datamap));

		// mantis 49129
		form.setMaxSize(Bytes.megabytes(20));

		WebMarkupContainer inputContainer = new WebMarkupContainer("inputContainer")
		{
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isVisible()
			{
				return super.isVisible() && form.getModelObject() != null;
			}
		};
		form.add(inputContainer);

		add(form);
		inputFields =
			new AutoFieldSet<JobDataMap>("inputFields", form.getModel(), getJobInfo().name());
		inputFields.setRenderMode(RenderMode.EDIT);
		inputFields.addFieldModifier(new FieldAdapter()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public <F> boolean isApplicable(AutoFieldSet<F> fieldSet, Property<F, ? , ? > property,
					Action action)
			{
				return action.equals(Action.INCLUSION);
			}

			@Override
			public <F> boolean isIncluded(AutoFieldSet<F> fieldSet, Property<F, ? , ? > property)
			{
				AutoForm autoForm = property.getAnnotation(AutoForm.class);
				if (autoForm != null && !autoForm.include())
					return false;
				return !property.getDeclaringClass().isAssignableFrom(JobDataMap.class)
					&& !property.getName().equals("attached");
			}
		});
		for (FieldModifier curModifier : getFieldModifiers())
			inputFields.addFieldModifier(curModifier);
		inputContainer.add(inputFields);
		if (!StringUtil.isEmpty(message))
			inputFields.add(new AppendingAttributeModifier("class", "contHalf"));

		WebMarkupContainer warningContainer = new WebMarkupContainer("warningContainer");
		warningContainer.setVisible(!StringUtil.isEmpty(message));
		warningContainer.add(new Label("message", message));
		inputContainer.add(warningContainer);

		final EduArteDataPanel<JobDescription> jobs =
			new EduArteDataPanel<JobDescription>("lopendeTaken", EduArteApp.get()
				.getEduarteScheduler()
				.getExecutingAndWaitingJobs(getJobDescriptionFilter(), jobClass),
				new JobDescriptionTable());
		add(jobs);

		JobRunZoekFilter<T> filter = createJobRunFilter();
		IDataProvider<T> provider = GeneralDataProvider.of(filter, getAccessHelper());
		jobRunTable = new JobRunTable<T>(getJobRunClass());
		EduArteDataPanel<T> taken = new EduArteDataPanel<T>("taken", provider, jobRunTable);

		taken.setReuseItems(true);
		taken.setRowFactory(new CustomDataPanelPageLinkRowFactory<T>(JobRunDetailPage.class)
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick(Item<T> item)
			{
				setResponsePage(new JobRunDetailPage<T>(item.getModelObject(),
					(SecurePage) getPage(), AbstractJobBeheerPanel.this));
			}
		});
		taken.setOutputMarkupId(true);
		taken.setItemsPerPage(10);
		taken.addBehaviorToTable(new AjaxSelfUpdatingTimerBehavior(Duration.seconds(1))
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void onPostProcessTarget(AjaxRequestTarget target)
			{
				if (getUpdateInterval().seconds() < 10)
					setUpdateInterval(getUpdateInterval().add(Duration.ONE_SECOND));
				else
					setUpdateInterval(Duration.seconds(10));
				target.addComponent(jobs);
			}
		});
		add(taken);

		JobRunFilterPanel filterPanel = createJobRunFilterPanel("filter", filter, taken);

		add(filterPanel);
	}

	protected JobRunFilterPanel createJobRunFilterPanel(String id, JobRunZoekFilter<T> filter,
			EduArteDataPanel<T> taken)
	{
		return new JobRunFilterPanel(id, filter, taken);
	}

	public void setMaxSize(Bytes size)
	{
		form.setMultiPart(true);
		form.setMaxSize(size);
	}

	protected JobDescriptionFilter getJobDescriptionFilter()
	{
		return new MatchAllFilter();
	}

	@SuppressWarnings("unchecked")
	protected Class< ? extends JobRunDataAccessHelper<T, ? >> getAccessHelper()
	{
		return (Class) JobRunDataAccessHelper.class;
	}

	protected List< ? extends FieldModifier> getFieldModifiers()
	{
		return Collections.emptyList();
	}

	protected abstract JobDataMap createDataMap();

	@SuppressWarnings("unchecked")
	protected JobRunZoekFilter<T> createJobRunFilter()
	{
		JobRunZoekFilter<T> filter = new JobRunZoekFilter(EduArteJob.getJobRunClass(jobClass));
		filter.addOrderByProperty("runStart");
		filter.setAscending(false);
		return filter;
	}

	public AbstractBottomRowButton createTaakStartenButton(BottomRowPanel panel)
	{
		return createTaakStartenButton(panel, "Taak nu starten");
	}

	public AbstractBottomRowButton createTaakStartenButton(BottomRowPanel panel, String buttonTekst)
	{
		return new OpslaanButton(panel, form, buttonTekst)
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void onSubmit()
			{
				JobDataMap datamap = form.getModelObject();
				if (datamap instanceof IDetachable)
					((IDetachable) datamap).detach();
				form.setModelObject(createDataMap());
				startJob(datamap == null ? new JobDataMap() : datamap);
			}
		};
	}

	protected void startJob(JobDataMap datamap)
	{
		try
		{
			EduArteScheduler scheduler = EduArteApp.get().getEduarteScheduler();
			scheduler.triggerJob(AbstractJobBeheerPanel.this.jobClass, datamap);
		}
		catch (SchedulerException e)
		{
			log.error(e.toString(), e);
			error("Taak kon niet opgestart worden.");
			error(e.getLocalizedMessage());
		}
		info("De taak is op de achtergrond gestart. De voortgang van de taak kunt u hieronder zien.");
	}

	public AutoFieldSet<JobDataMap> getAutoFieldSet()
	{
		return inputFields;
	}

	public JobInfo getJobInfo()
	{
		return CobraJob.getJobInfo(jobClass);
	}

	public Class< ? extends EduArteJob> getJobClass()
	{
		return jobClass;
	}

	@SuppressWarnings("unchecked")
	public Class<T> getJobRunClass()
	{
		return (Class<T>) EduArteJob.getJobRunClass(jobClass);
	}

	public boolean isSchedulable()
	{
		return CobraJob.getJobScheduleClass(jobClass) != null;
	}

	@Override
	public CustomDataPanelContentDescription<JobRunDetail> getDetailContent()
	{
		return new JobRunDetailTable();
	}

	@Override
	public IDataProvider<JobRunDetail> createDetailsDataProvider(T jobrun)
	{
		JobRunDetailZoekFilter<JobRunDetail> filter =
			new JobRunDetailZoekFilter<JobRunDetail>(jobrun);
		filter.addOrderByProperty("lastModifiedAt");
		filter.setAscending(false);
		return GeneralFilteredSortableDataProvider.of(filter, JobRunDetailDataAccessHelper.class);
	}

	@Override
	public Component createDetailsFilterPanel(String id,
			JobRunDetailZoekFilter< ? extends JobRunDetail> filter, IPageable pageable)
	{
		return new JobRunDetailZoekFilterPanel(id, filter, pageable);
	}

	@Override
	public void addExtraBottomRowButtons(BottomRowPanel panel, T run)
	{
	}

	@Override
	public Collection<String> addExtraFieldSetProperties()
	{
		return new ArrayList<String>();
	}

	public JobRunTable<T> getJobRunTable()
	{
		return jobRunTable;
	}

	public Form<JobDataMap> getForm()
	{
		return form;
	}

}
