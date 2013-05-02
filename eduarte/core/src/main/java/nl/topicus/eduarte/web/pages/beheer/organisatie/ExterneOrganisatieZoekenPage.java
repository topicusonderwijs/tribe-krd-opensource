package nl.topicus.eduarte.web.pages.beheer.organisatie;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.dataproviders.GeneralFilteredSortableDataProvider;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.security.RechtenSoort;
import nl.topicus.cobra.security.RechtenSoorten;
import nl.topicus.cobra.web.components.datapanel.CustomDataPanel;
import nl.topicus.cobra.web.components.datapanel.CustomDataPanelPageLinkRowFactory;
import nl.topicus.cobra.web.components.datapanel.CustomDataPanelRowFactory;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.cobra.web.components.shortcut.CobraKeyAction;
import nl.topicus.eduarte.core.principals.beheer.organisatie.ExterneOrganisatiesRead;
import nl.topicus.eduarte.dao.helpers.ExterneOrganisatieDataAccessHelper;
import nl.topicus.eduarte.dao.helpers.NummerGeneratorDataAccessHelper;
import nl.topicus.eduarte.entities.organisatie.Brin;
import nl.topicus.eduarte.entities.organisatie.ExterneOrganisatie;
import nl.topicus.eduarte.web.components.menu.RelatieBeheerMenuItem;
import nl.topicus.eduarte.web.components.panels.EduArteDataPanel;
import nl.topicus.eduarte.web.components.panels.bottomrow.ModuleEditPageButton;
import nl.topicus.eduarte.web.components.panels.datapanel.table.ExterneOrganisatieTable;
import nl.topicus.eduarte.web.components.panels.filter.ExterneOrganisatieZoekFilterPanel;
import nl.topicus.eduarte.web.pages.beheer.relatie.AbstractRelatieBeheerPage;
import nl.topicus.eduarte.zoekfilters.ExterneOrganisatieZoekFilter;

import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.IDataProvider;

/**
 * Pagina waarmee de structuur van de organisatie-eenheden van een onderwijsinstelling
 * ingezien kan worden.
 * 
 * @author loite
 */
@PageInfo(title = "Externe organisaties", menu = "Relatie > Externe organisaties")
@InPrincipal(ExterneOrganisatiesRead.class)
@RechtenSoorten({RechtenSoort.INSTELLING, RechtenSoort.BEHEER})
public class ExterneOrganisatieZoekenPage extends AbstractRelatieBeheerPage<Void>
{
	private static final long serialVersionUID = 1L;

	private ExterneOrganisatieZoekFilterPanel filterPanel;

	private CustomDataPanel<ExterneOrganisatie> datapanel;

	private static final ExterneOrganisatieZoekFilter getDefaultFilter()
	{
		ExterneOrganisatieZoekFilter filter = new ExterneOrganisatieZoekFilter();
		filter.addOrderByProperty("naam");
		return filter;
	}

	public ExterneOrganisatieZoekenPage()
	{
		this(getDefaultFilter());
	}

	public ExterneOrganisatieZoekenPage(ExterneOrganisatieZoekFilter filter)
	{
		super(RelatieBeheerMenuItem.ExterneOrganisaties);
		IDataProvider<ExterneOrganisatie> provider =
			GeneralFilteredSortableDataProvider
				.of(filter, ExterneOrganisatieDataAccessHelper.class);
		datapanel =
			new EduArteDataPanel<ExterneOrganisatie>("datapanel", provider,
				new ExterneOrganisatieTable());
		datapanel.setRowFactory(new CustomDataPanelPageLinkRowFactory<ExterneOrganisatie>(
			ExterneOrganisatieOverzichtPage.class)
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void onClick(Item<ExterneOrganisatie> item)
			{
				pushSearchResultToNavigationLevel(datapanel, item.getIndex());
				setResponsePage(new ExterneOrganisatieOverzichtPage(item.getModelObject()));
			}
		});
		datapanel.setItemsPerPage(20);
		add(datapanel);
		filterPanel = new ExterneOrganisatieZoekFilterPanel("filter", filter, datapanel, false);
		add(filterPanel);
		createComponents();
	}

	@Override
	protected void fillBottomRow(BottomRowPanel panel)
	{
		super.fillBottomRow(panel);
		panel.addButton(new ModuleEditPageButton<ExterneOrganisatie>(panel,
			"Externe organisatie toevoegen", CobraKeyAction.TOEVOEGEN, ExterneOrganisatie.class,
			RelatieBeheerMenuItem.ExterneOrganisaties, ExterneOrganisatieZoekenPage.this)
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected ExterneOrganisatie getEntity()
			{
				NummerGeneratorDataAccessHelper generator =
					DataAccessRegistry.getHelper(NummerGeneratorDataAccessHelper.class);

				ExterneOrganisatie externeOrganisatie = new ExterneOrganisatie();
				externeOrganisatie.setDebiteurennummer(generator.newOrganisatieDebiteurnummer());
				return externeOrganisatie;
			}
		});
		panel.addButton(new ModuleEditPageButton<ExterneOrganisatie>(panel, "BRIN toevoegen",
			CobraKeyAction.GEEN, ExterneOrganisatie.class,
			RelatieBeheerMenuItem.ExterneOrganisaties, ExterneOrganisatieZoekenPage.this)
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected ExterneOrganisatie getEntity()
			{
				NummerGeneratorDataAccessHelper generator =
					DataAccessRegistry.getHelper(NummerGeneratorDataAccessHelper.class);

				Brin brin = new Brin();
				brin.setDebiteurennummer(generator.newOrganisatieDebiteurnummer());
				return brin;
			}

			@Override
			public boolean isVisible()
			{
				return super.isVisible()
					&& ExterneOrganisatieZoekFilter.getGebruikLandelijkeSetting();
			}
		});
	}

	/**
	 * Overschrijft de default RowFactory van de zoeken page. Hiermee kan je de zoeken
	 * page in bv een modal window stoppen en een keuze maken.
	 * 
	 * @param factory
	 */
	public void setDataPanelRowFactory(CustomDataPanelRowFactory<ExterneOrganisatie> factory)
	{
		if (datapanel != null)
			datapanel.setRowFactory(factory);
	}
}
