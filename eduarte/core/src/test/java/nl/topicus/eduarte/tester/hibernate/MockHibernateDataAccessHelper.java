package nl.topicus.eduarte.tester.hibernate;

import java.io.Serializable;
import java.util.List;

import nl.topicus.cobra.dao.hibernate.HibernateDataAccessHelper;
import nl.topicus.cobra.dao.hibernate.HibernateSessionProvider;
import nl.topicus.cobra.dao.hibernate.QueryInterceptor;
import nl.topicus.cobra.entities.IdObject;

public class MockHibernateDataAccessHelper extends HibernateDataAccessHelper<Object>
{
	private final MockDatabase database = new MockDatabase();

	public MockHibernateDataAccessHelper(HibernateSessionProvider provider,
			QueryInterceptor interceptor)
	{
		super(provider, interceptor);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <R> R get(Class<R> class1, Serializable id)
	{
		return (R) database.get((Class< ? extends IdObject>) class1, id);
	}

	@Override
	public <R> R unproxy(R possibleProxy)
	{
		return possibleProxy;
	}

	/**
	 * @deprecated Use {@link #delete(Object)} instead
	 */
	@Deprecated
	@Override
	public void batchDelete(Object dataObject)
	{
		delete(dataObject);
	}

	@Override
	public void delete(Object dataObject)
	{
		database.delete((IdObject) dataObject);
	}

	@Override
	public void batchExecute()
	{
		database.commit();
	}

	@Override
	public void batchRollback()
	{
		database.rollback();
	}

	/**
	 * @deprecated Use {@link #save(Object)} instead
	 */
	@Deprecated
	@Override
	public Serializable batchSave(Object dataObject)
	{
		return save(dataObject);
	}

	@Override
	public Serializable save(Object dataObject)
	{
		IdObject object = (IdObject) dataObject;
		database.insert(object);
		return object.getIdAsSerializable();
	}

	/**
	 * @deprecated Use {@link #saveOrUpdate(Object)} instead
	 */
	@Deprecated
	@Override
	public void batchSaveOrUpdate(Object dataObject)
	{
		saveOrUpdate(dataObject);
	}

	@Override
	public void saveOrUpdate(Object dataObject)
	{
		IdObject object = (IdObject) dataObject;
		if (object.getIdAsSerializable() == null)
		{
			database.insert(object);
		}
		else
		{
			database.update(object);
		}
	}

	public List<IdObject> getObjectsFromTransaction()
	{
		return database.getObjectsFromTransaction();
	}

	public List<DatabaseAction> getTransactionLog()
	{
		return database.getTransactionLog();
	}

	public void clearTransactionLog()
	{
		database.clearTransactionLog();
	}
}