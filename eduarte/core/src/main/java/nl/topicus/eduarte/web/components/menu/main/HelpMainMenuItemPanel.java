/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.web.components.menu.main;

import nl.topicus.cobra.web.components.link.SecureAjaxFallbackLink;
import nl.topicus.cobra.web.components.menu.main.CobraMainMenu;
import nl.topicus.cobra.web.components.menu.main.DefaultMainMenuItemPanel;
import nl.topicus.cobra.web.pages.CobraSecurePage;
import nl.topicus.eduarte.app.EduArteRequestCycle;
import nl.topicus.eduarte.entities.organisatie.Instelling;
import nl.topicus.eduarte.entities.organisatie.Organisatie;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.security.checks.AlwaysGrantedSecurityCheck;

/**
 * @author loite
 */
public final class HelpMainMenuItemPanel extends DefaultMainMenuItemPanel
{
	private static final long serialVersionUID = 1L;

	public HelpMainMenuItemPanel(CobraMainMenu menu)
	{
		super(CoreMainMenuItem.Help, menu, "icon_help");
	}

	@Override
	public WebMarkupContainer getLink(String id)
	{
		SecureAjaxFallbackLink<Void> link = new SecureAjaxFallbackLink<Void>(id)
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick(AjaxRequestTarget target)
			{
				if (target != null)
				{
					String username = "";
					String password = "";
					Organisatie organisatie =
						(Organisatie) EduArteRequestCycle.get().getOrganisatie().doUnproxy();
					if (organisatie instanceof Instelling)
					{
						Instelling instelling = (Instelling) organisatie;
						username = instelling.getWikiUser() == null ? "" : instelling.getWikiUser();
						password =
							instelling.getWikiPassword() == null ? "" : instelling
								.getWikiPassword();
					}
					target.appendJavascript("wikiLogin('" + username + "','" + password + "','"
						+ ((CobraSecurePage) getPage()).getWikiName() + "')");
				}
			}

			@Override
			public boolean isEnabled()
			{
				// zorg ervoor dat de help altijd enabled is, ook op edit pages
				return true;
			}
		};
		link.setSecurityCheck(new AlwaysGrantedSecurityCheck());
		return link;
	}
}
