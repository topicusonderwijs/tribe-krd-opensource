/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.zoekfilters;

import nl.topicus.eduarte.entities.personen.RedenUitDienst;

/**
 * Filter voor {@link RedenUitDienst} .
 * 
 * @author hoeve
 */
public class RedenUitDienstZoekFilter extends CodeNaamActiefZoekFilter<RedenUitDienst>
{
	private static final long serialVersionUID = 1L;

	public RedenUitDienstZoekFilter()
	{
		super(RedenUitDienst.class);
	}

}
