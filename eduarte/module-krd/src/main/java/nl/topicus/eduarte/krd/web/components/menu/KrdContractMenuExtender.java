package nl.topicus.eduarte.krd.web.components.menu;

import java.util.ArrayList;
import java.util.List;

import nl.topicus.cobra.web.components.menu.AbstractMenuExtender;
import nl.topicus.cobra.web.components.menu.IMenuItem;
import nl.topicus.cobra.web.components.menu.MenuItem;
import nl.topicus.eduarte.web.components.menu.ContractMenu;
import nl.topicus.eduarte.web.components.menu.ContractMenuItem;
import nl.topicus.eduarte.web.pages.beheer.contract.ContractOverzichtPage;

public class KrdContractMenuExtender extends AbstractMenuExtender<ContractMenu>
{
	private static final long serialVersionUID = 1L;

	@Override
	public List<IMenuItem> getMenuExtension(ContractMenu menu)
	{
		List<IMenuItem> res = new ArrayList<IMenuItem>();
		menu.addItem(0, new MenuItem(menu.createPageLink(ContractOverzichtPage.class),
			ContractMenuItem.Contractkaart));
		return res;
	}
}