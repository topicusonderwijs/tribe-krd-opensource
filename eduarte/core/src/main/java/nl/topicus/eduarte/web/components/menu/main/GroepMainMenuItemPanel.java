/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.web.components.menu.main;

import nl.topicus.cobra.web.components.menu.main.CobraMainMenu;
import nl.topicus.cobra.web.components.menu.main.DefaultMainMenuItemPanel;
import nl.topicus.cobra.web.security.TargetBasedSecurePageLink;
import nl.topicus.eduarte.web.pages.SecurePage;
import nl.topicus.eduarte.web.pages.groep.GroepZoekenPage;

import org.apache.wicket.Page;
import org.apache.wicket.markup.html.WebMarkupContainer;

/**
 * @author loite
 */
public final class GroepMainMenuItemPanel extends DefaultMainMenuItemPanel
{
	private static final long serialVersionUID = 1L;

	public GroepMainMenuItemPanel(CobraMainMenu menu)
	{
		super(CoreMainMenuItem.Groep, menu, "icon_groep");
	}

	@Override
	public WebMarkupContainer getLink(String id)
	{
		Class< ? extends Page> clazz = GroepZoekenPage.class;

		return new TargetBasedSecurePageLink<Void>(id, clazz)
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
