/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.zoekfilters;

import nl.topicus.eduarte.entities.examen.ExamenWorkflow;
import nl.topicus.eduarte.entities.examen.ToegestaneExamenstatusOvergang;

import org.apache.wicket.model.IModel;

/**
 * @author vandekamp
 */
public class ToegestaneExamenstatusOvergangZoekFilter extends
		AbstractLandelijkOfInstellingZoekFilter<ToegestaneExamenstatusOvergang>
{
	private static final long serialVersionUID = 1L;

	private IModel<ExamenWorkflow> examenWorkflow;

	private String actie;

	public ToegestaneExamenstatusOvergangZoekFilter()
	{
	}

	public ExamenWorkflow getExamenWorkflow()
	{
		return getModelObject(examenWorkflow);
	}

	public void setExamenWorkflow(ExamenWorkflow examenWorkflow)
	{
		this.examenWorkflow = makeModelFor(examenWorkflow);
	}

	public void setActie(String actie)
	{
		this.actie = actie;
	}

	public String getActie()
	{
		return actie;
	}
}
