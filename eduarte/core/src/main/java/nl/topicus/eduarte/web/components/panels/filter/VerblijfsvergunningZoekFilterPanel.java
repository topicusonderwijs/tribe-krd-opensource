/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.web.components.panels.filter;

import java.util.Arrays;

import nl.topicus.eduarte.entities.landelijk.Verblijfsvergunning;
import nl.topicus.eduarte.zoekfilters.LandelijkCodeNaamZoekFilter;

import org.apache.wicket.markup.html.navigation.paging.IPageable;

/**
 * @author schimmel
 */
public class VerblijfsvergunningZoekFilterPanel extends
		AutoZoekFilterPanel<LandelijkCodeNaamZoekFilter<Verblijfsvergunning>>
{
	private static final long serialVersionUID = 1L;

	public VerblijfsvergunningZoekFilterPanel(String id,
			LandelijkCodeNaamZoekFilter<Verblijfsvergunning> filter, final IPageable pageable)
	{
		super(id, filter, pageable);
		setPropertyNames(Arrays.asList("code", "naam"));
	}
}
