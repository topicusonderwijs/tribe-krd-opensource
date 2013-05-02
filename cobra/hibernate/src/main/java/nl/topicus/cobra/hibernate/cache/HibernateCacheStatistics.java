package nl.topicus.cobra.hibernate.cache;

import java.io.Serializable;

import org.hibernate.stat.CategorizedStatistics;
import org.hibernate.stat.SecondLevelCacheStatistics;

/**
 * @author hoeve
 */
public class HibernateCacheStatistics implements Serializable, CacheStatistics
{
	private static final long serialVersionUID = 1L;

	private transient SecondLevelCacheStatistics statistics;

	public HibernateCacheStatistics(SecondLevelCacheStatistics statistics)
	{
		this.statistics = statistics;
	}

	@Override
	public long getCacheHits()
	{
		return statistics.getHitCount();
	}

	@Override
	public long getCacheHitsInMemory()
	{
		return statistics.getHitCount();
	}

	@Override
	public long getCacheHitsOnDisk()
	{
		return statistics.getHitCount();
	}

	@Override
	public long getCacheMisses()
	{
		return statistics.getMissCount();
	}

	@Override
	public long getDiskStoreCount()
	{
		return statistics.getElementCountOnDisk();
	}

	@Override
	public long getDiskStoreSize()
	{
		return 0;
	}

	@Override
	public long getMaxElementsInMemory()
	{
		return 0;
	}

	@Override
	public long getMaxElementsOnDisk()
	{
		return 0;
	}

	@Override
	public long getMemoryStoreCount()
	{
		return statistics.getElementCountInMemory();
	}

	@Override
	public long getMemoryStoreSize()
	{
		return statistics.getSizeInMemory();
	}

	@Override
	public String getName()
	{
		return ((CategorizedStatistics) statistics).getCategoryName();
	}

	@Override
	public STATISTICS_ACCURACY getStatisticsAccuracy()
	{
		return STATISTICS_ACCURACY.STATISTICS_ACCURACY_NONE;
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
