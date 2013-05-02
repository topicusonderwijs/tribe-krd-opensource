package nl.topicus.cobra.hibernate.cache;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import nl.topicus.cobra.hibernate.InterceptingSessionFactory;

import org.hibernate.Hibernate;
import org.hibernate.SessionFactory;
import org.hibernate.cache.impl.bridge.BaseRegionAdapter;
import org.hibernate.impl.SessionFactoryImpl;
import org.hibernate.stat.SecondLevelCacheStatistics;

/**
 * Geeft alle {@link SecondLevelCacheStatistics} terug die {@link Hibernate} kent, let wel
 * dat deze statistics pauper zijn en dat veel waarden 0 zijn.
 * 
 * @author hoeve
 */
public class HibernateCacheStatisticsFactory extends
		CacheStatisticsFactory<HibernateCacheStatistics>
{
	protected HibernateCacheStatisticsFactory(SessionFactory sessionFactory)
	{
		super(sessionFactory);
	}

	@Override
	public List<HibernateCacheStatistics> getCacheStatistics()
	{
		List<HibernateCacheStatistics> res = new ArrayList<HibernateCacheStatistics>(5);

		for (String name : getSessionFactory().getStatistics().getSecondLevelCacheRegionNames())
			res.add(new HibernateCacheStatistics(getSessionFactory().getStatistics()
				.getSecondLevelCacheStatistics(name)));

		return res;
	}

	public List<HibernateCacheStatistics> getCacheStatistics(List<String> regionNames)
	{
		List<HibernateCacheStatistics> res =
			new ArrayList<HibernateCacheStatistics>(regionNames.size());
		for (String curRegion : regionNames)
			res.add(new HibernateCacheStatistics(getSessionFactory().getStatistics()
				.getSecondLevelCacheStatistics(curRegion)));

		return res;
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
