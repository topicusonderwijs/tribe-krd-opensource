/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.zoekfilters;

import nl.topicus.cobra.web.components.form.AutoForm;
import nl.topicus.eduarte.entities.inschrijving.SoortVooropleiding;
import nl.topicus.eduarte.entities.inschrijving.SoortVooropleiding.SoortOnderwijs;

/**
 * Filter voor {@link SoortVooropleiding} .
 * 
 * @author hop
 */
public class SoortVooropleidingZoekFilter extends CodeNaamActiefZoekFilter<SoortVooropleiding>
{
	private static final long serialVersionUID = 1L;

	@AutoForm(label = "soort onderwijs", htmlClasses = "unit_max")
	private SoortOnderwijs soortOnderwijs;

	public SoortVooropleidingZoekFilter()
	{
		super(SoortVooropleiding.class);
		setActief(true);
	}

	public void setSoortOnderwijs(SoortOnderwijs soortOnderwijs)
	{
		this.soortOnderwijs = soortOnderwijs;
	}

	public SoortOnderwijs getSoortOnderwijs()
	{
		return soortOnderwijs;
	}
}
