/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.web.components.panels.filter;

import java.util.Arrays;

import nl.topicus.eduarte.zoekfilters.RelatieZoekFilter;

import org.apache.wicket.markup.html.navigation.paging.IPageable;

public class RelatieZoekFilterPanel extends AutoZoekFilterPanel<RelatieZoekFilter>
{
	private static final long serialVersionUID = 1L;

	public RelatieZoekFilterPanel(String id, RelatieZoekFilter filter, IPageable pageable)
	{
		super(id, filter, pageable);
		setPropertyNames(Arrays.asList("achternaam", "plaats", "postcode", "huisnummer",
			"zoekOngekoppeldePersonen"));
	}
}
