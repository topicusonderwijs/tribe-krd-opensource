package nl.topicus.eduarte.krd.web.pages.intake.stap2;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.dataproviders.GeneralFilteredSortableDataProvider;
import nl.topicus.cobra.web.components.datapanel.CustomDataPanel;
import nl.topicus.cobra.web.components.datapanel.CustomDataPanelPageLinkRowFactory;
import nl.topicus.cobra.web.components.panels.bottomrow.AnnulerenButton;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.eduarte.dao.helpers.ExterneOrganisatieDataAccessHelper;
import nl.topicus.eduarte.dao.helpers.NummerGeneratorDataAccessHelper;
import nl.topicus.eduarte.entities.organisatie.ExterneOrganisatie;
import nl.topicus.eduarte.entities.personen.Deelnemer;
import nl.topicus.eduarte.entities.personen.Persoon;
import nl.topicus.eduarte.entities.personen.PersoonExterneOrganisatie;
import nl.topicus.eduarte.krd.web.pages.intake.IntakeWizardModel;
import nl.topicus.eduarte.krd.web.pages.intake.IntakeWizardPage;
import nl.topicus.eduarte.web.components.panels.EduArteDataPanel;
import nl.topicus.eduarte.web.components.panels.datapanel.table.ExterneOrganisatieTable;
import nl.topicus.eduarte.web.components.panels.filter.ExterneOrganisatieZoekFilterPanel;
import nl.topicus.eduarte.web.pages.IModuleEditPage;
import nl.topicus.eduarte.web.pages.SecurePage;
import nl.topicus.eduarte.zoekfilters.ExterneOrganisatieZoekFilter;

import org.apache.wicket.Page;
import org.apache.wicket.markup.html.link.IPageLink;
import org.apache.wicket.markup.repeater.Item;

@PageInfo(title = "Intake stap 2 - Organisatie als relatie toevoegen", menu = {"Deelnemer > intake"})
public class IntakeStap2SelecteerOrganisatieRelatiePage extends IntakeWizardPage implements
		IModuleEditPage<Deelnemer>
{

	public IntakeStap2SelecteerOrganisatieRelatiePage(IntakeWizardModel wizard)
	{
		setWizard(wizard);

		ExterneOrganisatieZoekFilter filter = new ExterneOrganisatieZoekFilter();

		GeneralFilteredSortableDataProvider<ExterneOrganisatie, ExterneOrganisatieZoekFilter> dataprovider =
			GeneralFilteredSortableDataProvider.of(filter,
				ExterneOrganisatieDataAccessHelper.class);

		CustomDataPanel<ExterneOrganisatie> datapanel =
			new EduArteDataPanel<ExterneOrganisatie>("datapanelExterneOrganisatieOverzicht",
				dataprovider, new ExterneOrganisatieTable());
		datapanel.setRowFactory(new CustomDataPanelPageLinkRowFactory<ExterneOrganisatie>(
			IntakeStap2BewerkOrganisatieRelatiePage.class)
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick(Item<ExterneOrganisatie> item)
			{
				ExterneOrganisatie externeOrganisatie = item.getModelObject();
				PersoonExterneOrganisatie persoonExterneOrganisatie =
					createNieuwePersoonExterneRelatie(false);

				persoonExterneOrganisatie.setRelatie(externeOrganisatie);

				setResponsePage(new IntakeStap2BewerkOrganisatieRelatiePage(getWizard(),
					persoonExterneOrganisatie, true));
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

		panel.addButton(new AnnulerenButton(panel, new IPageLink()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public Page getPage()
			{
				return new IntakeStap2Achtergrond(getWizard());
			}

			@Override
			public Class< ? extends SecurePage> getPageIdentity()
			{
				return IntakeStap2Achtergrond.class;
			}

		}));
	}

	private PersoonExterneOrganisatie createNieuwePersoonExterneRelatie(
			boolean createExterneOrganisatie)
	{
		PersoonExterneOrganisatie nieuwePersoonExterneOrganisatie = new PersoonExterneOrganisatie();

		Persoon deelnemer = getWizard().getDeelnemer().getPersoon();
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
		deelnemer.getRelaties().add(nieuwePersoonExterneOrganisatie);
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

	@Override
	protected int getStapNummer()
	{
		return 2;
	}

	@Override
	protected String getStapTitel()
	{
		return "Gerelateerde organisatie selecteren";
	}
}
