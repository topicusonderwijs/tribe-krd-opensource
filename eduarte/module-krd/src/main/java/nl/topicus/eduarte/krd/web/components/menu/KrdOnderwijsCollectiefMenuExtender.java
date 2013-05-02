package nl.topicus.eduarte.krd.web.components.menu;

import java.util.Collections;
import java.util.List;

import nl.topicus.cobra.web.components.menu.AbstractMenuExtender;
import nl.topicus.cobra.web.components.menu.DropdownMenuItem;
import nl.topicus.cobra.web.components.menu.IMenuItem;
import nl.topicus.cobra.web.components.menu.MenuItem;
import nl.topicus.eduarte.krd.web.pages.onderwijs.opleiding.CriteriaKopierenOverzichtPage;
import nl.topicus.eduarte.krd.web.pages.onderwijs.opleiding.OpleidingInrichtingExporterenOverzichtPage;
import nl.topicus.eduarte.krd.web.pages.onderwijs.opleiding.OpleidingInrichtingImporterenOverzichtPage;
import nl.topicus.eduarte.krd.web.pages.onderwijs.opleiding.ProductregelsKopierenOverzichtPage;
import nl.topicus.eduarte.web.components.menu.OnderwijsCollectiefMenu;
import nl.topicus.eduarte.web.components.menu.OnderwijsCollectiefMenuItem;
import nl.topicus.eduarte.web.pages.onderwijs.taxonomie.TaxonomieElementZoekenPage;

public class KrdOnderwijsCollectiefMenuExtender extends
		AbstractMenuExtender<OnderwijsCollectiefMenu>
{
	@Override
	public List<IMenuItem> getMenuExtension(OnderwijsCollectiefMenu menu)
	{
		menu.addItem(new MenuItem(TaxonomieElementZoekenPage.class,
			OnderwijsCollectiefMenuItem.TaxonomieZoeken));

		DropdownMenuItem opleidingenMenu =
			findSubmenu(menu, OnderwijsCollectiefMenu.OPLEIDINGEN_MENU_NAME);
		opleidingenMenu.add(new MenuItem(ProductregelsKopierenOverzichtPage.class,
			OnderwijsCollectiefMenuItem.ProductregelsKopieren));
		opleidingenMenu.add(new MenuItem(CriteriaKopierenOverzichtPage.class,
			OnderwijsCollectiefMenuItem.CriteriaKopieren));
		opleidingenMenu.add(new MenuItem(OpleidingInrichtingExporterenOverzichtPage.class,
			OnderwijsCollectiefMenuItem.InrichtingExporteren));
		opleidingenMenu.add(new MenuItem(OpleidingInrichtingImporterenOverzichtPage.class,
			OnderwijsCollectiefMenuItem.InrichtingImporteren));
		return Collections.emptyList();
	}
}
