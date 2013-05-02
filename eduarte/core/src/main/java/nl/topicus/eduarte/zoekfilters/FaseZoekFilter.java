/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.zoekfilters;

import nl.topicus.eduarte.entities.hogeronderwijs.Fase;
import nl.topicus.eduarte.entities.hogeronderwijs.Hoofdfase;

public class FaseZoekFilter extends CodeNaamActiefZoekFilter<Fase>
{
	private static final long serialVersionUID = 1L;

	private Hoofdfase hoofdfase;

	private Fase volgendeFase;

	public FaseZoekFilter()
	{
		super(Fase.class);
	}

	public void setHoofdfase(Hoofdfase hoofdfase)
	{
		this.hoofdfase = hoofdfase;
	}

	public Hoofdfase getHoofdfase()
	{
		return hoofdfase;
	}

	public void setVolgendeFase(Fase volgendeFase)
	{
		this.volgendeFase = volgendeFase;
	}

	public Fase getVolgendeFase()
	{
		return volgendeFase;
	}

}
