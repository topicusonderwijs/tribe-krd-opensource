package nl.topicus.eduarte.krd.web.components.menu;

import java.util.Collections;
import java.util.List;

import nl.topicus.cobra.web.components.menu.AbstractMenuExtender;
import nl.topicus.cobra.web.components.menu.IMenuItem;
import nl.topicus.cobra.web.components.menu.MenuItem;
import nl.topicus.eduarte.web.components.menu.OnderwijsproductMenu;
import nl.topicus.eduarte.web.components.menu.OnderwijsproductMenuItem;
import nl.topicus.eduarte.web.pages.onderwijs.onderwijscatalogus.OnderwijsproductTaxonomiePage;

public class KrdOnderwijsproductMenuExtender extends AbstractMenuExtender<OnderwijsproductMenu>
{
	@Override
	public List<IMenuItem> getMenuExtension(OnderwijsproductMenu menu)
	{
		menu.addItem(new MenuItem(menu.createPageLink(OnderwijsproductTaxonomiePage.class),
			OnderwijsproductMenuItem.Taxonomie));
		return Collections.emptyList();
	}
}
