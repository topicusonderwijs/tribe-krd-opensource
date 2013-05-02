/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.zoekfilters;

import nl.topicus.eduarte.entities.inschrijving.Verbintenis;
import nl.topicus.eduarte.entities.vrijevelden.VerbintenisVrijVeld;

public class UitschrijvingZoekFilter extends
		AbstractVrijVeldableZoekFilter<VerbintenisVrijVeld, Verbintenis>
{
	private static final long serialVersionUID = 1L;

	public UitschrijvingZoekFilter()
	{
		super(VerbintenisVrijVeld.class);
	}
}
