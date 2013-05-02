/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.dao.helpers;

import java.util.List;

import nl.topicus.cobra.dao.helpers.BatchZoekFilterDataAccessHelper;
import nl.topicus.eduarte.entities.vrijevelden.VrijVeld;
import nl.topicus.eduarte.entities.vrijevelden.VrijVeldEntiteit;
import nl.topicus.eduarte.zoekfilters.VrijVeldZoekFilter;

import org.hibernate.criterion.DetachedCriteria;

/**
 * @author hoeve
 */
public interface VrijVeldDataAccessHelper extends
		BatchZoekFilterDataAccessHelper<VrijVeld, VrijVeldZoekFilter>
{
	public DetachedCriteria buildCriteria(List< ? extends VrijVeldEntiteit> vrijeVelden,
			Boolean gearchiveerd, Class< ? extends VrijVeldEntiteit> koppelClass,
			String projectieProperty);
}
