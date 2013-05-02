package nl.topicus.cobra.hibernate.cache;

/**
 * @author hoeve
 */
public interface CacheStatistics
{
	public enum STATISTICS_ACCURACY
	{
		/**
		 * Best efforts accuracy setting.
		 */
		STATISTICS_ACCURACY_BEST_EFFORT,

		/**
		 * Guaranteed accuracy setting.
		 */
		STATISTICS_ACCURACY_GUARANTEED,

		/**
		 * Fast but not accurate setting.
		 */
		STATISTICS_ACCURACY_NONE;
	}

	public abstract String getName();

	public abstract long getCacheHits();

	public abstract long getCacheMisses();

	public abstract long getCacheHitsInMemory();

	public abstract long getCacheHitsOnDisk();

	public abstract long getDiskStoreCount();

	public abstract long getMemoryStoreCount();

	/**
	 * Warning: This method can be very expensive to run. Running this method could create
	 * liveness problems because the object lock is held for a long period
	 * <p/>
	 * 
	 * @return the approximate size of memory
	 */
	public abstract long getDiskStoreSize();

	/**
	 * Warning: This method can be very expensive to run. Running this method could create
	 * liveness problems because the object lock is held for a long period
	 * <p/>
	 * 
	 * @return the approximate size of memory
	 */
	public abstract long getMemoryStoreSize();

	public abstract STATISTICS_ACCURACY getStatisticsAccuracy();

	public abstract long getMaxElementsInMemory();

	public abstract long getMaxElementsOnDisk();

}