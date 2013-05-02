package nl.topicus.eduarte.web.pages.deelnemerportaal.home;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.modelsv2.ModelFactory;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.web.components.panels.bottomrow.BewerkenButton;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.eduarte.core.principals.deelnemerportaal.DeelnemerportaalAccount;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis;
import nl.topicus.eduarte.web.components.menu.deelnemerPortaal.DeelnemerportaalHomeMenuItem;
import nl.topicus.eduarte.web.components.panels.AccountPanel;

import org.apache.wicket.Page;
import org.apache.wicket.markup.html.link.IPageLink;

/**
 * Pagina met de details van de ingelogde gebruiker.
 * 
 * @author marrink
 */
@PageInfo(title = "Account details ingelogde gebruiker", menu = "Home > Account")
@InPrincipal(DeelnemerportaalAccount.class)
public class DeelnemerportaalAccountPage extends AbstractDeelnemerportaalHomePage
{
	private static final long serialVersionUID = 1L;

	public DeelnemerportaalAccountPage()
	{
		this(getDefaultVerbintenis());
	}

	public DeelnemerportaalAccountPage(Verbintenis verbintenis)
	{
		super(DeelnemerportaalHomeMenuItem.Account, verbintenis);
		add(new AccountPanel("panel", ModelFactory.getModel(getIngelogdeAccount())));
		createComponents();
	}

	@Override
	protected void fillBottomRow(BottomRowPanel panel)
	{
		IPageLink wachtwoordWijzigen = new IPageLink()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public Page getPage()
			{
				return new EditDeelnemerportaalAccountPage(getContextVerbintenis());
			}

			@Override
			public Class< ? extends Page> getPageIdentity()
			{
				return EditDeelnemerportaalAccountPage.class;
			}
		};

		panel.addButton(new BewerkenButton<Void>(panel, wachtwoordWijzigen, "Wachtwoord wijzigen"));
	}
}
