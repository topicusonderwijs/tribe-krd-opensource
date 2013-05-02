/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.dao.helpers;

import java.util.List;

import nl.topicus.cobra.dao.helpers.BatchZoekFilterDataAccessHelper;
import nl.topicus.eduarte.entities.contract.TypeFinanciering;
import nl.topicus.eduarte.zoekfilters.TypeFinancieringZoekFilter;

/**
 * @author loite
 */
public interface TypeFinancieringDataAccessHelper extends
		BatchZoekFilterDataAccessHelper<TypeFinanciering, TypeFinancieringZoekFilter>
{
	public TypeFinanciering get(Long id);

	public List<TypeFinanciering> list();

	public TypeFinanciering getTypeFinanciering(String code);
}
