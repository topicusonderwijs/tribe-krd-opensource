/*
 * $Id: HibernateModel.java,v 1.3 2007-11-02 11:31:15 marrink Exp $
 * $Revision: 1.3 $
 * $Date: 2007-11-02 11:31:15 $
 *
 * ====================================================================
 * Copyright (c) 2005, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.cobra.modelsv2;

import java.io.Serializable;
import java.util.Arrays;

import nl.topicus.cobra.entities.IdObject;
import nl.topicus.cobra.util.Asserts;

import org.apache.wicket.RequestCycle;
import org.apache.wicket.injection.web.InjectorHolder;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Model for dynamicly attaching / detaching of Hibernate persisted entities. After each
 * detach the model is pulled fresh from the database, so any changes made to the object
 * are not persisted unless the object is manually saved.
 * 
 * @author marrink
 * @param <T>
 */
public class HibernateModel<T> implements IModel<T>
{
	/** Voor serializatie. */
	private static final long serialVersionUID = 1L;

	/** Logger. */
	private static final Logger log = LoggerFactory.getLogger(HibernateModel.class);

	/**
	 * The database id.
	 */
	protected Serializable id;

	/**
	 * Dual purpose field, it either contains the object or its class.
	 */
	protected Object objectIfIdIsNull;

	@SpringBean
	private IObjectAccess objectAccess;

	/**
	 * Constructor. for persisten or transient entities.
	 * 
	 * @param object
	 *            database entity can be null
	 */
	public HibernateModel(T object)
	{
		if (isSaved(object))
		{
			id = ((IdObject) object).getIdAsSerializable();
		}
		objectIfIdIsNull = object;
	}

	/**
	 * Constructor for persistent entities.
	 * 
	 * @param clazz
	 *            type of entity
	 * @param id
	 *            database identifier
	 */
	public HibernateModel(Class< ? extends T> clazz, Serializable id)
	{
		Asserts.assertNotNull("clazz", clazz);
		Asserts.assertNotNull("id", id);

		this.objectIfIdIsNull = clazz;
		this.id = id;
	}

	/**
	 * Detects the "real" class of an entity even if it is a proxied entity.
	 * 
	 * @param object
	 *            the entity
	 */
	@SuppressWarnings("unchecked")
	protected Class detectClass(Object object)
	{
		if (object instanceof IdObject)
			return getObjectAccess().getClass((IdObject) object);
		return object.getClass();
	}

	/**
	 * Loads the object. The caller can then cache it for re-use untill the detach.
	 * 
	 * @return the object
	 */
	@SuppressWarnings("unchecked")
	protected T load()
	{
		// we can only do something if we have an id
		if (id != null)
		{
			Class< ? extends IdObject> clazz = null;
			if (objectIfIdIsNull instanceof Class)
			{
				clazz = (Class) objectIfIdIsNull;
			}
			else
			{
				clazz = detectClass(objectIfIdIsNull);
			}
			return (T) getObjectAccess().get(clazz, Arrays.asList(id)).get(0);
		}
		return (T) objectIfIdIsNull;
	}

	/**
	 * @return the database identifier of the enity
	 */
	protected final Serializable getId()
	{
		return id;
	}

	/**
	 * @return the class of the entity
	 */
	@SuppressWarnings("unchecked")
	protected final Class< ? extends T> getObjectClass()
	{
		if (objectIfIdIsNull instanceof Class)
		{
			return (Class< ? extends T>) objectIfIdIsNull;
		}
		return detectClass(objectIfIdIsNull);
	}

	/**
	 * Calls {@link #onDetach()} if the model can be detached. If the model can not be
	 * detached {@link #isAttached()} will still return true.
	 * 
	 * @see org.apache.wicket.model.IDetachable#detach()
	 */
	public void detach()
	{
		if (isAttached() && (objectIfIdIsNull instanceof IdObject) && isSaved(objectIfIdIsNull))
		{
			if (id == null)
			{
				id = ((IdObject) objectIfIdIsNull).getIdAsSerializable();
			}
			objectIfIdIsNull = detectClass(objectIfIdIsNull);

			if (log.isDebugEnabled())
			{
				log.debug("removed transient object for " + this + ", requestCycle "
					+ RequestCycle.get());
			}
			onDetach();
		}
	}

	/**
	 * @see org.apache.wicket.model.IModel#getObject()
	 */
	@SuppressWarnings("unchecked")
	public T getObject()
	{
		if (!isAttached())
		{
			objectIfIdIsNull = load();

			if (log.isDebugEnabled())
			{
				log.debug("loaded transient object " + objectIfIdIsNull + " for " + this
					+ ", requestCycle " + RequestCycle.get());
			}

			onAttach();
		}
		return (T) objectIfIdIsNull;
	}

	/**
	 * Gets the attached status of this model instance. Opmerking dit model geeft nog
	 * steeds de status attached weer indien het een transient object betreft, dit ivm met
	 * het detectie algoritme.
	 * 
	 * @return true if the model is attached, false otherwise
	 */
	public boolean isAttached()
	{
		return (id != null && !(objectIfIdIsNull instanceof Class< ? >))
			|| (id == null && objectIfIdIsNull != null);
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		StringBuffer sb = new StringBuffer(super.toString());
		sb.append(":attached=[").append(isAttached()).append("]:id=[").append(id).append(
			"]:tempModelObject=[").append(this.objectIfIdIsNull).append("]");
		return sb.toString();
	}

	/**
	 * Attaches to the current request. Implement this method with custom behavior, such
	 * as loading the model object.
	 */
	protected void onAttach()
	{
	}

	/**
	 * Detaches from the current request. Implement this method with custom behavior, such
	 * as setting the model object to null.
	 */
	public void onDetach()
	{
	}

	/**
	 * Determines if the object in question is persisted to the database. If an object is
	 * persisted it must never return null for <code>getIdAsSerializable()</code>
	 * 
	 * @param object
	 * @return true if the object is persistet, false otherwise
	 */
	protected boolean isSaved(Object object)
	{
		return object != null && object instanceof IdObject && ((IdObject) object).isSaved();
		// unsaved-value for id's is kind of not possible using annotations, instead
		// hibernate has a few algorithms to get the unsaved-value automagically
	}

	protected IObjectAccess getObjectAccess()
	{
		if (objectAccess == null)
			InjectorHolder.getInjector().inject(this);

		return objectAccess;
	}

	@Override
	public boolean equals(Object object)
	{
		if (object == this)
			return true;
		if (object instanceof HibernateModel< ? >)
		{
			HibernateModel< ? > model = (HibernateModel< ? >) object;
			boolean attacheStateSame = model.isAttached() == isAttached();
			if (attacheStateSame
				&& ((model.id == null && id == null) || (model.id != null && model.id.equals(id)))
				&& model.objectIfIdIsNull != null)
			{
				return model.objectIfIdIsNull.equals(objectIfIdIsNull);
			}
			else if (!attacheStateSame)
			{
				// both not saved
				if (model.id == null && id == null && model.objectIfIdIsNull != null)
					return model.objectIfIdIsNull.equals(objectIfIdIsNull);
				// both saved
				else if (model.id != null && model.id.equals(id))
				{
					Class< ? > myClass = null;
					Class< ? > modelClass = null;
					if (objectIfIdIsNull instanceof IdObject)
						myClass = detectClass(objectIfIdIsNull);
					else
						myClass = (Class< ? >) objectIfIdIsNull;
					if (model.objectIfIdIsNull instanceof IdObject)
						modelClass = model.detectClass(model.objectIfIdIsNull);
					else
						modelClass = (Class< ? >) model.objectIfIdIsNull;
					return myClass.equals(modelClass);
				}
				// 1 saved object, 1 unsaved object: never the same
				return false;
			}
		}
		return false;
	}

	@Override
	public int hashCode()
	{
		return id == null ? objectIfIdIsNull == null ? 0 : objectIfIdIsNull.hashCode() : id
			.hashCode();
	}

	public void setObject(Object object)
	{
		if (object == null || object instanceof IdObject)
		{
			IdObject idObj = (IdObject) object;
			onSetObject(objectIfIdIsNull, id, idObj);
			if (isSaved(object))
			{
				id = idObj.getIdAsSerializable();
			}
			else
			{
				id = null;
			}
		}
		else
		{
			id = null;
		}
		objectIfIdIsNull = object;
	}

	/**
	 * Callback method when the id of the object, or the object itself changes.
	 * 
	 * @param oldObjectOrClass
	 * @param oldId
	 * @param newObject
	 */
	protected void onSetObject(Object oldObjectOrClass, Serializable oldId, IdObject newObject)
	{
	}
}
