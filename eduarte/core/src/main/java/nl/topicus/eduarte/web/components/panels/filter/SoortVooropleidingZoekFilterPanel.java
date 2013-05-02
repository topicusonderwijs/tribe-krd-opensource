/*
 * Copyright (c) 2009, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.web.components.panels.filter;

import java.util.Arrays;

import nl.topicus.cobra.zoekfilters.DetachableZoekFilter;

import org.apache.wicket.markup.html.navigation.paging.IPageable;

/**
 * @author hop
 */
public class SoortVooropleidingZoekFilterPanel extends
		AutoZoekFilterPanel<DetachableZoekFilter< ? >>
{
	private static final long serialVersionUID = 1L;

	public SoortVooropleidingZoekFilterPanel(String id, DetachableZoekFilter< ? > filter,
			IPageable pageable, boolean showActief)
	{
		super(id, filter, pageable);
		if (showActief)
		{
			setPropertyNames(Arrays.asList("code", "naam", "actief", "soortOnderwijs"));
		}
		else
		{
			setPropertyNames(Arrays.asList("code", "naam", "soortOnderwijs"));
		}

	}
}
