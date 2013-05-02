package nl.topicus.eduarte.resultaten.web.components.menu;

import java.util.ArrayList;
import java.util.List;

import nl.topicus.cobra.web.components.menu.AbstractMenuExtender;
import nl.topicus.cobra.web.components.menu.DropdownMenuItem;
import nl.topicus.cobra.web.components.menu.IMenuItem;
import nl.topicus.cobra.web.components.menu.MenuItem;
import nl.topicus.eduarte.resultaten.web.pages.groep.GroepResultatenmatrixPage;
import nl.topicus.eduarte.web.components.menu.GroepMenu;
import nl.topicus.eduarte.web.components.menu.GroepMenuItem;

public class ResultatenGroepMenuExtender extends AbstractMenuExtender<GroepMenu>
{
	private static final long serialVersionUID = 1L;

	@Override
	public List<IMenuItem> getMenuExtension(GroepMenu menu)
	{
		List<IMenuItem> res = new ArrayList<IMenuItem>();

		DropdownMenuItem resultatenMenu = menu.findMenu(GroepMenu.RESULTATEN_MENU_NAME);

		resultatenMenu.add(new MenuItem(menu.createPageLink(GroepResultatenmatrixPage.class),
			GroepMenuItem.Invoeren));
		return res;
	}
}
