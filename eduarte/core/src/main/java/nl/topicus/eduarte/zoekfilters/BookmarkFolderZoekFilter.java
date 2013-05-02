/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.zoekfilters;

import nl.topicus.eduarte.entities.security.authentication.Account;
import nl.topicus.eduarte.entities.sidebar.BookmarkFolder;

import org.apache.wicket.model.IModel;

public class BookmarkFolderZoekFilter extends AbstractZoekFilter<BookmarkFolder>
{
	private static final long serialVersionUID = 1L;

	private IModel<Account> account;

	private Integer volgorde;

	public BookmarkFolderZoekFilter()
	{
	}

	public void setAccount(Account account)
	{
		this.account = makeModelFor(account);
	}

	public Account getAccount()
	{
		return getModelObject(account);
	}

	public void setVolgorde(Integer volgorde)
	{
		this.volgorde = volgorde;
	}

	public Integer getVolgorde()
	{
		return volgorde;
	}
}
