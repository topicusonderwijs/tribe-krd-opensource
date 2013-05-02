package nl.topicus.eduarte.krdparticipatie.web.components.menu.extenders;

import java.util.ArrayList;
import java.util.List;

import nl.topicus.cobra.web.components.menu.AbstractMenuExtender;
import nl.topicus.cobra.web.components.menu.DropdownMenuItem;
import nl.topicus.cobra.web.components.menu.IMenuItem;
import nl.topicus.cobra.web.components.menu.MenuItem;
import nl.topicus.eduarte.krdparticipatie.web.components.menu.enums.ParticipatieDeelnemerCollectiefMenuItem;
import nl.topicus.eduarte.krdparticipatie.web.components.menu.enums.ParticipatieDeelnemerMenuItem;
import nl.topicus.eduarte.krdparticipatie.web.pages.deelnemer.collectief.LeerplichtRapportagePage;
import nl.topicus.eduarte.krdparticipatie.web.pages.verzuimloket.CollectiviteitVerzuimloketPage;
import nl.topicus.eduarte.web.components.menu.DeelnemerCollectiefMenu;
import nl.topicus.eduarte.web.components.menu.DeelnemerMenu;

/**
 * @author vandekamp
 */
public class KrdParticipatieDeelnemerCollectiefMenuExtender extends
		AbstractMenuExtender<DeelnemerCollectiefMenu>
{

	@Override
	public List<IMenuItem> getMenuExtension(DeelnemerCollectiefMenu menu)
	{
		List<IMenuItem> ret = new ArrayList<IMenuItem>();
		DropdownMenuItem participatie = findSubmenu(menu, DeelnemerMenu.AANWEZIGHEIDMENUNAME);
		if (participatie == null)
		{
			participatie = new DropdownMenuItem(DeelnemerMenu.AANWEZIGHEIDMENUNAME);
			ret.add(participatie);
		}
		participatie.add(new MenuItem(LeerplichtRapportagePage.class,
			ParticipatieDeelnemerCollectiefMenuItem.VerzuimRapportage));
		participatie.add(new MenuItem(CollectiviteitVerzuimloketPage.class,
			ParticipatieDeelnemerMenuItem.Verzuimloket));

		// participatie.add(new MenuItem(BudgetToekennenPage.class,
		// ParticipatieDeelnemerCollectiefMenuItem.Budget_toekennen));
		return ret;
	}
}
