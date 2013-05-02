package nl.topicus.cobra.hibernate;

import org.hibernate.SessionFactory;

/**
 * Marker voor objecten die een Hibernate SessionFactory hebben.
 * 
 * @author dashorst
 */
public interface HibernateSessionFactoryAware
{
	public SessionFactory getHibernateSessionFactory();
}
