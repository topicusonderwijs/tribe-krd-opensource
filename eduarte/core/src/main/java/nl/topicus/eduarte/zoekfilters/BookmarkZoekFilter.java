/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.zoekfilters;

import nl.topicus.eduarte.entities.security.authentication.Account;
import nl.topicus.eduarte.entities.sidebar.Bookmark;
import nl.topicus.eduarte.entities.sidebar.Bookmark.SoortBookmark;

import org.apache.wicket.model.IModel;

public class BookmarkZoekFilter extends AbstractZoekFilter<Bookmark>
{
	private static final long serialVersionUID = 1L;

	private IModel<Account> account;

	private SoortBookmark soort;

	public BookmarkZoekFilter()
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

	public void setSoort(SoortBookmark soort)
	{
		this.soort = soort;
	}

	public SoortBookmark getSoort()
	{
		return soort;
	}
}
