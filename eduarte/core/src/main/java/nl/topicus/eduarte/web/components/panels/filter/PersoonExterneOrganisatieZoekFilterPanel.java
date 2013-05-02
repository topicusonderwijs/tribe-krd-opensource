/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.web.components.panels.filter;

import java.util.Arrays;

import nl.topicus.eduarte.zoekfilters.PersoonExterneOrganisatieZoekFilter;

import org.apache.wicket.markup.html.navigation.paging.IPageable;

public class PersoonExterneOrganisatieZoekFilterPanel extends
		AutoZoekFilterPanel<PersoonExterneOrganisatieZoekFilter>
{
	private static final long serialVersionUID = 1L;

	public PersoonExterneOrganisatieZoekFilterPanel(String id,
			PersoonExterneOrganisatieZoekFilter filter, IPageable pageable)
	{
		super(id, filter, pageable);
		setPropertyNames(Arrays.asList("naam", "soortExterneOrganisaties", "plaats", "postcode"));
	}
}
