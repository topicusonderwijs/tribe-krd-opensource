package nl.topicus.cobra.modelsv2;

import java.io.Serializable;
import java.util.Collection;

import nl.topicus.cobra.entities.IdObject;
import nl.topicus.cobra.entities.TransientIdObject;

import org.apache.wicket.model.BoundCompoundPropertyModel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IComponentInheritedModel;
import org.apache.wicket.model.IModel;

/**
 * Factory class voor het aanmaken van veelgebruikte model sequenties.
 * 
 * @author Martijn Dashorst
 */
@SuppressWarnings("deprecation")
public class ModelFactory
{
	/**
	 * maakt vergelijkingen mogelijk voor hibernate models die in een
	 * compoundpropertymodel zijn genest. hierdoor wordt het mogelijk om in die situatie
	 * DataView.setItemReuseStrategy() te gebruiken
	 */
	private static class ExtendedCompoundHibernateModelWrapper<T> extends
			CompoundHibernateModelWrapper<T> implements ExtendedModel<T>
	{
		private static final long serialVersionUID = 1L;

		private ExtendedCompoundHibernateModelWrapper(ExtendedModel<T> arg0)
		{
			super(arg0);
		}

		@SuppressWarnings("unchecked")
		@Override
		public ModelManager getManager()
		{
			return ((ExtendedModel<T>) getChainedModel()).getManager();
		}

		@SuppressWarnings("unchecked")
		@Override
		public void discardChanges()
		{
			((ExtendedModel<T>) getChainedModel()).discardChanges();
		}

		@SuppressWarnings("unchecked")
		@Override
		public boolean equals(Object object)
		{
			return ((ExtendedModel<T>) getChainedModel()).equals(object);
		}

		@Override
		@SuppressWarnings("unchecked")
		public ObjectState getState()
		{
			return ((ExtendedModel<T>) getChainedModel()).getState();
		}

		@Override
		@SuppressWarnings("unchecked")
		public void setState(ObjectState state)
		{
			((ExtendedModel<T>) getChainedModel()).setState(state);
		}

		@Override
		@SuppressWarnings("unchecked")
		public boolean isAttached()
		{
			return ((ExtendedModel<T>) getChainedModel()).isAttached();
		}
	}

	/**
	 * maakt vergelijkingen mogelijk voor hibernate models die in een
	 * compoundpropertymodel zijn genest. hierdoor wordt het mogelijk om in die situatie
	 * DataView.setItemReuseStrategy() te gebruiken
	 */
	private static class ExtendedCompoundHibernateChangeRecordingModelWrapper<T extends TransientIdObject>
			extends ExtendedCompoundHibernateModelWrapper<T> implements IChangeRecordingModel<T>
	{
		private static final long serialVersionUID = 1L;

		private ExtendedCompoundHibernateChangeRecordingModelWrapper(IChangeRecordingModel<T> arg0)
		{
			super(arg0);
		}

		@Override
		@SuppressWarnings("unchecked")
		public void doNotDelete(IdObject object)
		{
			((IChangeRecordingModel<T>) getChainedModel()).doNotDelete(object);
		}

		@Override
		@SuppressWarnings("unchecked")
		public void recalculate()
		{
			((IChangeRecordingModel<T>) getChainedModel()).recalculate();
		}

		@Override
		@SuppressWarnings("unchecked")
		public void deleteObject(IModificationCallback... callback)
		{
			((IChangeRecordingModel<T>) getChainedModel()).deleteObject(callback);
		}

		@Override
		@SuppressWarnings("unchecked")
		public void saveObject(IModificationCallback... callback)
		{
			((IChangeRecordingModel<T>) getChainedModel()).saveObject(callback);
		}
	}

	/**
	 * maakt vergelijkingen mogelijk voor hibernate models die in een
	 * compoundpropertymodel zijn genest. hierdoor wordt het mogelijk om in die situatie
	 * DataView.setItemReuseStrategy() te gebruiken
	 */
	private static class CompoundHibernateModelWrapper<T> extends CompoundPropertyModel<T>
	{
		private static final long serialVersionUID = 1L;

		private CompoundHibernateModelWrapper(Object arg0)
		{
			super(arg0);
		}

		/**
		 * @see java.lang.Object#hashCode()
		 */
		@Override
		public int hashCode()
		{
			return getChainedModel().hashCode();
		}

		/**
		 * @see java.lang.Object#equals(java.lang.Object)
		 */
		@SuppressWarnings("unchecked")
		@Override
		public boolean equals(Object object)
		{
			if (object instanceof CompoundHibernateModelWrapper)
			{
				CompoundHibernateModelWrapper model = (CompoundHibernateModelWrapper) object;
				return getChainedModel().equals(model.getChainedModel());
			}
			return false;
		}

		@Override
		public T getObject()
		{
			return super.getObject();
		}
	}

	/**
	 * maakt vergelijkingen mogelijk voor hibernate models die in een
	 * compoundpropertymodel zijn genest. hierdoor wordt het mogelijk om in die situatie
	 * DataView.setItemReuseStrategy() te gebruiken
	 */
	@Deprecated
	private static final class BoundCompoundHibernateModelWrapper<T> extends
			BoundCompoundPropertyModel<T>
	{
		private static final long serialVersionUID = 1L;

		@Deprecated
		private BoundCompoundHibernateModelWrapper(Object arg0)
		{
			super(arg0);
		}

		/**
		 * @see java.lang.Object#hashCode()
		 */
		@Override
		public int hashCode()
		{
			return getChainedModel().hashCode();
		}

		/**
		 * @see java.lang.Object#equals(java.lang.Object)
		 */
		@SuppressWarnings("unchecked")
		@Override
		public boolean equals(Object object)
		{
			if (object instanceof BoundCompoundHibernateModelWrapper< ? >)
			{
				BoundCompoundHibernateModelWrapper<T> model =
					(BoundCompoundHibernateModelWrapper<T>) object;
				return getChainedModel().equals(model.getChainedModel());
			}
			return false;
		}
	}

	/**
	 * Een compound model waarvan de equals en hashcode doorgelinkt zijn naar het geneste
	 * model.
	 * 
	 * @param model
	 *            willekeurig IModel (geen compound of propertymodel).
	 * @return het model
	 */
	public static <T> IModel<T> getCompoundModelForModel(IModel<T> model)
	{
		return new CompoundHibernateModelWrapper<T>(model);
	}

	/**
	 * Geeft een unproxied, hibernate, compoundproperty model terug.
	 * 
	 * @param <T>
	 * @param modelObject
	 *            het object waarvoor een model gebakken moet worden
	 * @return het model
	 */
	public static <T extends IdObject> IModel<T> getCompoundModel(T modelObject)
	{
		return new CompoundHibernateModelWrapper<T>(new HibernateModel<T>(modelObject));
	}

	public static <T> IModel<T> getCompoundModelForObject(T modelObject)
	{
		return new CompoundHibernateModelWrapper<T>(new HibernateModel<T>(modelObject));
	}

	/**
	 * Geeft een bound compound property model terug.
	 * 
	 * @param <T>
	 * @param modelObject
	 * @return het model
	 */
	@Deprecated
	public static <T> BoundCompoundPropertyModel<T> getBoundCompoundModel(T modelObject)
	{
		return new BoundCompoundHibernateModelWrapper<T>(new HibernateModel<T>(modelObject));

	}

	/**
	 * CompoundModel met daarin een model dat de wijzigen op het hoofdobject kan bijhouden
	 * ook na een detach operatie.
	 * 
	 * @param modelObject
	 * @param manager
	 * @return model
	 */
	public static <T> IComponentInheritedModel<T> getCompoundModel(T modelObject,
			ModelManager manager)
	{
		IModel<T> rootModel = manager.getModel(modelObject, null);
		if (rootModel instanceof ExtendedModel< ? >)
		{
			return new ExtendedCompoundHibernateModelWrapper<T>((ExtendedModel<T>) rootModel);
		}
		return new CompoundHibernateModelWrapper<T>(rootModel);
	}

	/**
	 * CompoundModel met daarin een model dat de wijzigen op het hoofdobject kan bijhouden
	 * ook na een detach operatie. Dit model kan over meerdere attache-detach cycles het
	 * complete object (inclusief onderliggende objecten) updaten in de database.
	 * 
	 * @param <T>
	 * @param modelObject
	 * @param manager
	 * @return model
	 */
	public static <T extends TransientIdObject> IChangeRecordingModel<T> getCompoundChangeRecordingModel(
			T modelObject, ModelManager manager)
	{
		IChangeRecordingModel<T> rootModel = manager.getChangeRecordingModel(modelObject);
		return new ExtendedCompoundHibernateChangeRecordingModelWrapper<T>(rootModel);
	}

	/**
	 * Model dat de wijzigen op het hoofdobject kan bijhouden ook na een detach operatie.
	 * 
	 * @param <T>
	 * @param modelObject
	 * @param manager
	 * @return model
	 */
	public static <T> IModel<T> getModel(T modelObject, ModelManager manager)
	{
		return manager.getModel(modelObject, null);
	}

	/**
	 * Model dat de wijzigen op het hoofdobject kan bijhouden ook na een detach operatie.
	 * 
	 * @param <T>
	 * @param modelListObject
	 * @param manager
	 * @return model
	 */
	public static <T, Z extends Collection<T>> IModel<Z> getListModel(Z modelListObject,
			ModelManager manager)
	{
		return manager.getModel(modelListObject, null);
	}

	/**
	 * Model dat de wijzigen op het hoofdobject kan bijhouden ook na een detach operatie.
	 * 
	 * @param <T>
	 * @param modelObject
	 * @param filter
	 * @param managedEntities
	 * @return model
	 */
	public static <T> IModel<T> getModel(T modelObject, FieldPersistanceFilter filter,
			Class< ? >... managedEntities)
	{
		return new DefaultModelManager(filter, managedEntities).getModel(modelObject, null);
	}

	/**
	 * List model dat de wijzigen op het hoofdobject kan bijhouden ook na een detach
	 * operatie.
	 * 
	 * @param <T>
	 * @param modelListObject
	 * @param filter
	 * @param managedEntities
	 * @return model
	 */
	public static <T extends TransientIdObject, Z extends Collection<T>> IModel<Z> getListModel(
			Z modelListObject, FieldPersistanceFilter filter, Class< ? >... managedEntities)
	{
		return new DefaultModelManager(filter, managedEntities).getModel(modelListObject, null);
	}

	/**
	 * Geeft een unproxied, hibernate model terug.
	 * 
	 * @param <T>
	 * @param modelObject
	 *            het object waarvoor een model gebakken moet worden
	 * @return het model
	 */
	public static <T> IModel<T> getModel(T modelObject)
	{
		return new HibernateModel<T>(modelObject);
	}

	/**
	 * Geeft een unproxied, hibernate model terug.
	 * 
	 * @param <T>
	 * @param clazz
	 *            de klasse van de entiteit
	 * @param id
	 *            de identifier van de entiteit
	 * @return het model
	 */
	public static <T> IModel<T> getModel(Class< ? extends T> clazz, Serializable id)
	{
		return new HibernateModel<T>(clazz, id);
	}

	/**
	 * @param <T>
	 * @param list
	 * @return Een model voor de gegeven list.
	 */
	public static <T, Z extends Collection<T>> IModel<Z> getListModel(Z list)
	{
		return new HibernateObjectListModel<Z, T>(list);
	}
}
