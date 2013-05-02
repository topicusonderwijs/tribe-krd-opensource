package nl.topicus.cobra.hibernate.cache;

import java.io.Serializable;

import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Statistics;

/**
 * @author hoeve
 */
public class EhCacheStatistics implements Serializable, CacheStatistics
{
	private static final long serialVersionUID = 1L;

	private transient Statistics statistics;

	private transient Ehcache ehcache;

	public EhCacheStatistics(Ehcache ehcache)
	{
		this.ehcache = ehcache;
		this.statistics = ehcache.getStatistics();
	}

	@Override
	public long getCacheHits()
	{
		return statistics.getCacheHits();
	}

	@Override
	public long getCacheHitsInMemory()
	{
		return statistics.getInMemoryHits();
	}

	@Override
	public long getCacheHitsOnDisk()
	{
		return statistics.getOnDiskHits();
	}

	@Override
	public long getCacheMisses()
	{
		return statistics.getCacheMisses();
	}

	@Override
	public long getDiskStoreCount()
	{
		return ehcache.getDiskStoreSize();
	}

	@Override
	public long getDiskStoreSize()
	{
		return -1L;
	}

	@Override
	public long getMaxElementsInMemory()
	{
		return ehcache.getMaxElementsInMemory();
	}

	@Override
	public long getMaxElementsOnDisk()
	{
		return ehcache.getMaxElementsOnDisk();
	}

	@Override
	public long getMemoryStoreCount()
	{
		return ehcache.getMemoryStoreSize();
	}

	@Override
	public long getMemoryStoreSize()
	{
		return ehcache.calculateInMemorySize();
	}

	public String getName()
	{
		return statistics.getAssociatedCacheName();
	}

	@Override
	public STATISTICS_ACCURACY getStatisticsAccuracy()
	{
		return STATISTICS_ACCURACY.values()[(int) statistics.getStatisticsAccuracy()];
	}

	@Override
	public String toString()
	{
		StringBuffer buf =
			new StringBuffer().append("EhCacheStatistics").append("[cacheHits=").append(
				getCacheHits()).append(",cacheMisses=").append(getCacheMisses()).append(
				",cacheHitsInMemory=").append(getCacheHitsInMemory()).append(",cacheHitsOnDisk=")
				.append(getCacheHitsOnDisk()).append(",statisticsAccuracy=").append(
					getStatisticsAccuracy()).append(']');
		return buf.toString();
	}
}
