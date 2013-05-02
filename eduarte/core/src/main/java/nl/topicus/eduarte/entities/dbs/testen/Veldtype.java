/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.entities.dbs.testen;

/**
 * @author maatman
 */
public enum Veldtype
{
	/**
	 * Een tekstveld voor een test
	 */
	Tekst
	{
		@Override
		public Veldwaarde createWaarde()
		{
			return new TekstueleVeldwaarde();
		}
	},
	/**
	 * Een numeriek scoreveld voor een test
	 */
	Numeriek
	{
		@Override
		public Veldwaarde createWaarde()
		{
			return new NumeriekeVeldwaarde();
		}
	};

	public abstract Veldwaarde createWaarde();
}
