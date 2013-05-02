/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.cobra.modelsv2;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Comparator;

import org.apache.wicket.model.IModel;

/**
 * @author marrink
 * @param <T>
 */
public class ExtendedHibernateListModel<T extends Collection<Y>, Y> extends
		HibernateObjectListModel<T, Y> implements ExtendedModel<T>
{
	private static final long serialVersionUID = 1L;

	private ModelManager manager;

	private String fieldName = null;

	private Class< ? > fieldClass = null;

	public ExtendedHibernateListModel(T list, ModelManager manager, Field field)
	{
		super();
		this.manager = manager;
		if (field != null)
		{
			this.fieldName = field.getName();
			this.fieldClass = field.getDeclaringClass();
		}
		init(list);
	}

	public ExtendedHibernateListModel(T list, Comparator<IModel<Y>> comparator, ModelManager manager)
	{
		super(list, comparator);
		this.manager = manager;
	}

	@Override
	public ModelManager getManager()
	{
		return manager;
	}

	@Override
	public void discardChanges()
	{
		if (isAttached())
			detach();

		for (IModel<Y> curModel : getModels())
		{
			if (curModel instanceof ExtendedModel< ? >)
			{
				((ExtendedModel<Y>) curModel).discardChanges();
			}
		}
	}

	@Override
	protected IModel<Y> modelFor(Y object)
	{
		Field field;
		try
		{
			field = fieldName == null ? null : fieldClass.getDeclaredField(fieldName);
		}
		catch (SecurityException e)
		{
			field = null;
		}
		catch (NoSuchFieldException e)
		{
			field = null;
		}
		return getManager().getModel(object, field);
	}

	@Override
	public ObjectState getState()
	{
		throw new UnsupportedOperationException("List models do not (yet) support saving state");
	}

	@Override
	public void setState(ObjectState state)
	{
		throw new UnsupportedOperationException("List models do not (yet) support saving state");
	}
}
