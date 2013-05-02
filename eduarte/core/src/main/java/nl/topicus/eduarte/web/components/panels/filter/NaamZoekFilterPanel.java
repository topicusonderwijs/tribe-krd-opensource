/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.web.components.panels.filter;

import java.util.Arrays;

import nl.topicus.cobra.zoekfilters.DetachableZoekFilter;

import org.apache.wicket.markup.html.navigation.paging.IPageable;

/**
 * @author hoeve
 */
public class NaamZoekFilterPanel extends AutoZoekFilterPanel<DetachableZoekFilter< ? >>
{
	private static final long serialVersionUID = 1L;

	public NaamZoekFilterPanel(String id, DetachableZoekFilter< ? > filter, IPageable pageable)
	{
		super(id, filter, pageable);
		setPropertyNames(Arrays.asList("naam"));
	}
}
