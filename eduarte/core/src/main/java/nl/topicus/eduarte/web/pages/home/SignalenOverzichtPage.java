/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.web.pages.home;

import nl.topicus.cobra.app.INavigationBasePage;
import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.eduarte.core.principals.Always;
import nl.topicus.eduarte.web.components.menu.HomeMenuItem;
import nl.topicus.eduarte.web.components.panels.signalen.OverzichtSignalenPanel;

import org.apache.wicket.markup.html.WebMarkupContainer;

@PageInfo(title = "Overzicht signalen", menu = "Home > Signalen")
@InPrincipal(Always.class)
public class SignalenOverzichtPage extends AbstractHomePage<Void> implements INavigationBasePage
{
	private static final long serialVersionUID = 1L;

	public SignalenOverzichtPage()
	{
		super(HomeMenuItem.OverzichtSignalen);
		if (getIngelogdeMedewerker() != null)
		{
			add(new OverzichtSignalenPanel("signalen", getIngelogdeMedewerker().getPersoon(), 20));
		}
		else
		{
			add(new WebMarkupContainer("signalen").setVisible(false));
		}
		createComponents();
	}
}
