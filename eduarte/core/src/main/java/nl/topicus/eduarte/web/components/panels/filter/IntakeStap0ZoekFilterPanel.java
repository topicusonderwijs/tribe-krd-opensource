/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.web.components.panels.filter;

import java.util.Arrays;

import nl.topicus.eduarte.zoekfilters.VerbintenisZoekFilter;

import org.apache.wicket.markup.html.navigation.paging.IPageable;

/**
 * @author loite
 */
public class IntakeStap0ZoekFilterPanel extends AutoZoekFilterPanel<VerbintenisZoekFilter>
{
	private static final long serialVersionUID = 1L;

	public IntakeStap0ZoekFilterPanel(String id, VerbintenisZoekFilter filter, IPageable pageable)
	{
		super(id, filter, pageable);
		setPropertyNames(Arrays.asList("achternaam", "geboortedatum", "postcode", "bsn",
			"statusIntake", "deelnemernummer"));
	}
}
