package nl.topicus.cobra.entities;

import java.io.Serializable;

public class ObjectKey
{
	private Class< ? extends IdObject> clazz;

	private Serializable id;

	public ObjectKey(IdObject object)
	{
		clazz = object.getClass();
		id = object.getIdAsSerializable();
	}

	public ObjectKey(Class< ? extends IdObject> clazz, Serializable id)
	{
		this.clazz = clazz;
		this.id = id;
	}

	@Override
	public boolean equals(Object o)
	{
		if (o instanceof ObjectKey)
		{
			ObjectKey other = (ObjectKey) o;
			return other.id.equals(this.id) && other.clazz.equals(this.clazz);
		}
		return false;
	}

	@Override
	public int hashCode()
	{
		return clazz.hashCode() ^ id.hashCode();
	}

	@Override
	public String toString()
	{
		return clazz.toString() + ":" + id;
	}
}
