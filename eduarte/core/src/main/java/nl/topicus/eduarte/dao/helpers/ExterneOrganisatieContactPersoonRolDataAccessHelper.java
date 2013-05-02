/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.dao.helpers;

import nl.topicus.cobra.dao.helpers.BatchZoekFilterDataAccessHelper;
import nl.topicus.eduarte.entities.personen.ExterneOrganisatieContactPersoonRol;
import nl.topicus.eduarte.zoekfilters.ExterneOrganisatieContactPersoonRolZoekFilter;

/**
 * @author hoeve
 */
public interface ExterneOrganisatieContactPersoonRolDataAccessHelper
		extends
		BatchZoekFilterDataAccessHelper<ExterneOrganisatieContactPersoonRol, ExterneOrganisatieContactPersoonRolZoekFilter>
{
	public ExterneOrganisatieContactPersoonRol get(String naam);
}
