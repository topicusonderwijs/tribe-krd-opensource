/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.cobra.modelsv2;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.*;
import java.util.Map.Entry;

import nl.topicus.cobra.entities.IdObject;
import nl.topicus.cobra.entities.TransientIdObject;
import nl.topicus.cobra.util.JavaUtil;

import org.apache.wicket.Application;
import org.apache.wicket.RequestCycle;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.util.lang.PropertyResolver;
import org.apache.wicket.util.lang.PropertyResolverConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Model dat gewijzigde velden van objecten ook na de detach kan vasthouden. Ook als deze
 * al opgeslagen zijn in de database.
 * 
 * @author marrink, papegaaij
 * @param <T>
 *            het type object
 */
public class ExtendedHibernateModel<T> extends HibernateModel<T> implements ExtendedModel<T>
{
	private static final long serialVersionUID = 1L;

	private static final Logger log = LoggerFactory.getLogger(ExtendedHibernateModel.class);

	/**
	 * The fields that are persisted
	 */
	protected Map<String, IModel< ? >> persistedFields;

	/**
	 * The manager of this model
	 */
	protected ModelManager manager;

	/**
	 * True when the model is attached
	 */
	protected boolean attached;

	private boolean attaching;

	private boolean detaching;

	/**
	 * Constructor voor persistent en transient entiteiten.
	 * 
	 * @param object
	 * @param manager
	 */
	public ExtendedHibernateModel(T object, ModelManager manager)
	{
		super(object);
		this.manager = manager;
		attached = true;
		persistedFields = new HashMap<String, IModel< ? >>();
	}

	/**
	 * Geeft een lijst met velden die opgeslagen moeten worden.
	 * 
	 * @param objectClass
	 *            De class van het object waar de velden in zitten.
	 * @return De velden die opgeslagen moeten worden.
	 */
	protected List<Field> getFieldsToPersist(Class< ? > objectClass)
	{
		List<Field> ret = new ArrayList<Field>();
		Class< ? > clazz = objectClass;
		while (clazz != null)
		{
			for (Field field : clazz.getDeclaredFields())
			{
				if (manager.getFilter().shouldPersist(field, objectClass))
					ret.add(field);
			}
			clazz = clazz.getSuperclass();
		}
		return ret;
	}

	/**
	 * @see nl.topicus.cobra.models.ExtendedModel#persistFields(nl.topicus.cobra.models.FieldPersistanceFilter)
	 */
	private void persistFields()
	{
		for (Field curField : getFieldsToPersist(getObjectClass()))
		{
			updatePersistentField(curField);
		}
	}

	/**
	 * @param getFieldValue
	 * @param field
	 */
	private void updatePersistentField(Field field)
	{
		String key = getKey(field);
		objectIfIdIsNull = getObjectAccess().getActualObject(objectIfIdIsNull);
		IModel< ? > model = manager.getModel(getPropertyValue(key, objectIfIdIsNull), field);
		IModel< ? > oldModel = persistedFields.put(key, model);
		if (model != oldModel && oldModel != null)
		{
			oldModel.detach();
		}
		if (!field.getType().isPrimitive() && !((IdObject) objectIfIdIsNull).isSaved())
			PropertyResolver.setValue(field.getName(), objectIfIdIsNull, null, null);
		model.detach();
	}

	/**
	 * Maakt van een {@link Field} een key zodat de waarde van een veld bijgehouden kan
	 * worden.
	 * 
	 * @param field
	 * @return key
	 */
	protected final String getKey(Field field)
	{
		return field.getDeclaringClass().getName() + "#" + field.getName();
	}

	/**
	 * Haalt de veldnaam die gecodeeerd is in de key op.
	 * 
	 * @param key
	 *            key verkregen via {@link #getKey(Field)}
	 * @return naam van het veld
	 */
	protected final String getFieldName(String key)
	{
		int index = key.indexOf("#");
		if (index < 0)
			index = 0;
		else
			index++;
		return key.substring(index);
	}

	/**
	 * Haalt de waarde van een veld op.
	 * 
	 * @param field
	 *            key van een veld
	 * @param object
	 *            de entiteit
	 * @return de waarde (mogelijk null)
	 */
	protected Object getPropertyValue(String field, Object object)
	{
		return PropertyResolver.getValue(getFieldName(field), object);
	}

	/**
	 * Slaat een waarde op in een veld.
	 * 
	 * @param field
	 *            key van een veld
	 * @param target
	 *            de entiteit
	 */
	protected void setPropertyValue(String field, Object value, Object target)
	{
		Object valueToSet = value;
		while (valueToSet instanceof IModel< ? >)
			valueToSet = ((IModel< ? >) valueToSet).getObject();
		Object oldValue = PropertyResolver.getValue(getFieldName(field), target);
		if (mustUpdateField(oldValue, valueToSet))
			PropertyResolver.setValue(getFieldName(field), target, valueToSet,
				new PropertyResolverConverter(Application.get().getConverterLocator(), Locale
					.getDefault()));
	}

	private boolean mustUpdateField(Object oldValue, Object newValue)
	{
		if (oldValue instanceof List< ? > && newValue instanceof List< ? >)
			return !JavaUtil.containsSameItemsInOrder((List< ? >) oldValue, (List< ? >) newValue);
		else if (oldValue instanceof Collection< ? > && newValue instanceof Collection< ? >)
			return !JavaUtil.containsSameItems((Collection< ? >) oldValue,
				(Collection< ? >) newValue);
		else
			return !JavaUtil.equalsOrBothNull(oldValue, newValue);
	}

	/**
	 * @see nl.topicus.cobra.modelsv2.ExtendedModel#getManager()
	 */
	public ModelManager getManager()
	{
		return manager;
	}

	/**
	 * @see nl.topicus.cobra.modelsv2.HibernateModel#detach()
	 */
	@Override
	public void detach()
	{
		if (!isAttached())
			return;

		try
		{
			detaching = true;
			attached = false;
			if (attaching)
				throw new ConcurrentModificationException("Trying to detach " + this
					+ " while attaching");

			if ((objectIfIdIsNull instanceof TransientIdObject) && isSaved(objectIfIdIsNull))
			{
				if (id == null)
				{
					id = ((TransientIdObject) objectIfIdIsNull).getIdAsSerializable();

					// Het op null zetten van het temp id veranderd het id van het object.
					// Hierdoor kan de manager hem niet meer vinden in z'n map, waardoor
					// er
					// dubbele models gemaakt worden, waarvan sommige niet gedetached
					// worden.
					// ((TransientIdObject) objectIfIdIsNull).setTemporaryId(null);
					// strict genomen hoeft dit niet omdat de enigste ref naar dit object
					// hier
					// zou moeten zijn, maar in die gevallen waar ook nog iemand anders
					// dit
					// object vasthoud ... (junit test bv)
				}
				persistFields();
				objectIfIdIsNull = detectClass(objectIfIdIsNull);

				if (log.isDebugEnabled())
				{
					log.debug("removed transient object for " + this + ", requestCycle "
						+ RequestCycle.get());
				}
			}
			else if ((objectIfIdIsNull instanceof TransientIdObject))
			{
				persistFields();
			}
			onDetach();
			manager.detach();
		}
		finally
		{
			detaching = false;
		}
	}

	@Override
	public void discardChanges()
	{
		if (isAttached())
		{
			Object obj = getObject();
			detach();
			if (obj instanceof IdObject)
				getObjectAccess().evict(Arrays.asList((IdObject) obj));
		}
		List<IModel< ? >> models = new ArrayList<IModel< ? >>(persistedFields.values());
		persistedFields.clear();
		for (IModel< ? > curModel : models)
		{
			if (curModel instanceof ExtendedModel< ? >)
			{
				((ExtendedModel< ? >) curModel).discardChanges();
			}
		}
	}

	@Override
	protected T load()
	{
		try
		{
			attaching = true;
			if (detaching)
				throw new ConcurrentModificationException("Trying to attach " + this
					+ " while detaching");
			attached = true;
			return super.load();
		}
		finally
		{
			attaching = false;
		}
	}

	@Override
	protected void onAttach()
	{
		setPropertiesOnObject(persistedFields);
	}

	private void setPropertiesOnObject(Map<String, IModel< ? >> fields)
	{
		for (Map.Entry<String, IModel< ? >> entry : fields.entrySet())
			setPropertyValue(entry.getKey(), entry.getValue(), objectIfIdIsNull);
	}

	@Override
	public boolean isAttached()
	{
		return attached;
	}

	@Override
	public void setObject(Object object)
	{
		attached = true;
		super.setObject(object);
	}

	@Override
	protected void onSetObject(Object oldObjectOrClass, Serializable oldId, IdObject newObject)
	{
		for (Entry<String, IModel< ? >> curField : persistedFields.entrySet())
		{
			manager.updateModelObject(new Model<Serializable>(), curField.getValue().getObject(),
				null, null);
			curField.getValue().detach();
		}
		persistedFields.clear();
		manager.updateModelObject(this, oldObjectOrClass, oldId, newObject);
	}

	@Override
	public ObjectState getState()
	{
		return new ExtendedModelState(persistedFields);
	}

	@Override
	public void setState(ObjectState aState)
	{
		// make sure the model is attached
		getObject();
		ExtendedModelState state = (ExtendedModelState) aState;
		setPropertiesOnObject(state.getFieldStore());
	}
}
