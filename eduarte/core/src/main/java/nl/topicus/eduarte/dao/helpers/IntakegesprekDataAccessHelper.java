/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.dao.helpers;

import java.util.List;

import nl.topicus.cobra.dao.helpers.BatchZoekFilterDataAccessHelper;
import nl.topicus.cobra.dao.hibernate.CriteriaBuilder;
import nl.topicus.eduarte.entities.inschrijving.Intakegesprek;
import nl.topicus.eduarte.entities.personen.Deelnemer;
import nl.topicus.eduarte.zoekfilters.IntakegesprekZoekFilter;

public interface IntakegesprekDataAccessHelper extends
		BatchZoekFilterDataAccessHelper<Intakegesprek, IntakegesprekZoekFilter>
{
	public void addCriteria(CriteriaBuilder builder, IntakegesprekZoekFilter filter);

	public Intakegesprek get(Long id);

	public List<Intakegesprek> getByDeelnemer(Deelnemer deelnemer);

}