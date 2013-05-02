/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.krd.dao.helpers;

import nl.topicus.cobra.dao.helpers.BatchZoekFilterDataAccessHelper;
import nl.topicus.eduarte.entities.landelijk.Schooljaar;
import nl.topicus.eduarte.krd.entities.bron.BronAanleverpunt;
import nl.topicus.eduarte.krd.entities.bron.BronSchooljaarStatus;
import nl.topicus.eduarte.krd.zoekfilters.BronSchooljaarStatusZoekFilter;

public interface BronSchooljaarStatusDataAccessHelper extends
		BatchZoekFilterDataAccessHelper<BronSchooljaarStatus, BronSchooljaarStatusZoekFilter>
{
	public BronSchooljaarStatus getSchooljaarStatus(BronAanleverpunt aanleverpunt,
			Schooljaar schooljaar);
}
