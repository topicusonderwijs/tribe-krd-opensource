/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.web.components.panels.filter;

import java.util.Arrays;

import nl.topicus.eduarte.zoekfilters.ContractZoekFilter;

import org.apache.wicket.markup.html.navigation.paging.IPageable;

/**
 * @author hop
 */
public class ContractZoekFilterPanel extends AutoZoekFilterPanel<ContractZoekFilter>
{
	private static final long serialVersionUID = 1L;

	public ContractZoekFilterPanel(String id, ContractZoekFilter filter, IPageable pageable)
	{
		super(id, filter, pageable);

		setPropertyNames(Arrays.asList("code", "naam", "externeOrganisatie", "soortContract",
			"toonInactief"));
	}
}
