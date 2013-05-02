/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.web.components.panels.filter;

import java.util.Arrays;

import nl.topicus.eduarte.zoekfilters.SignaalZoekFilter;

import org.apache.wicket.markup.html.navigation.paging.IPageable;

public class SignaalZoekFilterPanel extends AutoZoekFilterPanel<SignaalZoekFilter>
{
	private static final long serialVersionUID = 1L;

	public SignaalZoekFilterPanel(String id, SignaalZoekFilter filter, final IPageable pageable)
	{
		super(id, filter, pageable);
		setPropertyNames(Arrays.asList("status"));
	}
}
