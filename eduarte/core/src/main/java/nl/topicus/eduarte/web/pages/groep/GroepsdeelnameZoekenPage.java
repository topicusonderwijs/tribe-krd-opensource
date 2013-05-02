/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.web.pages.groep;

import java.util.List;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.dataproviders.GeneralFilteredSortableDataProvider;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.web.components.datapanel.CustomDataPanel;
import nl.topicus.cobra.web.components.menu.AbstractMenuBar;
import nl.topicus.eduarte.core.principals.groep.GroepenZoeken;
import nl.topicus.eduarte.dao.helpers.GroepsdeelnameDataAccessHelper;
import nl.topicus.eduarte.entities.groep.Groepsdeelname;
import nl.topicus.eduarte.web.components.menu.GroepCollectiefMenu;
import nl.topicus.eduarte.web.components.menu.GroepCollectiefMenuItem;
import nl.topicus.eduarte.web.components.menu.main.CoreMainMenuItem;
import nl.topicus.eduarte.web.components.panels.EduArteDataPanel;
import nl.topicus.eduarte.web.components.panels.datapanel.table.GroepsdeelnameTable;
import nl.topicus.eduarte.web.components.panels.filter.GroepsdeelnameZoekFilterPanel;
import nl.topicus.eduarte.web.pages.SecurePage;
import nl.topicus.eduarte.zoekfilters.GroepsdeelnameZoekFilter;
import nl.topicus.eduarte.zoekfilters.OrganisatieEenheidLocatieAuthorizationContext;

import org.apache.wicket.markup.repeater.data.IDataProvider;

/**
 * @author vandekamp
 */
@PageInfo(title = "Groepsdeelname zoeken", menu = "Groep")
@InPrincipal(GroepenZoeken.class)
public class GroepsdeelnameZoekenPage extends SecurePage
{
	private static final long serialVersionUID = 1L;

	private final GroepsdeelnameZoekFilterPanel filterPanel;

	/**
	 * @return Default zoekfilter voor de ingelogde gebruiker.
	 */
	public static final GroepsdeelnameZoekFilter getDefaultFilter()
	{
		GroepsdeelnameZoekFilter filter = new GroepsdeelnameZoekFilter();
		filter.addOrderByProperty("persoon.achternaam");
		return filter;
	}

	public GroepsdeelnameZoekenPage()
	{
		this(getDefaultFilter());
	}

	public GroepsdeelnameZoekenPage(GroepsdeelnameZoekFilter filter)
	{
		super(CoreMainMenuItem.Groep);
		filter.setAuthorizationContext(new OrganisatieEenheidLocatieAuthorizationContext(this));
		IDataProvider<Groepsdeelname> provider =
			GeneralFilteredSortableDataProvider.of(filter, GroepsdeelnameDataAccessHelper.class);
		final CustomDataPanel<Groepsdeelname> datapanel =
			new EduArteDataPanel<Groepsdeelname>("datapanel", provider, new GroepsdeelnameTable(
				false, false, true));
		datapanel.setItemsPerPage(20);
		add(datapanel);
		filterPanel = new GroepsdeelnameZoekFilterPanel("filter", filter, datapanel, true);
		add(filterPanel);
		createComponents();
	}

	@Override
	public AbstractMenuBar createMenu(String id)
	{
		return new GroepCollectiefMenu(id, GroepCollectiefMenuItem.GroepsdeelnameZoeken);
	}

	@Override
	public void getBookmarkConstructorArguments(List<Class< ? >> ctorArgTypes,
			List<Object> ctorArgValues)
	{
		super.getBookmarkConstructorArguments(ctorArgTypes, ctorArgValues);
		ctorArgTypes.add(GroepsdeelnameZoekFilter.class);
		ctorArgValues.add(getCurrentZoekFilter());
	}

	public GroepsdeelnameZoekFilter getCurrentZoekFilter()
	{
		return filterPanel.getZoekfilter();
	}
}
