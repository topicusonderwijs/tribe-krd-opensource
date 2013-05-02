/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.web.pages.medewerker;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.modelsv2.ModelFactory;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.web.components.panels.bottomrow.BewerkenButton;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.eduarte.app.EduArteContext;
import nl.topicus.eduarte.core.principals.beheer.account.AccountsRead;
import nl.topicus.eduarte.entities.personen.Medewerker;
import nl.topicus.eduarte.entities.security.authentication.Account;
import nl.topicus.eduarte.entities.security.authorization.AuthorisatieNiveau;
import nl.topicus.eduarte.providers.MedewerkerProvider;
import nl.topicus.eduarte.web.components.menu.MedewerkerMenuItem;
import nl.topicus.eduarte.web.components.panels.AccountPanel;
import nl.topicus.eduarte.web.pages.shared.AccountEditPage;

import org.apache.wicket.Page;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.link.IPageLink;

/**
 * @author hoeve
 */
@PageInfo(title = "Account", menu = {"Medewerker > [Medewerker] > Account"})
@InPrincipal(AccountsRead.class)
public class MedewerkerAccountPage extends AbstractMedewerkerPage
{
	private static final long serialVersionUID = 1L;

	public MedewerkerAccountPage(MedewerkerProvider provider)
	{
		this(provider.getMedewerker());
	}

	public MedewerkerAccountPage(Medewerker medewerker)
	{
		super(MedewerkerMenuItem.Account, medewerker);

		add(new WebMarkupContainer("emptypanel").setVisible(medewerker.getAccount() == null));
		add(new AccountPanel("panel", ModelFactory.getModel(medewerker.getAccount()))
			.setVisible(medewerker.getAccount() != null));

		createComponents();
	}

	private Account getAccount()
	{
		return getContextMedewerker().getAccount();
	}

	@Override
	protected void fillBottomRow(BottomRowPanel panel)
	{
		panel.addButton(new BewerkenButton<Void>(panel, new IPageLink()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public Page getPage()
			{
				return new AccountEditPage(MedewerkerAccountPage.this, getAccount());
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
				return super.isVisible() && getAccount() != null
					&& authNiveau.implies(getAccount().getAuthorisatieNiveau());
			}
		});
	}
}
