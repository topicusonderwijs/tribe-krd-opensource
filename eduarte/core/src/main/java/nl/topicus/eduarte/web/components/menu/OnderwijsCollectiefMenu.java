package nl.topicus.eduarte.web.components.menu;

import nl.topicus.cobra.web.components.menu.AbstractMenuBar;
import nl.topicus.cobra.web.components.menu.DropdownMenuItem;
import nl.topicus.cobra.web.components.menu.HorizontalSeperator;
import nl.topicus.cobra.web.components.menu.MenuItem;
import nl.topicus.cobra.web.components.menu.MenuItemKey;
import nl.topicus.eduarte.web.pages.onderwijs.examenrapportage.OnderwijsDocumentTemplateZoekenPage;
import nl.topicus.eduarte.web.pages.onderwijs.examenrapportage.voorbeelden.OnderwijsDocumentTemplateVoorbeeldenPage;
import nl.topicus.eduarte.web.pages.onderwijs.onderwijscatalogus.OnderwijsproductUitgebreidZoekenPage;
import nl.topicus.eduarte.web.pages.onderwijs.onderwijscatalogus.OnderwijsproductZoekenPage;
import nl.topicus.eduarte.web.pages.onderwijs.onderwijscatalogus.ToetsCodeFiltersOverzichtPage;
import nl.topicus.eduarte.web.pages.onderwijs.opleiding.OpleidingHerstellenPage;
import nl.topicus.eduarte.web.pages.onderwijs.opleiding.OpleidingRapportagesPage;
import nl.topicus.eduarte.web.pages.onderwijs.opleiding.OpleidingZoekenPage;

/**
 * @author loite
 */
public class OnderwijsCollectiefMenu extends AbstractMenuBar
{
	private static final long serialVersionUID = 1L;

	public static final String ONDERWIJSPRODUCTEN_MENU_NAME = "Onderwijsproducten";

	public static final String OPLEIDINGEN_MENU_NAME = "Opleidingen";

	/**
	 * Constructor.
	 * 
	 * @param id
	 * @param selectedItem
	 */
	public OnderwijsCollectiefMenu(String id, MenuItemKey selectedItem)
	{
		super(id, selectedItem);
		DropdownMenuItem opleidingen = new DropdownMenuItem(OPLEIDINGEN_MENU_NAME);
		opleidingen.add(new MenuItem(OpleidingZoekenPage.class,
			OnderwijsCollectiefMenuItem.OpleidingenZoeken));
		opleidingen.add(new MenuItem(OpleidingHerstellenPage.class,
			OnderwijsCollectiefMenuItem.Herstellen));
		addItem(opleidingen);

		DropdownMenuItem onderwijsproducten = new DropdownMenuItem(ONDERWIJSPRODUCTEN_MENU_NAME);
		onderwijsproducten.add(new MenuItem(OnderwijsproductZoekenPage.class,
			OnderwijsCollectiefMenuItem.OnderwijsproductenZoeken));
		onderwijsproducten.add(new MenuItem(OnderwijsproductUitgebreidZoekenPage.class,
			OnderwijsCollectiefMenuItem.OnderwijsproductenUitgebreidZoeken));
		onderwijsproducten.add(new HorizontalSeperator());
		onderwijsproducten.add(new MenuItem(ToetsCodeFiltersOverzichtPage.class,
			OnderwijsCollectiefMenuItem.Toetsfilters));
		addItem(onderwijsproducten);

		DropdownMenuItem rapportages = new DropdownMenuItem("Rapportage");
		rapportages.add(new MenuItem(OpleidingRapportagesPage.class,
			OnderwijsCollectiefMenuItem.OpleidingRapportages));
		rapportages.add(new MenuItem(OnderwijsDocumentTemplateZoekenPage.class,
			OnderwijsCollectiefMenuItem.Samenvoegdocumenten));
		rapportages.add(new MenuItem(OnderwijsDocumentTemplateVoorbeeldenPage.class,
			OnderwijsCollectiefMenuItem.Voorbeelden));
		addItem(rapportages);

		addModuleMenuItems();
	}
}
