package nl.topicus.eduarte.web.pages.beheer.account;

import java.util.ArrayList;
import java.util.List;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.modelsv2.ModelFactory;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.web.components.panels.bottomrow.BewerkenButton;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.cobra.web.components.panels.bottomrow.ButtonAlignment;
import nl.topicus.cobra.web.components.panels.bottomrow.PageLinkButton;
import nl.topicus.cobra.web.components.panels.bottomrow.TerugButton;
import nl.topicus.eduarte.app.EduArteContext;
import nl.topicus.eduarte.core.principals.beheer.account.AccountsRead;
import nl.topicus.eduarte.entities.security.authentication.Account;
import nl.topicus.eduarte.entities.security.authorization.AuthorisatieNiveau;
import nl.topicus.eduarte.entities.sidebar.IContextInfoObject;
import nl.topicus.eduarte.web.components.menu.BeheerMenuItem;
import nl.topicus.eduarte.web.components.panels.AccountPanel;
import nl.topicus.eduarte.web.pages.beheer.AbstractBeheerPage;
import nl.topicus.eduarte.web.pages.shared.AccountEditPage;
import nl.topicus.eduarte.web.pages.shared.AccountPasswordEditPage;

import org.apache.wicket.Page;
import org.apache.wicket.markup.html.link.IPageLink;

@PageInfo(title = "Account details", menu = "Beheer > Gebruikers > [Account]")
@InPrincipal(AccountsRead.class)
public class AccountOverviewPage extends AbstractBeheerPage<Account>
{
	public AccountOverviewPage(Account account)
	{
		super(ModelFactory.getModel(account), BeheerMenuItem.Gebruikers);

		add(new AccountPanel("accountPanel", getContextModel()));
		createComponents();
	}

	public Account getAccount()
	{
		return (Account) getDefaultModelObject();
	}

	@Override
	protected void fillBottomRow(BottomRowPanel panel)
	{
		panel.addButton(new PageLinkButton(panel, "Wachtwoord wijzigen", ButtonAlignment.RIGHT,
			new IPageLink()
			{
				private static final long serialVersionUID = 1L;

				@Override
				public Class< ? extends Page> getPageIdentity()
				{
					return AccountPasswordEditPage.class;
				}

				@Override
				public Page getPage()
				{
					return new AccountPasswordEditPage(AccountOverviewPage.this, getAccount());
				}
			}));
		panel.addButton(new BewerkenButton<Void>(panel, new IPageLink()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public Page getPage()
			{
				return new AccountEditPage(AccountOverviewPage.this, getAccount());
			}

			@Override
			public Class< ? extends Page> getPageIdentity()
			{
				return AccountEditPage.class;
			}
		})
		{
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isVisible()
			{
				AuthorisatieNiveau authNiveau =
					EduArteContext.get().getAccount().getAuthorisatieNiveau();
				return super.isVisible()
					&& authNiveau.implies(getAccount().getAuthorisatieNiveau());
			}
		});
		panel.addButton(new PageLinkButton(panel, "Toon rechten", ButtonAlignment.LEFT,
			new IPageLink()
			{
				private static final long serialVersionUID = 1L;

				@Override
				public Class< ? extends Page> getPageIdentity()
				{
					return ToonRechtenPage.class;
				}

				@Override
				public Page getPage()
				{
					return new ToonRechtenPage(getAccount());
				}
			}));
		panel.addButton(new TerugButton(panel, GebruikersoverzichtPage.class));
	}

	/**
	 * @see nl.topicus.eduarte.web.pages.SecurePage#getBookmarkConstructorArguments(java.util.List,
	 *      java.util.List)
	 */
	@Override
	public void getBookmarkConstructorArguments(List<Class< ? >> ctorArgTypes,
			List<Object> ctorArgValues)
	{
		super.getBookmarkConstructorArguments(ctorArgTypes, ctorArgValues);
		ctorArgTypes.add(Account.class);
		ctorArgValues.add(getDefaultModel());
	}

	private static final List<Class< ? extends IContextInfoObject>> CONTEXT_PARAMETER_TYPES =
		new ArrayList<Class< ? extends IContextInfoObject>>(1);
	static
	{
		CONTEXT_PARAMETER_TYPES.add(Account.class);
	}

	/**
	 * @see nl.topicus.eduarte.web.pages.SecurePage#getContextParameterTypes()
	 */
	@Override
	public List<Class< ? extends IContextInfoObject>> getContextParameterTypes()
	{
		return CONTEXT_PARAMETER_TYPES;
	}

	/**
	 * @see nl.topicus.eduarte.web.pages.SecurePage#getContextValue(java.lang.Class)
	 */
	@Override
	public IContextInfoObject getContextValue(Class< ? extends IContextInfoObject> clazz)
	{
		if (clazz == Account.class)
		{
			return getAccount();
		}
		return null;
	}

}
