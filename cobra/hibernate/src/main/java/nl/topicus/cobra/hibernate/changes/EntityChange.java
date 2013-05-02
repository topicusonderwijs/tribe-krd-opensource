package nl.topicus.cobra.hibernate.changes;

import java.util.Arrays;

/**
 * Legt de wijzigingen vast van een entiteit en de bijbehorende Hibernate actie.
 */
public class EntityChange
{
	/**
	 * Creeert een wijziging van een entiteit wanneer de entiteit gesaved wordt.
	 */
	public static EntityChange save(Object entity, String[] properties, Object[] current)
	{
		Object[] emptystate = new Object[current.length];
		return new EntityChange(HibernateAction.SAVE, entity, properties, emptystate, current);
	}

	/**
	 * Creeert een wijziging van een entiteit wanneer deze uit de database verwijderd
	 * wordt.
	 */
	public static EntityChange delete(Object entity, String[] properties, Object[] original)
	{
		Object[] emptystate = new Object[original.length];
		return new EntityChange(HibernateAction.DELETE, entity, properties, original, emptystate);
	}

	/**
	 * Creeert een wijziging van een entiteit wanneer er een update op de entiteit gedaan
	 * wordt.
	 */
	public static EntityChange update(Object entity, String[] properties, Object[] original,
			Object[] current)
	{
		return new EntityChange(HibernateAction.UPDATE, entity, properties, original, current);
	}

	private final HibernateAction action;

	private final Object entity;

	private final String[] properties;

	private final Object[] original;

	private final Object[] current;

	private EntityChange(HibernateAction action, Object entity, String[] properties,
			Object[] original, Object[] current)
	{
		this.action = action;
		this.entity = entity;
		this.properties = properties;
		this.original = original;
		this.current = current;
	}

	public HibernateAction getAction()
	{
		return action;
	}

	public Object getEntity()
	{
		return entity;
	}

	public String[] getProperties()
	{
		return properties;
	}

	/**
	 * De vorige waardes van de velden van de entiteit. Is <code>null</code> als de
	 * entiteit ge-insert wordt.
	 */
	public Object[] getOriginal()
	{
		return original;
	}

	/**
	 * De huidige waardes van de velden van de entiteit. Is <code>null</code> wanneer de
	 * entiteit verwijderd wordt.
	 */
	public Object[] getCurrent()
	{
		return current;
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((action == null) ? 0 : action.hashCode());
		result = prime * result + Arrays.hashCode(current);
		result = prime * result + ((entity == null) ? 0 : entity.hashCode());
		result = prime * result + Arrays.hashCode(original);
		result = prime * result + Arrays.hashCode(properties);
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
		EntityChange other = (EntityChange) obj;
		if (action == null)
		{
			if (other.action != null)
				return false;
		}
		else if (!action.equals(other.action))
			return false;
		if (!Arrays.equals(current, other.current))
			return false;
		if (entity == null)
		{
			if (other.entity != null)
				return false;
		}
		else if (!entity.equals(other.entity))
			return false;
		if (!Arrays.equals(original, other.original))
			return false;
		if (!Arrays.equals(properties, other.properties))
			return false;
		return true;
	}
}
