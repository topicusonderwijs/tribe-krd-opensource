package nl.topicus.eduarte.modules;

import java.util.ArrayList;
import java.util.List;

import nl.topicus.cobra.web.components.menu.AbstractMenuExtender;
import nl.topicus.cobra.web.components.menu.DropdownMenuItem;
import nl.topicus.cobra.web.components.menu.HorizontalSeperator;
import nl.topicus.cobra.web.components.menu.IMenuItem;
import nl.topicus.cobra.web.components.menu.MenuItem;
import nl.topicus.eduarte.web.components.menu.BeheerMenu;
import nl.topicus.eduarte.web.components.menu.BeheerMenuItem;
import nl.topicus.eduarte.web.pages.beheer.onderwijs.OnderwijsproductNiveauaanduidingZoekenPage;

public class OnderwijscatalogusAmarantisBeheerMenuExtender extends AbstractMenuExtender<BeheerMenu>
{
	@Override
	public List<IMenuItem> getMenuExtension(BeheerMenu menu)
	{
		List<IMenuItem> ret = new ArrayList<IMenuItem>();

		DropdownMenuItem onderwijsMenu = menu.findMenu(BeheerMenu.ONDERWIJSMENUNAME);
		onderwijsMenu.add(new HorizontalSeperator());
		onderwijsMenu.add(new MenuItem(OnderwijsproductNiveauaanduidingZoekenPage.class,
			BeheerMenuItem.OnderwijsproductNiveau));
		onderwijsMenu.add(new HorizontalSeperator());

		return ret;
	}
}
