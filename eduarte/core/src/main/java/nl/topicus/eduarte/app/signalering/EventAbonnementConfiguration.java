/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.app.signalering;

public interface EventAbonnementConfiguration<T>
{
	public T getValue();

	public void setValue(T value);
}
