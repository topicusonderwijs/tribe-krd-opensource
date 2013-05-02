package nl.topicus.eduarte.web.pages.beheer.account;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.web.components.form.AutoFieldSet;
import nl.topicus.cobra.web.components.menu.AbstractMenuBar;
import nl.topicus.eduarte.core.principals.beheer.account.AccountsWrite;
import nl.topicus.eduarte.entities.jobs.AccountsImportJobRun;
import nl.topicus.eduarte.jobs.account.AccountImportDataMap;
import nl.topicus.eduarte.jobs.account.AccountImportJob;
import nl.topicus.eduarte.web.components.menu.BeheerMenu;
import nl.topicus.eduarte.web.components.menu.BeheerMenuItem;
import nl.topicus.eduarte.web.components.menu.main.CoreMainMenuItem;
import nl.topicus.eduarte.web.pages.shared.jobs.AbstractJobBeheerPage;

import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.validation.AbstractFormValidator;
import org.quartz.JobDataMap;

/**
 * @author vandekamp
 */
@PageInfo(title = "Accounts importeren", menu = {"Beheer > Accountbeheer > Gebruikers > Importeren"})
@InPrincipal(AccountsWrite.class)
public class AccountsImporterenPage extends AbstractJobBeheerPage<AccountsImportJobRun>
{
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor.
	 */
	public AccountsImporterenPage()
	{
		super(CoreMainMenuItem.Beheer, AccountImportJob.class, "");
		getJobPanel().getForm().add(new AbstractFormValidator()
		{
			private static final long serialVersionUID = 1L;

			@SuppressWarnings("unchecked")
			@Override
			public void validate(Form< ? > form)
			{
				AutoFieldSet<JobDataMap> fieldset = getJobPanel().getAutoFieldSet();
				Boolean wachtwoord =
					((FormComponent<Boolean>) fieldset.findFieldComponent("wachtwoordGenereren"))
						.getConvertedInput();
				Boolean email =
					((FormComponent<Boolean>) fieldset.findFieldComponent("emailVersturen"))
						.getConvertedInput();
				if (email != null && !email.booleanValue() && wachtwoord != null
					&& wachtwoord.booleanValue())
					form.error("U kunt geen wachtwoord genereren zonder e-mail te versturen");

			}

			@Override
			public FormComponent< ? >[] getDependentFormComponents()
			{
				return new FormComponent[] {};
			}
		});
	}

	@Override
	protected JobDataMap createDataMap()
	{
		return new AccountImportDataMap();
	}

	@Override
	public AbstractMenuBar createMenu(String id)
	{
		return new BeheerMenu(id, BeheerMenuItem.Gebruikers);
	}

}
