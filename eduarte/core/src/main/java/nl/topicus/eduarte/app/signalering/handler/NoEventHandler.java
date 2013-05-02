package nl.topicus.eduarte.app.signalering.handler;

import nl.topicus.cobra.web.components.datapanel.items.LinkItem;
import nl.topicus.eduarte.app.signalering.EventHandler;
import nl.topicus.eduarte.entities.signalering.Event;
import nl.topicus.eduarte.entities.signalering.Signaal;

public class NoEventHandler implements EventHandler<Event>
{
	@Override
	public void handleEvent(Event event, Signaal signaal)
	{
	}

	@Override
	public void postProcessLinkItem(LinkItem<Signaal> item, Event event, Signaal signaal)
	{
	}
}
