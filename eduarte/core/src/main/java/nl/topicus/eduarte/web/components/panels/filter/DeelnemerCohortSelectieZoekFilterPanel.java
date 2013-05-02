/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.web.components.panels.filter;

import java.util.Arrays;

import nl.topicus.cobra.web.components.form.modifier.RequiredModifier;
import nl.topicus.eduarte.zoekfilters.VerbintenisZoekFilter;

import org.apache.wicket.markup.html.navigation.paging.IPageable;

/**
 * @author loite
 */
public class DeelnemerCohortSelectieZoekFilterPanel extends DeelnemerZoekFilterPanel
{
	private static final long serialVersionUID = 1L;

	public DeelnemerCohortSelectieZoekFilterPanel(String id, VerbintenisZoekFilter filter,
			final IPageable pageable, boolean cohortEnOpleidingRequired)
	{
		this(id, filter, pageable, cohortEnOpleidingRequired, false);
	}

	public DeelnemerCohortSelectieZoekFilterPanel(String id, VerbintenisZoekFilter filter,
			final IPageable pageable, boolean cohortEnOpleidingRequired,
			boolean taxonomiecodeIpvGroep)
	{
		super(id, filter, pageable);
		if (taxonomiecodeIpvGroep)
		{
			setPropertyNames(Arrays.asList("peildatum", "cohort", "organisatieEenheid", "locatie",
				"taxonomiecode", "opleiding"));
		}
		else
		{
			setPropertyNames(Arrays.asList("peildatum", "cohort", "organisatieEenheid", "locatie",
				"opleiding", "groep"));
		}
		addFieldModifier(new RequiredModifier(cohortEnOpleidingRequired, "cohort", "opleiding"));
	}

	@Override
	protected String getGroepPropertyName()
	{
		return "basisgroep";
	}
}
