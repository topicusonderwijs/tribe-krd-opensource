package nl.topicus.cobra.hibernate;

import org.hibernate.Session;

/**
 * Marker voor objecten die een Hibernate sessie gezet willen krijgen om daar hun ding mee
 * te kunnen doen. Deze sessie wordt niet automagisch gezet, dus let erop als je deze
 * interface implementeerd dat je dan ook ervoor zorgt dat de factory van jouw
 * <tt>SessionAware</tt> object de sessie zet.
 * 
 * @author dashorst
 */
public interface SessionAware
{
	public void setSession(Session session);
}
