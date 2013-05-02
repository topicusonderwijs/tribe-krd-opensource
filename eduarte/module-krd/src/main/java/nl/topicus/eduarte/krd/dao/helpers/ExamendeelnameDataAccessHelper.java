/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.krd.dao.helpers;

import java.util.List;

import nl.topicus.cobra.dao.helpers.BatchZoekFilterDataAccessHelper;
import nl.topicus.eduarte.entities.examen.Examendeelname;
import nl.topicus.eduarte.krd.zoekfilters.ExamendeelnameZoekFilter;

public interface ExamendeelnameDataAccessHelper extends
		BatchZoekFilterDataAccessHelper<Examendeelname, ExamendeelnameZoekFilter>
{

	List<Long> getExamenDeelnameIds(ExamendeelnameZoekFilter filter);

	public long getAantalExamendeelnamesInWachtrij(ExamendeelnameZoekFilter filter);

}
