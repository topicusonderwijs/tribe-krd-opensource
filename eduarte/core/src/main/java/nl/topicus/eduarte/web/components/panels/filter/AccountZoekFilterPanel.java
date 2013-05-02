/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.web.components.panels.filter;

import java.util.Arrays;

import nl.topicus.eduarte.zoekfilters.AccountZoekFilter;

import org.apache.wicket.markup.html.navigation.paging.IPageable;

public class AccountZoekFilterPanel extends AutoZoekFilterPanel<AccountZoekFilter>
{
	private static final long serialVersionUID = 1L;

	public AccountZoekFilterPanel(String id, AccountZoekFilter filter, IPageable pageable)
	{
		super(id, filter, pageable);
		setPropertyNames(Arrays.asList("gebruikersnaam", "rol", "actief", "type"));
	}
}
