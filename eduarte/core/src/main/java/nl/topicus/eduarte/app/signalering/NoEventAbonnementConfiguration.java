/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.app.signalering;

public final class NoEventAbonnementConfiguration implements EventAbonnementConfiguration<Void>
{
	private NoEventAbonnementConfiguration()
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public Void getValue()
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public void setValue(Void value)
	{
		throw new UnsupportedOperationException();
	}
}
