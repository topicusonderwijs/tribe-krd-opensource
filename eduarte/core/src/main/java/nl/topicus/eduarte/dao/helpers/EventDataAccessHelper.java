package nl.topicus.eduarte.dao.helpers;

import nl.topicus.cobra.dao.DataAccessHelper;
import nl.topicus.eduarte.entities.signalering.Event;

public interface EventDataAccessHelper<T extends Event> extends DataAccessHelper<T>
{
	public boolean isEerderOpgetreden(T event);
}
