package nl.topicus.eduarte.krd.web.pages.beheer.mutatielog;

import java.util.Arrays;
import java.util.List;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.dataproviders.GeneralDataProvider;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.util.StringUtil;
import nl.topicus.cobra.web.components.form.AutoFieldSet;
import nl.topicus.cobra.web.components.form.RenderMode;
import nl.topicus.cobra.web.components.menu.AbstractMenuBar;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.cobra.web.components.panels.bottomrow.ButtonAlignment;
import nl.topicus.cobra.web.components.panels.bottomrow.OpslaanButton;
import nl.topicus.cobra.web.components.panels.bottomrow.PageLinkButton;
import nl.topicus.eduarte.app.EduArteApp;
import nl.topicus.eduarte.app.EduArteContext;
import nl.topicus.eduarte.app.EduArteScheduler;
import nl.topicus.eduarte.dao.helpers.DeelnemerDataAccessHelper;
import nl.topicus.eduarte.dao.helpers.GroepDataAccessHelper;
import nl.topicus.eduarte.dao.helpers.OpleidingDataAccessHelper;
import nl.topicus.eduarte.entities.groep.Groep;
import nl.topicus.eduarte.entities.groep.Groepsdeelname;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis;
import nl.topicus.eduarte.entities.opleiding.Opleiding;
import nl.topicus.eduarte.entities.personen.Deelnemer;
import nl.topicus.eduarte.krd.dao.helpers.MutatieLogVerwerkersLogDataAccessHelper;
import nl.topicus.eduarte.krd.entities.mutatielog.MutatieLogVerwerkersLog;
import nl.topicus.eduarte.krd.entities.mutatielog.MutatieLogVerwerkersTesterData;
import nl.topicus.eduarte.krd.entities.mutatielog.Range;
import nl.topicus.eduarte.krd.jobs.MutatieLogVerwerkenJob;
import nl.topicus.eduarte.krd.principals.beheer.systeemtabellen.MutatieLogVerwerkenPrincipal;
import nl.topicus.eduarte.krd.web.components.panels.datapanel.table.MutatieLogVerwerkersLogQueueTable;
import nl.topicus.eduarte.krd.zoekfilters.MutatieLogVerwerkersLogZoekFilter;
import nl.topicus.eduarte.web.components.menu.BeheerMenu;
import nl.topicus.eduarte.web.components.menu.BeheerMenuItem;
import nl.topicus.eduarte.web.components.menu.main.CoreMainMenuItem;
import nl.topicus.eduarte.web.components.panels.EduArteDataPanel;
import nl.topicus.eduarte.web.pages.SecurePage;

import org.apache.wicket.Component;
import org.apache.wicket.Page;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.ajax.markup.html.AjaxLazyLoadPanel;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.link.IPageLink;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.quartz.SchedulerException;

/**
 * @author jutten
 */
@PageInfo(title = "Mutatielog verwerkers test", menu = "Beheer > Mutatielog verwerkers test")
@InPrincipal(MutatieLogVerwerkenPrincipal.class)
public class MutatieLogVerwerkersTesterPage extends SecurePage
{
	private Form<MutatieLogVerwerkersTesterData> form;

	@SpringBean
	private DeelnemerDataAccessHelper helper;

	private EduArteDataPanel<MutatieLogVerwerkersLog> mutatieLogVerwerkersLogDatapanel;

	public MutatieLogVerwerkersTesterPage()
	{
		super(CoreMainMenuItem.Beheer);
		MutatieLogVerwerkersTesterData data = new MutatieLogVerwerkersTesterData();

		form = new Form<MutatieLogVerwerkersTesterData>("form", Model.of(data));
		add(form);

		AutoFieldSet<MutatieLogVerwerkersTesterData> mutatieLogVerwerkersDataFields =
			new AutoFieldSet<MutatieLogVerwerkersTesterData>("inputFieldsMutatieLogVerwerkers",
				form.getModel(), "Mutatielog verwerkers Data");
		mutatieLogVerwerkersDataFields.setPropertyNames(Arrays.asList("deelnemerRange",
			"verbintenisRange", "groepscode", "opleidingcode", "groepscodeVoorGroepsdeelnames"));
		mutatieLogVerwerkersDataFields.setRenderMode(RenderMode.EDIT);

		form.add(mutatieLogVerwerkersDataFields);
		add(new LazyDataPanel("mutatieLogVerwerkersLogDatapanel"));
		createComponents();
	}

	@Override
	protected void fillBottomRow(BottomRowPanel panel)
	{
		panel.addButton(new PageLinkButton(panel, "Webservice instellingen", ButtonAlignment.LEFT,
			new IPageLink()
			{
				private static final long serialVersionUID = 1L;

				@Override
				public Page getPage()
				{
					return new MutatieLogVerwerkersSettings();
				}

				@Override
				public Class< ? extends Page> getPageIdentity()
				{
					return MutatieLogVerwerkersSettings.class;
				}
			}));

		panel.addButton(new OpslaanButton(panel, form, "Zet in wachtrij")
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void onSubmit()
			{
				MutatieLogVerwerkersTesterData data = form.getModelObject();

				if (data.getDeelnemerRange() != null && data.getDeelnemerRange().getFrom() != null)
				{
					updateDeelnemers(data.getDeelnemerRange());
				}
				if (data.getVerbintenisRange() != null
					&& data.getVerbintenisRange().getFrom() != null)
				{
					updateVerbintenissen(data.getVerbintenisRange());
				}
				if (!StringUtil.isEmpty(data.getGroepscode()))
				{
					updateGroepscode(data.getGroepscode());
				}
				if (!StringUtil.isEmpty(data.getOpleidingcode()))
				{
					updateOpleidingcode(data.getOpleidingcode());
				}
				if (!StringUtil.isEmpty(data.getGroepscodeVoorGroepsdeelnames()))
				{
					updateGroepsdeelnames(data.getGroepscodeVoorGroepsdeelnames());
				}

				setResponsePage(new MutatieLogVerwerkersTesterPage());
			}
		});

		panel.addButton(new PageLinkButton(panel, "Start jobs", ButtonAlignment.RIGHT,
			new IPageLink()
			{
				private static final long serialVersionUID = 1L;

				@Override
				public Page getPage()
				{
					try
					{
						EduArteScheduler scheduler = EduArteApp.get().getEduarteScheduler();
						scheduler.triggerJob(MutatieLogVerwerkenJob.class);
					}
					catch (SchedulerException e)
					{
						throw new RuntimeException(e);
					}

					return new MutatieLogJobPage();
				}

				@Override
				public Class< ? extends Page> getPageIdentity()
				{
					return MutatieLogJobPage.class;
				}
			}));
	}

	private void updateDeelnemers(Range deelnemerRange)
	{
		Integer from = deelnemerRange.getFrom();
		Integer to = deelnemerRange.getTo();
		if (from != null && to != null && to >= from)
		{
			Integer aantal = helper.touch(Deelnemer.class, "deelnemernummer", from, to);
			helper.batchExecute();
			getSession().info(aantal + " deelnemers aangepast");
		}
		else if (from != null)
		{
			updateDeelnemer(from);
		}
	}

	private void updateVerbintenissen(Range verbintenisRange)
	{
		Integer from = verbintenisRange.getFrom();
		Integer to = verbintenisRange.getTo();
		if (from != null && to != null && to >= from)
		{
			Integer aantal = helper.touch(Verbintenis.class, "deelnemer.deelnemernummer", from, to);
			helper.batchExecute();
			getSession().info(aantal + " verbintenissen aangepast");
		}
		else if (from != null)
		{
			updateVerbintenissen(from);
		}
	}

	private void updateGroepscode(String groepscode)
	{
		GroepDataAccessHelper groepDataAccessHelper =
			DataAccessRegistry.getHelper(GroepDataAccessHelper.class);

		Groep groep = groepDataAccessHelper.getByGroepcode(groepscode);
		if (groep != null)
		{
			groep.touch();
			groep.commit();
		}
	}

	private void updateGroepsdeelnames(String groepscode)
	{
		GroepDataAccessHelper groepDataAccessHelper =
			DataAccessRegistry.getHelper(GroepDataAccessHelper.class);

		Groep groep = groepDataAccessHelper.getByGroepcode(groepscode);
		if (groep != null)
		{
			List< ? extends Groepsdeelname> deelnames = groep.getDeelnemers();
			for (Groepsdeelname deelname : deelnames)
			{
				deelname.touch();
				deelname.commit();
			}
		}
	}

	private void updateOpleidingcode(String opleidingcode)
	{
		OpleidingDataAccessHelper opleidingDataAccessHelper =
			DataAccessRegistry.getHelper(OpleidingDataAccessHelper.class);

		List<Opleiding> opleidingen = opleidingDataAccessHelper.getOpleidingen(opleidingcode);
		if (opleidingen != null)
		{
			for (Opleiding opleiding : opleidingen)
			{
				opleiding.touch();
				opleiding.commit();
			}
		}
	}

	private void updateDeelnemer(Integer deelnemerNummer)
	{
		Deelnemer deelnemer = helper.getByDeelnemernummer(deelnemerNummer);
		if (deelnemer != null)
		{
			deelnemer.touch();
			deelnemer.commit();
		}
	}

	private void updateVerbintenissen(Integer deelnemerNummer)
	{
		Deelnemer deelnemer = helper.getByDeelnemernummer(deelnemerNummer);
		if (deelnemer != null)
		{
			List<Verbintenis> verbintenissen = deelnemer.getVerbintenissen();
			for (Verbintenis verbintenis : verbintenissen)
			{
				verbintenis.touch();
				verbintenis.commit();
			}
		}
	}

	@Override
	public AbstractMenuBar createMenu(String id)
	{
		return new BeheerMenu(id, BeheerMenuItem.MutatieLogVerwerkersTester);
	}

	class LazyDataPanel extends AjaxLazyLoadPanel
	{

		private static final long serialVersionUID = 1L;

		public LazyDataPanel(String id)
		{
			super(id);
		}

		@Override
		public Component getLazyLoadComponent(String markupId)
		{
			MutatieLogVerwerkersLogZoekFilter filter = new MutatieLogVerwerkersLogZoekFilter();
			filter.setOrganisatie(EduArteContext.get().getOrganisatie());

			GeneralDataProvider<MutatieLogVerwerkersLog, MutatieLogVerwerkersLogZoekFilter> mutatieLogVerwerkersLogProvider =
				GeneralDataProvider.of(filter, MutatieLogVerwerkersLogDataAccessHelper.class);

			mutatieLogVerwerkersLogDatapanel =
				new EduArteDataPanel<MutatieLogVerwerkersLog>(markupId,
					mutatieLogVerwerkersLogProvider, new MutatieLogVerwerkersLogQueueTable()
					{
						private static final long serialVersionUID = 1L;

						@Override
						protected void refreshDataPanel(AjaxRequestTarget target)
						{
							target.addComponent(mutatieLogVerwerkersLogDatapanel);
						}
					});
			return mutatieLogVerwerkersLogDatapanel;
		}

	}
}
