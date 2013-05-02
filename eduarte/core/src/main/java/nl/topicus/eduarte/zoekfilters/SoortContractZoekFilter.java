/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.zoekfilters;

import nl.topicus.cobra.web.components.choice.JaNeeCombobox;
import nl.topicus.cobra.web.components.form.AutoForm;
import nl.topicus.eduarte.entities.contract.SoortContract;

public class SoortContractZoekFilter extends CodeNaamActiefZoekFilter<SoortContract> implements
		ICodeNaamActiefZoekFilter<SoortContract>
{
	private static final long serialVersionUID = 1L;

	@AutoForm(editorClass = JaNeeCombobox.class)
	private Boolean inburgering;

	public SoortContractZoekFilter()
	{
		super(SoortContract.class);
	}

	public void setInburgering(Boolean inburgering)
	{
		this.inburgering = inburgering;
	}

	public Boolean getInburgering()
	{
		return inburgering;
	}
}
