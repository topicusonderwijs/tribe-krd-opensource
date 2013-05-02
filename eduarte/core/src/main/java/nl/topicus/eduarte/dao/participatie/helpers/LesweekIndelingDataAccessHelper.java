/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.dao.participatie.helpers;

import java.util.List;

import nl.topicus.cobra.dao.helpers.BatchZoekFilterDataAccessHelper;
import nl.topicus.cobra.entities.IdObject;
import nl.topicus.eduarte.entities.participatie.LesweekIndeling;
import nl.topicus.eduarte.participatie.zoekfilters.LesweekindelingZoekFilter;

/**
 * @author loite
 */
public interface LesweekIndelingDataAccessHelper extends
		BatchZoekFilterDataAccessHelper<LesweekIndeling, LesweekindelingZoekFilter>
{
	public LesweekIndeling getlesweekIndeling(LesweekindelingZoekFilter filter);

	public List<LesweekIndeling> getLesweekIndelingen(IdObject contextParticipant);

	public List<LesweekIndeling> list();
}
