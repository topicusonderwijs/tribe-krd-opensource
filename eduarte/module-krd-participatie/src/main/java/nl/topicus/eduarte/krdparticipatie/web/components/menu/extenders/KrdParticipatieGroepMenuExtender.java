package nl.topicus.eduarte.krdparticipatie.web.components.menu.extenders;

import java.util.ArrayList;
import java.util.List;

import nl.topicus.cobra.web.components.menu.DropdownMenuItem;
import nl.topicus.cobra.web.components.menu.IMenuItem;
import nl.topicus.cobra.web.components.menu.MenuExtender;
import nl.topicus.cobra.web.components.menu.MenuItem;
import nl.topicus.eduarte.krdparticipatie.web.components.menu.enums.ParticipatieGroepMenuItem;
import nl.topicus.eduarte.krdparticipatie.web.pages.groep.TotalenPerGroepPage;
import nl.topicus.eduarte.web.components.menu.GroepMenu;

/**
 * @author vandekamp
 */
public class KrdParticipatieGroepMenuExtender implements MenuExtender<GroepMenu>
{

	@Override
	public List<IMenuItem> getMenuExtension(GroepMenu menu)
	{
		List<IMenuItem> ret = new ArrayList<IMenuItem>();
		DropdownMenuItem participatie = menu.createDropdown("Aanwezigheid");
		ret.add(participatie);
		// participatie.add(new
		// MenuItem(menu.createPageLink(WaarnemingGroepOverzichtPage.class),
		// ParticipatieGroepMenuItem.Waarnemingen_weekoverzicht));
		participatie.add(new MenuItem(menu.createPageLink(TotalenPerGroepPage.class),
			ParticipatieGroepMenuItem.Totalen));
		return ret;
	}
}
