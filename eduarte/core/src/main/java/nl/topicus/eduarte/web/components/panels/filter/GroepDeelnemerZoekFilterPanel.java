/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.web.components.panels.filter;

import java.util.Arrays;

import nl.topicus.eduarte.zoekfilters.VerbintenisZoekFilter;

import org.apache.wicket.markup.html.navigation.paging.IPageable;

/**
 * Zoekfilter panel voor de GroepDeelnemerSelectiePage
 * 
 * @author hoeve
 */
public class GroepDeelnemerZoekFilterPanel extends DeelnemerZoekFilterPanel
{
	private static final long serialVersionUID = 1L;

	public GroepDeelnemerZoekFilterPanel(String id, VerbintenisZoekFilter filter, IPageable pageable)
	{
		super(id, filter, pageable);
		setPropertyNames(Arrays.asList("peildatum", "achternaam", "deelnemernummer",
			"organisatieEenheid", "locatie", "opleiding"));
	}

}
