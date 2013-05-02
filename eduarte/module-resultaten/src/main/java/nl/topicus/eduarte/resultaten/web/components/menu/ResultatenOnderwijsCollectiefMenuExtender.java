package nl.topicus.eduarte.resultaten.web.components.menu;

import java.util.Collections;
import java.util.List;

import nl.topicus.cobra.web.components.menu.AbstractMenuExtender;
import nl.topicus.cobra.web.components.menu.DropdownMenuItem;
import nl.topicus.cobra.web.components.menu.IMenuItem;
import nl.topicus.cobra.web.components.menu.MenuItem;
import nl.topicus.eduarte.resultaten.web.pages.onderwijs.AlleResultatenHerberekenOverzichtPage;
import nl.topicus.eduarte.resultaten.web.pages.onderwijs.ResultaatstructurenExporterenOverzichtPage;
import nl.topicus.eduarte.resultaten.web.pages.onderwijs.ResultaatstructurenImporterenOverzichtPage;
import nl.topicus.eduarte.resultaten.web.pages.onderwijs.ResultaatstructurenKopierenOverzichtPage;
import nl.topicus.eduarte.resultaten.web.pages.onderwijs.ToetsNormeringZoekenPage;
import nl.topicus.eduarte.web.components.menu.OnderwijsCollectiefMenu;
import nl.topicus.eduarte.web.components.menu.OnderwijsCollectiefMenuItem;

public class ResultatenOnderwijsCollectiefMenuExtender extends
		AbstractMenuExtender<OnderwijsCollectiefMenu>
{
	@Override
	public List<IMenuItem> getMenuExtension(OnderwijsCollectiefMenu menu)
	{
		DropdownMenuItem onderwijsproductenMenu =
			findSubmenu(menu, OnderwijsCollectiefMenu.ONDERWIJSPRODUCTEN_MENU_NAME);
		onderwijsproductenMenu.add(new MenuItem(ResultaatstructurenKopierenOverzichtPage.class,
			OnderwijsCollectiefMenuItem.ResultaatstructurenKopieren));
		onderwijsproductenMenu.add(new MenuItem(ResultaatstructurenExporterenOverzichtPage.class,
			OnderwijsCollectiefMenuItem.ResultaatstructurenExporteren));
		onderwijsproductenMenu.add(new MenuItem(ResultaatstructurenImporterenOverzichtPage.class,
			OnderwijsCollectiefMenuItem.ResultaatstructurenImporteren));
		onderwijsproductenMenu.add(new MenuItem(ToetsNormeringZoekenPage.class,
			OnderwijsCollectiefMenuItem.Normeringen));
		onderwijsproductenMenu.add(new MenuItem(AlleResultatenHerberekenOverzichtPage.class,
			OnderwijsCollectiefMenuItem.Herberekeningen));
		return Collections.emptyList();
	}
}
