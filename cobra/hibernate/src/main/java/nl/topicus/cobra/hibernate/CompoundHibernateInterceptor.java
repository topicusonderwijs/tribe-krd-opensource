package nl.topicus.cobra.hibernate;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import nl.topicus.cobra.util.CollectionUtil;

import org.hibernate.CallbackException;
import org.hibernate.EmptyInterceptor;
import org.hibernate.EntityMode;
import org.hibernate.Interceptor;
import org.hibernate.Transaction;
import org.hibernate.type.Type;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * {@link CompoundHibernateInterceptor} is een workaround voor de beperking van slechts 1
 * Hibernate interceptor van Hibernate. De {@link CompoundHibernateInterceptor} neemt
 * meerdere {@link Interceptor}s en geeft de methode calls door aan elke interceptor.
 * <p>
 * Sommige methodes zijn short-circuited: als er een resultaat gevonden is, wordt er niet
 * langs de andere interceptors gegaan, maar direct het resultaat teruggegeven.
 * <p>
 * Als er een exceptie optreedt in een van de interceptors, worden de andere interceptors
 * niet meer aangeroepen.
 * <p>
 * <b>NOTA BENE</b> Deze class is noodzakelijkerwijs een Singleton zonder het pattern
 * officieel te implementeren: Hibernate ondersteunt slechts 1 interceptor tegelijkertijd,
 * dus daarom singleton. Voor gebruik in update tasks is een static referentie naar de
 * singleton opgenomen waardoor deze class niet thread safe is.
 */
public class CompoundHibernateInterceptor extends EmptyInterceptor implements Iterable<Interceptor>
{
	private static final long serialVersionUID = 1L;

	private static final Logger log = LoggerFactory.getLogger(CompoundHibernateInterceptor.class);

	private final List<Interceptor> interceptors = new ArrayList<Interceptor>();

	private static CompoundHibernateInterceptor singleton;

	/**
	 * Default constructor, voeg nieuwe interceptors toe met
	 * {@link #addInterceptor(Interceptor)}.
	 */
	public CompoundHibernateInterceptor()
	{
		log.debug("Created CompoundHibernateInterceptor");
		singleton = this;
	}

	/**
	 * Constructor op basis van geconfigureerde interceptors. Additionele interceptors
	 * kunnen worden toegevoegd met {@link #addInterceptor(Interceptor)}
	 */
	public CompoundHibernateInterceptor(Interceptor... interceptors)
	{
		this();
		this.interceptors.addAll(Arrays.asList(interceptors));
	}

	/**
	 * Voegt de <tt>interceptor</tt> toe aan de interceptors.
	 */
	public void addInterceptor(Interceptor interceptor)
	{
		if (interceptor != null)
		{
			this.interceptors.add(interceptor);
			log.debug("added {} interceptor", interceptor.getClass().getName());
		}
	}

	/**
	 * Verwijdert de {@link Interceptor} uit de lijst van de interceptors.
	 */
	public void removeInterceptor(Interceptor interceptor)
	{
		if (interceptor != null)
		{
			this.interceptors.remove(interceptor);
			log.debug("removed {} interceptor", interceptor.getClass().getName());
		}
	}

	@Override
	public Iterator<Interceptor> iterator()
	{
		return Collections.unmodifiableList(interceptors).iterator();
	}

	@Override
	public void afterTransactionBegin(Transaction tx)
	{
		for (Interceptor interceptor : interceptors)
		{
			interceptor.afterTransactionBegin(tx);
		}
	}

	@Override
	public void afterTransactionCompletion(Transaction tx)
	{
		for (Interceptor interceptor : interceptors)
		{
			interceptor.afterTransactionCompletion(tx);
		}
	}

	@Override
	public void beforeTransactionCompletion(Transaction tx)
	{
		for (Interceptor interceptor : interceptors)
		{
			interceptor.beforeTransactionCompletion(tx);
		}
	}

	/**
	 * Roept <tt>findDirty</tt> aan op elke interceptor en merget de resultaten van elk
	 * van de aanroepen. Hierdoor is het resultaat de union van de resultaten.
	 */
	@Override
	public int[] findDirty(Object entity, Serializable id, Object[] currentState,
			Object[] previousState, String[] propertyNames, Type[] types)
	{
		Set<Integer> result = new HashSet<Integer>();
		boolean shouldReturnNull = true;
		for (Interceptor interceptor : interceptors)
		{
			int[] dirtyBits =
				interceptor
					.findDirty(entity, id, currentState, previousState, propertyNames, types);
			if (dirtyBits == null)
			{
				continue;
			}
			shouldReturnNull = false;

			CollectionUtil.addAll(result, dirtyBits);
		}
		if (shouldReturnNull)
		{
			return null;
		}
		else
		{
			return CollectionUtil.toIntArray(result);
		}
	}

	/**
	 * Kort gesloten implementatie van <tt>getEntity</tt>, retourneert de eerst gevonden
	 * entity.
	 */
	@Override
	public Object getEntity(String entityName, Serializable id) throws CallbackException
	{
		for (Interceptor interceptor : interceptors)
		{
			Object tmp = interceptor.getEntity(entityName, id);
			if (tmp != null)
			{
				return tmp;
			}
		}
		return null;
	}

	/**
	 * Kort gesloten implementatie van <tt>getEntityName</tt>, retourneert de eerst
	 * gevonden entity naam.
	 */
	@Override
	public String getEntityName(Object object) throws CallbackException
	{
		for (Interceptor interceptor : interceptors)
		{
			String name = interceptor.getEntityName(object);
			if (name != null)
			{
				return name;
			}
		}
		return null;
	}

	/**
	 * Kort gesloten implementatie van <tt>instantiate</tt>, retourneert het eerst
	 * geinstantieerde object.
	 */
	@Override
	public Object instantiate(String entityName, EntityMode entityMode, Serializable id)
			throws CallbackException
	{
		for (Interceptor interceptor : interceptors)
		{
			Object entity = interceptor.instantiate(entityName, entityMode, id);
			if (entity != null)
			{
				return entity;
			}
		}
		return null;
	}

	/**
	 * Kort gesloten implementatie van <tt>isTransient</tt>, retourneert de eerst gevonden
	 * waarde die niet <tt>null</tt> is.
	 */
	@Override
	public Boolean isTransient(Object entity)
	{
		Boolean isTransient = null;
		for (Interceptor interceptor : interceptors)
		{
			isTransient = interceptor.isTransient(entity);
			if (isTransient != null)
			{
				return isTransient;
			}
		}
		return null;
	}

	@Override
	public void onCollectionRecreate(Object collection, Serializable key) throws CallbackException
	{
		for (Interceptor interceptor : interceptors)
		{
			interceptor.onCollectionRecreate(collection, key);
		}
	}

	@Override
	public void onCollectionRemove(Object collection, Serializable key) throws CallbackException
	{
		for (Interceptor interceptor : interceptors)
		{
			interceptor.onCollectionRemove(collection, key);
		}
	}

	@Override
	public void onCollectionUpdate(Object collection, Serializable key) throws CallbackException
	{
		for (Interceptor interceptor : interceptors)
		{
			interceptor.onCollectionUpdate(collection, key);
		}
	}

	@Override
	public void onDelete(Object entity, Serializable id, Object[] state, String[] propertyNames,
			Type[] types) throws CallbackException
	{
		for (Interceptor interceptor : interceptors)
		{
			interceptor.onDelete(entity, id, state, propertyNames, types);
		}
	}

	@Override
	public boolean onFlushDirty(Object entity, Serializable id, Object[] currentState,
			Object[] previousState, String[] propertyNames, Type[] types) throws CallbackException
	{
		boolean entityModified = false;
		for (Interceptor interceptor : interceptors)
		{
			entityModified =
				interceptor.onFlushDirty(entity, id, currentState, previousState, propertyNames,
					types)
					|| entityModified;
		}
		return entityModified;
	}

	@Override
	public boolean onLoad(Object entity, Serializable id, Object[] state, String[] propertyNames,
			Type[] types) throws CallbackException
	{
		boolean entityModified = false;
		for (Interceptor interceptor : interceptors)
		{
			entityModified =
				interceptor.onLoad(entity, id, state, propertyNames, types) || entityModified;
		}
		return entityModified;
	}

	@Override
	public String onPrepareStatement(String sql)
	{
		String result = sql;
		for (Interceptor interceptor : interceptors)
		{
			result = interceptor.onPrepareStatement(result);
		}
		return result;
	}

	@Override
	public boolean onSave(Object entity, Serializable id, Object[] state, String[] propertyNames,
			Type[] types) throws CallbackException
	{
		boolean entityModified = false;
		for (Interceptor interceptor : interceptors)
		{
			entityModified =
				interceptor.onSave(entity, id, state, propertyNames, types) || entityModified;
		}
		return entityModified;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void postFlush(Iterator entities) throws CallbackException
	{
		for (Interceptor interceptor : interceptors)
		{
			interceptor.postFlush(entities);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void preFlush(Iterator entities) throws CallbackException
	{
		for (Interceptor interceptor : interceptors)
		{
			interceptor.preFlush(entities);
		}
	}

	/**
	 * Haalt de singleton van de {@link CompoundHibernateInterceptor} op. <b>LET OP</b>
	 * DIT IS NIET THREAD SAFE!
	 */
	public static CompoundHibernateInterceptor get()
	{
		return singleton;
	}
}
