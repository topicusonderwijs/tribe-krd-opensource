package nl.topicus.cobra.hibernate;

import java.io.Serializable;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
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
import org.hibernate.dialect.Dialect;
import org.hibernate.engine.FilterDefinition;
import org.hibernate.engine.SessionFactoryImplementor;
import org.hibernate.impl.SessionFactoryImpl;
import org.hibernate.metadata.ClassMetadata;
import org.hibernate.metadata.CollectionMetadata;
import org.hibernate.stat.Statistics;

/**
 * Hibernate {@link SessionFactory} die optioneel een {@link InterceptorFactory} gebruikt
 * bij het openen van een sessie. Delegeert alle andere methoden direct door naar de
 * <tt>SessionFactory</tt>. Deze <tt>SessionFactory</tt> kan alleen {@link Interceptor}s
 * gebruiken die gemaakt worden door de factory, expliciet meegegeven Interceptor
 * instances bij <tt>openSession</tt> leiden tot een {@link UnsupportedOperationException}
 * .
 * <p>
 * Deze intercepting session factory wordt ge&iuml;nstantieerd door de
 * CobraAnnotationSessionFactoryBean indien er InterceptorFactory modules zijn
 * geregistreerd.
 */
@SuppressWarnings("unchecked")
public class InterceptingSessionFactory implements SessionFactory, InterceptorFactoryAware,
		HibernateSessionFactoryAware
{
	private static final long serialVersionUID = 1L;

	private SessionFactory sessionFactory;

	private List<InterceptorFactory> interceptorFactories = new ArrayList<InterceptorFactory>();

	public InterceptingSessionFactory(SessionFactory sessionFactory)
	{
		this.sessionFactory = sessionFactory;
	}

	public InterceptingSessionFactory(SessionFactory sessionFactory,
			InterceptorFactory interceptorFactory)
	{
		this.sessionFactory = sessionFactory;
		addInterceptorFactory(interceptorFactory);
	}

	public void addInterceptorFactory(InterceptorFactory interceptorFactory)
	{
		this.interceptorFactories.add(interceptorFactory);
	}

	public void addAll(List<InterceptorFactory> factories)
	{
		this.interceptorFactories.addAll(factories);
	}

	public Session openSession() throws HibernateException
	{
		if (!interceptorFactories.isEmpty())
		{
			return interceptedSession(null);
		}
		return sessionFactory.openSession();
	}

	private Session interceptedSession(Connection connection)
	{
		if (interceptorFactories.size() == 1)
		{
			return createSingleInterceptorSession(connection);
		}
		return createMultipleInterceptorSession(connection);
	}

	private Session createSingleInterceptorSession(Connection connection)
	{
		InterceptorFactory factory = interceptorFactories.get(0);
		Interceptor interceptor = factory.createInterceptor(this);
		Session session;
		if (connection == null)
			session = sessionFactory.openSession(interceptor);
		else
			session = sessionFactory.openSession(connection, interceptor);
		registerSession(session, interceptor);
		return session;
	}

	private Session createMultipleInterceptorSession(Connection connection)
	{
		CompoundHibernateInterceptor compound = new CompoundHibernateInterceptor();
		for (InterceptorFactory factory : interceptorFactories)
		{
			compound.addInterceptor(factory.createInterceptor(this));
		}
		Session session;
		if (connection == null)
			session = sessionFactory.openSession(compound);
		else
			session = sessionFactory.openSession(connection, compound);
		for (Interceptor interceptor : compound)
		{
			registerSession(session, interceptor);
		}
		return session;
	}

	private void registerSession(Session session, Interceptor interceptor)
	{
		if (interceptor instanceof SessionAware)
		{
			((SessionAware) interceptor).setSession(session);
		}
	}

	public Session openSession(Connection connection, Interceptor interceptor)
	{
		throw new UnsupportedOperationException(
			"Session openen met een andere interceptor dan die van de InterceptorFactory is niet correct.");
	}

	public Session openSession(Connection connection)
	{
		if (!interceptorFactories.isEmpty())
		{
			return interceptedSession(connection);
		}
		return sessionFactory.openSession(connection);
	}

	public Session openSession(Interceptor interceptor) throws HibernateException
	{
		throw new UnsupportedOperationException(
			"Session openen met een andere interceptor dan die van de InterceptorFactory is niet correct.");
	}

	public void close() throws HibernateException
	{
		sessionFactory.close();
	}

	@Deprecated
	public void evict(Class persistentClass, Serializable id) throws HibernateException
	{
		sessionFactory.evict(persistentClass, id);
	}

	@Deprecated
	public void evict(Class persistentClass) throws HibernateException
	{
		sessionFactory.evict(persistentClass);
	}

	@Deprecated
	public void evictCollection(String roleName, Serializable id) throws HibernateException
	{
		sessionFactory.evictCollection(roleName, id);
	}

	@Deprecated
	public void evictCollection(String roleName) throws HibernateException
	{
		sessionFactory.evictCollection(roleName);
	}

	@Deprecated
	public void evictEntity(String entityName, Serializable id) throws HibernateException
	{
		sessionFactory.evictEntity(entityName, id);
	}

	@Deprecated
	public void evictEntity(String entityName) throws HibernateException
	{
		sessionFactory.evictEntity(entityName);
	}

	@Deprecated
	public void evictQueries() throws HibernateException
	{
		sessionFactory.evictQueries();
	}

	@Deprecated
	public void evictQueries(String cacheRegion) throws HibernateException
	{
		sessionFactory.evictQueries(cacheRegion);
	}

	public Map getAllClassMetadata() throws HibernateException
	{
		return sessionFactory.getAllClassMetadata();
	}

	public Map getAllCollectionMetadata() throws HibernateException
	{
		return sessionFactory.getAllCollectionMetadata();
	}

	public ClassMetadata getClassMetadata(Class persistentClass) throws HibernateException
	{
		return sessionFactory.getClassMetadata(persistentClass);
	}

	public ClassMetadata getClassMetadata(String entityName) throws HibernateException
	{
		return sessionFactory.getClassMetadata(entityName);
	}

	public CollectionMetadata getCollectionMetadata(String roleName) throws HibernateException
	{
		return sessionFactory.getCollectionMetadata(roleName);
	}

	public Session getCurrentSession() throws HibernateException
	{
		return sessionFactory.getCurrentSession();
	}

	public Set getDefinedFilterNames()
	{
		return sessionFactory.getDefinedFilterNames();
	}

	public FilterDefinition getFilterDefinition(String filterName) throws HibernateException
	{
		return sessionFactory.getFilterDefinition(filterName);
	}

	public Reference getReference() throws NamingException
	{
		return sessionFactory.getReference();
	}

	public Statistics getStatistics()
	{
		return sessionFactory.getStatistics();
	}

	public boolean isClosed()
	{
		return sessionFactory.isClosed();
	}

	public StatelessSession openStatelessSession()
	{
		return sessionFactory.openStatelessSession();
	}

	public StatelessSession openStatelessSession(Connection connection)
	{
		return sessionFactory.openStatelessSession(connection);
	}

	@Override
	public boolean containsFetchProfileDefinition(String name)
	{
		return sessionFactory.containsFetchProfileDefinition(name);
	}

	@Override
	public Cache getCache()
	{
		return sessionFactory.getCache();
	}

	public Dialect getDialect()
	{
		if (sessionFactory instanceof SessionFactoryImpl)
			return ((SessionFactoryImpl) sessionFactory).getDialect();

		return null;
	}

	public Map getAllSecondLevelCacheRegions()
	{
		if (sessionFactory instanceof SessionFactoryImplementor)
			return ((SessionFactoryImplementor) sessionFactory).getAllSecondLevelCacheRegions();

		return null;
	}

	@Override
	public SessionFactory getHibernateSessionFactory()
	{
		if (sessionFactory instanceof HibernateSessionFactoryAware)
			((HibernateSessionFactoryAware) sessionFactory).getHibernateSessionFactory();

		return sessionFactory;
	}
}
