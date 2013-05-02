/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.web.components.panels.filter;

import java.util.Arrays;

import nl.topicus.eduarte.zoekfilters.ExterneOrganisatieContactPersoonZoekFilter;

import org.apache.wicket.markup.html.navigation.paging.IPageable;

public class ExterneOrganisatieContactPersoonZoekFilterPanel extends
		AutoZoekFilterPanel<ExterneOrganisatieContactPersoonZoekFilter>
{
	private static final long serialVersionUID = 1L;

	public ExterneOrganisatieContactPersoonZoekFilterPanel(String id,
			ExterneOrganisatieContactPersoonZoekFilter filter, IPageable pageable)
	{
		super(id, filter, pageable);
		setPropertyNames(Arrays.asList("peildatum", "naam"));
	}
}
