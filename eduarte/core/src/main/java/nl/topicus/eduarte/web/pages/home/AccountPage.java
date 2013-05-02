/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.web.pages.home;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.modelsv2.ModelFactory;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.web.components.panels.bottomrow.BewerkenButton;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.eduarte.app.EduArteContext;
import nl.topicus.eduarte.core.principals.Always;
import nl.topicus.eduarte.web.components.menu.HomeMenuItem;
import nl.topicus.eduarte.web.components.panels.AccountPanel;

/**
 * Pagina met de details van de ingelogde gebruiker.
 * 
 * @author marrink
 */
@PageInfo(title = "Account details ingelogde gebruiker", menu = "Home > Account")
@InPrincipal(Always.class)
public class AccountPage extends AbstractHomePage<Void>
{
	private static final long serialVersionUID = 1L;

	public AccountPage()
	{
		super(HomeMenuItem.Account);
		add(new AccountPanel("panel", ModelFactory.getModel(EduArteContext.get().getAccount())));
		createComponents();
	}

	@Override
	protected void fillBottomRow(BottomRowPanel panel)
	{
		BewerkenButton<Void> button = new BewerkenButton<Void>(panel, EditAccountPage.class);
		button.setLabel("Wachtwoord wijzigen");
		panel.addButton(button);
	}
}
