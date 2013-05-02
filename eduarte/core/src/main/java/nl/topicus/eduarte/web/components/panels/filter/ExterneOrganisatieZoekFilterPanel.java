/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.web.components.panels.filter;

import java.util.Arrays;

import nl.topicus.eduarte.zoekfilters.ExterneOrganisatieZoekFilter;

import org.apache.wicket.markup.html.navigation.paging.IPageable;

public class ExterneOrganisatieZoekFilterPanel extends
		AutoZoekFilterPanel<ExterneOrganisatieZoekFilter>
{
	private static final long serialVersionUID = 1L;

	public ExterneOrganisatieZoekFilterPanel(String id, ExterneOrganisatieZoekFilter filter,
			IPageable pageable, boolean bijVooropleiding)
	{
		super(id, filter, pageable);
		if (bijVooropleiding)
		{
			setPropertyNames(Arrays.asList("peildatum", "naam", "verkorteNaam", "plaats",
				"soortExterneOrganisaties", "adres"));
		}
		else
		{
			setPropertyNames(Arrays.asList("peildatum", "naam", "verkorteNaam", "plaats",
				"soortExterneOrganisaties", "kenniscentrum", "relatienummer"));
		}
	}
}
