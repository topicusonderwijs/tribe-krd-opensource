package nl.topicus.eduarte.modules;

import java.util.ArrayList;
import java.util.List;

import nl.topicus.cobra.web.components.menu.AbstractMenuExtender;
import nl.topicus.cobra.web.components.menu.DropdownMenuItem;
import nl.topicus.cobra.web.components.menu.IMenuItem;
import nl.topicus.cobra.web.components.menu.MenuItem;
import nl.topicus.eduarte.web.components.menu.HomeMenu;
import nl.topicus.eduarte.web.components.menu.HomeMenuItem;
import nl.topicus.eduarte.web.pages.onderwijs.curriculum.CurriculumWizardStap1Page;

public class OnderwijscatalogusAmarantisHomeMenuExtender extends AbstractMenuExtender<HomeMenu>
{
	@Override
	public List<IMenuItem> getMenuExtension(HomeMenu menu)
	{
		List<IMenuItem> ret = new ArrayList<IMenuItem>();

		DropdownMenuItem amarantis = new DropdownMenuItem("Amarantis");
		amarantis.add(new MenuItem(CurriculumWizardStap1Page.class,
			HomeMenuItem.AmarantisCurriculumWizard));
		ret.add(amarantis);

		return ret;
	}
}
