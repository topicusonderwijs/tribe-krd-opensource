package nl.topicus.cobra.hibernate.cache;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.sf.ehcache.hibernate.EhCache;
import nl.topicus.cobra.hibernate.InterceptingSessionFactory;
import nl.topicus.cobra.reflection.Property;

import org.hibernate.SessionFactory;
import org.hibernate.cache.Cache;
import org.hibernate.cache.impl.bridge.BaseRegionAdapter;
import org.hibernate.engine.SessionFactoryImplementor;
import org.hibernate.impl.SessionFactoryImpl;

/**
 * Geeft alle cache statistics terug van {@link EhCache}.
 * 
 * @author hoeve
 */
@SuppressWarnings("deprecation")
public class EhCacheStatisticsFactory extends CacheStatisticsFactory<EhCacheStatistics>
{

	protected EhCacheStatisticsFactory(SessionFactory sessionFactory)
	{
		super(sessionFactory);
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<EhCacheStatistics> getCacheStatistics()
	{
		List<EhCacheStatistics> result = new ArrayList<EhCacheStatistics>();
		Map<String, BaseRegionAdapter> res;

		if (getSessionFactory() instanceof InterceptingSessionFactory)
			res =
				((InterceptingSessionFactory) getSessionFactory()).getAllSecondLevelCacheRegions();
		else if (getSessionFactory() instanceof SessionFactoryImplementor)
			res = ((SessionFactoryImplementor) getSessionFactory()).getAllSecondLevelCacheRegions();
		else
			return result;

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
					/*
					 * Let op: EhCache != Ehcache. (let op de C en c dus).
					 */

					Property<EhCache, Object, net.sf.ehcache.Ehcache> underlyingCacheProperty =
						new Property<EhCache, Object, net.sf.ehcache.Ehcache>(EhCache.class
							.getDeclaredField("cache"));
					net.sf.ehcache.Ehcache underlyingCache =
						underlyingCacheProperty.getValue(underlyingEhCache);

					result.add(new EhCacheStatistics(underlyingCache));
				}
			}
			catch (SecurityException e)
			{
				throw new RuntimeException(e);
			}
			catch (NoSuchFieldException e)
			{
				throw new RuntimeException(e);
			}
		}

		return result;
	}

	@Override
	@SuppressWarnings("unchecked")
	public void clearSecondLevelCache()
	{
		Map< ? , BaseRegionAdapter> caches = null;
		if (getSessionFactory() instanceof InterceptingSessionFactory)
			caches =
				((InterceptingSessionFactory) getSessionFactory()).getAllSecondLevelCacheRegions();

		else if (getSessionFactory() instanceof SessionFactoryImpl)
			caches = ((SessionFactoryImpl) getSessionFactory()).getAllSecondLevelCacheRegions();

		else
			throw new IllegalStateException(
				"Session factory kon niet herkend worden als een session factory die een 2nd level cache kan opleveren");

		for (BaseRegionAdapter cache : caches.values())
			cache.clear();
	}

	@Override
	@SuppressWarnings("unchecked")
	public void clearSecondLevelCache(String regionName)
	{
		Map< ? , BaseRegionAdapter> caches = null;
		if (getSessionFactory() instanceof InterceptingSessionFactory)
			caches =
				((InterceptingSessionFactory) getSessionFactory()).getAllSecondLevelCacheRegions();

		else if (getSessionFactory() instanceof SessionFactoryImpl)
			caches = ((SessionFactoryImpl) getSessionFactory()).getAllSecondLevelCacheRegions();

		else
			throw new IllegalStateException(
				"Session factory kon niet herkend worden als een session factory die een 2nd level cache kan opleveren");

		for (BaseRegionAdapter cache : caches.values())
		{
			if (cache.getName().equals(regionName))
				cache.clear();

		}
	}
}
