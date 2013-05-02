/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.web.components.panels.filter;

import java.util.Arrays;

import nl.topicus.eduarte.zoekfilters.ICodeNaamActiefZoekFilter;

import org.apache.wicket.markup.html.navigation.paging.IPageable;

/**
 * @author hoeve
 */
public class CodeNaamActiefZoekFilterPanel extends
		AutoZoekFilterPanel<ICodeNaamActiefZoekFilter< ? >>
{
	private static final long serialVersionUID = 1L;

	public CodeNaamActiefZoekFilterPanel(String id, ICodeNaamActiefZoekFilter< ? > filter,
			IPageable pageable)
	{
		super(id, filter, pageable);
		setPropertyNames(Arrays.asList("code", "naam", "actief"));
	}
}
