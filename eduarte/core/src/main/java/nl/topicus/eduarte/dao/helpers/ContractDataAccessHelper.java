/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.dao.helpers;

import java.util.Date;
import java.util.List;

import nl.topicus.cobra.dao.helpers.BatchZoekFilterDataAccessHelper;
import nl.topicus.eduarte.entities.contract.Contract;
import nl.topicus.eduarte.zoekfilters.ContractZoekFilter;

/**
 * @author hoeve
 */
public interface ContractDataAccessHelper extends
		BatchZoekFilterDataAccessHelper<Contract, ContractZoekFilter>
{
	public Contract get(Long id);

	public List<Contract> list(Date datum);

	public Contract getContract(String code);
}
