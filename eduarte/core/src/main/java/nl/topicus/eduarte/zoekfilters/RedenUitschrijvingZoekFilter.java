/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.zoekfilters;

import nl.topicus.cobra.web.components.form.AutoForm;
import nl.topicus.eduarte.entities.inschrijving.RedenUitschrijving;
import nl.topicus.eduarte.entities.inschrijving.RedenUitschrijving.UitstroomredenWI;
import nl.topicus.eduarte.web.components.text.EnumSelectField;
import nl.topicus.onderwijs.duo.bron.bve.waardelijsten.RedenUitval;

/**
 * Filter voor {@link RedenUitschrijving} .
 * 
 * @author idserda
 */
public class RedenUitschrijvingZoekFilter extends
		AbstractCodeNaamActiefZoekFilter<RedenUitschrijving> implements
		ICodeNaamActiefZoekFilter<RedenUitschrijving>
{
	public enum SoortRedenUitschrijvingTonen
	{
		Verbintenis,
		BPV,
		Alle;
	}

	private static final long serialVersionUID = 1L;

	private SoortRedenUitschrijvingTonen soort;

	@AutoForm(editorClass = EnumSelectField.class, label = "")
	private RedenUitval redenUitval;

	@AutoForm(editorClass = EnumSelectField.class, label = "")
	private UitstroomredenWI uitstroomredenWI;

	public RedenUitschrijvingZoekFilter()
	{
		super(RedenUitschrijving.class);
	}

	public void setSoort(SoortRedenUitschrijvingTonen soort)
	{
		this.soort = soort;
	}

	public SoortRedenUitschrijvingTonen getSoort()
	{
		return soort;
	}

	public void setRedenUitval(RedenUitval redenUitval)
	{
		this.redenUitval = redenUitval;
	}

	public RedenUitval getRedenUitval()
	{
		return redenUitval;
	}

	public void setUitstroomredenWI(UitstroomredenWI uitstroomredenWI)
	{
		this.uitstroomredenWI = uitstroomredenWI;
	}

	public UitstroomredenWI getUitstroomredenWI()
	{
		return uitstroomredenWI;
	}

}
