package nl.topicus.eduarte.tester.hibernate;

import java.io.Serializable;
import java.sql.Connection;
import java.util.Map;
import java.util.Set;

import javax.naming.NamingException;
import javax.naming.Reference;

import org.hibernate.Cache;
import org.hibernate.HibernateException;
import org.hibernate.Interceptor;
import org.hibernate.SessionFactory;
import org.hibernate.StatelessSession;
import org.hibernate.classic.Session;
import org.hibernate.engine.FilterDefinition;
import org.hibernate.metadata.ClassMetadata;
import org.hibernate.metadata.CollectionMetadata;
import org.hibernate.stat.Statistics;

@SuppressWarnings( {"unchecked", "unused"})
public class MockHibernateSessionFactory implements SessionFactory
{
	private static final long serialVersionUID = 1L;

	@Override
	public void close() throws HibernateException
	{
	}

	@Override
	@Deprecated
	public void evict(Class persistentClass) throws HibernateException
	{
	}

	@Override
	@Deprecated
	public void evict(Class persistentClass, Serializable id) throws HibernateException
	{
	}

	@Override
	@Deprecated
	public void evictCollection(String roleName) throws HibernateException
	{
	}

	@Override
	@Deprecated
	public void evictCollection(String roleName, Serializable id) throws HibernateException
	{
	}

	@Override
	@Deprecated
	public void evictEntity(String entityName) throws HibernateException
	{
	}

	@Override
	@Deprecated
	public void evictEntity(String entityName, Serializable id) throws HibernateException
	{
	}

	@Override
	@Deprecated
	public void evictQueries() throws HibernateException
	{
	}

	@Override
	@Deprecated
	public void evictQueries(String cacheRegion) throws HibernateException
	{
	}

	@Override
	public Map getAllClassMetadata() throws HibernateException
	{
		return null;
	}

	@Override
	public Map getAllCollectionMetadata() throws HibernateException
	{
		return null;
	}

	@Override
	public ClassMetadata getClassMetadata(Class persistentClass) throws HibernateException
	{
		return null;
	}

	@Override
	public ClassMetadata getClassMetadata(String entityName) throws HibernateException
	{
		return null;
	}

	@Override
	public CollectionMetadata getCollectionMetadata(String roleName) throws HibernateException
	{
		return null;
	}

	@Override
	public Session getCurrentSession() throws HibernateException
	{
		return null;
	}

	@Override
	public Set getDefinedFilterNames()
	{
		return null;
	}

	@Override
	public FilterDefinition getFilterDefinition(String filterName) throws HibernateException
	{
		return null;
	}

	@Override
	public Statistics getStatistics()
	{
		return null;
	}

	@Override
	public boolean isClosed()
	{
		return false;
	}

	@Override
	public Session openSession() throws HibernateException
	{
		return new MockHibernateSession();
	}

	@Override
	public Session openSession(Connection connection)
	{
		return null;
	}

	@Override
	public Session openSession(Interceptor interceptor) throws HibernateException
	{
		return null;
	}

	@Override
	public Session openSession(Connection connection, Interceptor interceptor)
	{
		return null;
	}

	@Override
	public StatelessSession openStatelessSession()
	{
		return null;
	}

	@Override
	public StatelessSession openStatelessSession(Connection connection)
	{
		return null;
	}

	@Override
	public Reference getReference() throws NamingException
	{
		return null;
	}

	@Override
	public boolean containsFetchProfileDefinition(String name)
	{
		return false;
	}

	@Override
	public Cache getCache()
	{
		return null;
	}
}
