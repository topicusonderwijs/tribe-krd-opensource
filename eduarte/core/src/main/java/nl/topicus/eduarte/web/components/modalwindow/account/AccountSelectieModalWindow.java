package nl.topicus.eduarte.web.components.modalwindow.account;

import nl.topicus.cobra.util.ComponentUtil;
import nl.topicus.cobra.web.components.modal.CobraModalWindowBasePanel;
import nl.topicus.eduarte.entities.security.authentication.Account;
import nl.topicus.eduarte.web.components.modalwindow.AbstractZoekenModalWindow;
import nl.topicus.eduarte.zoekfilters.AccountZoekFilter;

import org.apache.wicket.model.IModel;

public class AccountSelectieModalWindow extends AbstractZoekenModalWindow<Account>
{
	private static final long serialVersionUID = 1L;

	private AccountZoekFilter filter;

	public AccountSelectieModalWindow(String id)
	{
		this(id, null, null);
	}

	public AccountSelectieModalWindow(String id, IModel<Account> model)
	{
		this(id, model, null);
	}

	public AccountSelectieModalWindow(String id, IModel<Account> model, AccountZoekFilter filter)
	{
		super(id, model, filter);
		this.filter = filter;
		setTitle("Account selecteren");
	}

	@Override
	protected CobraModalWindowBasePanel<Account> createContents(String id)
	{
		return new AccountSelectiePanel(id, this, filter);
	}

	@Override
	protected void onDetach()
	{
		super.onDetach();
		ComponentUtil.detachQuietly(filter);
	}
}
