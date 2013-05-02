/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.web.components.menu;

import nl.topicus.cobra.web.components.menu.AbstractMenuBar;
import nl.topicus.cobra.web.components.menu.MenuItem;
import nl.topicus.cobra.web.components.menu.MenuItemKey;
import nl.topicus.eduarte.web.pages.groep.GroepRapportagesPage;
import nl.topicus.eduarte.web.pages.groep.GroepZoekenPage;
import nl.topicus.eduarte.web.pages.groep.GroepsdeelnameZoekenPage;
import nl.topicus.eduarte.web.pages.groep.MijnGroepenPage;

/**
 * @author hoeve
 */
public class GroepCollectiefMenu extends AbstractMenuBar
{
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor.
	 * 
	 * @param id
	 * @param selectedItem
	 */
	public GroepCollectiefMenu(String id, MenuItemKey selectedItem)
	{
		super(id, selectedItem);
		addItem(new MenuItem(GroepZoekenPage.class, GroepCollectiefMenuItem.GroepZoeken));
		addItem(new MenuItem(GroepsdeelnameZoekenPage.class,
			GroepCollectiefMenuItem.GroepsdeelnameZoeken));

		addItem(new MenuItem(MijnGroepenPage.class, GroepCollectiefMenuItem.MijnGroepen));

		addItem(new MenuItem(GroepRapportagesPage.class, GroepCollectiefMenuItem.Rapportages));

		addModuleMenuItems();
	}
}
