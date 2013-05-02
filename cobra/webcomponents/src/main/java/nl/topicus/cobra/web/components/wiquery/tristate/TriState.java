/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.cobra.web.components.wiquery.tristate;

/**
 * A switch for 3 states. It can be on, off, or inherited which means half on / half off.
 * 
 * @author marrink
 */
public enum TriState
{
	/**
	 * @see Boolean#TRUE
	 */
	On,
	/**
	 * @see Boolean#FALSE
	 */
	Off,
	/**
	 * Partially on or partially off depending on your point of view.
	 */
	Partial;

	public TriState or(TriState other)
	{
		if (this.equals(On) || other.equals(On))
			return On;
		if (this.equals(Partial) || other.equals(Partial))
			return Partial;
		return Off;
	}
}
