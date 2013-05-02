package nl.topicus.eduarte.web.pages.beheer.contract;

import java.util.List;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.dataproviders.ListModelDataProvider;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.util.ComponentUtil;
import nl.topicus.cobra.web.components.form.AutoFieldSet;
import nl.topicus.cobra.web.components.form.RenderMode;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.cobra.web.components.panels.bottomrow.ButtonAlignment;
import nl.topicus.cobra.web.components.panels.bottomrow.PageLinkButton;
import nl.topicus.cobra.web.components.panels.bottomrow.TerugButton;
import nl.topicus.cobra.web.components.shortcut.CobraKeyAction;
import nl.topicus.eduarte.app.EduArteApp;
import nl.topicus.eduarte.core.principals.beheer.contract.ContractenRead;
import nl.topicus.eduarte.entities.contract.Contract;
import nl.topicus.eduarte.entities.contract.ContractOnderdeel;
import nl.topicus.eduarte.entities.contract.ContractVerplichting;
import nl.topicus.eduarte.entities.organisatie.Locatie;
import nl.topicus.eduarte.entities.vrijevelden.ContractVrijVeld;
import nl.topicus.eduarte.web.components.factory.KRDModuleComponentFactory;
import nl.topicus.eduarte.web.components.menu.ContractMenuItem;
import nl.topicus.eduarte.web.components.menu.RelatieBeheerMenuItem;
import nl.topicus.eduarte.web.components.panels.EduArteDataPanel;
import nl.topicus.eduarte.web.components.panels.VrijVeldEntiteitPanel;
import nl.topicus.eduarte.web.components.panels.bottomrow.JasperReportBottomRowButton;
import nl.topicus.eduarte.web.components.panels.bottomrow.ModuleEditPageButton;
import nl.topicus.eduarte.web.components.panels.datapanel.table.DeelnemerTable;
import nl.topicus.eduarte.web.components.panels.datapanel.table.LocatieTable;
import nl.topicus.eduarte.web.pages.SecurePage;
import nl.topicus.eduarte.web.pages.deelnemer.DeelnemerZoekenPage;
import nl.topicus.eduarte.zoekfilters.VerbintenisZoekFilter;

import org.apache.wicket.Component;
import org.apache.wicket.Page;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.IPageLink;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

@PageInfo(title = "Contract", menu = "Relatie > Contract > [Contract]")
@InPrincipal(ContractenRead.class)
public class ContractOverzichtPage extends AbstractContractPage
{

	private SecurePage securePage;

	public ContractOverzichtPage(Contract contract)
	{
		this(contract, new ContractZoekenPage());
	}

	public ContractOverzichtPage(Contract contract, SecurePage securePage)
	{
		super(ContractMenuItem.Contractkaart, contract);
		this.securePage = securePage;

		AutoFieldSet<Contract> contractLeftFieldSet =
			new AutoFieldSet<Contract>("contractLeft", getContextContractModel());
		contractLeftFieldSet.setPropertyNames("code", "naam", "organisatieEenheid",
			"onderaanneming", "onderaannemingBij", "externeOrganisatie", "externNummer",
			"contactPersoon", "soortContract", "beheerder", "aanwezigBij");
		contractLeftFieldSet.setRenderMode(RenderMode.DISPLAY);
		contractLeftFieldSet.setSortAccordingToPropertyNames(true);
		add(contractLeftFieldSet);

		AutoFieldSet<Contract> contractRightFieldSet =
			new AutoFieldSet<Contract>("contractRight", getContextContractModel());
		contractRightFieldSet.setPropertyNames("minimumAantalDeelnemers",
			"maximumAantalDeelnemers", "kostprijsEuro", "typeFinanciering", "begindatum",
			"eindeInstroom", "einddatum", "locatie");
		contractRightFieldSet.setSortAccordingToPropertyNames(true);
		contractRightFieldSet.setRenderMode(RenderMode.DISPLAY);
		add(contractRightFieldSet);

		VrijVeldEntiteitPanel<ContractVrijVeld, Contract> vrijvelden =
			new VrijVeldEntiteitPanel<ContractVrijVeld, Contract>("vrijvelden",
				getContextContractModel());
		vrijvelden.getVrijVeldZoekFilter().setDossierScherm(true);
		add(vrijvelden);

		EduArteDataPanel<Locatie> locatiePanel =
			new EduArteDataPanel<Locatie>("locatiepanel", new ListModelDataProvider<Locatie>(
				new PropertyModel<List<Locatie>>(getContextContractModel(), "locaties")),
				new LocatieTable());
		add(locatiePanel);

		EduArteDataPanel<ContractOnderdeel> onderdelenPanel =
			new EduArteDataPanel<ContractOnderdeel>("onderdelenPanel",
				new ListModelDataProvider<ContractOnderdeel>(
					new PropertyModel<List<ContractOnderdeel>>(getContextContractModel(),
						"contractOnderdelen")), new ContractOnderdeelTable());
		add(onderdelenPanel);

		EduArteDataPanel<ContractVerplichting> verplichtingenPanel =
			new EduArteDataPanel<ContractVerplichting>("verplichtingenPanel",
				new ListModelDataProvider<ContractVerplichting>(
					new PropertyModel<List<ContractVerplichting>>(getContextContractModel(),
						"contractVerplichtingen")), new ContractVerplichtingTable());
		add(verplichtingenPanel);

		createComponents();
	}

	@Override
	protected void fillBottomRow(BottomRowPanel panel)
	{
		panel.addButton(new PageLinkButton(panel, "Deelnemers onder dit contract",
			ButtonAlignment.LEFT, new IPageLink()
			{
				private static final long serialVersionUID = 1L;

				@Override
				public Page getPage()
				{
					Contract contract = getContextContract();
					VerbintenisZoekFilter filter = VerbintenisZoekFilter.getDefaultFilter();
					filter.setContract(contract);
					return new DeelnemerZoekenPage(filter, ContractOverzichtPage.class,
						DeelnemerTable.createContractDeelnemerTable(), ContractOverzichtPage.this);
				}

				@SuppressWarnings("unchecked")
				@Override
				public Class getPageIdentity()
				{
					return DeelnemerZoekenPage.class;
				}
			}));

		for (KRDModuleComponentFactory factory : EduArteApp.get().getPanelFactories(
			KRDModuleComponentFactory.class))
		{
			factory.koppelContractenCollectiefKnop(panel, getContextContractModel(),
				ContractOverzichtPage.this);
		}

		// pdf
		panel.addButton(new JasperReportBottomRowButton<Contract>(panel, "contractkaart.jrxml",
			getClass(), "contract")
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected IModel<Contract> getContextModel()
			{
				return getContextContractModel();
			}
		});

		panel.addButton(new ModuleEditPageButton<Contract>(panel, "Bewerken",
			CobraKeyAction.BEWERKEN, Contract.class, RelatieBeheerMenuItem.Contracten,
			ContractOverzichtPage.this, getContextContractModel()));

		panel.addButton(new TerugButton(panel, securePage));

		// toevoegen knop om factuurregeldefinities toe te wijzen aan dit contract.
		/*
		 * naar tabbed menu verhuisd List<FinancieelModuleComponentFactory> factories =
		 * EduArteApp.get().getPanelFactories(FinancieelModuleComponentFactory.class); for
		 * (FinancieelModuleComponentFactory factory : factories) {
		 * factory.newContractFactuurregelDefinitiesBewerkenKnop(panel,
		 * ContractOverzichtPage.this .getModel());
		 * factory.newContractFactuurregelToevoegenKnop(panel, ContractOverzichtPage.this
		 * .getModel()); }
		 */
	}

	@Override
	/*
	 * public void getBookmarkConstructorArguments(List<Class< ? >> ctorArgTypes,
	 * List<Object> ctorArgValues) { super.getBookmarkConstructorArguments(ctorArgTypes,
	 * ctorArgValues); ctorArgTypes.add(Contract.class); ctorArgValues.add(getModel()); }
	 */
	public boolean supportsBookmarks()
	{
		return false;
	}

	@Override
	public Component createTitle(String id)
	{
		return new Label(id, new PropertyModel<String>(getDefaultModel(), "naam"));
	}

	@Override
	public void onDetach()
	{
		ComponentUtil.detachQuietly(securePage);
		super.onDetach();
	}
}
