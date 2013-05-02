package nl.topicus.eduarte.web.components.menu.main;

import nl.topicus.cobra.web.components.menu.main.CobraMainMenu;
import nl.topicus.cobra.web.components.menu.main.DefaultMainMenuItemPanel;
import nl.topicus.cobra.web.security.TargetBasedSecurePageLink;
import nl.topicus.eduarte.web.pages.SecurePage;
import nl.topicus.eduarte.web.pages.deelnemerportaal.dossier.DeelnemerportaalGroepenPage;

import org.apache.wicket.markup.html.WebMarkupContainer;

public class DeelnemerportaalDossierMainMenuItemPanel extends DefaultMainMenuItemPanel
{
	private static final long serialVersionUID = 1L;

	public DeelnemerportaalDossierMainMenuItemPanel(CobraMainMenu menu)
	{
		super(DeelnemerportaalMainMenuItem.Dossier, menu, "icon_dossier");
	}

	@Override
	public WebMarkupContainer getLink(String id)
	{
		// TODO Paul - Rechten
		// Class< ? extends Page> clazz =
		// SoundScapeMainMenu.PersonaliaRechten() ? DeelnemerportaalPersonaliaPage.class
		// : DeelnemerportaalGroepenPage.class;

		return new TargetBasedSecurePageLink<Void>(id, DeelnemerportaalGroepenPage.class)
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
