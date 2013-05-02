package nl.topicus.eduarte.app.signalering;

import nl.topicus.eduarte.entities.signalering.Event;

public interface EventTransport
{
	public String getName();

	public boolean sendEvent(Event event, EventReceiver ontvanger);
}
