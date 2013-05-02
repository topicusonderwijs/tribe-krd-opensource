/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.krd.dao.helpers;

import java.util.List;

import nl.topicus.cobra.dao.BatchDataAccessHelper;
import nl.topicus.eduarte.krd.entities.bron.BronAanleverpunt;

/**
 * @author vandekamp
 */
public interface BronAanleverpuntDataAccessHelper extends BatchDataAccessHelper<BronAanleverpunt>
{

	public List<BronAanleverpunt> getBronAanleverpunten();

	public BronAanleverpunt getAanleverpunt(int aanleverpuntnummer);

	boolean existsBronAanleverpunten();

}
