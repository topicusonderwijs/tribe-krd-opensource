package nl.topicus.eduarte.resultaten.web.pages.beheer;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.dao.BatchDataAccessHelper;
import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.modelsv2.DefaultModelManager;
import nl.topicus.cobra.modelsv2.IChangeRecordingModel;
import nl.topicus.cobra.modelsv2.ModelFactory;
import nl.topicus.cobra.modelsv2.ModelManager;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.web.behaviors.AjaxFormComponentSaveBehaviour;
import nl.topicus.cobra.web.components.form.AutoFieldSet;
import nl.topicus.cobra.web.components.form.RenderMode;
import nl.topicus.cobra.web.components.form.VersionedForm;
import nl.topicus.cobra.web.components.form.modifier.BehaviorModifier;
import nl.topicus.cobra.web.components.form.modifier.EnableModifier;
import nl.topicus.cobra.web.components.form.modifier.HtmlClassModifier;
import nl.topicus.cobra.web.components.form.modifier.ValidateModifier;
import nl.topicus.cobra.web.components.panels.bottomrow.AnnulerenButton;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.cobra.web.components.panels.bottomrow.OpslaanButton;
import nl.topicus.cobra.web.components.panels.bottomrow.VerwijderButton;
import nl.topicus.cobra.web.pages.IEditPage;
import nl.topicus.cobra.web.validators.NumberPrecisionValidator;
import nl.topicus.cobra.web.validators.UniqueConstraintFormValidator;
import nl.topicus.eduarte.app.EduArteApp;
import nl.topicus.eduarte.app.EduArteSession;
import nl.topicus.eduarte.core.principals.beheer.onderwijs.Schalen;
import nl.topicus.eduarte.dao.helpers.ToetsDataAccessHelper;
import nl.topicus.eduarte.entities.resultaatstructuur.Resultaatstructuur;
import nl.topicus.eduarte.entities.resultaatstructuur.Schaal;
import nl.topicus.eduarte.entities.resultaatstructuur.Schaalwaarde;
import nl.topicus.eduarte.entities.resultaatstructuur.Toets;
import nl.topicus.eduarte.entities.resultaatstructuur.Resultaatstructuur.Status;
import nl.topicus.eduarte.entities.resultaatstructuur.Schaal.Schaaltype;
import nl.topicus.eduarte.resultaten.jobs.ResultatenHerberekenJob;
import nl.topicus.eduarte.resultaten.jobs.ResultatenHerberekenJobDataMap;
import nl.topicus.eduarte.resultaten.web.pages.onderwijs.AlleResultatenHerberekenOverzichtPage;
import nl.topicus.eduarte.web.components.autoform.EduArteAjaxRefreshModifier;
import nl.topicus.eduarte.web.components.menu.BeheerMenuItem;
import nl.topicus.eduarte.web.components.modalwindow.schaalwaarde.SchaalwaardeEditPanel;
import nl.topicus.eduarte.web.components.panels.datapanel.table.SchaalwaardeTable;
import nl.topicus.eduarte.web.pages.beheer.AbstractBeheerPage;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.PropertyModel;
import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@PageInfo(title = "Schaal bewerken", menu = "Beheer > Onderwijs > Schalen > bewerken")
@InPrincipal(Schalen.class)
public class SchaalEditPage extends AbstractBeheerPage<Schaal> implements IEditPage
{
	private static final Logger log = LoggerFactory.getLogger(SchaalEditPage.class);

	private Form<Schaal> form;

	private WebMarkupContainer waardenContainer;

	private ModelManager manager;

	private EduArteAjaxRefreshModifier calcRefresh;

	private AutoFieldSet<Schaal> schaalFieldSet;

	private String schaalHash;

	public SchaalEditPage(Schaal schaal)
	{
		super(BeheerMenuItem.Schalen);
		schaalHash = schaal.computeHash();
		manager = new DefaultModelManager(Schaalwaarde.class, Schaal.class);
		setDefaultModel(ModelFactory.getCompoundChangeRecordingModel(schaal, manager));

		waardenContainer = new WebMarkupContainer("waardenContainer")
		{
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isVisible()
			{
				return super.isVisible()
					&& Schaaltype.Tekstueel.equals(getSchaal().getSchaaltype());
			}
		};
		waardenContainer.setOutputMarkupPlaceholderTag(true);
		add(waardenContainer);

		waardenContainer.add(createWaardenPanel());

		form = new VersionedForm<Schaal>("form", getContextModel());
		add(form);
		form.add(createSchaalEditor());

		createComponents();
	}

	private SchaalwaardeEditPanel createWaardenPanel()
	{
		SchaalwaardeEditPanel schaalwaarden =
			new SchaalwaardeEditPanel("waarden", new PropertyModel<List<Schaalwaarde>>(
				getContextModel(), "schaalwaarden"), manager, new SchaalwaardeTable())
			{
				private static final long serialVersionUID = 1L;

				@Override
				public Schaalwaarde createNewT()
				{
					return new Schaalwaarde(getSchaal());
				}

				@Override
				public void onDelete(Schaalwaarde object, AjaxRequestTarget target)
				{
					getSchaal().prepareForSave();
					calcRefresh.refreshComponents(schaalFieldSet, target);
				}

				@Override
				protected void onSaveNew(AjaxRequestTarget target, Schaalwaarde object)
				{
					getSchaal().prepareForSave();
					calcRefresh.refreshComponents(schaalFieldSet, target);
				}

				@Override
				protected void onSaveCurrent(AjaxRequestTarget target, Schaalwaarde object)
				{
					getSchaal().prepareForSave();
					calcRefresh.refreshComponents(schaalFieldSet, target);
				}
			};
		return schaalwaarden;
	}

	private AutoFieldSet<Schaal> createSchaalEditor()
	{
		schaalFieldSet = new AutoFieldSet<Schaal>("schaal", getContextModel(), "Schaal");
		schaalFieldSet.setOutputMarkupId(true);
		schaalFieldSet.setPropertyNames("naam", "actief", "schaaltype", "minimumVoorBehaald",
			"aantalDecimalen", "minimum", "maximum");
		schaalFieldSet.setSortAccordingToPropertyNames(true);
		schaalFieldSet.setRenderMode(RenderMode.EDIT);
		schaalFieldSet.addFieldModifier(new ValidateModifier(new NumberPrecisionValidator(10),
			"minimumVoorBehaald"));
		schaalFieldSet.addFieldModifier(new ValidateModifier(new NumberPrecisionValidator(10),
			"minimum"));
		schaalFieldSet.addFieldModifier(new ValidateModifier(new NumberPrecisionValidator(10),
			"maximum"));
		if (getSchaal().isInGebruik())
		{
			info("Deze schaal is in gebruik en kan daarom beperkt bewerkt worden.");
			schaalFieldSet.addFieldModifier(new EnableModifier(false, "schaaltype",
				"aantalDecimalen"));
		}
		else
		{
			schaalFieldSet.addFieldModifier(new EduArteAjaxRefreshModifier("schaaltype",
				waardenContainer, "minimumVoorBehaald", "aantalDecimalen", "minimum", "maximum"));
		}
		calcRefresh =
			new EduArteAjaxRefreshModifier(null, "minimumVoorBehaald", "minimum", "maximum");

		schaalFieldSet.addFieldModifier(new EnableModifier(new AbstractReadOnlyModel<Boolean>()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public Boolean getObject()
			{
				return Schaaltype.Cijfer.equals(getSchaal().getSchaaltype());
			}
		}, "minimumVoorBehaald", "aantalDecimalen", "minimum", "maximum"));
		schaalFieldSet.addFieldModifier(new HtmlClassModifier("unit_max", "naam", "schaaltype",
			"minimumVoorBehaald", "aantalDecimalen", "minimum", "maximum"));
		schaalFieldSet.addFieldModifier(new BehaviorModifier(new AjaxFormComponentSaveBehaviour(),
			"naam"));
		schaalFieldSet.addFieldModifier(new BehaviorModifier(new AjaxFormComponentSaveBehaviour(),
			"minimumVoorBehaald"));
		schaalFieldSet.addFieldModifier(new BehaviorModifier(new AjaxFormComponentSaveBehaviour(),
			"minimum"));
		schaalFieldSet.addFieldModifier(new BehaviorModifier(new AjaxFormComponentSaveBehaviour(),
			"maximum"));
		form.add(new UniqueConstraintFormValidator(schaalFieldSet, "Schaal", "naam"));
		form.add(new Van1Tot10Validator());
		form.add(new SchaalwaardenValidator());
		return schaalFieldSet;
	}

	@Override
	protected void fillBottomRow(BottomRowPanel panel)
	{
		panel.addButton(new OpslaanButton(panel, form)
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void onSubmit()
			{
				Schaal schaal = getSchaal();
				boolean doeHerberekening =
					schaal.isSaved() && !schaalHash.equals(schaal.computeHash());
				schaal.prepareForSave();
				IChangeRecordingModel<Schaal> model = getSchaalModel();
				model.saveObject();
				schaal.commit();
				if (doeHerberekening)
				{
					herberekenResultaten();
					setResponsePage(AlleResultatenHerberekenOverzichtPage.class);
				}
				else
					setResponsePage(SchaalOverzichtPage.class);
			}
		});
		panel.addButton(new AnnulerenButton(panel, SchaalOverzichtPage.class));

		panel.addButton(new VerwijderButton(panel)
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void onClick()
			{
				getSchaalModel().deleteObject();
				DataAccessRegistry.getHelper(BatchDataAccessHelper.class).batchExecute();
				setResponsePage(SchaalOverzichtPage.class);
			}

			@Override
			public boolean isVisible()
			{
				return getSchaal().isSaved() && !getSchaal().isInGebruik();
			}
		});
	}

	private void herberekenResultaten()
	{
		List<Toets> gewijzigdeToetsen =
			DataAccessRegistry.getHelper(ToetsDataAccessHelper.class).getToetsenMetSchaal(
				getSchaal());
		List<ResultatenHerberekenJobDataMap> jobDataMaps =
			new ArrayList<ResultatenHerberekenJobDataMap>();
		Set<Resultaatstructuur> structuren = new HashSet<Resultaatstructuur>();
		for (Toets curToets : gewijzigdeToetsen)
			structuren.add(curToets.getResultaatstructuur());
		for (Resultaatstructuur structuur : structuren)
		{
			boolean maakBeschikbaar = Status.BESCHIKBAAR.equals(structuur.getStatus());
			structuur.touch();
			structuur.setStatus(Status.IN_HERBEREKENING);
			jobDataMaps.add(createDataMap(structuur, maakBeschikbaar, gewijzigdeToetsen));
		}
		DataAccessRegistry.getHelper(BatchDataAccessHelper.class).batchExecute();
		for (ResultatenHerberekenJobDataMap curDataMap : jobDataMaps)
		{
			try
			{
				EduArteApp.get().getEduarteScheduler().triggerJob(ResultatenHerberekenJob.class,
					curDataMap);
			}
			catch (SchedulerException e)
			{
				log.error(e.toString(), e);
				EduArteSession.get().error(
					"Taak kon niet opgestart worden voor "
						+ curDataMap.getResultaatstructuur().getOnderwijsproduct());
				EduArteSession.get().error(e.getLocalizedMessage());
			}
		}
	}

	private ResultatenHerberekenJobDataMap createDataMap(Resultaatstructuur structuur,
			boolean maakBeschikbaar, List<Toets> gewijzigdeToetsen)
	{
		List<Toets> toetsen = new ArrayList<Toets>();
		for (Toets curToets : structuur.getToetsen())
		{
			if (gewijzigdeToetsen.contains(curToets))
				toetsen.add(curToets);
		}
		return new ResultatenHerberekenJobDataMap(structuur, toetsen, maakBeschikbaar);
	}

	private Schaal getSchaal()
	{
		return getSchaalModel().getObject();
	}

	private IChangeRecordingModel<Schaal> getSchaalModel()
	{
		return (IChangeRecordingModel<Schaal>) getContextModel();
	}
}
