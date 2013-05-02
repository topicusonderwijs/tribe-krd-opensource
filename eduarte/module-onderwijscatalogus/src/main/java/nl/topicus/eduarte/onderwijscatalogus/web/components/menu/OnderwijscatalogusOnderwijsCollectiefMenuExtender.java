package nl.topicus.eduarte.onderwijscatalogus.web.components.menu;

import java.util.Collections;
import java.util.List;

import nl.topicus.cobra.web.components.menu.AbstractMenuExtender;
import nl.topicus.cobra.web.components.menu.DropdownMenuItem;
import nl.topicus.cobra.web.components.menu.IMenuItem;
import nl.topicus.cobra.web.components.menu.MenuItem;
import nl.topicus.eduarte.onderwijscatalogus.web.pages.onderwijsproduct.importeren.OnderwijsproductImportOverzichtPage;
import nl.topicus.eduarte.onderwijscatalogus.web.pages.onderwijsproduct.importeren.ToegestaanOndProdImportOverzichtPage;
import nl.topicus.eduarte.web.components.menu.OnderwijsCollectiefMenu;
import nl.topicus.eduarte.web.components.menu.OnderwijsCollectiefMenuItem;

public class OnderwijscatalogusOnderwijsCollectiefMenuExtender extends
		AbstractMenuExtender<OnderwijsCollectiefMenu>
{
	@Override
	public List<IMenuItem> getMenuExtension(OnderwijsCollectiefMenu menu)
	{
		DropdownMenuItem onderwijsproductMenu =
			findSubmenu(menu, OnderwijsCollectiefMenu.ONDERWIJSPRODUCTEN_MENU_NAME);
		onderwijsproductMenu.add(new MenuItem(OnderwijsproductImportOverzichtPage.class,
			OnderwijsCollectiefMenuItem.OnderwijsproductenImporteren));
		onderwijsproductMenu.add(new MenuItem(ToegestaanOndProdImportOverzichtPage.class,
			OnderwijsCollectiefMenuItem.ToegestaneOnderwijsproductenImporteren));
		return Collections.emptyList();
	}
}
