/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.zoekfilters;

import nl.topicus.eduarte.entities.inschrijving.UitkomstIntakegesprek;

public class UitkomstIntakegesprekZoekFilter extends
		CodeNaamActiefZoekFilter<UitkomstIntakegesprek> implements
		ICodeNaamActiefZoekFilter<UitkomstIntakegesprek>
{
	private static final long serialVersionUID = 1L;

	public UitkomstIntakegesprekZoekFilter()
	{
		super(UitkomstIntakegesprek.class);
	}

}
