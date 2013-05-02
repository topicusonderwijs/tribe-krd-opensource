/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.resultaten.zoekfilters;

import nl.topicus.eduarte.entities.onderwijsproduct.Onderwijsproduct;
import nl.topicus.eduarte.resultaten.entities.ResultatenHerberekenenJobRun;
import nl.topicus.eduarte.zoekfilters.JobRunZoekFilter;

import org.apache.wicket.model.IModel;

/**
 * @author maatman
 */
public class ResultaatHerberekenenJobRunZoekFilter extends
		JobRunZoekFilter<ResultatenHerberekenenJobRun>
{
	private static final long serialVersionUID = 1L;

	private IModel<Onderwijsproduct> onderwijsproduct;

	public ResultaatHerberekenenJobRunZoekFilter()
	{
		super(ResultatenHerberekenenJobRun.class);
	}

	public void setOnderwijsproduct(Onderwijsproduct onderwijsproduct)
	{
		this.onderwijsproduct = makeModelFor(onderwijsproduct);
	}

	public Onderwijsproduct getOnderwijsproduct()
	{
		return getModelObject(onderwijsproduct);
	}
}
