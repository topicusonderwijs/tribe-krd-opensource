/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.web.pages.shared.jobs;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.modelsv2.DefaultModelManager;
import nl.topicus.cobra.modelsv2.ModelFactory;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.util.ComponentUtil;
import nl.topicus.cobra.web.components.form.AutoFieldSet;
import nl.topicus.cobra.web.components.form.RenderMode;
import nl.topicus.cobra.web.components.form.modifier.ValidateModifier;
import nl.topicus.cobra.web.components.form.modifier.VisibilityModifier;
import nl.topicus.cobra.web.components.menu.AbstractMenuBar;
import nl.topicus.cobra.web.components.panels.bottomrow.AnnulerenButton;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.cobra.web.components.panels.bottomrow.OpslaanButton;
import nl.topicus.cobra.web.pages.IEditPage;
import nl.topicus.eduarte.app.EduArteApp;
import nl.topicus.eduarte.app.EduArteScheduler;
import nl.topicus.eduarte.core.principals.Always;
import nl.topicus.eduarte.dao.helpers.JobScheduleDataAccessHelper;
import nl.topicus.eduarte.entities.jobs.PersistentJobSchedule;
import nl.topicus.eduarte.entities.jobs.ScheduleType;
import nl.topicus.eduarte.jobs.EduArteJob;
import nl.topicus.eduarte.web.components.autoform.EduArteAjaxRefreshModifier;
import nl.topicus.eduarte.web.pages.AbstractDynamicContextPage;
import nl.topicus.eduarte.web.pages.SubpageContext;

import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.validation.validator.RangeValidator;
import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@PageInfo(title = "Taakschema beheer", menu = "verschillende paden")
@InPrincipal(Always.class)
public class JobScheduleBeheerPage extends AbstractDynamicContextPage<Void> implements IEditPage
{
	private static final long serialVersionUID = 1L;

	private static final Logger log = LoggerFactory.getLogger(JobScheduleBeheerPage.class);

	private AbstractJobBeheerPage< ? > overzichtPage;

	private Form<PersistentJobSchedule> form;

	public JobScheduleBeheerPage(Class< ? extends EduArteJob> type,
			AbstractJobBeheerPage< ? > overzichtPage)
	{
		super(new SubpageContext(overzichtPage));
		this.overzichtPage = overzichtPage;
		JobScheduleDataAccessHelper helper =
			DataAccessRegistry.getHelper(JobScheduleDataAccessHelper.class);
		PersistentJobSchedule jobSchedule = helper.getSchedule(type);
		if (jobSchedule == null)
		{
			jobSchedule = helper.createJobSchedule(type);
		}
		form =
			new Form<PersistentJobSchedule>("form", ModelFactory.getCompoundModel(jobSchedule,
				new DefaultModelManager(PersistentJobSchedule.class)));
		add(form);

		AutoFieldSet<PersistentJobSchedule> autoFieldSet =
			new AutoFieldSet<PersistentJobSchedule>("inputFields", form.getModel(),
				"Schedule instellingen");
		autoFieldSet.setRenderMode(RenderMode.EDIT);
		autoFieldSet.addFieldModifier(new VisibilityModifier(new AbstractReadOnlyModel<Boolean>()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public Boolean getObject()
			{
				PersistentJobSchedule jobSch = form.getModelObject();
				return ScheduleType.Dagelijks.equals(jobSch.getScheduleType());
			}
		}, "hour", "minutes"));
		autoFieldSet.addFieldModifier(new VisibilityModifier(new AbstractReadOnlyModel<Boolean>()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public Boolean getObject()
			{
				PersistentJobSchedule jobSch = form.getModelObject();
				return ScheduleType.Interval.equals(jobSch.getScheduleType());
			}
		}, "intervalMinutes"));
		autoFieldSet.addFieldModifier(new EduArteAjaxRefreshModifier("scheduleType", form));
		autoFieldSet.addFieldModifier(new ValidateModifier(new RangeValidator<Integer>(0, 23),
			"hour"));
		autoFieldSet.addFieldModifier(new ValidateModifier(new RangeValidator<Integer>(0, 59),
			"minutes"));
		autoFieldSet.addFieldModifier(new ValidateModifier(new RangeValidator<Integer>(1, 1439),
			"intervalMinutes"));
		form.add(autoFieldSet);
		createComponents();
	}

	@Override
	protected void fillBottomRow(BottomRowPanel panel)
	{
		panel.addButton(new AnnulerenButton(panel, overzichtPage));
		panel.addButton(new OpslaanButton(panel, form)
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void onSubmit()
			{
				PersistentJobSchedule schedule = form.getModelObject();
				schedule.saveOrUpdate();
				schedule.commit();

				// get de scheduler en reschedule de job
				EduArteScheduler scheduler = EduArteApp.get().getEduarteScheduler();
				try
				{
					scheduler.reschedule(schedule);
				}
				catch (SchedulerException e)
				{
					log.error("Fout tijdens re-schedulen van de job voor instelling "
						+ schedule.getOrganisatie().getNaam() + ". " + e.getLocalizedMessage());
					JobScheduleBeheerPage.this
						.warn("De wijzigingen zijn opgeslagen, maar de job kon niet direct aangepast worden en zal nog starten op de oude tijd.");
					return;
				}

				setResponsePage(JobScheduleBeheerPage.this.overzichtPage);
			}
		});
	}

	@Override
	public AbstractMenuBar createMenu(String id)
	{
		return overzichtPage.createMenu(id);
	}

	@Override
	protected void onDetach()
	{
		super.onDetach();
		ComponentUtil.detachQuietly(overzichtPage);
	}
}
