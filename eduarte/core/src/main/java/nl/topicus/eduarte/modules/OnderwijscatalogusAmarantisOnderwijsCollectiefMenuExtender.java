package nl.topicus.eduarte.modules;

import java.util.ArrayList;
import java.util.List;

import nl.topicus.cobra.web.components.menu.AbstractMenuExtender;
import nl.topicus.cobra.web.components.menu.IMenuItem;
import nl.topicus.cobra.web.components.menu.MenuItem;
import nl.topicus.eduarte.web.components.menu.OnderwijsCollectiefMenu;
import nl.topicus.eduarte.web.components.menu.OnderwijsCollectiefMenuItem;
import nl.topicus.eduarte.web.pages.onderwijs.curriculum.CurriculumOnderwijsproductOverzichtPage;

public class OnderwijscatalogusAmarantisOnderwijsCollectiefMenuExtender extends
		AbstractMenuExtender<OnderwijsCollectiefMenu>
{
	@Override
	public List<IMenuItem> getMenuExtension(OnderwijsCollectiefMenu menu)
	{
		List<IMenuItem> ret = new ArrayList<IMenuItem>();

		ret
			.add(new MenuItem(CurriculumOnderwijsproductOverzichtPage.class, OnderwijsCollectiefMenuItem.Curriculum));

		return ret;
	}
}
