package nl.topicus.eduarte.web.components.quicksearch.account;

import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.util.ComponentUtil;
import nl.topicus.cobra.web.components.quicksearch.QuickSearchModel;
import nl.topicus.cobra.web.components.wiquery.auto.IAutoCompleteChoiceRenderer;
import nl.topicus.cobra.web.components.wiquery.auto.QuickSearchField;
import nl.topicus.eduarte.dao.helpers.AccountDataAccessHelper;
import nl.topicus.eduarte.entities.security.authentication.Account;
import nl.topicus.eduarte.zoekfilters.AccountZoekFilter;

import org.apache.wicket.model.IModel;

public class AccountQuickSearchField extends QuickSearchField<Account>
{
	private static final long serialVersionUID = 1L;

	private AccountZoekFilter filter;

	public AccountQuickSearchField(String id, IModel<Account> model, AccountZoekFilter filter)
	{
		super(id, model, QuickSearchModel.of(AccountDataAccessHelper.class, filter),
			new IAutoCompleteChoiceRenderer<Account>()
			{
				private static final long serialVersionUID = 1L;

				@Override
				public String getDisplayValue(Account value)
				{
					return value.getGebruikersnaam();
				}

				@Override
				public String getFieldValue(Account value)
				{
					return value.getGebruikersnaam();
				}

				@Override
				public String getIdValue(Account value)
				{
					return value.getGebruikersnaam();
				}

				@Override
				public Account getObject(String username)
				{
					AccountDataAccessHelper helper =
						DataAccessRegistry.getHelper(AccountDataAccessHelper.class);
					return helper.getAccount(username);
				}

				@Override
				public void detach()
				{
				}
			});
		this.filter = filter;
	}

	@Override
	protected void onDetach()
	{
		super.onDetach();
		ComponentUtil.detachQuietly(filter);
	}

	@Override
	public Integer getWidth()
	{
		return 300;
	}
}
