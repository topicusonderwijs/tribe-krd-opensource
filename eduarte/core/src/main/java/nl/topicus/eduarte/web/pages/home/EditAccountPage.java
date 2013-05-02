/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.web.pages.home;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.modelsv2.ModelFactory;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.util.ComponentUtil;
import nl.topicus.cobra.util.StringUtil;
import nl.topicus.cobra.web.components.panels.bottomrow.AnnulerenButton;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.cobra.web.components.panels.bottomrow.OpslaanButton;
import nl.topicus.cobra.web.pages.IEditPage;
import nl.topicus.eduarte.app.EduArteContext;
import nl.topicus.eduarte.core.principals.Always;
import nl.topicus.eduarte.entities.security.authentication.Account;
import nl.topicus.eduarte.web.components.menu.HomeMenuItem;
import nl.topicus.eduarte.web.validators.PasswordValidator;

import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.validation.EqualPasswordInputValidator;
import org.apache.wicket.model.Model;

/**
 * Pagina met de details van de ingelogde gebruiker.
 * 
 * @author marrink
 */
@PageInfo(title = "Account details ingelogde gebruiker", menu = "Home > Account > Wijzigen")
@InPrincipal(Always.class)
public class EditAccountPage extends AbstractHomePage<Void> implements IEditPage
{

	private static final long serialVersionUID = 1L;

	private final Form<Account> form;

	public EditAccountPage()
	{
		super(HomeMenuItem.Account);
		form = new Form<Account>("form", ModelFactory.getModel(EduArteContext.get().getAccount()))
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
				setResponsePage(AccountPage.class);
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
		panel.addButton(new AnnulerenButton(panel, AccountPage.class));
	}
}
