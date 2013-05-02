package nl.topicus.eduarte.app.resultaat;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeoutException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ResultaatMutexImpl implements ResultaatMutex
{
	private static final Logger log = LoggerFactory.getLogger(ResultaatMutexImpl.class);

	private Set<ResultaatVersionKey> locks = new HashSet<ResultaatVersionKey>();

	@Override
	public void execute(ResultaatVersionCollection lockset, long timeout, Runnable code)
			throws TimeoutException
	{
		execute(lockset.getLockKeys(), timeout, code);
	}

	@Override
	public void execute(Set<ResultaatVersionKey> lockset, long timeout, Runnable code)
			throws TimeoutException
	{
		try
		{
			long startTime = System.currentTimeMillis();
			aquireLock(lockset, timeout);
			log.info("Lock aquired in " + (System.currentTimeMillis() - startTime) + "ms");
			code.run();
		}
		finally
		{
			releaseLock(lockset);
		}
	}

	private synchronized void aquireLock(Set<ResultaatVersionKey> lockset, long timeout)
			throws TimeoutException
	{
		long endTime = System.currentTimeMillis() + timeout;
		while (isLocked(lockset))
		{
			try
			{
				wait(endTime - System.currentTimeMillis());
				if (System.currentTimeMillis() > endTime)
				{
					throw new TimeoutException("Could not aquire resultaten lock within " + timeout
						+ " ms.");
				}
			}
			catch (InterruptedException e)
			{
				// ignore and try again
			}
		}
		lock(lockset);
	}

	private synchronized void releaseLock(Set<ResultaatVersionKey> lockset)
	{
		log.info("Releasing locks for " + lockset.size() + " keys");
		locks.removeAll(lockset);
		notifyAll();
	}

	private boolean isLocked(Set<ResultaatVersionKey> set)
	{
		for (ResultaatVersionKey curKey : set)
			if (locks.contains(curKey))
				return true;
		return false;
	}

	private void lock(Set<ResultaatVersionKey> set)
	{
		log.info("Locking " + set.size() + " keys");
		locks.addAll(set);
	}
}
