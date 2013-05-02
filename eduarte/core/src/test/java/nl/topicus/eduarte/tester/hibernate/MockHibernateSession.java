package nl.topicus.eduarte.tester.hibernate;

import java.io.Serializable;
import java.sql.Connection;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.hibernate.*;
import org.hibernate.jdbc.Work;
import org.hibernate.stat.SessionStatistics;
import org.hibernate.type.Type;

@SuppressWarnings( {"unchecked", "deprecation"})
public class MockHibernateSession implements Session, org.hibernate.classic.Session
{
	private static final long serialVersionUID = 1L;

	@Override
	public Transaction beginTransaction() throws HibernateException
	{
		return null;
	}

	@Override
	public void cancelQuery() throws HibernateException
	{
	}

	@Override
	public void clear()
	{
	}

	@Override
	public Connection close() throws HibernateException
	{
		return null;
	}

	@Override
	public Connection connection() throws HibernateException
	{
		return null;
	}

	@Override
	public boolean contains(Object object)
	{
		return false;
	}

	@Override
	public Criteria createCriteria(Class persistentClass)
	{
		return null;
	}

	@Override
	public Criteria createCriteria(String entityName)
	{
		return null;
	}

	@Override
	public Criteria createCriteria(Class persistentClass, String alias)
	{
		return null;
	}

	@Override
	public Criteria createCriteria(String entityName, String alias)
	{
		return null;
	}

	@Override
	public Query createFilter(Object collection, String queryString) throws HibernateException
	{
		return null;
	}

	@Override
	public Query createQuery(String queryString) throws HibernateException
	{
		return null;
	}

	@Override
	public SQLQuery createSQLQuery(String queryString) throws HibernateException
	{
		return null;
	}

	@Override
	public void delete(Object object) throws HibernateException
	{
	}

	@Override
	public void delete(String entityName, Object object) throws HibernateException
	{
	}

	@Override
	public void disableFilter(String filterName)
	{
	}

	@Override
	public Connection disconnect() throws HibernateException
	{
		return null;
	}

	@Override
	public void doWork(Work work) throws HibernateException
	{
	}

	@Override
	public Filter enableFilter(String filterName)
	{
		return null;
	}

	@Override
	public void evict(Object object) throws HibernateException
	{
	}

	@Override
	public void flush() throws HibernateException
	{
	}

	@Override
	public Object get(Class clazz, Serializable id) throws HibernateException
	{
		return null;
	}

	@Override
	public Object get(String entityName, Serializable id) throws HibernateException
	{
		return null;
	}

	@Override
	public Object get(Class clazz, Serializable id, LockMode lockMode) throws HibernateException
	{
		return null;
	}

	@Override
	public Object get(String entityName, Serializable id, LockMode lockMode)
			throws HibernateException
	{
		return null;
	}

	@Override
	public CacheMode getCacheMode()
	{
		return null;
	}

	@Override
	public LockMode getCurrentLockMode(Object object) throws HibernateException
	{
		return null;
	}

	@Override
	public Filter getEnabledFilter(String filterName)
	{
		return null;
	}

	@Override
	public EntityMode getEntityMode()
	{
		return null;
	}

	@Override
	public String getEntityName(Object object) throws HibernateException
	{
		return null;
	}

	@Override
	public FlushMode getFlushMode()
	{
		return null;
	}

	@Override
	public Serializable getIdentifier(Object object) throws HibernateException
	{
		return null;
	}

	@Override
	public Query getNamedQuery(String queryName) throws HibernateException
	{
		return null;
	}

	@Override
	public Session getSession(EntityMode entityMode)
	{
		return null;
	}

	@Override
	public SessionFactory getSessionFactory()
	{
		return null;
	}

	@Override
	public SessionStatistics getStatistics()
	{
		return null;
	}

	@Override
	public Transaction getTransaction()
	{
		return null;
	}

	@Override
	public boolean isConnected()
	{
		return true;
	}

	@Override
	public boolean isDirty() throws HibernateException
	{
		return false;
	}

	@Override
	public boolean isOpen()
	{
		return true;
	}

	@Override
	public Object load(Class theClass, Serializable id) throws HibernateException
	{
		return null;
	}

	@Override
	public Object load(String entityName, Serializable id) throws HibernateException
	{
		return null;
	}

	@Override
	public void load(Object object, Serializable id) throws HibernateException
	{
	}

	@Override
	public Object load(Class theClass, Serializable id, LockMode lockMode)
			throws HibernateException
	{
		return null;
	}

	@Override
	public Object load(String entityName, Serializable id, LockMode lockMode)
			throws HibernateException
	{
		return null;
	}

	@Override
	public void lock(Object object, LockMode lockMode) throws HibernateException
	{
	}

	@Override
	public void lock(String entityName, Object object, LockMode lockMode) throws HibernateException
	{
	}

	@Override
	public Object merge(Object object) throws HibernateException
	{
		return null;
	}

	@Override
	public Object merge(String entityName, Object object) throws HibernateException
	{
		return null;
	}

	@Override
	public void persist(Object object) throws HibernateException
	{
	}

	@Override
	public void persist(String entityName, Object object) throws HibernateException
	{
	}

	@Override
	public void reconnect() throws HibernateException
	{
	}

	@Override
	public void reconnect(Connection connection) throws HibernateException
	{
	}

	@Override
	public void refresh(Object object) throws HibernateException
	{
	}

	@Override
	public void refresh(Object object, LockMode lockMode) throws HibernateException
	{
	}

	@Override
	public void replicate(Object object, ReplicationMode replicationMode) throws HibernateException
	{
	}

	@Override
	public void replicate(String entityName, Object object, ReplicationMode replicationMode)
			throws HibernateException
	{
	}

	@Override
	public Serializable save(Object object) throws HibernateException
	{
		return null;
	}

	@Override
	public Serializable save(String entityName, Object object) throws HibernateException
	{
		return null;
	}

	@Override
	public void saveOrUpdate(Object object) throws HibernateException
	{
	}

	@Override
	public void saveOrUpdate(String entityName, Object object) throws HibernateException
	{
	}

	@Override
	public void setCacheMode(CacheMode cacheMode)
	{
	}

	@Override
	public void setFlushMode(FlushMode flushMode)
	{
	}

	@Override
	public void setReadOnly(Object entity, boolean readOnly)
	{
	}

	@Override
	public void update(Object object) throws HibernateException
	{
	}

	@Override
	public void update(String entityName, Object object) throws HibernateException
	{
	}

	@Override
	public Query createSQLQuery(String sql, String returnAlias, Class returnClass)
	{
		return null;
	}

	@Override
	public Query createSQLQuery(String sql, String[] returnAliases, Class[] returnClasses)
	{
		return null;
	}

	@Override
	public int delete(String query) throws HibernateException
	{
		return 0;
	}

	@Override
	public int delete(String query, Object value, Type type) throws HibernateException
	{
		return 0;
	}

	@Override
	public int delete(String query, Object[] values, Type[] types) throws HibernateException
	{
		return 0;
	}

	@Override
	public Collection filter(Object collection, String filter) throws HibernateException
	{
		return null;
	}

	@Override
	public Collection filter(Object collection, String filter, Object value, Type type)
			throws HibernateException
	{
		return null;
	}

	@Override
	public Collection filter(Object collection, String filter, Object[] values, Type[] types)
			throws HibernateException
	{
		return null;
	}

	@Override
	public List find(String query) throws HibernateException
	{
		return null;
	}

	@Override
	public List find(String query, Object value, Type type) throws HibernateException
	{
		return null;
	}

	@Override
	public List find(String query, Object[] values, Type[] types) throws HibernateException
	{
		return null;
	}

	@Override
	public Iterator iterate(String query) throws HibernateException
	{
		return null;
	}

	@Override
	public Iterator iterate(String query, Object value, Type type) throws HibernateException
	{
		return null;
	}

	@Override
	public Iterator iterate(String query, Object[] values, Type[] types) throws HibernateException
	{
		return null;
	}

	@Override
	public void save(Object object, Serializable id) throws HibernateException
	{
	}

	@Override
	public void save(String entityName, Object object, Serializable id) throws HibernateException
	{
	}

	@Override
	public Object saveOrUpdateCopy(Object object) throws HibernateException
	{
		return null;
	}

	@Override
	public Object saveOrUpdateCopy(Object object, Serializable id) throws HibernateException
	{
		return null;
	}

	@Override
	public Object saveOrUpdateCopy(String entityName, Object object) throws HibernateException
	{
		return null;
	}

	@Override
	public Object saveOrUpdateCopy(String entityName, Object object, Serializable id)
			throws HibernateException
	{
		return null;
	}

	@Override
	public void update(Object object, Serializable id) throws HibernateException
	{
	}

	@Override
	public void update(String entityName, Object object, Serializable id) throws HibernateException
	{
	}

	@Override
	public LockRequest buildLockRequest(LockOptions lockOptions)
	{
		return null;
	}

	@Override
	public void disableFetchProfile(String name) throws UnknownProfileException
	{
	}

	@Override
	public void enableFetchProfile(String name) throws UnknownProfileException
	{
	}

	@Override
	public Object get(Class clazz, Serializable id, LockOptions lockOptions)
			throws HibernateException
	{
		return null;
	}

	@Override
	public Object get(String entityName, Serializable id, LockOptions lockOptions)
			throws HibernateException
	{
		return null;
	}

	@Override
	public boolean isDefaultReadOnly()
	{
		return false;
	}

	@Override
	public boolean isFetchProfileEnabled(String name) throws UnknownProfileException
	{
		return false;
	}

	@Override
	public boolean isReadOnly(Object entityOrProxy)
	{
		return false;
	}

	@Override
	public Object load(Class theClass, Serializable id, LockOptions lockOptions)
			throws HibernateException
	{
		return null;
	}

	@Override
	public Object load(String entityName, Serializable id, LockOptions lockOptions)
			throws HibernateException
	{
		return null;
	}

	@Override
	public void refresh(Object object, LockOptions lockOptions) throws HibernateException
	{
	}

	@Override
	public void setDefaultReadOnly(boolean readOnly)
	{
	}
}
