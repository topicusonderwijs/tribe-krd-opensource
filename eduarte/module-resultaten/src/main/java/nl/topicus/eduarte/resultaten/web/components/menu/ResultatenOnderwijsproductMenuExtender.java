package nl.topicus.eduarte.resultaten.web.components.menu;

import java.util.ArrayList;
import java.util.List;

import nl.topicus.cobra.web.components.menu.AbstractMenuExtender;
import nl.topicus.cobra.web.components.menu.DropdownMenuItem;
import nl.topicus.cobra.web.components.menu.IMenuItem;
import nl.topicus.cobra.web.components.menu.MenuItem;
import nl.topicus.eduarte.resultaten.web.pages.onderwijs.OnderwijsproductResultatenPage;
import nl.topicus.eduarte.resultaten.web.pages.onderwijs.ResultatenHerberekenOverzichtPage;
import nl.topicus.eduarte.web.components.menu.OnderwijsproductMenu;
import nl.topicus.eduarte.web.components.menu.OnderwijsproductMenuItem;

/**
 * Menu-extender voor het KRD voor edit-pagina's van onderwijsproducten.
 * 
 * @author papegaaij
 */
public class ResultatenOnderwijsproductMenuExtender extends
		AbstractMenuExtender<OnderwijsproductMenu>
{
	@Override
	public List<IMenuItem> getMenuExtension(OnderwijsproductMenu menu)
	{
		if (menu.getOnderwijsproduct() != null
			&& menu.getOnderwijsproduct().getSoortProduct() != null
			&& menu.getOnderwijsproduct().getSoortProduct().isSummatief())
		{
			DropdownMenuItem resultaten = menu.findMenu(OnderwijsproductMenu.RESULTATEN_MENU_NAME);
			resultaten.add(new MenuItem(menu.createPageLink(OnderwijsproductResultatenPage.class),
				OnderwijsproductMenuItem.SummatieveStructuur));
			resultaten.add(new MenuItem(menu
				.createPageLink(ResultatenHerberekenOverzichtPage.class),
				OnderwijsproductMenuItem.Herberekeningen));
		}
		return new ArrayList<IMenuItem>();
	}
}
