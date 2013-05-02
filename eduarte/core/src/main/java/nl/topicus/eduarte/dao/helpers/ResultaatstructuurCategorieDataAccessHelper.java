/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.dao.helpers;

import java.util.List;

import nl.topicus.cobra.dao.helpers.BatchZoekFilterDataAccessHelper;
import nl.topicus.eduarte.entities.resultaatstructuur.ResultaatstructuurCategorie;
import nl.topicus.eduarte.zoekfilters.ResultaatstructuurCategorieZoekFilter;
import nl.topicus.eduarte.zoekfilters.ResultaatstructuurZoekFilter;

public interface ResultaatstructuurCategorieDataAccessHelper
		extends
		BatchZoekFilterDataAccessHelper<ResultaatstructuurCategorie, ResultaatstructuurCategorieZoekFilter>
{
	public ResultaatstructuurCategorie get(String naam);

	public List<ResultaatstructuurCategorie> getCategorien(ResultaatstructuurZoekFilter filter);
}
