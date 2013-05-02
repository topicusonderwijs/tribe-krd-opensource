package nl.topicus.eduarte.krd.web.components.menu;

import java.util.ArrayList;
import java.util.List;

import nl.topicus.cobra.web.components.menu.AbstractMenuExtender;
import nl.topicus.cobra.web.components.menu.IMenuItem;
import nl.topicus.cobra.web.components.menu.MenuItem;
import nl.topicus.eduarte.entities.taxonomie.Taxonomie;
import nl.topicus.eduarte.entities.taxonomie.Verbintenisgebied;
import nl.topicus.eduarte.krd.web.pages.taxonomie.TaxonomieElementCriteriaPage;
import nl.topicus.eduarte.krd.web.pages.taxonomie.TaxonomieElementProductregelsPage;
import nl.topicus.eduarte.krd.web.pages.taxonomie.TaxonomieElementTypesPage;
import nl.topicus.eduarte.web.components.menu.TaxonomieElementMenu;
import nl.topicus.eduarte.web.components.menu.TaxonomieElementMenuItem;

/**
 * Menu-extender voor de taxonomie-elementpagina's. Deze voegt pagina's toe voor het
 * definieren van nieuwe taxonomieen.
 * 
 * @author loite
 */
public class KrdTaxonomieElementMenuExtender extends AbstractMenuExtender<TaxonomieElementMenu>
{

	@Override
	public List<IMenuItem> getMenuExtension(TaxonomieElementMenu menu)
	{
		List<IMenuItem> ret = new ArrayList<IMenuItem>();
		if (menu.getTaxonomieElementModel().getObject() instanceof Verbintenisgebied)
		{
			ret.add(new MenuItem(menu.createPageLink(TaxonomieElementProductregelsPage.class),
				TaxonomieElementMenuItem.Productregels));
			ret.add(new MenuItem(menu.createPageLink(TaxonomieElementCriteriaPage.class),
				TaxonomieElementMenuItem.Criteria));
		}
		if (menu.getTaxonomieElementModel().getObject() instanceof Taxonomie)
		{
			ret.add(new MenuItem(menu.createPageLink(TaxonomieElementTypesPage.class),
				TaxonomieElementMenuItem.TaxonomieElementTypes));
		}
		return ret;
	}

}
