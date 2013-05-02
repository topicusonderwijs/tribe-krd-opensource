/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.krd.web.components.panels.filter;

import java.util.Arrays;

import nl.topicus.eduarte.web.components.panels.filter.AutoZoekFilterPanel;
import nl.topicus.eduarte.zoekfilters.SoortProductregelZoekFilter;

import org.apache.wicket.markup.html.navigation.paging.IPageable;

/**
 * @author vandekamp
 */
public class SoortProductregelZoekFilterPanel extends
		AutoZoekFilterPanel<SoortProductregelZoekFilter>
{
	private static final long serialVersionUID = 1L;

	public SoortProductregelZoekFilterPanel(String id, SoortProductregelZoekFilter filter,
			IPageable pageable)
	{
		super(id, filter, pageable);
		setPropertyNames(Arrays.asList("taxonomie", "naam", "actief"));
	}
}
