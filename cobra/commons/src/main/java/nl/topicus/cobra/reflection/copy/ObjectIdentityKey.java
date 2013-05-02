/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.cobra.reflection.copy;

public class ObjectIdentityKey
{
	private Object obj;

	public ObjectIdentityKey(Object obj)
	{
		this.obj = obj;
	}

	@Override
	public int hashCode()
	{
		return System.identityHashCode(obj);
	}

	@Override
	public boolean equals(Object other)
	{
		if (other instanceof ObjectIdentityKey)
			return this.obj == ((ObjectIdentityKey) other).obj;
		return false;
	}
}