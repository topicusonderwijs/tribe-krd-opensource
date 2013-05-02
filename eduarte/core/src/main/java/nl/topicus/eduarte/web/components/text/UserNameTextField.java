package nl.topicus.eduarte.web.components.text;

import java.util.HashMap;
import java.util.Map;

import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.web.pages.FeedbackComponent;
import nl.topicus.eduarte.dao.helpers.AccountDataAccessHelper;
import nl.topicus.eduarte.entities.security.authentication.Account;

import org.apache.wicket.Page;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.IModel;
import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.validator.AbstractValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Een verplicht ajax veld dat controleerd of de ingevulde gebruikersnaam niet al bestaat
 * en komt met suggesties als dit wel het geval is.
 * 
 * @author marrink
 */
public class UserNameTextField extends TextField<String>
{
	private static final long serialVersionUID = 1L;

	private static final Logger log = LoggerFactory.getLogger(UserNameTextField.class);

	private IModel<Account> accountModel;

	public UserNameTextField(String id, IModel<Account> account)
	{
		super(id, String.class);
		init(account);
	}

	public UserNameTextField(String id, IModel<Account> account, IModel<String> input)
	{
		super(id, input, String.class);
		init(account);
	}

	private void init(IModel<Account> account)
	{
		accountModel = account;
		setRequired(true);
		setOutputMarkupId(true);
		add(new AbstractValidator<String>()
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void onValidate(IValidatable<String> validatable)
			{
				String username = validatable.getValue();
				AccountDataAccessHelper helper =
					DataAccessRegistry.getHelper(AccountDataAccessHelper.class);
				Account myAccount = accountModel.getObject();
				if (helper.usernameExists(username, myAccount))
				{
					error(validatable, "duplicate.username");
				}
			}

			@Override
			protected Map<String, Object> variablesMap(IValidatable<String> validatable)
			{
				Map<String, Object> map = new HashMap<String, Object>(2);
				AccountDataAccessHelper helper =
					DataAccessRegistry.getHelper(AccountDataAccessHelper.class);
				Account myAccount = accountModel.getObject();
				String username = validatable.getValue();
				map
					.put("alternative", helper
						.suggestUsername(username, myAccount.getOrganisatie()));
				map.put("username", username);
				return map;
			}

		});
		add(new AjaxFormComponentUpdatingBehavior("onchange")
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void onUpdate(AjaxRequestTarget target)
			{
				// noop, validation handles most
			}

			@Override
			protected void onError(AjaxRequestTarget target, RuntimeException e)
			{
				if (e != null)
					log.error(e.getMessage(), e);
				else
				{
					// validaton error
					Page feedbackPage = getPage();
					if (feedbackPage instanceof FeedbackComponent)
						((FeedbackComponent) feedbackPage).refreshFeedback(target);
				}
			}
		});
	}

	@Override
	protected void onDetach()
	{
		super.onDetach();
		accountModel.detach();
	}
}
