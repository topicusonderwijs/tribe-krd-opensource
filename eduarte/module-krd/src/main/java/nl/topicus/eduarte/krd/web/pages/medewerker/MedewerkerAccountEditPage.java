/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.krd.web.pages.medewerker;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.modelsv2.ModelFactory;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.util.ComponentUtil;
import nl.topicus.cobra.util.StringUtil;
import nl.topicus.cobra.web.components.panels.bottomrow.AnnulerenButton;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.cobra.web.components.panels.bottomrow.OpslaanButton;
import nl.topicus.cobra.web.security.RequiredSecurityCheck;
import nl.topicus.eduarte.app.security.checks.NietOverledenSecurityCheck;
import nl.topicus.eduarte.core.principals.beheer.account.AccountsWrite;
import nl.topicus.eduarte.entities.personen.Medewerker;
import nl.topicus.eduarte.entities.security.authentication.Account;
import nl.topicus.eduarte.entities.security.authentication.MedewerkerAccount;
import nl.topicus.eduarte.web.components.menu.MedewerkerMenuItem;
import nl.topicus.eduarte.web.pages.IModuleEditPage;
import nl.topicus.eduarte.web.pages.SecurePage;
import nl.topicus.eduarte.web.pages.medewerker.AbstractMedewerkerPage;
import nl.topicus.eduarte.web.validators.PasswordValidator;

import org.apache.wicket.Page;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.validation.EqualPasswordInputValidator;
import org.apache.wicket.markup.html.link.IPageLink;
import org.apache.wicket.model.Model;

/**
 * Pagina met de details van de ingelogde gebruiker.
 * 
 * @author hoeve
 */
@PageInfo(title = "Account", menu = {"Medewerker > [Medewerker] > Account"})
@InPrincipal(AccountsWrite.class)
@RequiredSecurityCheck(NietOverledenSecurityCheck.class)
public class MedewerkerAccountEditPage extends AbstractMedewerkerPage implements
		IModuleEditPage<MedewerkerAccount>
{
	private static final long serialVersionUID = 1L;

	private final SecurePage returnToPage;

	private Form<Account> form;

	public MedewerkerAccountEditPage(Medewerker medewerker, SecurePage returnPage)
	{
		super(MedewerkerMenuItem.Account, medewerker);
		this.returnToPage = returnPage;
		form = new Form<Account>("form", ModelFactory.getModel(medewerker.getAccount()))
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void onSubmit()
			{
				Account myAccount = getModelObject();
				String oldPassword = get("old.wachtwoord").getDefaultModelObjectAsString();
				if (StringUtil.isNotEmpty(oldPassword))
				{
					if (myAccount.verifyPassword(oldPassword))
					{
						// set wachtwoord
						String wachtwoord = get("wachtwoord").getDefaultModelObjectAsString();
						if (StringUtil.isNotEmpty(wachtwoord))
							myAccount.setWachtwoord(wachtwoord);
						// e-mail sturen niet nodig wachtwoord is ingetyped, niet
						// gegenereerd
						myAccount.saveOrUpdate();
					}
					else
					{
						get("old.wachtwoord").error("Oude wachtwoord is onjuist");
						return;
					}
				}
				else
				{
					get("old.wachtwoord").error("Oude wachtwoord is niet ingevuld");
					return;
				}
				myAccount.commit();

				setResponsePage(returnToPage);
			}
		};
		add(form);

		PasswordTextField oldPassword =
			new PasswordTextField("old.wachtwoord", new Model<String>());
		oldPassword.setLabel(new Model<String>("Oude wachtwoord")).setRequired(false);
		form.add(oldPassword);
		PasswordTextField password = new PasswordTextField("wachtwoord", new Model<String>());
		password.add(new PasswordValidator());
		password.setLabel(new Model<String>("Nieuwe wachtwoord")).setRequired(false);
		form.add(ComponentUtil.fixLength(password, Account.class));
		PasswordTextField password2 = new PasswordTextField("wachtwoord2", new Model<String>());
		password2.setLabel(new Model<String>("Herhaal wachtwoord")).setRequired(false);
		form.add(ComponentUtil.fixLength(password2, Account.class));
		form.add(new EqualPasswordInputValidator(password, password2));
		createComponents();
	}

	@Override
	protected void fillBottomRow(BottomRowPanel panel)
	{
		panel.addButton(new OpslaanButton(panel, form));
		panel.addButton(new AnnulerenButton(panel, new IPageLink()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public Page getPage()
			{
				return returnToPage;
			}

			@Override
			public Class< ? extends SecurePage> getPageIdentity()
			{
				return returnToPage.getClass();
			}
		}));
	}
}
