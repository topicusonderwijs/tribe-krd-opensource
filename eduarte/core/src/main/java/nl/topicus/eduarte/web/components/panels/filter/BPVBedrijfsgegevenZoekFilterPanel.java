/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.web.components.panels.filter;

import java.util.Arrays;

import nl.topicus.eduarte.zoekfilters.BPVBedrijfsgegevenZoekFilter;

import org.apache.wicket.markup.html.navigation.paging.IPageable;

public class BPVBedrijfsgegevenZoekFilterPanel extends
		AutoZoekFilterPanel<BPVBedrijfsgegevenZoekFilter>
{
	private static final long serialVersionUID = 1L;

	public BPVBedrijfsgegevenZoekFilterPanel(String id, BPVBedrijfsgegevenZoekFilter filter,
			IPageable pageable)
	{
		super(id, filter, pageable);
		setPropertyNames(Arrays.asList("naam", "plaats", "soortExterneOrganisaties",
			"kenniscentrum", "codeLeerbedrijf", "relatienummer"));
	}
}
