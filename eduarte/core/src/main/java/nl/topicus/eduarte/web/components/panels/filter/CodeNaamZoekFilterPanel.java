/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.web.components.panels.filter;

import java.util.Arrays;

import nl.topicus.eduarte.zoekfilters.ICodeNaamZoekFilter;

import org.apache.wicket.markup.html.navigation.paging.IPageable;

/**
 * @author vandekamp
 */
public class CodeNaamZoekFilterPanel extends AutoZoekFilterPanel<ICodeNaamZoekFilter< ? >>
{
	private static final long serialVersionUID = 1L;

	public CodeNaamZoekFilterPanel(String id, ICodeNaamZoekFilter< ? > filter, IPageable pageable)
	{
		this(id, filter, pageable, false);
	}

	public CodeNaamZoekFilterPanel(String id, ICodeNaamZoekFilter< ? > filter, IPageable pageable,
			boolean showPeildatum)
	{
		super(id, filter, pageable);
		if (showPeildatum)
			setPropertyNames(Arrays.asList("peildatum", "code", "naam"));
		else
			setPropertyNames(Arrays.asList("code", "naam"));
	}
}
