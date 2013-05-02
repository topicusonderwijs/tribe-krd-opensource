/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.dao.helpers;

import java.util.List;

import nl.topicus.cobra.dao.helpers.BatchZoekFilterDataAccessHelper;
import nl.topicus.eduarte.entities.inschrijving.UitkomstIntakegesprek;
import nl.topicus.eduarte.zoekfilters.UitkomstIntakegesprekZoekFilter;

/**
 * @author idserda
 */
public interface UitkomstIntakegesprekDataAccessHelper extends
		BatchZoekFilterDataAccessHelper<UitkomstIntakegesprek, UitkomstIntakegesprekZoekFilter>
{
	public UitkomstIntakegesprek get(Long id);

	public List<UitkomstIntakegesprek> list();

	public UitkomstIntakegesprek getUitkomstIntakegesprek(String code);
}
