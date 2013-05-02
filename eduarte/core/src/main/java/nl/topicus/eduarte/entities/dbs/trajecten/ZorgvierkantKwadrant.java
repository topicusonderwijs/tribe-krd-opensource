/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.entities.dbs.trajecten;

/**
 * @author maatman
 */
public enum ZorgvierkantKwadrant
{
	/**
	 * Linksboven
	 */
	Studievoortgang,
	/**
	 * Rechtsboven
	 */
	Gedrag,
	/**
	 * Linksonder
	 */
	Capaciteiten,
	/**
	 * Rechtsonder
	 */
	Ontwikkeling
	{
		/**
		 * @see java.lang.Enum#toString()
		 */
		@Override
		public String toString()
		{
			return "Begeleiding en Ontwikkeling";
		}
	}
}
