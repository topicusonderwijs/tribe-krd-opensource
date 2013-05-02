package nl.topicus.eduarte.krd.web.pages.beheer.contract;

import java.util.ArrayList;
import java.util.List;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.modelsv2.DefaultModelManager;
import nl.topicus.cobra.modelsv2.ModelFactory;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.util.ComponentUtil;
import nl.topicus.cobra.web.components.menu.AbstractMenuBar;
import nl.topicus.cobra.web.components.panels.bottomrow.AnnulerenButton;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.cobra.web.components.panels.bottomrow.OpslaanButton;
import nl.topicus.cobra.web.components.text.DatumField;
import nl.topicus.cobra.web.validators.DatumGroterOfGelijkDatumValidator;
import nl.topicus.cobra.web.validators.DatumKleinerOfGelijkDatumValidator;
import nl.topicus.eduarte.dao.helpers.VerbintenisDataAccessHelper;
import nl.topicus.eduarte.entities.contract.Contract;
import nl.topicus.eduarte.entities.contract.ContractOnderdeel;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis;
import nl.topicus.eduarte.entities.inschrijving.VerbintenisContract;
import nl.topicus.eduarte.entities.personen.ExterneOrganisatieContactPersoon;
import nl.topicus.eduarte.krd.principals.deelnemer.verbintenis.DeelnemerVerbintenissenWrite;
import nl.topicus.eduarte.web.components.choice.ContractOnderdeelCombobox;
import nl.topicus.eduarte.web.components.choice.ExterneOrganisatieContactPersoonCombobox;
import nl.topicus.eduarte.web.components.menu.ContractMenu;
import nl.topicus.eduarte.web.components.menu.ContractMenuItem;
import nl.topicus.eduarte.web.components.menu.main.CoreMainMenuItem;
import nl.topicus.eduarte.web.pages.IModuleEditPage;
import nl.topicus.eduarte.web.pages.SecurePage;
import nl.topicus.eduarte.web.pages.beheer.contract.ContractOverzichtPage;

import org.apache.wicket.Component;
import org.apache.wicket.Page;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.link.IPageLink;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.PropertyModel;

@PageInfo(title = "Contract", menu = "Relatie > Contract > [Contract] > Collectief koppelen")
@InPrincipal(DeelnemerVerbintenissenWrite.class)
public class ContractCollectiefAanmakenPage extends SecurePage implements
		IModuleEditPage<VerbintenisContract>
{
	private static final long serialVersionUID = 1L;

	private Form<VerbintenisContract> form;

	private IModel<Contract> contractModel;

	private IModel<List<Verbintenis>> verbintenisModel;

	private boolean error;

	public ContractCollectiefAanmakenPage(IModel<Contract> contractModel,
			IModel<List<Verbintenis>> verbintenisListModel)
	{
		super(CoreMainMenuItem.Relatie);

		this.contractModel = contractModel;
		this.verbintenisModel = verbintenisListModel;

		form =
			new Form<VerbintenisContract>("form", ModelFactory.getCompoundModel(
				new VerbintenisContract(), new DefaultModelManager(VerbintenisContract.class)))
			{
				private static final long serialVersionUID = 1L;

				@Override
				protected void onSubmit()
				{
					List<Verbintenis> verbintenisList = new ArrayList<Verbintenis>();

					VerbintenisContract referentieContract = getModelObject();

					if (verbintenisModel != null && verbintenisModel.getObject() != null)
					{
						verbintenisList = verbintenisModel.getObject();
					}

					if (!error)
					{
						for (Verbintenis verbintenis : verbintenisList)
						{
							createNieuwVerbintenisContract(referentieContract, verbintenis);
						}
						DataAccessRegistry.getHelper(VerbintenisDataAccessHelper.class)
							.batchExecute();
						super.onSubmit();
						ContractOverzichtPage responsePage =
							new ContractOverzichtPage(getContract());
						responsePage.info("Contracten gekoppeld");

						setResponsePage(responsePage);
					}
					else
					{
						super.onSubmit();
					}

				}
			};

		DatumField beginDatum = new DatumField("begindatum");
		DatumField eindDatum = new DatumField("einddatum");
		form.add(beginDatum);
		form.add(eindDatum);

		ContractOnderdeelCombobox onderdelenCombobox =
			new ContractOnderdeelCombobox("onderdeel",
				new AbstractReadOnlyModel<List<ContractOnderdeel>>()
				{
					private static final long serialVersionUID = 1L;

					@Override
					public List<ContractOnderdeel> getObject()
					{
						Contract obj = getContract();
						if (obj == null)
							return new ArrayList<ContractOnderdeel>();

						return obj.getContractOnderdelen();
					}
				});
		onderdelenCombobox.setAddSelectedItemToChoicesWhenNotInList(false);
		onderdelenCombobox.setOutputMarkupId(true);
		onderdelenCombobox.setNullValid(true);
		form.add(onderdelenCombobox);

		ExterneOrganisatieContactPersoonCombobox extOrgContactPersoonComboBox =
			new ExterneOrganisatieContactPersoonCombobox("externeOrganisatieContactPersoon",
				new PropertyModel<ExterneOrganisatieContactPersoon>(form.getDefaultModelObject(),
					"externeOrganisatieContactPersoon"),
				new LoadableDetachableModel<List<ExterneOrganisatieContactPersoon>>()
				{
					private static final long serialVersionUID = 1L;

					@Override
					protected List<ExterneOrganisatieContactPersoon> load()
					{
						if (getContract() != null && getContract().getExterneOrganisatie() != null)
							return getContract().getExterneOrganisatie().getContactPersonen();
						return new ArrayList<ExterneOrganisatieContactPersoon>();
					}
				});
		form.add(extOrgContactPersoonComboBox);

		add(form);
		createComponents();
	}

	@Override
	protected void onBeforeRender()
	{
		DatumField beginDatum = (DatumField) form.get("begindatum");
		DatumField eindDatum = (DatumField) form.get("einddatum");
		form.add(new DatumKleinerOfGelijkDatumValidator(eindDatum, getContract().getEinddatum()));
		form.add(new DatumGroterOfGelijkDatumValidator(beginDatum, getContract().getBegindatum()));
		super.onBeforeRender();
	}

	private void createNieuwVerbintenisContract(VerbintenisContract referentieContract,
			Verbintenis verbintenis)
	{
		VerbintenisContract nieuwContract = new VerbintenisContract();
		nieuwContract.setContract(getContract());
		nieuwContract.setBegindatum(referentieContract.getBegindatum());
		nieuwContract.setEinddatum(referentieContract.getEinddatum());
		nieuwContract.setVerbintenis(verbintenis);
		nieuwContract.setExterneOrganisatieContactPersoon(referentieContract
			.getExterneOrganisatieContactPersoon());
		nieuwContract.setOnderdeel(referentieContract.getOnderdeel());
		verbintenis.getContracten().add(nieuwContract);
		nieuwContract.save();
	}

	private IModel<Contract> getContractModel()
	{
		return contractModel;
	}

	private Contract getContract()
	{
		if (getContractModel() != null)
			return getContractModel().getObject();
		return null;
	}

	@Override
	public AbstractMenuBar createMenu(String id)
	{
		return new ContractMenu(id, getContractModel(), ContractMenuItem.Contractkaart);
	}

	@Override
	public Component createTitle(String id)
	{
		return new Label(id, "Collectief contract toekennen");
	}

	@Override
	protected void fillBottomRow(BottomRowPanel panel)
	{
		super.fillBottomRow(panel);
		panel.addButton(new OpslaanButton(panel, form));
		panel.addButton(new AnnulerenButton(panel, new IPageLink()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public Page getPage()
			{
				return new ContractOverzichtPage(getContract());
			}

			@Override
			public Class< ? extends Page> getPageIdentity()
			{
				return ContractOverzichtPage.class;
			}
		}));
	}

	@Override
	protected void onDetach()
	{
		super.onDetach();
		ComponentUtil.detachQuietly(verbintenisModel);

	}

}
