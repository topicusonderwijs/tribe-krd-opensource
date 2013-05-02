/*
 * $Id: HibernateObjectListModel.java,v 1.5 2007-12-06 09:04:54 marrink Exp $
 * $Revision: 1.5 $
 * $Date: 2007-12-06 09:04:54 $
 *
 * ====================================================================
 * Copyright (c) 2005, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.cobra.modelsv2;

import java.io.Serializable;
import java.util.*;

import nl.topicus.cobra.reflection.ReflectionUtil;

import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;

/**
 * Model that maintains the integrity of a collection of hibernate objects. Supports
 * mainly List's but there is some support for Set and other collections. Use at your own
 * risk with any other collection type as List.
 * 
 * @author loite
 * @author marrink
 * @param <T>
 *            het type hibernate object.
 */
public class HibernateObjectListModel<T extends Collection<Y>, Y> extends AbstractReadOnlyModel<T>
{
	private static final long serialVersionUID = 7468325210117094972L;

	private Comparator<IModel<Y>> comparator;

	private List<IModel<Y>> modelList;

	private transient T objectList;

	private Class<T> collectionType;

	@SuppressWarnings("unchecked")
	protected HibernateObjectListModel()
	{
		this.modelList = new ArrayList<IModel<Y>>();
		this.collectionType = (Class<T>) new ArrayList<Y>().getClass();
		this.comparator = null;
	}

	public HibernateObjectListModel(T list)
	{
		this.modelList = new ArrayList<IModel<Y>>(list.size());
		this.comparator = null;
		init(list);
	}

	public HibernateObjectListModel(T list, Comparator<IModel<Y>> comparator)
	{
		this.modelList = new ArrayList<IModel<Y>>(list.size());
		this.comparator = comparator;
		init(list);
	}

	/**
	 * (Re)initializes this model based on the content of the provided collection.
	 * 
	 * @param list
	 */
	@SuppressWarnings("unchecked")
	protected final void init(T list)
	{
		if (!(list instanceof Serializable))
			throw new IllegalArgumentException(
				"Cannot make a list model for not-Serializable class " + list.getClass());

		for (IModel model : modelList)
			model.detach();

		modelList.clear();

		for (Y object : list)
			modelList.add(modelFor(object));

		if (this.comparator != null)
		{
			Collections.sort(modelList, this.comparator);

			objectList = ReflectionUtil.invokeConstructor(collectionType);
			for (IModel model : modelList)
				objectList.add((Y) model.getObject());
		}
		else
			objectList = list;

		/**
		 * if else constructie om de {$link PersistentBag}, {$link PersistentSet} en
		 * {$link PersistentMap} te omzeilen.
		 */
		if (list instanceof List)
			collectionType = (Class<T>) new ArrayList<Y>().getClass();
		else if (list instanceof SortedSet)
			collectionType = (Class<T>) new TreeSet<Y>().getClass();
		else if (list instanceof Set)
			collectionType = (Class<T>) new HashSet<Y>().getClass();
		else
			collectionType = (Class<T>) list.getClass();
	}

	@Override
	public void detach()
	{
		if (objectList != null)
			init(objectList);

		for (IModel<Y> model : modelList)
			model.detach();

		objectList = null;
	}

	@Override
	public T getObject()
	{
		if (objectList != null)
			return objectList;

		T list = ReflectionUtil.invokeConstructor(collectionType);

		for (IModel<Y> model : modelList)
		{
			list.add(model.getObject());
		}

		objectList = list;
		return list;
	}

	/**
	 * Returned het model voor het item op de index. Tip gebruik dit in
	 * ListView.getListItemModel en voorkom het onnodig aanmaken van extra HibernateModels
	 * 
	 * @param index
	 * @return imodel
	 * @thows IndexOutOfBoundsException
	 */
	public IModel<Y> get(int index)
	{
		return modelList.get(index);
	}

	/**
	 * Voegt object van hetzelfde type toe aan de lijst. Objecten kunnen meerdere malen
	 * voorkomen.
	 * 
	 * @param object
	 * @return true if the item was added, false if the item was not added (for instance
	 *         if the underlying set would contain duplicate items because of this
	 *         action).
	 * @deprecated modificatie op een list model is deprecated, verander de lijst
	 */
	@Deprecated
	public boolean add(Y object)
	{
		if (Set.class.isAssignableFrom(collectionType))
		{
			if (contains(object))
				return false;
		}
		modelList.add(modelFor(object));

		if (this.comparator != null)
			Collections.sort(modelList, this.comparator);

		return true;
	}

	/**
	 * Wraps the object in a new IModel implementation. Default implementation uses a
	 * HibernateModel
	 * 
	 * @param object
	 * @return a new IridiumModel
	 */
	protected IModel<Y> modelFor(Y object)
	{
		return new HibernateModel<Y>(object);
	}

	/**
	 * Verwijdert het eerste object dat gelijk is aan het argument uit de lijst. Het kan
	 * dus zijn dat het object hierna nog voorkomt in de lijst.
	 * 
	 * @param object
	 * @deprecated modificatie op een list model is deprecated, verander de lijst
	 */
	@Deprecated
	public void remove(Y object)
	{
		IModel<Y> found = null;
		for (IModel<Y> model : modelList)
		{
			if (model.equals(object))
			{
				found = model;
				break;
			}
		}
		if (found != null)
		{
			modelList.remove(found);
			objectList.remove(object);
		}
	}

	/**
	 * De lengte van de lijst.
	 * 
	 * @return de lengte.
	 */
	public int size()
	{
		return modelList.size();
	}

	/**
	 * Bepaalt of het gegeven object al aanwezig is in de lijst.
	 * 
	 * @param object
	 * @return true, als het object aanwezig is, anders false
	 */
	public boolean contains(Y object)
	{
		boolean found = false;
		for (IModel<Y> model : modelList)
		{
			if (model.equals(object))
			{
				found = true;
				break;
			}
		}

		return found;
	}

	/**
	 * De interne lijst met modellen. Niet wijzigbaar.
	 * 
	 * @return lijst
	 */
	protected final List<IModel<Y>> getModels()
	{
		return Collections.unmodifiableList(modelList);
	}

	public boolean isAttached()
	{
		return objectList != null;
	}
}
