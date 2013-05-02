/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.dao.helpers;

import java.util.List;

import nl.topicus.cobra.dao.BatchDataAccessHelper;
import nl.topicus.eduarte.entities.organisatie.ExterneOrganisatie;
import nl.topicus.eduarte.entities.organisatie.ExterneOrganisatieAdres;

/**
 * @author vandekamp
 */
public interface ExterneOrganisatieAdresDataAccessHelper extends
		BatchDataAccessHelper<ExterneOrganisatieAdres>
{
	public List<ExterneOrganisatie> getBestaandeExtOrganisatiesOpZelfdeAdres(
			ExterneOrganisatie organisatie, ExterneOrganisatieAdres externeOrganisatieAdres);

}
