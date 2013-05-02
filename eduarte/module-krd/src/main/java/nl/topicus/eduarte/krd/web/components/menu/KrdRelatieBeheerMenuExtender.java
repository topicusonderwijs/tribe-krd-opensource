package nl.topicus.eduarte.krd.web.components.menu;

import java.util.ArrayList;
import java.util.List;

import nl.topicus.cobra.web.components.menu.AbstractMenuExtender;
import nl.topicus.cobra.web.components.menu.IMenuItem;
import nl.topicus.cobra.web.components.menu.MenuItem;
import nl.topicus.eduarte.web.components.menu.RelatieBeheerMenu;
import nl.topicus.eduarte.web.components.menu.RelatieBeheerMenuItem;
import nl.topicus.eduarte.web.pages.beheer.contract.ContractZoekenPage;
import nl.topicus.eduarte.web.pages.beheer.organisatie.ExterneOrganisatieZoekenPage;

/**
 * 
 * 
 * @author vanharen
 */
public class KrdRelatieBeheerMenuExtender extends AbstractMenuExtender<RelatieBeheerMenu>

{

	@Override
	public List<IMenuItem> getMenuExtension(RelatieBeheerMenu menu)
	{
		List<IMenuItem> ret = new ArrayList<IMenuItem>();

		ret.add(new MenuItem(ExterneOrganisatieZoekenPage.class,
			RelatieBeheerMenuItem.ExterneOrganisaties));
		ret.add(new MenuItem(ContractZoekenPage.class, RelatieBeheerMenuItem.Contracten));
		return ret;
	}
}
