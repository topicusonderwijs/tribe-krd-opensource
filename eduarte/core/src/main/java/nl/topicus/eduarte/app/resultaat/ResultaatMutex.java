package nl.topicus.eduarte.app.resultaat;

import java.util.Set;
import java.util.concurrent.TimeoutException;

public interface ResultaatMutex
{
	public void execute(Set<ResultaatVersionKey> lockset, long timeout, Runnable code)
			throws TimeoutException;

	public void execute(ResultaatVersionCollection lockset, long timeout, Runnable code)
			throws TimeoutException;
}
