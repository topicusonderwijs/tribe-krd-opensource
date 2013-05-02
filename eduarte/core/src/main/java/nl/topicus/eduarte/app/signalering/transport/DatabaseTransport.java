package nl.topicus.eduarte.app.signalering.transport;

import nl.topicus.eduarte.app.signalering.EventReceiver;
import nl.topicus.eduarte.app.signalering.EventTransport;
import nl.topicus.eduarte.entities.signalering.Event;
import nl.topicus.eduarte.entities.signalering.Signaal;
import nl.topicus.eduarte.providers.PersoonProvider;

public class DatabaseTransport implements EventTransport
{
	public DatabaseTransport()
	{
	}

	@Override
	public String getName()
	{
		return "Web";
	}

	@Override
	public boolean sendEvent(Event event, EventReceiver ontvanger)
	{
		if (ontvanger instanceof PersoonProvider)
		{
			Signaal signaal = new Signaal(event, ((PersoonProvider) ontvanger).getPersoon());
			signaal.save();
			return true;
		}
		return false;
	}
}
