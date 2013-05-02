package nl.topicus.eduarte.krd.web.pages.beheer.contract;

import java.util.List;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.modelsv2.DefaultModelManager;
import nl.topicus.cobra.modelsv2.IChangeRecordingModel;
import nl.topicus.cobra.modelsv2.ModelFactory;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.web.components.form.AutoFieldSet;
import nl.topicus.cobra.web.components.form.RenderMode;
import nl.topicus.cobra.web.components.form.modifier.ConstructorArgModifier;
import nl.topicus.cobra.web.components.panels.bottomrow.AnnulerenButton;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.cobra.web.components.panels.bottomrow.OpslaanButton;
import nl.topicus.cobra.web.components.shortcut.ActionKey;
import nl.topicus.cobra.web.components.shortcut.CobraKeyAction;
import nl.topicus.cobra.web.components.text.DatumField;
import nl.topicus.cobra.web.components.wiquery.DropDownCheckList;
import nl.topicus.cobra.web.validators.BegindatumVoorEinddatumValidator;
import nl.topicus.cobra.web.validators.UniqueConstraintFormValidator;
import nl.topicus.eduarte.app.EduArteRequestCycle;
import nl.topicus.eduarte.core.principals.beheer.contract.ContractenWrite;
import nl.topicus.eduarte.dao.helpers.LocatieDataAccessHelper;
import nl.topicus.eduarte.entities.contract.Contract;
import nl.topicus.eduarte.entities.contract.ContractOnderdeel;
import nl.topicus.eduarte.entities.contract.ContractVerplichting;
import nl.topicus.eduarte.entities.organisatie.ExterneOrganisatie;
import nl.topicus.eduarte.entities.organisatie.Locatie;
import nl.topicus.eduarte.entities.vrijevelden.ContractVrijVeld;
import nl.topicus.eduarte.entities.vrijevelden.VrijVeldCategorie;
import nl.topicus.eduarte.entities.vrijevelden.VrijVeldOptieKeuze;
import nl.topicus.eduarte.krd.web.components.modalwindow.contract.contractonderdeel.ContractOnderdeelEditPanel;
import nl.topicus.eduarte.krd.web.components.modalwindow.contract.contractverplichting.ContractVerplichtingEditPanel;
import nl.topicus.eduarte.krd.web.components.panels.VrijVeldEntiteitEditPanel;
import nl.topicus.eduarte.web.components.menu.RelatieBeheerMenuItem;
import nl.topicus.eduarte.web.pages.IModuleEditPage;
import nl.topicus.eduarte.web.pages.SecurePage;
import nl.topicus.eduarte.web.pages.beheer.contract.ContractOnderdeelTable;
import nl.topicus.eduarte.web.pages.beheer.contract.ContractOverzichtPage;
import nl.topicus.eduarte.web.pages.beheer.contract.ContractVerplichtingTable;
import nl.topicus.eduarte.web.pages.beheer.contract.ContractZoekenPage;
import nl.topicus.eduarte.web.pages.beheer.relatie.AbstractRelatieBeheerPage;
import nl.topicus.eduarte.zoekfilters.LocatieZoekFilter;
import nl.topicus.eduarte.zoekfilters.MedewerkerZoekFilter;

import org.apache.wicket.Component;
import org.apache.wicket.Page;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.link.IPageLink;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.PropertyModel;

@PageInfo(title = "Contract bewerken/toevoegen", menu = "Relatie > Contracten > [Contract] > Bewerken")
@InPrincipal(ContractenWrite.class)
public class ContractEditPage extends AbstractRelatieBeheerPage<Contract> implements
		IModuleEditPage<Contract>
{
	private Form<Void> form;

	private AutoFieldSet<Contract> contractRightFieldSet;

	private DatumField begindatumField;

	private DatumField einddatumField;

	private AutoFieldSet<Contract> contractLeftFieldSet;

	public ContractEditPage(SecurePage returnPage)
	{
		this(new Contract(), returnPage);
	}

	public ContractEditPage(Contract contract, SecurePage returnPage)
	{
		super(ModelFactory.getCompoundChangeRecordingModel(contract, new DefaultModelManager(
			ContractOnderdeel.class, ContractVerplichting.class, VrijVeldOptieKeuze.class,
			ContractVrijVeld.class, Contract.class)), RelatieBeheerMenuItem.Contracten);
		setReturnPage(returnPage);
		createForm();
	}

	private void createForm()
	{
		form = new Form<Void>("form");
		add(form);

		contractLeftFieldSet = new AutoFieldSet<Contract>("contractLeft", getContextModel());
		contractLeftFieldSet.setOutputMarkupId(true);
		contractLeftFieldSet.setPropertyNames("soortContract", "code", "naam",
			"organisatieEenheid", "onderaanneming", "onderaannemingBij", "externeOrganisatie",
			"externNummer", "contactPersoon", "beheerder", "aanwezigBij", "toelichting");
		contractLeftFieldSet.setRenderMode(RenderMode.EDIT);
		contractLeftFieldSet.addFieldModifier(new ConstructorArgModifier("beheerder",
			createMedewerkerZoekFilter()));
		form.add(new UniqueConstraintFormValidator(contractLeftFieldSet, "Contract", "code"));
		contractLeftFieldSet.addFieldModifier(new ConstructorArgModifier("contactPersoon",
			new PropertyModel<ExterneOrganisatie>(getDefaultModel(), "externeOrganisatie")));
		contractLeftFieldSet.setSortAccordingToPropertyNames(true);
		form.add(contractLeftFieldSet);

		contractRightFieldSet = new AutoFieldSet<Contract>("contractRight", getContextModel());
		contractRightFieldSet.setPropertyNames("minimumAantalDeelnemers",
			"maximumAantalDeelnemers", "kostprijs", "typeFinanciering", "begindatum",
			"eindeInstroom", "einddatum", "locaties");
		contractRightFieldSet.setRenderMode(RenderMode.EDIT);
		contractRightFieldSet.setSortAccordingToPropertyNames(true);
		contractRightFieldSet.addFieldModifier(new ConstructorArgModifier("locaties",
			new LocatiesModel(), true, "(Alle locaties)"));
		form.add(contractRightFieldSet);

		form.add(new ContractOnderdeelEditPanel("onderdelenPanel",
			new PropertyModel<List<ContractOnderdeel>>(getDefaultModel(), "contractOnderdelen"),
			new ContractOnderdeelTable(), getContractModel())
		{
			private static final long serialVersionUID = 1L;

			@Override
			public ContractOnderdeel createNewT()
			{
				return new ContractOnderdeel(getContract());
			}

		});

		form.add(new ContractVerplichtingEditPanel("verplichtingenPanel",
			new PropertyModel<List<ContractVerplichting>>(getDefaultModel(),
				"contractVerplichtingen"), new ContractVerplichtingTable(), getContractModel())
		{
			private static final long serialVersionUID = 1L;

			@Override
			public ContractVerplichting createNewT()
			{
				return new ContractVerplichting(getContract());
			}
		});

		VrijVeldEntiteitEditPanel<Contract> vrijVeldenPanel =
			new VrijVeldEntiteitEditPanel<Contract>("vrijvelden", getContextModel());
		vrijVeldenPanel.getVrijVeldZoekFilter().setDossierScherm(true);
		vrijVeldenPanel.getVrijVeldZoekFilter().setCategorie(VrijVeldCategorie.CONTRACT);
		form.add(vrijVeldenPanel);

		createComponents();
	}

	protected Contract getContract()
	{
		return getContextModelObject();
	}

	private IChangeRecordingModel<Contract> getContractModel()
	{
		return (IChangeRecordingModel<Contract>) getContextModel();
	}

	private static MedewerkerZoekFilter createMedewerkerZoekFilter()
	{
		MedewerkerZoekFilter ret = new MedewerkerZoekFilter();
		return ret;
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
				Contract soortContract = getContextModelObject();
				getContractModel().saveObject();
				soortContract.commit();

				EduArteRequestCycle.get().setResponsePage(getReturnPage());
			}
		});

		panel.addButton(new OpslaanButton(panel, form)
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void onSubmit()
			{
				Contract soortContract = getContextModelObject();
				getContractModel().saveObject();
				soortContract.commit();

				EduArteRequestCycle.get().setResponsePage(new ContractEditPage(getReturnPage()));
			}

			@Override
			public String getLabel()
			{
				return "Opslaan en nieuwe toevoegen";
			}

			@Override
			public ActionKey getAction()
			{
				ActionKey action = CobraKeyAction.VOLGENDE;
				return action;
			}
		});

		panel.addButton(new AnnulerenButton(panel, new IPageLink()
		{
			private static final long serialVersionUID = 1L;

			public Page getPage()
			{
				if (getContract().isSaved())
					return new ContractOverzichtPage(getContract());
				else
					return new ContractZoekenPage();
			}

			public Class< ? extends Page> getPageIdentity()
			{
				if (getContract().isSaved())
					return ContractOverzichtPage.class;
				else
					return ContractZoekenPage.class;

			}
		}));
	}

	@Override
	public Component createTitle(String id)
	{
		if (getContract().isSaved())
			return new Label(id, new PropertyModel<String>(getDefaultModel(), "naam"));
		return new Label(id, "Nieuw contract");
	}

	@Override
	protected void onBeforeRender()
	{
		super.onBeforeRender();
		begindatumField = (DatumField) contractRightFieldSet.findFieldComponent("begindatum");
		einddatumField = (DatumField) contractRightFieldSet.findFieldComponent("einddatum");
		DropDownCheckList< ? > locatiesCheckList =
			(DropDownCheckList< ? >) contractRightFieldSet.findFieldComponent("locaties");
		locatiesCheckList.getOptions().put("width", 231);
		locatiesCheckList.getOptions().put("maxDropHeight", 250);
		form.add(new BegindatumVoorEinddatumValidator(begindatumField, einddatumField));
	}

	private final class LocatiesModel extends LoadableDetachableModel<List<Locatie>>
	{
		private static final long serialVersionUID = 1L;

		private LocatieZoekFilter getDefaultFilter()
		{
			LocatieZoekFilter zoekFilter = new LocatieZoekFilter();
			zoekFilter.addOrderByProperty("naam");
			return zoekFilter;
		}

		@Override
		protected List<Locatie> load()
		{
			LocatieDataAccessHelper helper =
				DataAccessRegistry.getHelper(LocatieDataAccessHelper.class);
			return helper.list(getDefaultFilter());
		}

	}
}
