/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.providers;

import nl.topicus.eduarte.app.signalering.EventReceiver;
import nl.topicus.eduarte.entities.personen.Persoon;

/**
 * @author loite
 */
public interface PersoonProvider extends EventReceiver
{
	/**
	 * @return De persoon
	 */
	public Persoon getPersoon();

}
