/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.dao.helpers;

import java.util.List;

import nl.topicus.cobra.dao.helpers.BatchZoekFilterDataAccessHelper;
import nl.topicus.eduarte.entities.contract.SoortContract;
import nl.topicus.eduarte.zoekfilters.SoortContractZoekFilter;

public interface SoortContractDataAccessHelper extends
		BatchZoekFilterDataAccessHelper<SoortContract, SoortContractZoekFilter>
{
	public SoortContract get(Long id);

	public List<SoortContract> list();

	public SoortContract getSoortContract(String code);
}
