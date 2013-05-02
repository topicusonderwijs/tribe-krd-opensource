package nl.topicus.eduarte.web.components.menu.main;

import nl.topicus.cobra.web.components.menu.main.CobraMainMenu;
import nl.topicus.cobra.web.components.menu.main.DefaultMainMenuItemPanel;
import nl.topicus.cobra.web.security.TargetBasedSecurePageLink;
import nl.topicus.eduarte.web.pages.SecurePage;
import nl.topicus.eduarte.web.pages.deelnemerportaal.home.DeelnemerportaalHomePage;

import org.apache.wicket.markup.html.WebMarkupContainer;

public class DeelnemerportaalHomeMainMenuItemPanel extends DefaultMainMenuItemPanel
{
	private static final long serialVersionUID = 1L;

	public DeelnemerportaalHomeMainMenuItemPanel(CobraMainMenu menu)
	{
		super(DeelnemerportaalMainMenuItem.Home, menu, "icon_home");
	}

	@Override
	public WebMarkupContainer getLink(String id)
	{
		return new TargetBasedSecurePageLink<Void>(id, DeelnemerportaalHomePage.class)
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
