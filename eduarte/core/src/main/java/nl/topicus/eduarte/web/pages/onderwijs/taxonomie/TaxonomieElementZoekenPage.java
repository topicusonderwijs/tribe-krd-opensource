package nl.topicus.eduarte.web.pages.onderwijs.taxonomie;

import java.util.List;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.dataproviders.GeneralFilteredSortableDataProvider;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.security.RechtenSoort;
import nl.topicus.cobra.security.RechtenSoorten;
import nl.topicus.cobra.web.components.datapanel.CustomDataPanel;
import nl.topicus.cobra.web.components.datapanel.CustomDataPanelPageLinkRowFactory;
import nl.topicus.cobra.web.components.menu.AbstractMenuBar;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.cobra.web.components.shortcut.CobraKeyAction;
import nl.topicus.eduarte.core.principals.onderwijs.taxonomie.TaxonomieElementZoeken;
import nl.topicus.eduarte.dao.helpers.TaxonomieElementDataAccessHelper;
import nl.topicus.eduarte.entities.organisatie.EntiteitContext;
import nl.topicus.eduarte.entities.taxonomie.Taxonomie;
import nl.topicus.eduarte.entities.taxonomie.TaxonomieElement;
import nl.topicus.eduarte.entities.taxonomie.TaxonomieElementType;
import nl.topicus.eduarte.web.components.menu.OnderwijsCollectiefMenu;
import nl.topicus.eduarte.web.components.menu.OnderwijsCollectiefMenuItem;
import nl.topicus.eduarte.web.components.menu.main.CoreMainMenuItem;
import nl.topicus.eduarte.web.components.panels.EduArteDataPanel;
import nl.topicus.eduarte.web.components.panels.bottomrow.ModuleEditPageButton;
import nl.topicus.eduarte.web.components.panels.datapanel.table.TaxonomieElementTable;
import nl.topicus.eduarte.web.components.panels.filter.TaxonomieElementZoekFilterPanel;
import nl.topicus.eduarte.web.pages.SecurePage;
import nl.topicus.eduarte.zoekfilters.TaxonomieElementZoekFilter;

import org.apache.wicket.markup.repeater.Item;

/**
 * Pagina voor het zoeken naar taxonomie-elementen.
 * 
 * @author loite
 */
@PageInfo(title = "Taxonomie zoeken", menu = "Onderwijs > Taxonomie")
@InPrincipal(TaxonomieElementZoeken.class)
@RechtenSoorten( {RechtenSoort.INSTELLING, RechtenSoort.BEHEER})
public class TaxonomieElementZoekenPage extends SecurePage
{
	private static final long serialVersionUID = 1L;

	private final TaxonomieElementZoekFilterPanel filterPanel;

	private static final TaxonomieElementZoekFilter getDefaultFilter()
	{
		TaxonomieElementZoekFilter filter = new TaxonomieElementZoekFilter(TaxonomieElement.class);
		return filter;
	}

	public TaxonomieElementZoekenPage()
	{
		this(getDefaultFilter());
	}

	public TaxonomieElementZoekenPage(TaxonomieElementZoekFilter filter)
	{
		super(CoreMainMenuItem.Onderwijs);
		GeneralFilteredSortableDataProvider<TaxonomieElement, TaxonomieElementZoekFilter> provider =
			GeneralFilteredSortableDataProvider.of(filter,
				TaxonomieElementDataAccessHelper.class);
		final CustomDataPanel<TaxonomieElement> datapanel =
			new EduArteDataPanel<TaxonomieElement>("datapanel", provider,
				new TaxonomieElementTable("Taxonomie"));

		datapanel.setRowFactory(new CustomDataPanelPageLinkRowFactory<TaxonomieElement>(
			TaxonomieElementkaartPage.class)
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick(Item<TaxonomieElement> item)
			{
				TaxonomieElement taxonomieElement = item.getModelObject();
				pushSearchResultToNavigationLevel(datapanel, item.getIndex());
				setResponsePage(new TaxonomieElementkaartPage(taxonomieElement));
			}
		});
		datapanel.setItemsPerPage(20);
		add(datapanel);
		filterPanel = new TaxonomieElementZoekFilterPanel("filter", filter, datapanel, true);
		add(filterPanel);
		createComponents();
	}

	@Override
	public AbstractMenuBar createMenu(String id)
	{
		return new OnderwijsCollectiefMenu(id, OnderwijsCollectiefMenuItem.TaxonomieZoeken);
	}

	@Override
	public void getBookmarkConstructorArguments(List<Class< ? >> ctorArgTypes,
			List<Object> ctorArgValues)
	{
		super.getBookmarkConstructorArguments(ctorArgTypes, ctorArgValues);
		ctorArgTypes.add(TaxonomieElementZoekFilter.class);
		ctorArgValues.add(filterPanel.getZoekfilter());
	}

	@Override
	protected void fillBottomRow(BottomRowPanel panel)
	{
		super.fillBottomRow(panel);
		ModuleEditPageButton<TaxonomieElement> toevoegen =
			new ModuleEditPageButton<TaxonomieElement>(panel, "Nieuwe taxonomie toevoegen",
				CobraKeyAction.TOEVOEGEN, TaxonomieElement.class,
				OnderwijsCollectiefMenuItem.TaxonomieZoeken, TaxonomieElementZoekenPage.this)
			{
				private static final long serialVersionUID = 1L;

				/**
				 * @see nl.topicus.eduarte.web.components.panels.bottomrow.ModuleEditPageButton#getEntity()
				 */
				@Override
				protected TaxonomieElement getEntity()
				{
					Taxonomie taxonomie = new Taxonomie(EntiteitContext.INSTELLING);
					taxonomie.setTaxonomie(taxonomie);
					taxonomie.setTaxonomieElementType(TaxonomieElementType.getTaxonomieType());
					return taxonomie;
				}
			};
		panel.addButton(toevoegen);
	}
}
