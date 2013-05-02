package nl.topicus.eduarte.modules;

import java.util.ArrayList;
import java.util.List;

import nl.topicus.cobra.web.components.menu.AbstractMenuExtender;
import nl.topicus.cobra.web.components.menu.DropdownMenuItem;
import nl.topicus.cobra.web.components.menu.IMenuItem;
import nl.topicus.cobra.web.components.menu.MenuItem;
import nl.topicus.eduarte.web.components.menu.DeelnemerCollectiefMenu;
import nl.topicus.eduarte.web.components.menu.DeelnemerCollectiefMenuItem;
import nl.topicus.eduarte.web.pages.deelnemer.ExactCsvExportPage;

public class ExactCsvExportMenuExtender extends AbstractMenuExtender<DeelnemerCollectiefMenu>
{
	@Override
	public List<IMenuItem> getMenuExtension(DeelnemerCollectiefMenu menu)
	{
		List<IMenuItem> ret = new ArrayList<IMenuItem>();
		DropdownMenuItem submenu = findSubmenu(menu, DeelnemerCollectiefMenu.RAPPORTAGESNAME);
		submenu.add(new MenuItem(ExactCsvExportPage.class,
			DeelnemerCollectiefMenuItem.ExactCsvRapportage));
		return ret;
	}
}
