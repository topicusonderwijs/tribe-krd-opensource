/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.dao.helpers;

import nl.topicus.cobra.dao.helpers.BatchZoekFilterDataAccessHelper;
import nl.topicus.cobra.dao.hibernate.CriteriaBuilder;
import nl.topicus.eduarte.entities.personen.PersoonExterneOrganisatie;
import nl.topicus.eduarte.zoekfilters.PersoonExterneOrganisatieZoekFilter;

public interface PersoonExterneOrganisatieDataAccessHelper
		extends
		BatchZoekFilterDataAccessHelper<PersoonExterneOrganisatie, PersoonExterneOrganisatieZoekFilter>
{
	public void addCriteria(CriteriaBuilder builder, PersoonExterneOrganisatieZoekFilter filter,
			String persoonExterneOrganisatieAlias, String externeOrganisatieAlias,
			String persoonAlias);

	public PersoonExterneOrganisatie get(Long id);

}
