package nl.topicus.eduarte.modules;

import java.util.ArrayList;
import java.util.List;

import nl.topicus.cobra.web.components.menu.AbstractMenuExtender;
import nl.topicus.cobra.web.components.menu.IMenuItem;
import nl.topicus.cobra.web.components.menu.MenuItem;
import nl.topicus.eduarte.web.components.menu.OpleidingMenu;
import nl.topicus.eduarte.web.components.menu.OpleidingMenuItem;
import nl.topicus.eduarte.web.pages.onderwijs.opleiding.CurriculumOverzichtPage;

public class OnderwijscatalogusAmarantisOpleidingMenuExtender extends
		AbstractMenuExtender<OpleidingMenu>
{
	@Override
	public List<IMenuItem> getMenuExtension(OpleidingMenu menu)
	{
		List<IMenuItem> ret = new ArrayList<IMenuItem>();
		ret.add(new MenuItem(OpleidingMenu.createPageLink(CurriculumOverzichtPage.class, menu
			.getOpleidingModel()), OpleidingMenuItem.Curriculum));
		return ret;
	}
}
