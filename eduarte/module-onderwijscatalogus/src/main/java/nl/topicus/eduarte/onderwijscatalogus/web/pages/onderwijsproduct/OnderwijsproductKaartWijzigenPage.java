package nl.topicus.eduarte.onderwijscatalogus.web.pages.onderwijsproduct;

import java.math.BigDecimal;
import java.util.List;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.dao.helpers.SessionDataAccessHelper;
import nl.topicus.cobra.entities.IdObject;
import nl.topicus.cobra.modelsv2.DefaultModelManager;
import nl.topicus.cobra.modelsv2.ExtendedModel;
import nl.topicus.cobra.modelsv2.IChangeRecordingModel;
import nl.topicus.cobra.modelsv2.IModificationCallback;
import nl.topicus.cobra.modelsv2.ModelFactory;
import nl.topicus.cobra.modelsv2.ModelManager;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.util.ComponentUtil;
import nl.topicus.cobra.web.components.choice.EnumRequiredCombobox;
import nl.topicus.cobra.web.components.choice.JaNeeCombobox;
import nl.topicus.cobra.web.components.form.VersionedForm;
import nl.topicus.cobra.web.components.panels.bottomrow.AnnulerenButton;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.cobra.web.components.panels.bottomrow.OpslaanButton;
import nl.topicus.cobra.web.components.panels.bottomrow.VerwijderButton;
import nl.topicus.cobra.web.components.shortcut.CobraKeyAction;
import nl.topicus.cobra.web.components.text.DatumField;
import nl.topicus.cobra.web.components.text.RequiredDatumField;
import nl.topicus.cobra.web.validators.BegindatumVoorEinddatumValidator;
import nl.topicus.cobra.web.validators.UniqueConstraintValidator;
import nl.topicus.eduarte.app.EduArteApp;
import nl.topicus.eduarte.app.EduArteModuleKey;
import nl.topicus.eduarte.app.EduArteSession;
import nl.topicus.eduarte.entities.bpv.BPVCriteriaOnderwijsproduct;
import nl.topicus.eduarte.entities.onderwijsproduct.Onderwijsproduct;
import nl.topicus.eduarte.entities.onderwijsproduct.OnderwijsproductAanbod;
import nl.topicus.eduarte.entities.onderwijsproduct.OnderwijsproductStatus;
import nl.topicus.eduarte.entities.onderwijsproduct.OnderwijsproductTaxonomie;
import nl.topicus.eduarte.entities.onderwijsproduct.OnderwijsproductZoekterm;
import nl.topicus.eduarte.entities.onderwijsproduct.SoortOnderwijsproduct;
import nl.topicus.eduarte.entities.vrijevelden.OnderwijsproductVrijVeld;
import nl.topicus.eduarte.entities.vrijevelden.VrijVeldCategorie;
import nl.topicus.eduarte.entities.vrijevelden.VrijVeldOptieKeuze;
import nl.topicus.eduarte.onderwijscatalogus.principals.onderwijs.OnderwijsproductAlgemeenWrite;
import nl.topicus.eduarte.web.components.choice.AggregatieniveauCombobox;
import nl.topicus.eduarte.web.components.choice.LeerstijlCombobox;
import nl.topicus.eduarte.web.components.choice.SoortOnderwijsproductCombobox;
import nl.topicus.eduarte.web.components.choice.SoortPraktijklokaalCombobox;
import nl.topicus.eduarte.web.components.choice.TypeLocatieCombobox;
import nl.topicus.eduarte.web.components.choice.TypeToetsCombobox;
import nl.topicus.eduarte.web.components.factory.KRDModuleComponentFactory;
import nl.topicus.eduarte.web.components.menu.OnderwijsproductMenuItem;
import nl.topicus.eduarte.web.components.modalwindow.bpvcriteria.BPVCriteriaOnderwijsproductEditPanel;
import nl.topicus.eduarte.web.components.panels.AbstractVrijVeldEntiteitPanel;
import nl.topicus.eduarte.web.components.panels.datapanel.table.BPVCriteriaOnderwijsproductTable;
import nl.topicus.eduarte.web.components.panels.datapanel.table.OrganisatieLocatieTable;
import nl.topicus.eduarte.web.components.panels.organisatielocatie.OrganisatieEenheidLocatieEntiteitEditPanel;
import nl.topicus.eduarte.web.components.panels.taxonomie.TaxonomieElementToevoegenPanel;
import nl.topicus.eduarte.web.pages.IModuleEditPage;
import nl.topicus.eduarte.web.pages.SecurePage;
import nl.topicus.eduarte.web.pages.onderwijs.onderwijscatalogus.AbstractOnderwijsproductPage;
import nl.topicus.eduarte.web.pages.onderwijs.onderwijscatalogus.OnderwijsproductKaartPage;
import nl.topicus.eduarte.web.pages.onderwijs.onderwijscatalogus.OnderwijsproductZoekenPage;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.validation.validator.RangeValidator;

/**
 * Pagina voor toevoegen/wijzigen van een onderwijsproduct
 * 
 * @author vandekamp
 */
@PageInfo(title = "Onderwijsproduct bewerken", menu = {
	"Onderwijs > Onderwijsproducten > [Onderwijsproduct] > Bewerken",
	"Onderwijs > Onderwijsproducten > toevoegen"})
@InPrincipal(OnderwijsproductAlgemeenWrite.class)
public class OnderwijsproductKaartWijzigenPage extends AbstractOnderwijsproductPage implements
		IModuleEditPage<Onderwijsproduct>
{
	private static final long serialVersionUID = 1L;

	private final VersionedForm<Onderwijsproduct> form;

	private final SecurePage returnToPage;

	private BPVCriteriaOnderwijsproductEditPanel bpvCriteriaOnderwijsproductEditPanel;

	public OnderwijsproductKaartWijzigenPage(Onderwijsproduct onderwijsproduct,
			SecurePage returnToPage)
	{
		this(onderwijsproduct, returnToPage, null);
	}

	public OnderwijsproductKaartWijzigenPage(Onderwijsproduct onderwijsproduct,
			SecurePage returnToPage, String infoMelding)
	{
		super(OnderwijsproductMenuItem.Algemeen, ModelFactory.getCompoundChangeRecordingModel(
			onderwijsproduct, createModelManager()));
		if (infoMelding != null)
			info(infoMelding);
		if (onderwijsproduct.getBegindatum() == null && !onderwijsproduct.isSaved())
		{
			onderwijsproduct.setBegindatum((EduArteSession.get().getSelectedCohortModel()
				.getObject()).getBegindatum());
		}
		this.returnToPage = returnToPage;
		form = new VersionedForm<Onderwijsproduct>("form", getContextOnderwijsproductModel());
		add(form);
		form.add(ComponentUtil.fixLength(new RequiredTextField<String>("code")
			.add(new UniqueConstraintValidator<String>(form, onderwijsproduct.getClass().getName(),
				"code", "organisatie")), Onderwijsproduct.class));
		form.add(ComponentUtil.fixLength(new RequiredTextField<String>("titel"),
			Onderwijsproduct.class));
		form.add(new TextArea<String>("omschrijving").setRequired(true));
		form.add(new TextArea<String>("zoektermenAlsString"));
		form.add(new TypeToetsCombobox("typeToets")
		{
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isEnabled()
			{
				SoortOnderwijsproduct soort = getProductModel().getObject().getSoortProduct();
				return soort == null || soort.isSummatief();
			}
		});

		bpvCriteriaOnderwijsproductEditPanel =
			new BPVCriteriaOnderwijsproductEditPanel("bpvCriteriaOnderwijsproductPanel",
				new PropertyModel<List<BPVCriteriaOnderwijsproduct>>(getProductModel(),
					"bpvCriteria"), getProductModel().getManager(),
				new BPVCriteriaOnderwijsproductTable())
			{
				private static final long serialVersionUID = 1L;

				@Override
				public BPVCriteriaOnderwijsproduct createNewT()
				{
					return new BPVCriteriaOnderwijsproduct(getProductModel().getObject());
				}

				@Override
				public boolean isVisible()
				{
					return getProductModel().getObject().getSoortProduct() != null
						&& getProductModel().getObject().getSoortProduct().isStage();
				}

			};
		bpvCriteriaOnderwijsproductEditPanel.setOutputMarkupId(true).setOutputMarkupPlaceholderTag(
			true);

		form.add(bpvCriteriaOnderwijsproductEditPanel);

		form.add(new SoortOnderwijsproductCombobox("soortProduct").connectListForAjaxRefresh(
			form.get("typeToets"), bpvCriteriaOnderwijsproductEditPanel).setRequired(true));

		form.add(new JaNeeCombobox("heeftWerkstuktitel").setRequired(true));
		form.add(new JaNeeCombobox("alleenExtern").setRequired(true));
		form.add(new EnumRequiredCombobox<OnderwijsproductStatus>("status", OnderwijsproductStatus
			.values()));
		RequiredDatumField begindatumField = new RequiredDatumField("begindatum");
		DatumField einddatumField = new DatumField("einddatum");
		form.add(begindatumField);
		form.add(einddatumField);

		WebMarkupContainer wmc = new WebMarkupContainer("creditsContainer");
		FormComponent<Integer> credits =
			ComponentUtil.fixLength(new TextField<Integer>("credits", Integer.class)
				.add(new RangeValidator<Integer>(0, 300)), Onderwijsproduct.class);
		wmc.add(credits);
		form.add(wmc.setVisible(EduArteApp.get().isModuleActive(EduArteModuleKey.HOGER_ONDERWIJS)));

		form.add(ComponentUtil.fixLength(new TextField<BigDecimal>("omvang", BigDecimal.class)
			.add(new RangeValidator<BigDecimal>(new BigDecimal(0), new BigDecimal(100000))),
			Onderwijsproduct.class));
		form.add(ComponentUtil.fixLength(new TextField<BigDecimal>("belasting", BigDecimal.class)
			.add(new RangeValidator<BigDecimal>(new BigDecimal(0), new BigDecimal(100000))),
			Onderwijsproduct.class));
		form.add(new AggregatieniveauCombobox("aggregatieniveau").setRequired(true));
		form.add(new JaNeeCombobox("startonderwijsproduct").setRequired(true));
		form.add(new LeerstijlCombobox("leerstijl"));
		form.add(ComponentUtil.fixLength(new TextField<Integer>("minimumAantalDeelnemers",
			Integer.class).add(new RangeValidator<Integer>(0, 100000)), Onderwijsproduct.class));
		form.add(ComponentUtil.fixLength(new TextField<Integer>("maximumAantalDeelnemers",
			Integer.class).add(new RangeValidator<Integer>(0, 100000)), Onderwijsproduct.class));
		form.add(new SoortPraktijklokaalCombobox("soortPraktijklokaal"));
		form.add(new TypeLocatieCombobox("typeLocatie"));
		form.add(ComponentUtil.fixLength(new TextField<BigDecimal>("kostprijs", BigDecimal.class)
			.add(new RangeValidator<BigDecimal>(new BigDecimal(0), new BigDecimal(100000))),
			Onderwijsproduct.class));
		form.add(new JaNeeCombobox("bijIntake").setRequired(true));
		form.add(new BegindatumVoorEinddatumValidator(begindatumField, einddatumField));
		form.add(new OrganisatieEenheidLocatieEntiteitEditPanel<OnderwijsproductAanbod>("aanbod",
			new PropertyModel<List<OnderwijsproductAanbod>>(getDefaultModel(),
				"onderwijsproductAanbodList"), getProductModel().getManager(),
			new OrganisatieLocatieTable<OnderwijsproductAanbod>(false), false)
		{
			private static final long serialVersionUID = 1L;

			@Override
			public OnderwijsproductAanbod createNewT()
			{
				return new OnderwijsproductAanbod(getContextOnderwijsproduct());
			}
		});

		for (KRDModuleComponentFactory factory : EduArteApp.get().getPanelFactories(
			KRDModuleComponentFactory.class))
		{
			AbstractVrijVeldEntiteitPanel<Onderwijsproduct> VVEEPanel =
				factory.newVrijVeldEntiteitEditPanel("vrijVelden",
					getContextOnderwijsproductModel(), "Vrije velden");
			VVEEPanel.getVrijVeldZoekFilter().setDossierScherm(true);
			VVEEPanel.getVrijVeldZoekFilter().setCategorie(VrijVeldCategorie.ONDERWIJSPRODUCT);
			form.add(VVEEPanel);
		}
		if (EduArteApp.get().getPanelFactories(KRDModuleComponentFactory.class).size() == 0)
			form.add(new WebMarkupContainer("vrijVelden").setVisible(false));

		createComponents();
		form.add(new TaxonomieElementToevoegenPanel("taxToevoegen",
			(ExtendedModel<Onderwijsproduct>) getContextOnderwijsproductModel()));
	}

	private static ModelManager createModelManager()
	{
		return new DefaultModelManager(OnderwijsproductTaxonomie.class, VrijVeldOptieKeuze.class,
			OnderwijsproductVrijVeld.class, OnderwijsproductAanbod.class,
			BPVCriteriaOnderwijsproduct.class, Onderwijsproduct.class);
	}

	public Onderwijsproduct saveOnderwijsProduct()
	{
		IChangeRecordingModel<Onderwijsproduct> model = getProductModel();

		Onderwijsproduct product = model.getObject();

		model.saveObject(new IModificationCallback()
		{

			@Override
			public void delete(IdObject object, Class< ? extends IdObject> clazz)
			{
			}

			@Override
			public void saveOrUpdate(IdObject object, Class< ? extends IdObject> clazz)
			{
				DataAccessRegistry.getHelper(SessionDataAccessHelper.class)
					.getHibernateSessionProvider().getSession().flush();
			}

		});
		for (OnderwijsproductZoekterm term : product.getZoektermen())
		{
			term.saveOrUpdate();
		}
		product.commit();
		return product;
	}

	private IChangeRecordingModel<Onderwijsproduct> getProductModel()
	{
		return (IChangeRecordingModel<Onderwijsproduct>) form.getModel();
	}

	@Override
	protected void fillBottomRow(BottomRowPanel panel)
	{
		super.fillBottomRow(panel);
		panel.addButton(new OpslaanButton(panel, form, "Opslaan en nieuwe kopie toevoegen")
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void onSubmit()
			{
				Onderwijsproduct product = saveOnderwijsProduct();
				setResponsePage(new OnderwijsproductKaartWijzigenPage(product.getCopy(),
					returnToPage, "Onderwijsproduct " + product.getCode()
						+ " is succesvol opgeslagen"));
			}

		}.setAction(CobraKeyAction.GEEN));
		panel.addButton(new OpslaanButton(panel, form)
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void onSubmit()
			{
				Onderwijsproduct product = saveOnderwijsProduct();
				setResponsePage(new OnderwijsproductKaartPage(product));
			}
		});
		panel.addButton(new AnnulerenButton(panel, returnToPage));
		panel.addButton(new VerwijderButton(panel, "Verwijderen", "Weet u zeker dat u "
			+ getContextOnderwijsproduct().toString() + " wilt verwijderen?")
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void onClick()
			{
				Onderwijsproduct product = getContextOnderwijsproduct();
				product.delete();
				product.commit();
				setResponsePage(new OnderwijsproductZoekenPage());
			}

			@Override
			public boolean isVisible()
			{
				return getContextOnderwijsproduct().isVerwijderbaar();
			}

		});
	}
}
