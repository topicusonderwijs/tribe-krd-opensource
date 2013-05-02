package nl.topicus.eduarte.resultaten.web.components.menu;

import java.util.Collections;
import java.util.List;

import nl.topicus.cobra.web.components.menu.AbstractMenuExtender;
import nl.topicus.cobra.web.components.menu.IMenuItem;
import nl.topicus.cobra.web.components.menu.MenuItem;
import nl.topicus.eduarte.resultaten.web.pages.deelnemer.DeelnemerAlleToetsenBevriezenPage;
import nl.topicus.eduarte.web.components.menu.DeelnemerMenu;
import nl.topicus.eduarte.web.components.menu.DeelnemerMenuItem;

public class ResultatenDeelnemerMenuExtender extends AbstractMenuExtender<DeelnemerMenu>
{
	private static final long serialVersionUID = 1L;

	@Override
	public List<IMenuItem> getMenuExtension(DeelnemerMenu menu)
	{
		findSubmenu(menu, DeelnemerMenu.RESULTATEN_MENU).add(
			new MenuItem(menu.createPageLink(DeelnemerAlleToetsenBevriezenPage.class),
				DeelnemerMenuItem.Bevriezen));

		return Collections.emptyList();
	}
}
