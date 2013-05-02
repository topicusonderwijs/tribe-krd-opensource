package nl.topicus.eduarte.onderwijscatalogus.web.components.menu;

import java.util.ArrayList;
import java.util.List;

import nl.topicus.cobra.web.components.menu.AbstractMenuExtender;
import nl.topicus.cobra.web.components.menu.DropdownMenuItem;
import nl.topicus.cobra.web.components.menu.HorizontalSeperator;
import nl.topicus.cobra.web.components.menu.IMenuItem;
import nl.topicus.cobra.web.components.menu.MenuItem;
import nl.topicus.eduarte.onderwijscatalogus.web.pages.beheer.leerstijl.LeerstijlZoekenPage;
import nl.topicus.eduarte.onderwijscatalogus.web.pages.beheer.middelen.AggregatieniveauZoekenPage;
import nl.topicus.eduarte.onderwijscatalogus.web.pages.beheer.middelen.GebruiksmiddelZoekenPage;
import nl.topicus.eduarte.onderwijscatalogus.web.pages.beheer.middelen.VerbruiksmiddelZoekenPage;
import nl.topicus.eduarte.onderwijscatalogus.web.pages.beheer.soortOnderwijsproduct.SoortOnderwijsproductZoekenPage;
import nl.topicus.eduarte.onderwijscatalogus.web.pages.beheer.soortpraktijklokaal.SoortPraktijklokaalZoekenPage;
import nl.topicus.eduarte.onderwijscatalogus.web.pages.beheer.typelocatie.TypeLocatieZoekenPage;
import nl.topicus.eduarte.onderwijscatalogus.web.pages.beheer.typetoets.TypeToetsZoekenPage;
import nl.topicus.eduarte.web.components.menu.BeheerMenu;
import nl.topicus.eduarte.web.components.menu.BeheerMenuItem;

/**
 * Extender voor het beheer gedeelte.
 * 
 * @author vandekamp
 */
public class OnderwijscatalogusBeheerMenuExtender extends AbstractMenuExtender<BeheerMenu>
{

	@Override
	public List<IMenuItem> getMenuExtension(BeheerMenu menu)
	{
		List<IMenuItem> ret = new ArrayList<IMenuItem>();

		DropdownMenuItem onderwijsMenu = findSubmenu(menu, BeheerMenu.ONDERWIJSMENUNAME);
		onderwijsMenu.add(new HorizontalSeperator());
		onderwijsMenu.add(new MenuItem(LeerstijlZoekenPage.class, BeheerMenuItem.Leerstijlen));
		onderwijsMenu.add(new MenuItem(TypeLocatieZoekenPage.class, BeheerMenuItem.TypeLocaties));
		onderwijsMenu.add(new MenuItem(TypeToetsZoekenPage.class, BeheerMenuItem.TypeToetsen));
		onderwijsMenu.add(new MenuItem(SoortOnderwijsproductZoekenPage.class,
			BeheerMenuItem.SoortOnderwijsproducten));
		onderwijsMenu.add(new MenuItem(SoortPraktijklokaalZoekenPage.class,
			BeheerMenuItem.SoortPraktijklokalen));
		onderwijsMenu.add(new MenuItem(AggregatieniveauZoekenPage.class,
			BeheerMenuItem.Aggregatieniveaus));
		onderwijsMenu.add(new HorizontalSeperator());
		onderwijsMenu.add(new MenuItem(GebruiksmiddelZoekenPage.class,
			BeheerMenuItem.Gebruiksmiddelen));
		onderwijsMenu.add(new MenuItem(VerbruiksmiddelZoekenPage.class,
			BeheerMenuItem.Verbruiksmiddelen));
		return ret;
	}

}
