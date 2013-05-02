/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.dao.helpers;

import java.util.List;

import nl.topicus.cobra.dao.helpers.BatchZoekFilterDataAccessHelper;
import nl.topicus.cobra.dao.hibernate.CriteriaBuilder;
import nl.topicus.eduarte.entities.inschrijving.Plaatsing;
import nl.topicus.eduarte.entities.personen.Deelnemer;
import nl.topicus.eduarte.zoekfilters.PlaatsingZoekFilter;

public interface PlaatsingDataAccessHelper extends
		BatchZoekFilterDataAccessHelper<Plaatsing, PlaatsingZoekFilter>
{
	public void addCriteria(CriteriaBuilder builder, PlaatsingZoekFilter filter,
			String deelnemerAlias);

	public Plaatsing get(Long id);

	public List<Plaatsing> getByDeelnemer(Deelnemer deelnemer);

}