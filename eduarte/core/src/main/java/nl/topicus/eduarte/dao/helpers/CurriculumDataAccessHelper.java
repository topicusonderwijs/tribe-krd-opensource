/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.dao.helpers;

import nl.topicus.cobra.dao.helpers.BatchZoekFilterDataAccessHelper;
import nl.topicus.eduarte.entities.curriculum.Curriculum;
import nl.topicus.eduarte.entities.landelijk.Cohort;
import nl.topicus.eduarte.entities.opleiding.Opleiding;
import nl.topicus.eduarte.entities.organisatie.Locatie;
import nl.topicus.eduarte.entities.organisatie.OrganisatieEenheid;
import nl.topicus.eduarte.zoekfilters.CurriculumZoekFilter;

public interface CurriculumDataAccessHelper extends
		BatchZoekFilterDataAccessHelper<Curriculum, CurriculumZoekFilter>
{
	public Curriculum get(OrganisatieEenheid organisatieEenheid, Locatie locatie,
			Opleiding opleiding, Cohort cohort);
}
