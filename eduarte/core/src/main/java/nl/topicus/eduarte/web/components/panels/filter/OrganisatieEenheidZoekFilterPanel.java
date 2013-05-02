/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.web.components.panels.filter;

import java.util.Arrays;

import nl.topicus.eduarte.zoekfilters.OrganisatieEenheidZoekFilter;

import org.apache.wicket.markup.html.navigation.paging.IPageable;

/**
 * @author hoeve
 */
public class OrganisatieEenheidZoekFilterPanel extends
		AutoZoekFilterPanel<OrganisatieEenheidZoekFilter>
{
	private static final long serialVersionUID = 1L;

	public OrganisatieEenheidZoekFilterPanel(String id, OrganisatieEenheidZoekFilter filter,
			IPageable pageable)
	{
		super(id, filter, pageable);
		setPropertyNames(Arrays.asList("peildatum", "afkorting", "naam"));
	}
}
