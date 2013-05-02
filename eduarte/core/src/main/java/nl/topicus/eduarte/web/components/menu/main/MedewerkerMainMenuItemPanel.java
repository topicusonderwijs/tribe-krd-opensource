/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.web.components.menu.main;

import nl.topicus.cobra.web.components.menu.main.CobraMainMenu;
import nl.topicus.cobra.web.components.menu.main.DefaultMainMenuItemPanel;
import nl.topicus.cobra.web.security.TargetBasedSecurePageLink;
import nl.topicus.eduarte.web.pages.SecurePage;
import nl.topicus.eduarte.web.pages.medewerker.MedewerkerZoekenPage;

import org.apache.wicket.markup.html.WebMarkupContainer;

/**
 * @author loite
 */
public final class MedewerkerMainMenuItemPanel extends DefaultMainMenuItemPanel
{
	private static final long serialVersionUID = 1L;

	public MedewerkerMainMenuItemPanel(CobraMainMenu menu)
	{
		super(CoreMainMenuItem.Medewerker, menu, "icon_medewerker");
	}

	@Override
	public WebMarkupContainer getLink(String id)
	{
		return new TargetBasedSecurePageLink<Void>(id, MedewerkerZoekenPage.class)
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick()
			{
				((SecurePage) getPage()).addToNavigationLevel();
				super.onClick();
			}
		};
	}
}
