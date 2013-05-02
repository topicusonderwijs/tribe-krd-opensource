/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.web.components.menu.main;

import nl.topicus.cobra.web.components.menu.main.CobraMainMenu;
import nl.topicus.cobra.web.components.menu.main.DefaultMainMenuItemPanel;
import nl.topicus.cobra.web.security.TargetBasedSecurePageLink;
import nl.topicus.eduarte.app.EduArteApp;
import nl.topicus.eduarte.web.pages.SecurePage;
import nl.topicus.eduarte.web.pages.onderwijs.onderwijscatalogus.OnderwijsproductZoekenPage;
import nl.topicus.eduarte.web.pages.onderwijs.opleiding.OpleidingZoekenPage;
import nl.topicus.eduarte.web.pages.onderwijs.taxonomie.TaxonomieElementZoekenPage;

import org.apache.wicket.Page;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.security.actions.Enable;
import org.apache.wicket.security.checks.ClassSecurityCheck;

/**
 * @author loite
 */
public final class OnderwijsMainMenuItemPanel extends DefaultMainMenuItemPanel
{
	private static final long serialVersionUID = 1L;

	public OnderwijsMainMenuItemPanel(CobraMainMenu menu)
	{
		super(CoreMainMenuItem.Onderwijs, menu, "icon_onderwijs");
	}

	@Override
	public WebMarkupContainer getLink(String id)
	{
		boolean opleidingZoekenRecht =
			new ClassSecurityCheck(OpleidingZoekenPage.class).isActionAuthorized(EduArteApp.get()
				.getActionFactory().getAction(Enable.class));
		boolean onderwijsproductZoekenRecht =
			new ClassSecurityCheck(OnderwijsproductZoekenPage.class).isActionAuthorized(EduArteApp
				.get().getActionFactory().getAction(Enable.class));

		Class< ? extends Page> clazz;
		if (opleidingZoekenRecht)
			clazz = OpleidingZoekenPage.class;
		else if (onderwijsproductZoekenRecht)
			clazz = OnderwijsproductZoekenPage.class;
		else
			clazz = TaxonomieElementZoekenPage.class;
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
