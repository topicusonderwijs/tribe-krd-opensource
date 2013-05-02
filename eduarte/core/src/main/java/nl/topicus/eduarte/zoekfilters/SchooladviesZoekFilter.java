/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.zoekfilters;

import nl.topicus.eduarte.entities.inschrijving.Schooladvies;

/**
 * Filter voor {@link Schooladvies} .
 * 
 * @author hop
 */
public class SchooladviesZoekFilter extends NaamActiefZoekFilter<Schooladvies>
{
	private static final long serialVersionUID = 1L;

	public SchooladviesZoekFilter()
	{
		super(Schooladvies.class);
	}
}
