/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.zoekfilters;

import nl.topicus.eduarte.entities.personen.Functie;

public class FunctieZoekFilter extends CodeNaamActiefZoekFilter<Functie>
{
	private static final long serialVersionUID = 1L;

	public FunctieZoekFilter()
	{
		super(Functie.class);
	}

}
