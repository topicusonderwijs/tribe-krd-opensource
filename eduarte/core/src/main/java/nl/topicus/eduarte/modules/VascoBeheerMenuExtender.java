package nl.topicus.eduarte.modules;

import java.util.ArrayList;
import java.util.List;

import nl.topicus.cobra.web.components.menu.AbstractMenuExtender;
import nl.topicus.cobra.web.components.menu.DropdownMenuItem;
import nl.topicus.cobra.web.components.menu.IMenuItem;
import nl.topicus.cobra.web.components.menu.MenuItem;
import nl.topicus.eduarte.web.components.menu.BeheerMenu;
import nl.topicus.eduarte.web.components.menu.BeheerMenuItem;
import nl.topicus.eduarte.web.pages.beheer.vasco.TokensImporterenPage;
import nl.topicus.eduarte.web.pages.beheer.vasco.TokensOverzichtPage;

public class VascoBeheerMenuExtender extends AbstractMenuExtender<BeheerMenu>
{
	@Override
	public List<IMenuItem> getMenuExtension(BeheerMenu menu)
	{
		List<IMenuItem> ret = new ArrayList<IMenuItem>();

		DropdownMenuItem submenu = findSubmenu(menu, BeheerMenu.ACCOUNTBEHEERNAME);

		submenu.add(new MenuItem(TokensOverzichtPage.class,
			BeheerMenuItem.TokensOrganisatieOverzicht));
		submenu.add(new MenuItem(TokensImporterenPage.class, BeheerMenuItem.TokensImporteren));

		return ret;
	}
}
