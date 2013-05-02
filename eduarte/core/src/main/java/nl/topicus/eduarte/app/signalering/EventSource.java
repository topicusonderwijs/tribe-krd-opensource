package nl.topicus.eduarte.app.signalering;

import java.util.List;
import java.util.Map;

import nl.topicus.eduarte.entities.signalering.Event;
import nl.topicus.eduarte.entities.signalering.settings.EventAbonnementSetting;

public interface EventSource<T extends Event>
{
	public T createEvent();

	public Map<EventAbonnementType, List< ? extends EventReceiver>> getReceivers();

	public boolean isEventEnabledVoorReceiver(EventReceiver actualReceiver,
			EventAbonnementSetting eventAbonnementSetting);
}
