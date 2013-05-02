package nl.topicus.cobra.hibernate.cache;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.ehcache.hibernate.EhCache;
import nl.topicus.cobra.hibernate.HibernateSessionFactoryAware;
import nl.topicus.cobra.hibernate.InterceptingSessionFactory;
import nl.topicus.cobra.reflection.Property;

import org.hibernate.SessionFactory;
import org.hibernate.cache.Cache;
import org.hibernate.cache.impl.bridge.BaseRegionAdapter;
import org.hibernate.impl.SessionFactoryImpl;

/**
 * Factory voor het ophalen van second level cache statistics. Kan de statistics van de
 * cache implementatie ophalen maar ook die van hibernate (is pauper).
 * 
 * @author hoeve
 */
@SuppressWarnings("deprecation")
public abstract class CacheStatisticsFactory<T extends CacheStatistics>
{
	@SuppressWarnings("unchecked")
	public static CacheStatisticsFactory< ? extends CacheStatistics> getCacheStatisticsFactory(
			HibernateSessionFactoryAware awareobject)
	{
		Map<String, BaseRegionAdapter> res;

		if (awareobject.getHibernateSessionFactory() instanceof InterceptingSessionFactory)
			res =
				((InterceptingSessionFactory) awareobject.getHibernateSessionFactory())
					.getAllSecondLevelCacheRegions();
		else if (awareobject.getHibernateSessionFactory() instanceof SessionFactoryImpl)
			res =
				((SessionFactoryImpl) awareobject.getHibernateSessionFactory())
					.getAllSecondLevelCacheRegions();
		else
			res = new HashMap<String, BaseRegionAdapter>();

		for (String key : res.keySet())
		{
			BaseRegionAdapter region = res.get(key);

			try
			{
				Property<BaseRegionAdapter, Object, Cache> underlyingEhCacheProperty =
					new Property<BaseRegionAdapter, Object, Cache>(BaseRegionAdapter.class
						.getDeclaredField("underlyingCache"));

				Cache underlyingEhCache = underlyingEhCacheProperty.getValue(region);
				if (underlyingEhCache instanceof EhCache)
				{
					return new EhCacheStatisticsFactory(awareobject.getHibernateSessionFactory());
				}
				/**
				 * Voeg hier eventuele andere factories toe wanneer je geen EhCache
				 * gebruikt.
				 */
			}
			catch (SecurityException e)
			{
				throw new RuntimeException(
					"Kan niet bepalen welke cache implementatie gebruikt wordt.", e);
			}
			catch (NoSuchFieldException e)
			{
				throw new RuntimeException(
					"Kan niet bepalen welke cache implementatie gebruikt wordt.", e);
			}
		}

		throw new RuntimeException(
			"Kan niet bepalen welke cache implementatie gebruikt wordt, kan geen regions vinden.");
	}

	public static HibernateCacheStatisticsFactory getHibernateCacheStatisticsFactory(
			HibernateSessionFactoryAware awareobject)
	{
		return new HibernateCacheStatisticsFactory(awareobject.getHibernateSessionFactory());
	}

	private SessionFactory sessionFactory;

	protected CacheStatisticsFactory(SessionFactory sessionFactory)
	{
		this.sessionFactory = sessionFactory;
	}

	protected SessionFactory getSessionFactory()
	{
		return sessionFactory;
	}

	/**
	 * @return Een lijst met statistieken van alle 2nd level caches die gebruikt worden
	 *         door de applicatie
	 */
	public abstract List<T> getCacheStatistics();

	/**
	 * Verwijdert alle entries uit de gegeven 2nd level cache.
	 */
	public abstract void clearSecondLevelCache();

	/**
	 * Verwijdert alle entries uit de meegegeven 2nd level cache.
	 * 
	 * @param regionName
	 */
	public abstract void clearSecondLevelCache(String regionName);
}
