package nl.topicus.eduarte.web.pages.deelnemerportaal.home;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.modelsv2.ModelFactory;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.util.ComponentUtil;
import nl.topicus.cobra.util.StringUtil;
import nl.topicus.cobra.web.components.panels.bottomrow.AnnulerenButton;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.cobra.web.components.panels.bottomrow.OpslaanButton;
import nl.topicus.cobra.web.pages.IEditPage;
import nl.topicus.eduarte.core.principals.deelnemerportaal.DeelnemerportaalAccountWrite;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis;
import nl.topicus.eduarte.entities.security.authentication.Account;
import nl.topicus.eduarte.entities.security.authentication.DeelnemerAccount;
import nl.topicus.eduarte.web.components.menu.deelnemerPortaal.DeelnemerportaalHomeMenuItem;
import nl.topicus.eduarte.web.components.text.UserNameTextField;
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
 * @author marrink
 */
@PageInfo(title = "Account details ingelogde gebruiker", menu = "Home > Account > Wijzigen")
@InPrincipal(DeelnemerportaalAccountWrite.class)
public class EditDeelnemerportaalAccountPage extends AbstractDeelnemerportaalHomePage implements
		IEditPage
{
	private static final long serialVersionUID = 1L;

	private final Form<Account> form;

	public EditDeelnemerportaalAccountPage(Verbintenis verbintenis)
	{
		super(DeelnemerportaalHomeMenuItem.Account, verbintenis);
		form = new Form<Account>("form", ModelFactory.getCompoundModel(getIngelogdeAccount()));
		add(form);

		form.add(ComponentUtil.fixLength(new UserNameTextField("gebruikersnaam", form.getModel()),
			Account.class).setEnabled(false));
		PasswordTextField oldPassword =
			new PasswordTextField("old.wachtwoord", new Model<String>());
		oldPassword.setLabel(new Model<String>("Oude wachtwoord")).setRequired(true);
		form.add(oldPassword);
		PasswordTextField password = new PasswordTextField("wachtwoord", new Model<String>());
		password.setRequired(true);
		password.setLabel(new Model<String>("Wachtwoord"));
		password.add(new PasswordValidator());
		form.add(ComponentUtil.fixLength(password, Account.class));
		PasswordTextField password2 = new PasswordTextField("wachtwoord2", new Model<String>());
		password2.setRequired(true);
		password2.setLabel(new Model<String>("Herhaal wachtwoord"));
		form.add(ComponentUtil.fixLength(password2, Account.class));
		form.add(new EqualPasswordInputValidator(password, password2));
		createComponents();
	}

	@Override
	protected void fillBottomRow(BottomRowPanel panel)
	{
		panel.addButton(new OpslaanButton(panel, form)
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void onSubmit()
			{
				DeelnemerAccount deelnemerAccount = (DeelnemerAccount) form.getModelObject();
				String oldPassword = form.get("old.wachtwoord").getDefaultModelObjectAsString();
				String wachtwoord = form.get("wachtwoord").getDefaultModelObjectAsString();
				if (StringUtil.isNotEmpty(oldPassword))
				{
					if (deelnemerAccount.verifyPassword(oldPassword))
					{
						// set wachtwoord
						if (StringUtil.isNotEmpty(wachtwoord))
							deelnemerAccount.setWachtwoord(wachtwoord);
						// email sturen niet nodig wachtwoord is ingetyped, niet
						// gegenereerd
						deelnemerAccount.saveOrUpdate();
					}
					else
					{
						form.get("old.wachtwoord").error("Oude wachtwoord is onjuist");
						return;
					}
				}
				else
				{
					form.get("old.wachtwoord").error("Oude wachtwoord is niet ingevuld");
					return;
				}
				deelnemerAccount
					.setAuthorisatieNiveau(deelnemerAccount.berekenAuthorisatieNiveau());
				deelnemerAccount.saveOrUpdate();
				deelnemerAccount.commit();
				setResponsePage(getReturnPage());
			}
		});
		panel.addButton(new AnnulerenButton(panel, new IPageLink()
		{
			private static final long serialVersionUID = 1L;

			public Page getPage()
			{
				return new DeelnemerportaalAccountPage(getContextVerbintenis());
			}

			public Class< ? extends Page> getPageIdentity()
			{
				return DeelnemerportaalAccountPage.class;
			}
		}));
	}

	private Page getReturnPage()
	{
		return new DeelnemerportaalAccountPage(getContextVerbintenis());
	}
}
