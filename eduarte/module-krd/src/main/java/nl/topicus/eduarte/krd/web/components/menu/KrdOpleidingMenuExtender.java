package nl.topicus.eduarte.krd.web.components.menu;

import java.util.ArrayList;
import java.util.List;

import nl.topicus.cobra.web.components.menu.AbstractMenuExtender;
import nl.topicus.cobra.web.components.menu.IMenuItem;
import nl.topicus.cobra.web.components.menu.MenuItem;
import nl.topicus.eduarte.krd.web.pages.onderwijs.opleiding.OpleidingCriteriaPage;
import nl.topicus.eduarte.krd.web.pages.onderwijs.opleiding.OpleidingProductregelsPage;
import nl.topicus.eduarte.krd.web.pages.onderwijs.opleiding.OpleidingResultaatstructurenPage;
import nl.topicus.eduarte.web.components.menu.OpleidingMenu;
import nl.topicus.eduarte.web.components.menu.OpleidingMenuItem;

/**
 * Menu-extender voor het KRD voor edit-pagina's van opleidingen.
 * 
 * @author loite
 */
public class KrdOpleidingMenuExtender extends AbstractMenuExtender<OpleidingMenu>
{

	@Override
	public List<IMenuItem> getMenuExtension(OpleidingMenu menu)
	{
		List<IMenuItem> ret = new ArrayList<IMenuItem>();
		ret.add(new MenuItem(OpleidingMenu.createPageLink(OpleidingProductregelsPage.class, menu
			.getOpleidingModel()), OpleidingMenuItem.Productregels));
		ret.add(new MenuItem(OpleidingMenu.createPageLink(OpleidingCriteriaPage.class, menu
			.getOpleidingModel()), OpleidingMenuItem.Criteria));
		ret.add(new MenuItem(OpleidingMenu.createPageLink(OpleidingResultaatstructurenPage.class,
			menu.getOpleidingModel()), OpleidingMenuItem.Resultaatstructuren));
		return ret;
	}

}
