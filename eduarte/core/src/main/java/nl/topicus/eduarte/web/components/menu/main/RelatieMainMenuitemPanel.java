package nl.topicus.eduarte.web.components.menu.main;

import nl.topicus.cobra.web.components.menu.main.CobraMainMenu;
import nl.topicus.cobra.web.components.menu.main.DefaultMainMenuItemPanel;
import nl.topicus.cobra.web.security.TargetBasedSecurePageLink;
import nl.topicus.eduarte.app.EduArteApp;
import nl.topicus.eduarte.web.pages.SecurePage;
import nl.topicus.eduarte.web.pages.beheer.contract.ContractZoekenPage;
import nl.topicus.eduarte.web.pages.beheer.organisatie.ExterneOrganisatieZoekenPage;

import org.apache.wicket.Page;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.security.actions.Enable;
import org.apache.wicket.security.checks.ClassSecurityCheck;

/**
 * 
 * 
 * @author vanharen
 */
public class RelatieMainMenuitemPanel extends DefaultMainMenuItemPanel
{

	private static final long serialVersionUID = 1L;

	public RelatieMainMenuitemPanel(CobraMainMenu menu)
	{
		super(CoreMainMenuItem.Relatie, menu, "icon_contacts");
	}

	@Override
	public WebMarkupContainer getLink(String id)
	{

		boolean externeorganisatieZoekenRecht =
			new ClassSecurityCheck(ExterneOrganisatieZoekenPage.class)
				.isActionAuthorized(EduArteApp.get().getActionFactory().getAction(Enable.class));

		Class< ? extends Page> clazz;
		if (externeorganisatieZoekenRecht)
			clazz = ExterneOrganisatieZoekenPage.class;
		else
			clazz = ContractZoekenPage.class;

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
