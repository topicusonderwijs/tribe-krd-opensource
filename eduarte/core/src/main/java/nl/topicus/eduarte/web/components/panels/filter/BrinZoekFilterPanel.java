/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.web.components.panels.filter;

import java.util.Arrays;

import nl.topicus.eduarte.zoekfilters.BrinZoekFilter;

import org.apache.wicket.markup.html.navigation.paging.IPageable;

public class BrinZoekFilterPanel extends AutoZoekFilterPanel<BrinZoekFilter>
{
	private static final long serialVersionUID = 1L;

	public BrinZoekFilterPanel(String id, BrinZoekFilter filter, IPageable pageable)
	{
		super(id, filter, pageable);
		setPropertyNames(Arrays.asList("code", "organisatie", "plaats", "onderwijssector"));
	}
}
