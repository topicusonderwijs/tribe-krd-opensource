/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.zoekfilters;

import nl.topicus.eduarte.entities.contract.TypeFinanciering;

public class TypeFinancieringZoekFilter extends CodeNaamActiefZoekFilter<TypeFinanciering>
		implements ICodeNaamActiefZoekFilter<TypeFinanciering>
{
	private static final long serialVersionUID = 1L;

	public TypeFinancieringZoekFilter()
	{
		super(TypeFinanciering.class);
	}
}
