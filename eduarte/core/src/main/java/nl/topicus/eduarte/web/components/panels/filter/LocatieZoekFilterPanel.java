/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.web.components.panels.filter;

import java.util.Arrays;

import nl.topicus.eduarte.zoekfilters.LocatieZoekFilter;

import org.apache.wicket.markup.html.navigation.paging.IPageable;

/**
 * @author hoeve
 */
public class LocatieZoekFilterPanel extends AutoZoekFilterPanel<LocatieZoekFilter>
{
	private static final long serialVersionUID = 1L;

	public LocatieZoekFilterPanel(String id, LocatieZoekFilter filter, IPageable pageable)
	{
		super(id, filter, pageable);
		setPropertyNames(Arrays.asList("peildatum", "afkortingZoeken", "naamZoeken"));
	}
}
