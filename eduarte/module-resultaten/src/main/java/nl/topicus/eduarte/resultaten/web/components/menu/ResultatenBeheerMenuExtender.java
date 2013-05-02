package nl.topicus.eduarte.resultaten.web.components.menu;

import java.util.ArrayList;
import java.util.List;

import nl.topicus.cobra.web.components.menu.AbstractMenuExtender;
import nl.topicus.cobra.web.components.menu.DropdownMenuItem;
import nl.topicus.cobra.web.components.menu.IMenuItem;
import nl.topicus.cobra.web.components.menu.MenuItem;
import nl.topicus.eduarte.resultaten.web.pages.beheer.SchaalOverzichtPage;
import nl.topicus.eduarte.web.components.menu.BeheerMenu;
import nl.topicus.eduarte.web.components.menu.BeheerMenuItem;

/**
 * Extender voor het beheer gedeelte.
 */
public class ResultatenBeheerMenuExtender extends AbstractMenuExtender<BeheerMenu>
{
	@Override
	public List<IMenuItem> getMenuExtension(BeheerMenu menu)
	{
		DropdownMenuItem onderwijsMenu = findSubmenu(menu, BeheerMenu.ONDERWIJSMENUNAME);
		onderwijsMenu.add(new MenuItem(SchaalOverzichtPage.class, BeheerMenuItem.Schalen));

		List<IMenuItem> ret = new ArrayList<IMenuItem>();
		return ret;
	}
}
