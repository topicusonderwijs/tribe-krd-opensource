package nl.topicus.cobra.hibernate;

import org.hibernate.Interceptor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

/**
 * Factory voor het aanmaken van {@link Session} local {@link Interceptor}s. Wordt voor
 * elke session die aangemaakt wordt aangeroepen.
 */
public interface InterceptorFactory
{
	/**
	 * @return een nieuwe {@link Session} local {@link Interceptor}
	 */
	public Interceptor createInterceptor(SessionFactory sessionFactory);
}
