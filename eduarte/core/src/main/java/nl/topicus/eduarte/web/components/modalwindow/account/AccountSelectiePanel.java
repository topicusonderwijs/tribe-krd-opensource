package nl.topicus.eduarte.web.components.modalwindow.account;

import nl.topicus.cobra.web.components.datapanel.CustomDataPanel;
import nl.topicus.cobra.web.components.modal.CobraModalWindow;
import nl.topicus.eduarte.dao.helpers.AccountDataAccessHelper;
import nl.topicus.eduarte.entities.security.authentication.Account;
import nl.topicus.eduarte.web.components.modalwindow.AbstractZoekenPanel;
import nl.topicus.eduarte.web.components.panels.datapanel.table.AccountTable;
import nl.topicus.eduarte.web.components.panels.filter.AccountZoekFilterPanel;
import nl.topicus.eduarte.zoekfilters.AccountZoekFilter;

import org.apache.wicket.Component;

public class AccountSelectiePanel extends AbstractZoekenPanel<Account, AccountZoekFilter>
{
	private static final long serialVersionUID = 1L;

	public AccountSelectiePanel(String id, CobraModalWindow<Account> window,
			AccountZoekFilter filter)
	{
		super(id, window, filter == null ? getDefaultFilter() : filter,
			AccountDataAccessHelper.class, new AccountTable());
	}

	private static final AccountZoekFilter getDefaultFilter()
	{
		AccountZoekFilter filter = new AccountZoekFilter();
		filter.addOrderByProperty("persoon.achternaam");
		return filter;
	}

	@Override
	protected Component createFilterPanel(String id, AccountZoekFilter filter,
			CustomDataPanel<Account> datapanel)
	{
		return new AccountZoekFilterPanel(id, filter, datapanel);
	}
}
