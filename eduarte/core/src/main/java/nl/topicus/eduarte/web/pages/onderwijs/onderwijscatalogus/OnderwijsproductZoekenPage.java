package nl.topicus.eduarte.web.pages.onderwijs.onderwijscatalogus;

import java.util.List;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.dataproviders.GeneralFilteredSortableDataProvider;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.web.components.datapanel.CustomDataPanel;
import nl.topicus.cobra.web.components.datapanel.CustomDataPanelPageLinkRowFactory;
import nl.topicus.cobra.web.components.menu.AbstractMenuBar;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.cobra.web.components.shortcut.CobraKeyAction;
import nl.topicus.cobra.web.security.DisableSecurityCheckMarker;
import nl.topicus.eduarte.app.security.actions.Instelling;
import nl.topicus.eduarte.app.security.actions.OrganisatieEenheid;
import nl.topicus.eduarte.app.security.actions.SearchImplementsActions;
import nl.topicus.eduarte.app.security.checks.OrganisatieEenheidLocatieKoppelbaarSecurityCheck;
import nl.topicus.eduarte.core.principals.onderwijs.OnderwijsproductenZoeken;
import nl.topicus.eduarte.dao.helpers.OnderwijsproductDataAccessHelper;
import nl.topicus.eduarte.entities.onderwijsproduct.Onderwijsproduct;
import nl.topicus.eduarte.entities.onderwijsproduct.OnderwijsproductStatus;
import nl.topicus.eduarte.web.components.menu.OnderwijsCollectiefMenu;
import nl.topicus.eduarte.web.components.menu.OnderwijsCollectiefMenuItem;
import nl.topicus.eduarte.web.components.menu.main.CoreMainMenuItem;
import nl.topicus.eduarte.web.components.panels.EduArteDataPanel;
import nl.topicus.eduarte.web.components.panels.bottomrow.ModuleEditPageButton;
import nl.topicus.eduarte.web.components.panels.datapanel.table.OnderwijsproductTable;
import nl.topicus.eduarte.web.components.panels.filter.OnderwijsproductZoekFilterPanel;
import nl.topicus.eduarte.web.pages.SecurePage;
import nl.topicus.eduarte.zoekfilters.OnderwijsproductZoekFilter;
import nl.topicus.eduarte.zoekfilters.OrganisatieEenheidLocatieAuthorizationContext;

import org.apache.wicket.markup.repeater.Item;

/**
 * Pagina voor het zoeken naar onderwijsproducten.
 * 
 * @author loite
 */
@PageInfo(title = "Onderwijsproduct zoeken", menu = "Onderwijs > Onderwijsproducten")
@InPrincipal(OnderwijsproductenZoeken.class)
@SearchImplementsActions( {Instelling.class, OrganisatieEenheid.class})
public class OnderwijsproductZoekenPage extends SecurePage
{
	private static final long serialVersionUID = 1L;

	private static final OnderwijsproductZoekFilter getDefaultFilter()
	{
		OnderwijsproductZoekFilter filter = new OnderwijsproductZoekFilter();
		filter.setStatus(OnderwijsproductStatus.Beschikbaar);
		filter.addOrderByProperty("titel");
		return filter;
	}

	private OnderwijsproductZoekFilter filter;

	public OnderwijsproductZoekenPage()
	{
		this(getDefaultFilter());
	}

	public OnderwijsproductZoekenPage(OnderwijsproductZoekFilter filter)
	{
		super(CoreMainMenuItem.Onderwijs);
		this.filter = filter;
		filter.setAuthorizationContext(new OrganisatieEenheidLocatieAuthorizationContext(this));
		GeneralFilteredSortableDataProvider<Onderwijsproduct, OnderwijsproductZoekFilter> provider =
			GeneralFilteredSortableDataProvider.of(filter,
				OnderwijsproductDataAccessHelper.class);
		final CustomDataPanel<Onderwijsproduct> datapanel =
			new EduArteDataPanel<Onderwijsproduct>("datapanel", provider,
				new OnderwijsproductTable(true));

		datapanel.setRowFactory(new CustomDataPanelPageLinkRowFactory<Onderwijsproduct>(
			OnderwijsproductKaartPage.class)
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick(Item<Onderwijsproduct> item)
			{
				pushSearchResultToNavigationLevel(datapanel, item.getIndex());
				super.onClick(item);
			}
		});
		add(datapanel);
		OnderwijsproductZoekFilterPanel filterPanel =
			new OnderwijsproductZoekFilterPanel("filter", filter, datapanel);
		add(filterPanel);
		createComponents();
	}

	@Override
	public AbstractMenuBar createMenu(String id)
	{
		return new OnderwijsCollectiefMenu(id, OnderwijsCollectiefMenuItem.OnderwijsproductenZoeken);
	}

	@Override
	public void getBookmarkConstructorArguments(List<Class< ? >> ctorArgTypes,
			List<Object> ctorArgValues)
	{
		super.getBookmarkConstructorArguments(ctorArgTypes, ctorArgValues);
		ctorArgTypes.add(OnderwijsproductZoekFilter.class);
		ctorArgValues.add(filter);
	}

	@Override
	protected void fillBottomRow(BottomRowPanel panel)
	{
		super.fillBottomRow(panel);
		ModuleEditPageButton<Onderwijsproduct> toevoegen =
			new ModuleEditPageButton<Onderwijsproduct>(panel, "Toevoegen",
				CobraKeyAction.TOEVOEGEN, Onderwijsproduct.class,
				OnderwijsCollectiefMenuItem.OnderwijsproductenZoeken,
				OnderwijsproductZoekenPage.this)
			{
				private static final long serialVersionUID = 1L;

				@Override
				protected Onderwijsproduct getEntity()
				{
					return new Onderwijsproduct();
				}

			};
		DisableSecurityCheckMarker.place(toevoegen,
			OrganisatieEenheidLocatieKoppelbaarSecurityCheck.class);
		panel.addButton(toevoegen);
	}
}
