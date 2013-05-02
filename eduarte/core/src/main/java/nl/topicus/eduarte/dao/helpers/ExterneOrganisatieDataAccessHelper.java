/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.dao.helpers;

import nl.topicus.cobra.dao.helpers.BatchZoekFilterDataAccessHelper;
import nl.topicus.eduarte.entities.organisatie.ExterneOrganisatie;
import nl.topicus.eduarte.zoekfilters.ExterneOrganisatieZoekFilter;

/**
 * @author vandekamp
 */
public interface ExterneOrganisatieDataAccessHelper extends
		BatchZoekFilterDataAccessHelper<ExterneOrganisatie, ExterneOrganisatieZoekFilter>
{
}
