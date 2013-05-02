/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.cobra.providers;

import java.io.Serializable;
import java.util.Date;

/**
 * @author loite
 */
public interface DatumProvider extends Serializable
{

	/**
	 * @return De geselecteerde datum
	 */
	public Date getDatum();

}
