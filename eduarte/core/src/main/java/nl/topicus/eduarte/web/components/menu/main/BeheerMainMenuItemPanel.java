/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.web.components.menu.main;

import nl.topicus.cobra.web.components.menu.main.CobraMainMenu;
import nl.topicus.cobra.web.components.menu.main.DefaultMainMenuItemPanel;
import nl.topicus.cobra.web.security.TargetBasedSecurePageLink;
import nl.topicus.eduarte.web.pages.SecurePage;
import nl.topicus.eduarte.web.pages.beheer.BeheerHomePage;

import org.apache.wicket.markup.html.WebMarkupContainer;

/**
 * @author loite
 */
public final class BeheerMainMenuItemPanel extends DefaultMainMenuItemPanel
{
	private static final long serialVersionUID = 1L;

	public BeheerMainMenuItemPanel(CobraMainMenu menu)
	{
		super(CoreMainMenuItem.Beheer, menu, "icon_beheer");
	}

	@Override
	public WebMarkupContainer getLink(String id)
	{
		return new TargetBasedSecurePageLink<Void>(id, BeheerHomePage.class)
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
