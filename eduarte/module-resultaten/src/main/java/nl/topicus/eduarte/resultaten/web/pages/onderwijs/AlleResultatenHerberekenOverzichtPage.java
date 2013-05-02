package nl.topicus.eduarte.resultaten.web.pages.onderwijs;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.web.components.menu.AbstractMenuBar;
import nl.topicus.eduarte.resultaten.principals.onderwijs.OnderwijsproductHerberekeningen;
import nl.topicus.eduarte.resultaten.web.pages.shared.HerberekenJobBeheerPanel;
import nl.topicus.eduarte.web.components.menu.OnderwijsCollectiefMenu;
import nl.topicus.eduarte.web.components.menu.OnderwijsCollectiefMenuItem;
import nl.topicus.eduarte.web.components.menu.main.CoreMainMenuItem;
import nl.topicus.eduarte.web.pages.SecurePage;

/**
 * @author papegaaij
 */
@PageInfo(title = "Herberekeningen", menu = "Onderwijs > Onderwijsproducten > Herberekeningen")
@InPrincipal(OnderwijsproductHerberekeningen.class)
public class AlleResultatenHerberekenOverzichtPage extends SecurePage
{
	public AlleResultatenHerberekenOverzichtPage()
	{
		super(CoreMainMenuItem.Onderwijs);
		add(new HerberekenJobBeheerPanel("jobPanel"));
		createComponents();
	}

	@Override
	public AbstractMenuBar createMenu(String id)
	{
		return new OnderwijsCollectiefMenu(id, OnderwijsCollectiefMenuItem.Herberekeningen);
	}
}
