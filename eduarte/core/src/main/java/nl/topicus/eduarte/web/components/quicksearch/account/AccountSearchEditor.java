package nl.topicus.eduarte.web.components.quicksearch.account;

import nl.topicus.cobra.web.components.quicksearch.AbstractSearchEditor;
import nl.topicus.cobra.web.components.wiquery.auto.QuickSearchField;
import nl.topicus.cobra.zoekfilters.ZoekFilterCopyManager;
import nl.topicus.eduarte.entities.security.authentication.Account;
import nl.topicus.eduarte.web.components.modalwindow.AbstractZoekenModalWindow;
import nl.topicus.eduarte.web.components.modalwindow.account.AccountSelectieModalWindow;
import nl.topicus.eduarte.zoekfilters.AccountZoekFilter;

import org.apache.wicket.model.IModel;

public class AccountSearchEditor extends AbstractSearchEditor<Account>
{
	private static final long serialVersionUID = 1L;

	private static final AccountZoekFilter getDefaultFilter()
	{
		AccountZoekFilter filter = new AccountZoekFilter();
		return filter;
	}

	private AccountZoekFilter filter;

	public AccountSearchEditor(String id, IModel<Account> model)
	{
		this(id, model, getDefaultFilter());
	}

	public AccountSearchEditor(String id, IModel<Account> model, AccountZoekFilter filter)
	{
		super(id, model);
		this.filter = filter;
	}

	@Override
	public AbstractZoekenModalWindow<Account> createModelWindow(String id, IModel<Account> model)
	{
		return new AccountSelectieModalWindow(id, model, filter);
	}

	@Override
	public QuickSearchField<Account> createSearchField(String id, IModel<Account> model)
	{
		return new AccountQuickSearchField(id, model, new ZoekFilterCopyManager()
			.copyObject(filter));
	}
}
