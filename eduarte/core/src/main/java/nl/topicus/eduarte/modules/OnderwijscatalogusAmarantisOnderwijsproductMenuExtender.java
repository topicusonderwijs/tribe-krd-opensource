package nl.topicus.eduarte.modules;

import java.util.ArrayList;
import java.util.List;

import nl.topicus.cobra.web.components.menu.AbstractMenuExtender;
import nl.topicus.cobra.web.components.menu.DropdownMenuItem;
import nl.topicus.cobra.web.components.menu.IMenuItem;
import nl.topicus.cobra.web.components.menu.MenuItem;
import nl.topicus.eduarte.web.components.menu.OnderwijsproductMenu;
import nl.topicus.eduarte.web.components.menu.OnderwijsproductMenuItem;
import nl.topicus.eduarte.web.pages.onderwijs.onderwijscatalogus.amarantis.OnderwijsproductAmarantisBelastingOverzichtPage;
import nl.topicus.eduarte.web.pages.onderwijs.onderwijscatalogus.amarantis.OnderwijsproductAmarantisMetadataOverzichtPage;

public class OnderwijscatalogusAmarantisOnderwijsproductMenuExtender extends
		AbstractMenuExtender<OnderwijsproductMenu>
{
	@Override
	public List<IMenuItem> getMenuExtension(OnderwijsproductMenu menu)
	{
		List<IMenuItem> ret = new ArrayList<IMenuItem>();

		DropdownMenuItem amarantis = new DropdownMenuItem("Amarantis");
		amarantis.add(new MenuItem(menu
			.createPageLink(OnderwijsproductAmarantisBelastingOverzichtPage.class),
			OnderwijsproductMenuItem.AmarantisBelasting));
		amarantis.add(new MenuItem(menu
			.createPageLink(OnderwijsproductAmarantisMetadataOverzichtPage.class),
			OnderwijsproductMenuItem.AmarantisMetadata));
		ret.add(amarantis);
		return ret;
	}

}
