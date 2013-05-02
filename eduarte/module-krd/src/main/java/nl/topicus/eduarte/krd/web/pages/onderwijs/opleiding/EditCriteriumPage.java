package nl.topicus.eduarte.krd.web.pages.onderwijs.opleiding;

import java.util.List;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.dataproviders.CollectionDataProvider;
import nl.topicus.cobra.modelsv2.DefaultModelManager;
import nl.topicus.cobra.modelsv2.IChangeRecordingModel;
import nl.topicus.cobra.modelsv2.ModelFactory;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.web.components.ComponentFactory;
import nl.topicus.cobra.web.components.datapanel.CustomDataPanelContentDescription;
import nl.topicus.cobra.web.components.datapanel.columns.AbstractCustomColumn;
import nl.topicus.cobra.web.components.datapanel.columns.CustomPropertyColumn;
import nl.topicus.cobra.web.components.form.AutoFieldSet;
import nl.topicus.cobra.web.components.form.RenderMode;
import nl.topicus.cobra.web.components.form.modifier.EnableModifier;
import nl.topicus.cobra.web.components.panels.bottomrow.AbstractAjaxLinkButton;
import nl.topicus.cobra.web.components.panels.bottomrow.AnnulerenButton;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.cobra.web.components.panels.bottomrow.ButtonAlignment;
import nl.topicus.cobra.web.components.panels.bottomrow.OpslaanButton;
import nl.topicus.cobra.web.components.panels.bottomrow.VerwijderButton;
import nl.topicus.cobra.web.components.shortcut.ActionKey;
import nl.topicus.cobra.web.components.shortcut.CobraKeyAction;
import nl.topicus.cobra.web.pages.IEditPage;
import nl.topicus.cobra.web.validators.UniqueConstraintFormValidator;
import nl.topicus.eduarte.entities.criteriumbank.Criterium;
import nl.topicus.eduarte.entities.opleiding.Opleiding;
import nl.topicus.eduarte.entities.organisatie.EntiteitContext;
import nl.topicus.eduarte.entities.productregel.Productregel;
import nl.topicus.eduarte.krd.principals.onderwijs.OpleidingWrite;
import nl.topicus.eduarte.providers.CriteriumProvider;
import nl.topicus.eduarte.web.components.autoform.EduArteAjaxRefreshModifier;
import nl.topicus.eduarte.web.components.menu.OpleidingMenuItem;
import nl.topicus.eduarte.web.components.panels.EduArteDataPanel;
import nl.topicus.eduarte.web.pages.SecurePage;
import nl.topicus.eduarte.web.pages.onderwijs.opleiding.AbstractOpleidingPage;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import org.odlabs.wiquery.ui.dialog.Dialog;

/**
 * Pagina voor het bewerken of aanmaken van een criterium bij een opleiding.
 * 
 * @author loite
 */
@PageInfo(title = "Criterium bewerken", menu = {"Onderwijs > [opleiding] > Criteria > Toevoegen",
	"Onderwijs > [opleiding] > Criteria > [criterium] > Bewerken"})
@InPrincipal(OpleidingWrite.class)
public class EditCriteriumPage extends AbstractOpleidingPage implements IEditPage,
		CriteriumProvider
{
	private static final long serialVersionUID = 1L;

	private final class VariabelenTable extends CustomDataPanelContentDescription<Productregel>
	{
		private static final long serialVersionUID = 1L;

		public VariabelenTable()
		{
			super("Variabelen");
			addColumn(new CustomPropertyColumn<Productregel>("Naam", "Productregel", "naam"));
			addColumn(new CustomPropertyColumn<Productregel>("Eindresultaat ((nominaal) cijfer)",
				"Resultaat", "criteriumbankResultaatVariabelenaam"));
			addColumn(new CustomPropertyColumn<Productregel>("Eindresultaat (tekst)", "Tekst",
				"criteriumbankTekstVariabelenaam"));
			addColumn(new CustomPropertyColumn<Productregel>("Eindresultaat (alleen cijfer)",
				"Cijfer", "criteriumbankCijferVariabelenaam"));
			addColumn(new CustomPropertyColumn<Productregel>("Eindresultaat (behaald)", "Behaald",
				"criteriumbankBehaaldVariabelenaam"));
			addColumn(new CustomPropertyColumn<Productregel>("Eindresultaat (volgnummer)",
				"Volgnummer", "criteriumbankVolgnummerVariabelenaam"));
			addColumn(new CustomPropertyColumn<Productregel>("Eindresultaat (studiepunten)",
				"Studiepunten", "criteriumbankStudiepuntenVariabelenaam"));

			addColumn(new AbstractCustomColumn<Productregel>("Toetsresultaten", "Toetsen")
			{
				private static final long serialVersionUID = 1L;

				@Override
				public void populateItem(WebMarkupContainer cellItem, String componentId,
						WebMarkupContainer row, IModel<Productregel> rowModel, int span)
				{
					Opleiding opleiding = getContextOpleiding();
					Productregel productregel = rowModel.getObject();
					String toetsen = productregel.getCriteriumbankToetsVariabelenamen(opleiding);
					cellItem.add(ComponentFactory.getDataLabel(componentId, toetsen));
				}

			});
			addColumn(new CustomPropertyColumn<Productregel>("Is ingevuld", "Is ingevuld",
				"criteriumbankIngevuldVariabelenaam"));
			addColumn(new CustomPropertyColumn<Productregel>("Gekozen onderwijsproduct (code)",
				"Code", "criteriumbankCodeVariabelenaam"));
			addColumn(new CustomPropertyColumn<Productregel>(
				"Gekozen onderwijsproduct (externe code)", "Externe code",
				"criteriumbankExterneCodeVariabelenaam"));
			addColumn(new CustomPropertyColumn<Productregel>(
				"Gekozen onderwijsproduct (taxonomiecode)", "Taxonomiecode",
				"criteriumbankTaxonomiecodeVariabelenaam"));
			addColumn(new CustomPropertyColumn<Productregel>("Minimale waarde", "Min",
				"criteriumbankMinVariabelenaam"));
			addColumn(new CustomPropertyColumn<Productregel>("Minimale tekstwaarde", "Min (tekst)",
				"criteriumbankMinTekstVariabelenaam"));
			addColumn(new AbstractCustomColumn<Productregel>("Schaal", "Schaal")
			{
				private static final long serialVersionUID = 1L;

				@Override
				public void populateItem(WebMarkupContainer cellItem, String componentId,
						WebMarkupContainer row, IModel<Productregel> rowModel, int span)
				{
					Opleiding opleiding = getContextOpleiding();
					Productregel productregel = rowModel.getObject();
					String schalen = productregel.getCriteriumbankSchaalnamen(opleiding);
					cellItem.add(ComponentFactory.getDataLabel(componentId, schalen));
				}

			});
		}
	}

	private final Dialog functiesWindow;

	private final Dialog variabelenWindow;

	private final Form<Criterium> form;

	private final SecurePage returnToPage;

	public EditCriteriumPage(Criterium criterium, SecurePage returnToPage)
	{
		super(OpleidingMenuItem.Criteria, criterium.getOpleiding());
		this.returnToPage = returnToPage;
		IModel<Criterium> criteriumModel =
			ModelFactory.getCompoundChangeRecordingModel(criterium, new DefaultModelManager(
				Criterium.class));
		add(form = new Form<Criterium>("form", criteriumModel));

		AutoFieldSet<Criterium> fieldSet =
			new AutoFieldSet<Criterium>("fieldSetLeft", criteriumModel, "Criterium");
		fieldSet.setPropertyNames("volgnummer", "naam", "cohort");
		fieldSet.setRenderMode(RenderMode.EDIT);
		fieldSet.setSortAccordingToPropertyNames(true);
		fieldSet.addFieldModifier(new EnableModifier("cohort", new Model<Boolean>(false)));
		form.add(fieldSet);

		form.add(new UniqueConstraintFormValidator(fieldSet, "Criterium", "volgnummer")
			.setProperties("opleiding", "cohort"));
		form.add(new UniqueConstraintFormValidator(fieldSet, "Criterium", "naam").setProperties(
			"opleiding", "cohort", "organisatie"));

		final TextArea<String> melding = new TextArea<String>("melding");
		melding.setOutputMarkupId(true);
		melding.add(new AjaxFormComponentUpdatingBehavior("onchange")
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void onUpdate(AjaxRequestTarget target)
			{
				target.addComponent(melding);
			}
		});
		melding.setRequired(true);
		form.add(melding);
		TextArea<String> formule = new TextArea<String>("formule");
		formule.setRequired(true);
		form.add(formule);
		fieldSet.addFieldModifier(new EduArteAjaxRefreshModifier("naam")
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void onUpdate(AjaxRequestTarget target)
			{
				if (getCriterium().getMelding() == null && getCriterium().getNaam() != null)
				{
					getCriterium().setMelding(getCriterium().getNaam() + " is niet behaald");
					target.addComponent(melding);
				}
			}
		});

		functiesWindow = new Dialog("functiesWindow");
		functiesWindow.setWidth(400);
		add(functiesWindow);

		variabelenWindow = new Dialog("variabelenWindow");
		variabelenWindow.setWidth(850);
		add(variabelenWindow);

		CollectionDataProvider<Productregel> variabelenProvider =
			new CollectionDataProvider<Productregel>(
				new LoadableDetachableModel<List<Productregel>>()
				{
					private static final long serialVersionUID = 1L;

					@Override
					protected List<Productregel> load()
					{
						return getContextOpleiding().getLandelijkeEnLokaleProductregels(
							getCriterium().getCohort());
					}
				});
		EduArteDataPanel<Productregel> variabelen =
			new EduArteDataPanel<Productregel>("variabelen", variabelenProvider,
				new VariabelenTable());
		variabelenWindow.add(variabelen);

		createComponents();
	}

	public Criterium getCriterium()
	{
		return form.getModelObject();
	}

	@Override
	protected void fillBottomRow(BottomRowPanel panel)
	{
		VerwijderButton verwijderen =
			new VerwijderButton(panel, "Verwijderen",
				"Weet u zeker dat u dit criterium wilt verwijderen?")
			{
				private static final long serialVersionUID = 1L;

				@Override
				public boolean isVisible()
				{
					return getCriterium().isSaved() && !getCriterium().isLandelijk();
				}

				@Override
				protected void onClick()
				{
					getCriterium().delete();
					getCriterium().commit();
					setResponsePage(new OpleidingCriteriaPage(getContextOpleiding()));
				}
			};
		panel.addButton(verwijderen);
		panel.addButton(new OpslaanButton(panel, form)
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void onSubmit()
			{
				IChangeRecordingModel<Criterium> model =
					(IChangeRecordingModel<Criterium>) form.getModel();
				model.saveObject();
				getCriterium().commit();
				setResponsePage(EditCriteriumPage.this.returnToPage);
			}
		});
		panel.addButton(new OpslaanButton(panel, form)
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void onSubmit()
			{
				IChangeRecordingModel<Criterium> model =
					(IChangeRecordingModel<Criterium>) form.getModel();
				model.saveObject();
				getCriterium().commit();
				Criterium criterium = new Criterium(EntiteitContext.INSTELLING);
				criterium.setOpleiding(getContextOpleiding());
				criterium.setVerbintenisgebied(getContextOpleiding().getVerbintenisgebied());
				criterium.setCohort(getCriterium().getCohort());
				criterium.setVolgnummer(getContextOpleiding().getMaxCriteriumVolgnummer(
					getCriterium().getCohort()) + 1);
				setResponsePage(new EditCriteriumPage(criterium,
					EditCriteriumPage.this.returnToPage));
			}

			@Override
			public ActionKey getAction()
			{
				return CobraKeyAction.VOLGENDE;
			}

			@Override
			public String getLabel()
			{
				return "Opslaan en nieuwe toevoegen";
			}
		});
		panel.addButton(new AnnulerenButton(panel, returnToPage));
		panel.addButton(new CriteriumTestenButton(panel, form));
		panel.addButton(new AbstractAjaxLinkButton(panel, "Functies tonen", CobraKeyAction.GEEN,
			ButtonAlignment.LEFT)
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void onClick(AjaxRequestTarget target)
			{
				functiesWindow.open(target);
			}
		});
		panel.addButton(new AbstractAjaxLinkButton(panel, "Variabelen tonen", CobraKeyAction.GEEN,
			ButtonAlignment.LEFT)
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void onClick(AjaxRequestTarget target)
			{
				variabelenWindow.open(target);
			}
		});
	}
}
