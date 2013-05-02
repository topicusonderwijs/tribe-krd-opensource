/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.dao.helpers;

import java.util.List;

import nl.topicus.cobra.dao.helpers.BatchZoekFilterDataAccessHelper;
import nl.topicus.eduarte.entities.organisatie.Brin;
import nl.topicus.eduarte.zoekfilters.BrinZoekFilter;

/**
 * @author idserda
 */
public interface BrinDataAccessHelper extends BatchZoekFilterDataAccessHelper<Brin, BrinZoekFilter>
{
	public Brin get(String code);

	public List<Brin> getAlleBrins();

	public Brin getBrinByNaam(String naam);

}
