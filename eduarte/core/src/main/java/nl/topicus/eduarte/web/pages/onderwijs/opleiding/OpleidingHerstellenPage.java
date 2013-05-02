package nl.topicus.eduarte.web.pages.onderwijs.opleiding;

import java.util.List;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.dataproviders.GeneralFilteredSortableDataProvider;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.web.components.datapanel.CustomDataPanel;
import nl.topicus.cobra.web.components.datapanel.CustomDataPanelPageLinkRowFactory;
import nl.topicus.cobra.web.components.menu.AbstractMenuBar;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.eduarte.core.principals.onderwijs.OpleidingenZoeken;
import nl.topicus.eduarte.dao.helpers.OpleidingDataAccessHelper;
import nl.topicus.eduarte.entities.opleiding.Opleiding;
import nl.topicus.eduarte.web.components.datapanel.ActiefRowFactoryDecorator;
import nl.topicus.eduarte.web.components.menu.OnderwijsCollectiefMenu;
import nl.topicus.eduarte.web.components.menu.OnderwijsCollectiefMenuItem;
import nl.topicus.eduarte.web.components.menu.main.CoreMainMenuItem;
import nl.topicus.eduarte.web.components.panels.EduArteDataPanel;
import nl.topicus.eduarte.web.components.panels.datapanel.table.OpleidingTable;
import nl.topicus.eduarte.web.components.panels.filter.OpleidingZoekFilterPanel;
import nl.topicus.eduarte.web.pages.SecurePage;
import nl.topicus.eduarte.zoekfilters.OpleidingZoekFilter;
import nl.topicus.eduarte.zoekfilters.OrganisatieEenheidLocatieAuthorizationContext;

import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.security.checks.AlwaysGrantedSecurityCheck;

/**
 * @author schimmel
 */
@PageInfo(title = "Opleiding herstellen", menu = "Onderwijs")
@InPrincipal(OpleidingenZoeken.class)
public class OpleidingHerstellenPage extends SecurePage
{
	private static final long serialVersionUID = 1L;

	private final OpleidingZoekFilterPanel filterPanel;

	private static final OpleidingZoekFilter getDefaultFilter()
	{
		OpleidingZoekFilter filter = new OpleidingZoekFilter();
		filter.addOrderByProperty("code");
		return filter;
	}

	public OpleidingHerstellenPage()
	{
		this(getDefaultFilter());
	}

	public OpleidingHerstellenPage(OpleidingZoekFilter filter)
	{
		super(CoreMainMenuItem.Onderwijs);

		filter.setAuthorizationContext(new OrganisatieEenheidLocatieAuthorizationContext(
			new AlwaysGrantedSecurityCheck()));

		GeneralFilteredSortableDataProvider<Opleiding, OpleidingZoekFilter> provider =
			GeneralFilteredSortableDataProvider.of(filter,
				OpleidingDataAccessHelper.class);
		final CustomDataPanel<Opleiding> datapanel =
			new EduArteDataPanel<Opleiding>("datapanel", provider, new OpleidingTable());
		datapanel.setRowFactory(new ActiefRowFactoryDecorator<Opleiding>(
			new CustomDataPanelPageLinkRowFactory<Opleiding>(OpleidingkaartPage.class)
			{
				private static final long serialVersionUID = 1L;

				@Override
				public void onClick(Item<Opleiding> item)
				{
					Opleiding opleiding = item.getModelObject();
					pushSearchResultToNavigationLevel(datapanel, item.getIndex());
					setResponsePage(new OpleidingkaartPage(opleiding, OpleidingHerstellenPage.this));
				}
			}));
		datapanel.setItemsPerPage(20);
		add(datapanel);
		filterPanel = new OpleidingZoekFilterPanel("filter", filter, datapanel);
		add(filterPanel);
		createComponents();
	}

	@Override
	protected void fillBottomRow(BottomRowPanel panel)
	{
		super.fillBottomRow(panel);
	}

	@Override
	public AbstractMenuBar createMenu(String id)
	{
		return new OnderwijsCollectiefMenu(id, OnderwijsCollectiefMenuItem.Herstellen);
	}

	@Override
	public void getBookmarkConstructorArguments(List<Class< ? >> ctorArgTypes,
			List<Object> ctorArgValues)
	{
		super.getBookmarkConstructorArguments(ctorArgTypes, ctorArgValues);
		ctorArgTypes.add(OpleidingZoekFilter.class);
		ctorArgValues.add(filterPanel.getZoekfilter());
	}
}
