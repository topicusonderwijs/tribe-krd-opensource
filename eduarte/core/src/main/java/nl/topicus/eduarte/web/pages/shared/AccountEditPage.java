package nl.topicus.eduarte.web.pages.shared;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.modelsv2.DefaultModelManager;
import nl.topicus.cobra.modelsv2.IChangeRecordingModel;
import nl.topicus.cobra.modelsv2.ModelFactory;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.util.StringUtil;
import nl.topicus.cobra.web.components.KoppelTabelModelSelection;
import nl.topicus.cobra.web.components.form.AutoFieldSet;
import nl.topicus.cobra.web.components.form.RenderMode;
import nl.topicus.cobra.web.components.form.VersionedForm;
import nl.topicus.cobra.web.components.form.modifier.ConstructorArgModifier;
import nl.topicus.cobra.web.components.form.modifier.RequiredModifier;
import nl.topicus.cobra.web.components.form.modifier.ValidateModifier;
import nl.topicus.cobra.web.components.form.modifier.WachtwoordModifier;
import nl.topicus.cobra.web.components.panels.bottomrow.AnnulerenButton;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.cobra.web.components.panels.bottomrow.OpslaanButton;
import nl.topicus.cobra.web.components.shortcut.ActionKey;
import nl.topicus.cobra.web.components.shortcut.CobraKeyAction;
import nl.topicus.cobra.web.pages.IEditPage;
import nl.topicus.cobra.web.validators.UniqueConstraintValidator;
import nl.topicus.eduarte.app.EduArteApp;
import nl.topicus.eduarte.app.EduArteContext;
import nl.topicus.eduarte.core.principals.beheer.account.AccountsWrite;
import nl.topicus.eduarte.entities.security.authentication.Account;
import nl.topicus.eduarte.entities.security.authentication.AccountRol;
import nl.topicus.eduarte.entities.security.authentication.DeelnemerAccount;
import nl.topicus.eduarte.entities.security.authentication.DigitaalAanmelderAccount;
import nl.topicus.eduarte.entities.security.authentication.ExterneOrganisatieContactPersoonAccount;
import nl.topicus.eduarte.entities.security.authentication.MedewerkerAccount;
import nl.topicus.eduarte.entities.security.authorization.AuthorisatieNiveau;
import nl.topicus.eduarte.entities.security.authorization.Rol;
import nl.topicus.eduarte.web.components.autoform.EduArteAjaxRefreshModifier;
import nl.topicus.eduarte.web.components.panels.bottomrow.ProbeerTeVerwijderenButton;
import nl.topicus.eduarte.web.pages.AbstractDynamicContextPage;
import nl.topicus.eduarte.web.pages.SecurePage;
import nl.topicus.eduarte.web.pages.SubpageContext;
import nl.topicus.eduarte.web.pages.beheer.account.AccountOverviewPage;
import nl.topicus.eduarte.web.pages.beheer.account.GebruikersoverzichtPage;
import nl.topicus.eduarte.web.validators.PasswordValidator;
import nl.topicus.eduarte.zoekfilters.ExterneOrganisatieContactPersoonZoekFilter;
import nl.topicus.eduarte.zoekfilters.MedewerkerZoekFilter;
import nl.topicus.eduarte.zoekfilters.RolZoekFilter;
import nl.topicus.eduarte.zoekfilters.VerbintenisZoekFilter;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Page;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.validation.EqualPasswordInputValidator;
import org.apache.wicket.markup.html.link.IPageLink;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.hibernate.Hibernate;

@PageInfo(title = "Account bewerken", menu = {"Beheer > Gebruikers > [Account] > Bewerken",
	"Beheer > Gebruikers > Toevoegen"})
@InPrincipal(AccountsWrite.class)
public class AccountEditPage extends AbstractDynamicContextPage<Account> implements IEditPage
{
	private class RolSelection extends KoppelTabelModelSelection<AccountRol, Rol>
	{
		private static final long serialVersionUID = 1L;

		public RolSelection(IModel<List<AccountRol>> model)
		{
			super(model);
		}

		@Override
		protected Rol convertRtoS(AccountRol object)
		{
			return object.getRol();
		}

		@Override
		protected AccountRol newR(Rol object)
		{
			AccountRol ret = new AccountRol();
			ret.setAccount(getAccount());
			ret.setRol(object);
			return ret;
		}
	}

	private Form<Account> form;

	private AutoFieldSet<Account> accountFieldSet;

	private SecurePage returnPage;

	public AccountEditPage(SecurePage returnPage, Account account)
	{
		super(new SubpageContext(returnPage));
		this.returnPage = returnPage;
		setDefaultModel(ModelFactory.getCompoundChangeRecordingModel(account,
			new DefaultModelManager(AccountRol.class, DeelnemerAccount.class,
				ExterneOrganisatieContactPersoonAccount.class, MedewerkerAccount.class,
				DigitaalAanmelderAccount.class, Account.class)));

		AuthorisatieNiveau userAuthNiveau =
			EduArteContext.get().getAccount().getAuthorisatieNiveau();

		form = new VersionedForm<Account>("form", getAccountModel());
		add(form);

		accountFieldSet = new AutoFieldSet<Account>("account", getAccountModel(), "Account");
		accountFieldSet.setOutputMarkupId(true);
		List<String> propertyNames =
			new ArrayList<String>(Arrays.asList("organisatie", "medewerker", "deelnemer",
				"externeOrganisatieContactPersoon", "gebruikersnaam", "wachtwoord",
				"wachtwoordCheck", "ipAdressen", "actief"));
		if (!Hibernate.getClass(account).equals(MedewerkerAccount.class))
			propertyNames.remove("medewerker");
		if (!Hibernate.getClass(account).equals(DeelnemerAccount.class))
			propertyNames.remove("deelnemer");
		if (!Hibernate.getClass(account).equals(ExterneOrganisatieContactPersoonAccount.class))
			propertyNames.remove("externeOrganisatieContactPersoon");

		if (!account.isSaved())
		{
			accountFieldSet.addFieldModifier(new WachtwoordModifier("wachtwoord"));
			accountFieldSet.addFieldModifier(new ValidateModifier(new PasswordValidator(),
				"wachtwoord"));
			accountFieldSet.addFieldModifier(new RequiredModifier(!account.isSaved(), "wachtwoord",
				"wachtwoordCheck"));
		}
		else
		{
			propertyNames.remove("wachtwoord");
			propertyNames.remove("wachtwoordCheck");
		}

		accountFieldSet.setPropertyNames(propertyNames);
		accountFieldSet.setRenderMode(RenderMode.EDIT);
		accountFieldSet.setSortAccordingToPropertyNames(true);
		accountFieldSet.addModifier("gebruikersnaam", new AttributeModifier("autocomplete", true,
			new Model<String>("off")));

		accountFieldSet.addFieldModifier(new ConstructorArgModifier("medewerker",
			createMedewerkerZoekFilter(), true));
		accountFieldSet.addFieldModifier(new EduArteAjaxRefreshModifier("medewerker",
			"gebruikersnaam")
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void onUpdate(AjaxRequestTarget target)
			{
				MedewerkerAccount medewerkerAccount = (MedewerkerAccount) getAccount();

				if (medewerkerAccount != null && medewerkerAccount.getMedewerker() != null
					&& StringUtil.isEmpty(getAccount().getGebruikersnaam()))
					getAccount()
						.setGebruikersnaam(medewerkerAccount.getMedewerker().getAfkorting());

			}
		});

		accountFieldSet.addFieldModifier(new ConstructorArgModifier("deelnemer",
			createVerbintenisZoekFilter()));
		accountFieldSet.addFieldModifier(new EduArteAjaxRefreshModifier("deelnemer",
			"gebruikersnaam")
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void onUpdate(AjaxRequestTarget target)
			{
				DeelnemerAccount deelnemerAccount = (DeelnemerAccount) getAccount();

				if (deelnemerAccount != null && deelnemerAccount.getDeelnemer() != null
					&& StringUtil.isEmpty(getAccount().getGebruikersnaam()))
					getAccount().setGebruikersnaam(
						Integer.toString(deelnemerAccount.getDeelnemer().getDeelnemernummer()));

			}
		});
		accountFieldSet.addFieldModifier(new ConstructorArgModifier(
			"externeOrganisatieContactPersoon", createExterContactPersoonZoekFilter()));
		accountFieldSet.addFieldModifier(new EduArteAjaxRefreshModifier(
			"externeOrganisatieContactPersoon", "gebruikersnaam")
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void onUpdate(AjaxRequestTarget target)
			{
				ExterneOrganisatieContactPersoonAccount extOrgAccount =
					(ExterneOrganisatieContactPersoonAccount) getAccount();

				if (extOrgAccount != null
					&& extOrgAccount.getExterneOrganisatieContactPersoon() != null
					&& StringUtil.isEmpty(getAccount().getGebruikersnaam()))
					getAccount().setGebruikersnaam(
						extOrgAccount.getExterneOrganisatieContactPersoon().getNaam());
			}
		});
		accountFieldSet.addModifier("gebruikersnaam", new UniqueConstraintValidator<String>(form,
			"gebruiker", "gebruikersnaam", "organisatie"));

		form.add(accountFieldSet);

		final RolZoekFilter filter = new RolZoekFilter();
		filter.setRechtenSoort(account.getRechtenSoort());
		filter.setAuthorisatieNiveau(userAuthNiveau);

		AccountRolSelectiePanel hulpmiddelen =
			new AccountRolSelectiePanel("rollen", filter, new RolSelection(
				new PropertyModel<List<AccountRol>>(getAccountModel(), "roles")));
		form.add(hulpmiddelen);

		createComponents();
	}

	private Account getAccount()
	{
		return (Account) getDefaultModelObject();
	}

	private static MedewerkerZoekFilter createMedewerkerZoekFilter()
	{
		MedewerkerZoekFilter ret = new MedewerkerZoekFilter();
		ret.setHeeftAccount(false);
		return ret;
	}

	private static VerbintenisZoekFilter createVerbintenisZoekFilter()
	{
		VerbintenisZoekFilter ret = new VerbintenisZoekFilter();
		ret.getDeelnemerZoekFilter().setHeeftAccount(false);
		return ret;
	}

	private static ExterneOrganisatieContactPersoonZoekFilter createExterContactPersoonZoekFilter()
	{
		ExterneOrganisatieContactPersoonZoekFilter ret =
			new ExterneOrganisatieContactPersoonZoekFilter();
		ret.setHeeftAccount(false);
		ret.setContactpersoonBPV(true);
		return ret;
	}

	@Override
	protected void onBeforeRender()
	{
		super.onBeforeRender();
		if (!getAccount().isSaved())
			form.add(new EqualPasswordInputValidator((FormComponent< ? >) accountFieldSet
				.findFieldComponent("wachtwoord"), (FormComponent< ? >) accountFieldSet
				.findFieldComponent("wachtwoordCheck")));
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
				saveAccount();
			}
		});

		panel.addButton(new OpslaanButton(panel, form)
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void onSubmit()
			{
				saveAccount();
				AccountEditPage page = new AccountEditPage(returnPage, new MedewerkerAccount());
				setResponsePage(page);
			}

			@Override
			public String getLabel()
			{
				return "Opslaan en nieuwe toevoegen";
			}

			@Override
			public ActionKey getAction()
			{
				return CobraKeyAction.VOLGENDE;
			}

			@Override
			public boolean isVisible()
			{
				return getAccount() instanceof MedewerkerAccount;
			}
		});
		panel.addButton(new AnnulerenButton(panel, new IPageLink()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public Page getPage()
			{
				if (getAccount().isSaved())
					return new AccountOverviewPage(getAccount());
				else
					return new GebruikersoverzichtPage();
			}

			@Override
			public Class< ? extends Page> getPageIdentity()
			{
				if (getAccount().isSaved())
					return AccountOverviewPage.class;
				else
					return GebruikersoverzichtPage.class;
			}
		}));
		panel.addButton(new ProbeerTeVerwijderenButton(panel, getAccountModel(), "dit account",
			GebruikersoverzichtPage.class));
	}

	private void saveAccount()
	{
		IChangeRecordingModel<Account> model = getAccountModel();
		Account account = getAccount();
		account.setAuthorisatieNiveau(account.berekenAuthorisatieNiveau());
		model.saveObject();
		model.getObject().commit();
		EduArteApp.get().removeLoginAttempts(account.getGebruikersnaam(),
			EduArteContext.get().getOrganisatie());
		setResponsePage(returnPage);
	}

	private IChangeRecordingModel<Account> getAccountModel()
	{
		return (IChangeRecordingModel<Account>) getContextModel();
	}
}
