/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.web.components.panels.filter;

import java.util.Arrays;

import nl.topicus.cobra.modelsv2.ModelFactory;
import nl.topicus.cobra.web.components.form.modifier.ConstructorArgModifier;
import nl.topicus.cobra.web.components.form.modifier.RequiredModifier;
import nl.topicus.eduarte.entities.examen.ExamenWorkflow;
import nl.topicus.eduarte.zoekfilters.VerbintenisZoekFilter;

import org.apache.wicket.markup.html.navigation.paging.IPageable;

/**
 * @author loite
 */
public class DeelnemerCohortExamenstatusSelectieZoekFilterPanel extends DeelnemerZoekFilterPanel
{
	private static final long serialVersionUID = 1L;

	public DeelnemerCohortExamenstatusSelectieZoekFilterPanel(String id,
			VerbintenisZoekFilter filter, final IPageable pageable,
			boolean cohortEnOpleidingRequired, ExamenWorkflow examenWorkflow)
	{
		super(id, filter, pageable);
		setPropertyNames(Arrays.asList("peildatum", "cohort", "organisatieEenheid", "locatie",
			"taxonomiecode", "opleiding", "examenstatus"));
		addFieldModifier(new ConstructorArgModifier("examenstatus", ModelFactory
			.getModel(examenWorkflow)));
		addFieldModifier(new RequiredModifier(cohortEnOpleidingRequired, "cohort", "opleiding"));
	}

	@Override
	protected String getGroepPropertyName()
	{
		return "basisgroep";
	}
}
