/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.cobra.modelsv2;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.List;

import nl.topicus.cobra.entities.FieldPersistenceMode;
import nl.topicus.cobra.entities.IdObject;
import nl.topicus.cobra.entities.TransientIdObject;

import org.apache.wicket.model.IDetachable;
import org.apache.wicket.model.IModel;

/**
 * The {@code ModelManager} interface keeps track of models, so we don't create duplicate
 * models for one object. A {@code ModelManager} is also responsible for determining which
 * classes need to be managed. The difference between a managed class and an unmanaged
 * class is, that instances of managed classes can be modified, whereas instances of
 * unmanaged classes cannot.
 * 
 * @author marrink, papegaaij
 */
public interface ModelManager extends IDetachable
{
	/**
	 * Returns a model for the given object.
	 * 
	 * @param object
	 *            The object to create the model for.
	 * @param field
	 *            The field this object is referenced by, or null if it is the root
	 *            object. Passing null will disable the effects of
	 *            {@link FieldPersistenceMode#SAVE}, acting as if it were
	 *            {@link FieldPersistenceMode#SAVE_AND_FOLLOW}.
	 * @return The model.
	 */
	public <T> IModel<T> getModel(T object, Field field);

	/**
	 * Creates a model capable of recording changes to the object.
	 * 
	 * @param <T>
	 *            The type of the object.
	 * @param object
	 *            The object to create the model for.
	 * @return The model.
	 */
	public <T extends TransientIdObject> IChangeRecordingModel<T> getChangeRecordingModel(T object);

	/**
	 * Returns the {@link FieldPersistanceFilter} used by this manager.
	 * 
	 * @return The filter.
	 */
	public FieldPersistanceFilter getFilter();

	/**
	 * Returns true when the given value is managed by this manager.
	 * 
	 * @param fieldValue
	 * @param field
	 *            The field this object is referenced by, or null if it is the root
	 *            object.
	 * @return True when the object is managed.
	 */
	public boolean isManaged(Object fieldValue, Field field);

	/**
	 * Returns a list of classes managed by this manager.
	 * 
	 * @return The managed classes.
	 */
	public List< ? extends Class< ? extends TransientIdObject>> getManagedClasses();

	/**
	 * Callback method for models to notify the manager about changed ids. This allows the
	 * manager to update the model cache. This method should normally not be used.
	 * 
	 * @param model
	 * @param oldObjectOrClass
	 * @param oldId
	 * @param newObject
	 */
	public void updateModelObject(IModel< ? > model, Object oldObjectOrClass, Serializable oldId,
			IdObject newObject);
}
