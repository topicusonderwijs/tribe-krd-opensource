package nl.topicus.eduarte.krd.web.pages.deelnemer.relatie;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.dataproviders.GeneralFilteredSortableDataProvider;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.web.components.datapanel.CustomDataPanel;
import nl.topicus.cobra.web.components.datapanel.CustomDataPanelPageLinkRowFactory;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.cobra.web.components.panels.bottomrow.TerugButton;
import nl.topicus.cobra.web.components.shortcut.CobraKeyAction;
import nl.topicus.cobra.web.security.RequiredSecurityCheck;
import nl.topicus.eduarte.app.security.checks.NietOverledenSecurityCheck;
import nl.topicus.eduarte.dao.helpers.ExterneOrganisatieDataAccessHelper;
import nl.topicus.eduarte.dao.helpers.NummerGeneratorDataAccessHelper;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis;
import nl.topicus.eduarte.entities.organisatie.ExterneOrganisatie;
import nl.topicus.eduarte.entities.personen.Deelnemer;
import nl.topicus.eduarte.entities.personen.Persoon;
import nl.topicus.eduarte.entities.personen.PersoonExterneOrganisatie;
import nl.topicus.eduarte.krd.principals.deelnemer.relatie.DeelnemerRelatieOrganisatieWrite;
import nl.topicus.eduarte.web.components.menu.DeelnemerMenuItem;
import nl.topicus.eduarte.web.components.panels.EduArteDataPanel;
import nl.topicus.eduarte.web.components.panels.bottomrow.ModuleEditPageButton;
import nl.topicus.eduarte.web.components.panels.datapanel.table.ExterneOrganisatieTable;
import nl.topicus.eduarte.web.components.panels.filter.ExterneOrganisatieZoekFilterPanel;
import nl.topicus.eduarte.web.pages.IModuleEditPage;
import nl.topicus.eduarte.web.pages.SecurePage;
import nl.topicus.eduarte.web.pages.deelnemer.AbstractDeelnemerPage;
import nl.topicus.eduarte.web.pages.deelnemer.personalia.DeelnemerRelatiesOverzichtPage;
import nl.topicus.eduarte.zoekfilters.ExterneOrganisatieZoekFilter;

import org.apache.wicket.Page;
import org.apache.wicket.markup.html.link.IPageLink;
import org.apache.wicket.markup.repeater.Item;

@PageInfo(title = "Organsatie als relatie toevoegen", menu = {"Deelnemer > [deelnemer]",
	"Groep > [groep] > [deelnemer]"})
@InPrincipal(DeelnemerRelatieOrganisatieWrite.class)
@RequiredSecurityCheck(NietOverledenSecurityCheck.class)
public class DeelnemerOrganisatieAlsRelatieToevoegenPage extends AbstractDeelnemerPage implements
		IModuleEditPage<Deelnemer>
{

	public DeelnemerOrganisatieAlsRelatieToevoegenPage(Deelnemer deelnemer, Verbintenis verbintenis)
	{
		super(DeelnemerMenuItem.Relaties, deelnemer, verbintenis);

		ExterneOrganisatieZoekFilter filter = new ExterneOrganisatieZoekFilter();

		GeneralFilteredSortableDataProvider<ExterneOrganisatie, ExterneOrganisatieZoekFilter> dataprovider =
			GeneralFilteredSortableDataProvider.of(filter,
				ExterneOrganisatieDataAccessHelper.class);

		CustomDataPanel<ExterneOrganisatie> datapanel =
			new EduArteDataPanel<ExterneOrganisatie>("datapanelExterneOrganisatieOverzicht",
				dataprovider, new ExterneOrganisatieTable());
		datapanel.setRowFactory(new CustomDataPanelPageLinkRowFactory<ExterneOrganisatie>(
			EditPersoonExterneOrganisatiePage.class)
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick(Item<ExterneOrganisatie> item)
			{
				ExterneOrganisatie externeOrganisatie = item.getModelObject();
				PersoonExterneOrganisatie persoonExterneOrganisatie =
					createNieuwePersoonExterneRelatie(false);

				persoonExterneOrganisatie.setRelatie(externeOrganisatie);

				setResponsePage(new EditPersoonExterneOrganisatiePage(persoonExterneOrganisatie,
					new DeelnemerRelatiesOverzichtPage(getContextDeelnemerModel())));
			}

		});
		add(datapanel);

		ExterneOrganisatieZoekFilterPanel filterPanel =
			new ExterneOrganisatieZoekFilterPanel("filter", filter, datapanel, false);
		add(filterPanel);

		createComponents();
	}

	@Override
	protected void fillBottomRow(BottomRowPanel panel)
	{

		ModuleEditPageButton<PersoonExterneOrganisatie> toevoegen =
			new ModuleEditPageButton<PersoonExterneOrganisatie>(panel,
				"Nieuwe relatie - Organisatie", CobraKeyAction.TOEVOEGEN,
				PersoonExterneOrganisatie.class, getSelectedMenuItem(), null, null)
			{
				private static final long serialVersionUID = 1L;

				@Override
				protected PersoonExterneOrganisatie getEntity()
				{
					return createNieuwePersoonExterneRelatie(true);

				}

				@Override
				protected SecurePage getReturnPage()
				{
					return new DeelnemerRelatiesOverzichtPage(getContextDeelnemer(),
						getContextVerbintenis());
				}

			};

		panel.addButton(toevoegen);
		panel.addButton(new TerugButton(panel, new IPageLink()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public Page getPage()
			{
				return new DeelnemerRelatiesOverzichtPage(getContextDeelnemer(),
					getContextVerbintenis());
			}

			@Override
			public Class< ? extends SecurePage> getPageIdentity()
			{
				return DeelnemerOrganisatieAlsRelatieToevoegenPage.class;
			}

		}));

	}

	private PersoonExterneOrganisatie createNieuwePersoonExterneRelatie(
			boolean createExterneOrganisatie)
	{
		PersoonExterneOrganisatie nieuwePersoonExterneOrganisatie = new PersoonExterneOrganisatie();

		Persoon deelnemer = getContextDeelnemer().getPersoon();
		nieuwePersoonExterneOrganisatie.setPersoon(deelnemer);

		if (createExterneOrganisatie)
			nieuwePersoonExterneOrganisatie.setRelatie(createNieuweExterneOrganisatie());

		if (!deelnemer.isMeerderjarig())
		{
			// indien geen betalingsplichtige: maak betalingsplichtige
			if (deelnemer.getBetalingsplichtigeRelatie() == null)
				nieuwePersoonExterneOrganisatie.setBetalingsplichtige(true);
			// minderjarige deelnemer geen wettelijk vertegenwoordiger: maak wettelijk
			// vertegenwoordiger
			if (deelnemer.getWettelijkVertegenwoordigers().isEmpty())
				nieuwePersoonExterneOrganisatie.setWettelijkeVertegenwoordiger(true);
		}
		return nieuwePersoonExterneOrganisatie;
	}

	private ExterneOrganisatie createNieuweExterneOrganisatie()
	{
		NummerGeneratorDataAccessHelper generator =
			DataAccessRegistry.getHelper(NummerGeneratorDataAccessHelper.class);

		ExterneOrganisatie nieuweExterneOrganisatie = new ExterneOrganisatie();
		nieuweExterneOrganisatie.setDebiteurennummer(generator.newOrganisatieDebiteurnummer());

		return nieuweExterneOrganisatie;
	}

}
