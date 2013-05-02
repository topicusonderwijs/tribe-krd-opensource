package nl.topicus.eduarte.web.pages.shared;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.modelsv2.DefaultModelManager;
import nl.topicus.cobra.modelsv2.IChangeRecordingModel;
import nl.topicus.cobra.modelsv2.ModelFactory;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.web.components.form.AutoFieldSet;
import nl.topicus.cobra.web.components.form.RenderMode;
import nl.topicus.cobra.web.components.form.VersionedForm;
import nl.topicus.cobra.web.components.form.modifier.RequiredModifier;
import nl.topicus.cobra.web.components.form.modifier.ValidateModifier;
import nl.topicus.cobra.web.components.form.modifier.WachtwoordModifier;
import nl.topicus.cobra.web.components.panels.bottomrow.AnnulerenButton;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.cobra.web.components.panels.bottomrow.OpslaanButton;
import nl.topicus.cobra.web.pages.IEditPage;
import nl.topicus.eduarte.app.EduArteApp;
import nl.topicus.eduarte.app.EduArteContext;
import nl.topicus.eduarte.core.principals.beheer.account.PasswordWrite;
import nl.topicus.eduarte.entities.security.authentication.Account;
import nl.topicus.eduarte.entities.security.authentication.DeelnemerAccount;
import nl.topicus.eduarte.entities.security.authentication.DigitaalAanmelderAccount;
import nl.topicus.eduarte.entities.security.authentication.ExterneOrganisatieContactPersoonAccount;
import nl.topicus.eduarte.entities.security.authentication.MedewerkerAccount;
import nl.topicus.eduarte.web.pages.AbstractDynamicContextPage;
import nl.topicus.eduarte.web.pages.SecurePage;
import nl.topicus.eduarte.web.pages.SubpageContext;
import nl.topicus.eduarte.web.validators.PasswordValidator;

import org.apache.wicket.Page;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.validation.EqualPasswordInputValidator;
import org.apache.wicket.markup.html.link.IPageLink;

@PageInfo(title = "Wachtwoord wijzigen", menu = {"Beheer > Gebruikers > [Account] > Wachtwoord wijzigen"})
@InPrincipal(PasswordWrite.class)
public class AccountPasswordEditPage extends AbstractDynamicContextPage<Account> implements
		IEditPage
{
	private Form<Account> form;

	private AutoFieldSet<Account> accountFieldSet;

	private SecurePage returnPage;

	public AccountPasswordEditPage(SecurePage returnPage, Account account)
	{
		super(new SubpageContext(returnPage));
		this.returnPage = returnPage;

		setDefaultModel(ModelFactory.getCompoundChangeRecordingModel(account,
			new DefaultModelManager(DeelnemerAccount.class,
				ExterneOrganisatieContactPersoonAccount.class, MedewerkerAccount.class,
				DigitaalAanmelderAccount.class, Account.class)));

		form = new VersionedForm<Account>("form", getAccountModel());
		add(form);

		accountFieldSet =
			new AutoFieldSet<Account>("account", getAccountModel(), "Wachtwoord wijzigen");
		accountFieldSet.setOutputMarkupId(true);
		List<String> propertyNames =
			new ArrayList<String>(Arrays.asList("wachtwoord", "wachtwoordCheck"));

		accountFieldSet.setPropertyNames(propertyNames);
		accountFieldSet.setRenderMode(RenderMode.EDIT);
		accountFieldSet.setSortAccordingToPropertyNames(true);
		accountFieldSet.addFieldModifier(new WachtwoordModifier("wachtwoord"));
		accountFieldSet
			.addFieldModifier(new ValidateModifier(new PasswordValidator(), "wachtwoord"));
		accountFieldSet.addFieldModifier(new RequiredModifier(!account.isSaved(), "wachtwoord",
			"wachtwoordCheck"));

		form.add(accountFieldSet);

		createComponents();
	}

	private Account getAccount()
	{
		return (Account) getDefaultModelObject();
	}

	@Override
	protected void onBeforeRender()
	{
		super.onBeforeRender();
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
				setResponsePage(returnPage);
			}
		});

		panel.addButton(new AnnulerenButton(panel, new IPageLink()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public Page getPage()
			{
				return returnPage;
			}

			@Override
			public Class< ? extends Page> getPageIdentity()
			{
				return returnPage.getPageClass();
			}
		}));
	}

	private void saveAccount()
	{
		IChangeRecordingModel<Account> model = getAccountModel();
		Account account = getAccount();
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
