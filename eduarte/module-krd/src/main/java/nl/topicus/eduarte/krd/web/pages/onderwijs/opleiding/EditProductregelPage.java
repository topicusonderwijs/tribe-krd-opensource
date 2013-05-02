package nl.topicus.eduarte.krd.web.pages.onderwijs.opleiding;

import java.util.List;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.modelsv2.DefaultModelManager;
import nl.topicus.cobra.modelsv2.IChangeRecordingModel;
import nl.topicus.cobra.modelsv2.ModelFactory;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.util.ComponentUtil;
import nl.topicus.cobra.util.HibernateObjectCopyManager;
import nl.topicus.cobra.web.components.ComponentFactory;
import nl.topicus.cobra.web.components.choice.EnumCombobox;
import nl.topicus.cobra.web.components.choice.JaNeeCombobox;
import nl.topicus.cobra.web.components.panels.bottomrow.AnnulerenButton;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.cobra.web.components.panels.bottomrow.OpslaanButton;
import nl.topicus.cobra.web.components.panels.bottomrow.VerwijderButton;
import nl.topicus.cobra.web.components.quicksearch.ISelectListener;
import nl.topicus.cobra.web.components.shortcut.ActionKey;
import nl.topicus.cobra.web.components.shortcut.CobraKeyAction;
import nl.topicus.cobra.web.pages.IEditPage;
import nl.topicus.eduarte.entities.onderwijsproduct.Onderwijsproduct;
import nl.topicus.eduarte.entities.onderwijsproduct.OnderwijsproductStatus;
import nl.topicus.eduarte.entities.opleiding.OpleidingAanbod;
import nl.topicus.eduarte.entities.organisatie.OrganisatieEenheid;
import nl.topicus.eduarte.entities.productregel.Productregel;
import nl.topicus.eduarte.entities.productregel.ToegestaanDeelgebied;
import nl.topicus.eduarte.entities.productregel.ToegestaanOnderwijsproduct;
import nl.topicus.eduarte.entities.productregel.Productregel.TypeProductregel;
import nl.topicus.eduarte.entities.taxonomie.Taxonomie;
import nl.topicus.eduarte.krd.principals.onderwijs.OpleidingWrite;
import nl.topicus.eduarte.krd.web.components.choice.SoortProductregelCombobox;
import nl.topicus.eduarte.krd.web.validators.ProductregelsUniqueValidator;
import nl.topicus.eduarte.krd.web.validators.ValidVariableNameValidator;
import nl.topicus.eduarte.util.OrganisatieEenheidLocatieUtil;
import nl.topicus.eduarte.web.components.choice.OrganisatieEenheidCombobox;
import nl.topicus.eduarte.web.components.menu.OpleidingMenuItem;
import nl.topicus.eduarte.web.components.quicksearch.onderwijsproduct.OnderwijsproductSearchEditor;
import nl.topicus.eduarte.web.pages.SecurePage;
import nl.topicus.eduarte.web.pages.onderwijs.opleiding.AbstractOpleidingPage;
import nl.topicus.eduarte.zoekfilters.OnderwijsproductZoekFilter;
import nl.topicus.eduarte.zoekfilters.OrganisatieEenheidLocatieAuthorizationContext;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.form.validation.AbstractFormValidator;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;

/**
 * Pagina voor het bewerken of aanmaken van een productregel bij een opleiding.
 * 
 * @author loite
 */
@PageInfo(title = "Productregel bewerken", menu = {
	"Onderwijs > [opleiding] > Productregels > Toevoegen",
	"Onderwijs > [opleiding] > Productregels > [productregel] > Bewerken"})
@InPrincipal(OpleidingWrite.class)
public class EditProductregelPage extends AbstractOpleidingPage implements IEditPage
{
	private static final long serialVersionUID = 1L;

	private final Form<Productregel> form;

	private final SecurePage returnToPage;

	private final OrganisatieEenheidLocatieAuthorizationContext authorizationContext;

	private final class AanbodModel extends LoadableDetachableModel<List<OpleidingAanbod>>
	{
		private static final long serialVersionUID = 1L;

		@Override
		protected List<OpleidingAanbod> load()
		{
			return getContextOpleiding().getAanbod();
		}
	}

	private final IModel<List<Onderwijsproduct>> excludeProductenModel;

	private AanbodModel aanbodModel = new AanbodModel();

	private final IModel<Productregel> productRegelModel;

	private WebMarkupContainer producten;

	public EditProductregelPage(Productregel productregel, SecurePage returnToPage)
	{
		super(OpleidingMenuItem.Productregels, productregel.getOpleiding());
		this.returnToPage = returnToPage;
		productRegelModel =
			ModelFactory.getCompoundChangeRecordingModel(productregel, new DefaultModelManager(
				ToegestaanOnderwijsproduct.class, Productregel.class));
		form = new Form<Productregel>("form", productRegelModel);
		add(form);
		EnumCombobox<TypeProductregel> typeProductregel =
			new EnumCombobox<TypeProductregel>("typeProductregel", TypeProductregel.values());
		typeProductregel.setRequired(true);
		typeProductregel.setNullValid(false);
		typeProductregel.setEnabled(!productregel.isSaved());
		form.add(typeProductregel);
		RequiredTextField<Integer> volgnummer =
			new RequiredTextField<Integer>("volgnummer", Integer.class);
		volgnummer.add(new ProductregelsUniqueValidator<Integer>(form, "Productregel",
			"volgnummer", getContextOpleidingModel(), "cohort"));
		ComponentUtil.fixLength(volgnummer, 3);
		form.add(volgnummer);
		RequiredTextField<String> afkorting = new RequiredTextField<String>("afkorting");
		afkorting.add(new ProductregelsUniqueValidator<String>(form, "Productregel", "afkorting",
			getContextOpleidingModel(), "cohort"));
		afkorting.add(new ValidVariableNameValidator());
		ComponentUtil.fixLength(afkorting, Productregel.class);
		form.add(afkorting);
		RequiredTextField<String> naam = new RequiredTextField<String>("naam");
		naam.add(new ProductregelsUniqueValidator<String>(form, "Productregel", "naam",
			getContextOpleidingModel(), "cohort"));
		ComponentUtil.fixLength(naam, Productregel.class);
		form.add(naam);
		SoortProductregelCombobox soortProductregel =
			new SoortProductregelCombobox("soortProductregel", null, new PropertyModel<Taxonomie>(
				form.getModel(), "opleiding.verbintenisgebied.taxonomie"));
		soortProductregel.setNullValid(false);
		soortProductregel.setRequired(true);
		form.add(soortProductregel);
		JaNeeCombobox verplicht = new JaNeeCombobox("verplicht");
		verplicht.setNullValid(false);
		verplicht.setRequired(true);
		form.add(verplicht);
		form.add(ComponentFactory.getDataLabel("cohort.naam"));

		OrganisatieEenheidCombobox alleProductenToestaanVan =
			new OrganisatieEenheidCombobox("alleOnderwijsproductenToestaanVan")
			{
				private static final long serialVersionUID = 1L;

				@Override
				protected void onUpdate(AjaxRequestTarget target, OrganisatieEenheid newSelection)
				{
					super.onUpdate(target, newSelection);
					if (newSelection != null)
						getProductregel().getToegestaneOnderwijsproducten().clear();
					target.addComponent(producten);
				}
			};
		alleProductenToestaanVan.setNullValid(true);
		form.add(alleProductenToestaanVan);

		form.add(new AbstractFormValidator()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void validate(Form< ? > form1)
			{
				Productregel pr = (Productregel) form1.getModelObject();
				for (ToegestaanOnderwijsproduct top : pr.getToegestaneOnderwijsproducten())
				{
					if (!OrganisatieEenheidLocatieUtil.gelijkeOrganisatieEenheidLocatie(top
						.getOnderwijsproduct(), getContextOpleiding())
						&& !OrganisatieEenheidLocatieUtil.gelijkeOrganisatieEenheidLocatie(
							getContextOpleiding(), top.getOnderwijsproduct()))
					{
						form.error("Het onderwijsproductaanbod van  "
							+ top.getOnderwijsproduct().getTitel()
							+ " komt niet overeen met het opleidingaanbod");
					}
				}

			}

			@Override
			public FormComponent< ? >[] getDependentFormComponents()
			{
				return null;
			}
		});

		authorizationContext = new OrganisatieEenheidLocatieAuthorizationContext(this);
		producten = new WebMarkupContainer("onderwijsproducten")
		{
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isVisible()
			{
				return getProductregel().getAlleOnderwijsproductenToestaanVan() == null;
			}
		};
		final WebMarkupContainer productenTabel = new WebMarkupContainer("productenTabel");
		productenTabel.setOutputMarkupId(true);
		producten.setOutputMarkupPlaceholderTag(true);
		excludeProductenModel =
			new PropertyModel<List<Onderwijsproduct>>(this,
				"toegestaneOnderwijsproductenAlsProducten");
		ListView<ToegestaanOnderwijsproduct> onderwijsproducten =
			new ListView<ToegestaanOnderwijsproduct>("onderwijsproducten",
				new PropertyModel<List<ToegestaanOnderwijsproduct>>(this,
					"toegestaneOnderwijsproducten"))
			{
				private static final long serialVersionUID = 1L;

				@Override
				protected void populateItem(final ListItem<ToegestaanOnderwijsproduct> item)
				{
					OnderwijsproductSearchEditor textfield =
						createOnderwijsproductEditor(new PropertyModel<Onderwijsproduct>(item
							.getModel(), "onderwijsproduct"), excludeProductenModel);
					textfield.setEnabled(false);
					item.add(textfield);
					item.setOutputMarkupId(true);
					item.add(new AjaxLink<Void>("delete")
					{
						private static final long serialVersionUID = 1L;

						@Override
						public void onClick(AjaxRequestTarget target)
						{
							getToegestaneOnderwijsproducten().remove(item.getIndex());
							target.addComponent(productenTabel);
						}
					});
				}
			};
		productenTabel.add(onderwijsproducten);
		OnderwijsproductSearchEditor addfield =
			createOnderwijsproductEditor(new Model<Onderwijsproduct>()
			{
				private static final long serialVersionUID = 1L;

				@Override
				public void setObject(Onderwijsproduct object)
				{
					Onderwijsproduct product = object;
					if (!getToegestaneOnderwijsproductenAlsProducten().contains(product))
					{
						ToegestaanOnderwijsproduct toegestaan = new ToegestaanOnderwijsproduct();
						toegestaan.setProductregel(getProductregel());
						toegestaan.setOnderwijsproduct(product);
						getToegestaneOnderwijsproducten().add(toegestaan);
					}
				}
			}, excludeProductenModel);
		addfield.addListener(new ISelectListener()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void onUpdate(AjaxRequestTarget target)
			{
				target.addComponent(productenTabel);
			}
		});
		productenTabel.add(addfield);
		producten.add(productenTabel);
		form.add(producten);

		createComponents();
	}

	private OnderwijsproductSearchEditor createOnderwijsproductEditor(
			IModel<Onderwijsproduct> model, IModel<List<Onderwijsproduct>> excludedProductenModel)
	{
		OnderwijsproductZoekFilter zoekfilter = new OnderwijsproductZoekFilter();
		zoekfilter.setStatus(OnderwijsproductStatus.Beschikbaar);
		zoekfilter.setExcludedProductenModel(excludedProductenModel);
		final OnderwijsproductSearchEditor textfield =
			new OnderwijsproductSearchEditor("ondproduct", model, zoekfilter);
		return textfield;
	}

	private Productregel getProductregel()
	{
		return form.getModelObject();
	}

	public List<ToegestaanOnderwijsproduct> getToegestaneOnderwijsproducten()
	{
		return getProductregel().getToegestaneOnderwijsproducten();
	}

	public List<Onderwijsproduct> getToegestaneOnderwijsproductenAlsProducten()
	{
		return getProductregel().getToegestaneOnderwijsproductenAlsProducten();
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
				IChangeRecordingModel<Productregel> model =
					(IChangeRecordingModel<Productregel>) productRegelModel;
				model.saveObject();
				getProductregel().commit();
				setResponsePage(EditProductregelPage.this.returnToPage);
			}

		});
		panel.addButton(new OpslaanButton(panel, form)
		{

			private static final long serialVersionUID = 1L;

			@Override
			protected void onSubmit()
			{
				IChangeRecordingModel<Productregel> model =
					(IChangeRecordingModel<Productregel>) productRegelModel;
				model.saveObject();
				getProductregel().commit();

				HibernateObjectCopyManager copyManager =
					new HibernateObjectCopyManager(ToegestaanOnderwijsproduct.class,
						Productregel.class);

				Productregel productregel = copyManager.copyObject(getProductregel());
				// productregel.setNaam(null);
				// productregel.setAfkorting(null);

				info("Productregel opgeslagen en nieuwe productregel aangemaakt");
				setResponsePage(new EditProductregelPage(productregel,
					EditProductregelPage.this.returnToPage));
			}

			@Override
			public ActionKey getAction()
			{
				return CobraKeyAction.VOLGENDE;
			}

			@Override
			public String getLabel()
			{
				return "Opslaan en kopie maken";
			}

		});
		panel.addButton(new AnnulerenButton(panel, returnToPage));
		VerwijderButton verwijderen =
			new VerwijderButton(panel, "Verwijderen",
				"Weet u zeker dat u deze productregel wilt verwijderen?")
			{
				private static final long serialVersionUID = 1L;

				@Override
				protected void onClick()
				{
					Productregel productregel = getProductregel();
					productregel.getVerbintenisgebied().getProductregels().remove(productregel);
					for (ToegestaanOnderwijsproduct to : productregel
						.getToegestaneOnderwijsproducten())
						to.delete();
					for (ToegestaanDeelgebied td : productregel.getToegestaneDeelgebieden())
						td.delete();
					productregel.delete();
					productregel.commit();
					setResponsePage(new OpleidingProductregelsPage(getContextOpleiding()));
				}

				@Override
				public boolean isVisible()
				{
					return getProductregel().isSaved() && !getProductregel().isInGebruik();
				}

			};
		panel.addButton(verwijderen);
	}

	@Override
	public void detachModels()
	{
		super.detachModels();
		ComponentUtil.detachQuietly(productRegelModel);
		ComponentUtil.detachQuietly(authorizationContext);
		ComponentUtil.detachQuietly(aanbodModel);
	}
}
