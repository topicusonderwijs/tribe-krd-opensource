/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.dao.helpers;

import nl.topicus.cobra.dao.helpers.BatchZoekFilterDataAccessHelper;
import nl.topicus.cobra.dao.hibernate.CriteriaBuilder;
import nl.topicus.eduarte.entities.personen.Relatie;
import nl.topicus.eduarte.zoekfilters.RelatieZoekFilter;

/**
 * @author loite
 */
public interface RelatieDataAccessHelper extends
		BatchZoekFilterDataAccessHelper<Relatie, RelatieZoekFilter>
{

	public void addCriteria(CriteriaBuilder builder, RelatieZoekFilter filter, String relatieAlias,
			String persoonAlias);

	public Relatie get(Long id);

}
