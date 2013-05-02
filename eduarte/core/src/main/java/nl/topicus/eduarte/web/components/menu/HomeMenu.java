/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.web.components.menu;

import nl.topicus.cobra.web.components.menu.AbstractMenuBar;
import nl.topicus.cobra.web.components.menu.DropdownMenuItem;
import nl.topicus.cobra.web.components.menu.HorizontalSeperator;
import nl.topicus.cobra.web.components.menu.MenuItem;
import nl.topicus.cobra.web.components.menu.MenuItemKey;
import nl.topicus.eduarte.web.pages.home.*;

/**
 * @author loite
 */
public class HomeMenu extends AbstractMenuBar
{
	private static final long serialVersionUID = 1L;

	/**
	 * @param id
	 * @param selectedTab
	 */
	public HomeMenu(String id, MenuItemKey selectedTab)
	{
		super(id, selectedTab);
		addItem(new MenuItem(HomePage.class, HomeMenuItem.Home));
		addItem(new MenuItem(MyRunningJobsPage.class, HomeMenuItem.Rapportages));

		DropdownMenuItem teDoen = new DropdownMenuItem("Te Doen");
		teDoen.add(new MenuItem(MyIntakegesprekkenPage.class, HomeMenuItem.Intakegesprekken));
		teDoen.add(new MenuItem(MyContractVerplichtingenPage.class,
			HomeMenuItem.Contractverplichtingen));
		addItem(teDoen);

		addItem(new MenuItem(SignalenOverzichtPage.class, HomeMenuItem.OverzichtSignalen));

		addItem(new MenuItem(AccountPage.class, HomeMenuItem.Account));

		DropdownMenuItem instellingen = new DropdownMenuItem("Instellingen");
		instellingen.add(new MenuItem(ToetsFiltersPage.class, HomeMenuItem.Toetsfilters));
		instellingen.add(new HorizontalSeperator());
		instellingen.add(new MenuItem(PersoonlijkBeheerSignalenPage.class,
			HomeMenuItem.BeheerSignalen));
		instellingen.add(new MenuItem(SignaleringGroepenSelectiePage.class,
			HomeMenuItem.Signaleringsgroepen));
		instellingen.add(new MenuItem(SignaleringDeelnemerSelectiePage.class,
			HomeMenuItem.Signaleringsdeelnemers));
		addItem(instellingen);

		addModuleMenuItems();
	}
}
