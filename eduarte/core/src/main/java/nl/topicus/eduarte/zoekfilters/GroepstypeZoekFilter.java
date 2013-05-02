/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.zoekfilters;

import nl.topicus.eduarte.entities.groep.Groepstype;

/**
 * @author hoeve
 */
public class GroepstypeZoekFilter extends AbstractCodeNaamActiefZoekFilter<Groepstype>
{
	private static final long serialVersionUID = 1L;

	private Boolean plaatsingsgroep;

	public GroepstypeZoekFilter()
	{
		super(Groepstype.class);
	}

	public Boolean getPlaatsingsgroep()
	{
		return plaatsingsgroep;
	}

	public void setPlaatsingsgroep(Boolean plaatsingsgroep)
	{
		this.plaatsingsgroep = plaatsingsgroep;
	}
}
