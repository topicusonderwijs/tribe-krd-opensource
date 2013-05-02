package nl.topicus.eduarte.tester.hibernate;

import static nl.topicus.cobra.hibernate.changes.HibernateAction.*;
import nl.topicus.cobra.entities.IdObject;
import nl.topicus.cobra.hibernate.changes.HibernateAction;

public class DatabaseAction
{
	private final HibernateAction action;

	private final IdObject object;

	private DatabaseAction(HibernateAction action, IdObject object)
	{
		this.action = action;
		this.object = object;
	}

	public HibernateAction getAction()
	{
		return action;
	}

	public IdObject getObject()
	{
		return object;
	}

	@Override
	public String toString()
	{
		return action.name().toLowerCase() + " " + object;
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((action == null) ? 0 : action.hashCode());
		result = prime * result + ((object == null) ? 0 : object.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DatabaseAction other = (DatabaseAction) obj;
		if (action == null)
		{
			if (other.action != null)
				return false;
		}
		else if (!action.equals(other.action))
			return false;
		if (object == null)
		{
			if (other.object != null)
				return false;
		}
		else if (!object.equals(other.object))
			return false;
		return true;
	}

	public static DatabaseAction insert(Object object)
	{
		return new DatabaseAction(SAVE, (IdObject) object);
	}

	public static DatabaseAction update(Object object)
	{
		return new DatabaseAction(UPDATE, (IdObject) object);
	}

	public static DatabaseAction delete(Object object)
	{
		return new DatabaseAction(DELETE, (IdObject) object);
	}
}
