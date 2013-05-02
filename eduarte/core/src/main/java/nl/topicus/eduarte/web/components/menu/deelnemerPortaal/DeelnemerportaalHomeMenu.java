/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.web.components.menu.deelnemerPortaal;

import nl.topicus.cobra.web.components.menu.MenuItem;
import nl.topicus.cobra.web.components.menu.MenuItemKey;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis;
import nl.topicus.eduarte.web.pages.deelnemerportaal.home.DeelnemerportaalAccountPage;
import nl.topicus.eduarte.web.pages.deelnemerportaal.home.DeelnemerportaalHomePage;

import org.apache.wicket.model.IModel;

/**
 * @author ambrosius, papegaaij
 */
public class DeelnemerportaalHomeMenu extends AbstractDeelnemerportaalMenu
{
	private static final long serialVersionUID = 1L;

	public DeelnemerportaalHomeMenu(String id, MenuItemKey selectedTab,
			IModel<Verbintenis> inschrijvingModel)
	{
		super(id, selectedTab, inschrijvingModel);

		addItem(new MenuItem(createPageLink(DeelnemerportaalHomePage.class),
			DeelnemerportaalHomeMenuItem.Home));
		addItem(new MenuItem(createPageLink(DeelnemerportaalAccountPage.class),
			DeelnemerportaalHomeMenuItem.Account));

		addModuleMenuItems();
	}
}
