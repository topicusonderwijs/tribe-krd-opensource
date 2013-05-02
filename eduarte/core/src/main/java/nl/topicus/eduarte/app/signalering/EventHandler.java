package nl.topicus.eduarte.app.signalering;

import nl.topicus.cobra.web.components.datapanel.items.LinkItem;
import nl.topicus.eduarte.entities.signalering.Event;
import nl.topicus.eduarte.entities.signalering.Signaal;

public interface EventHandler<T extends Event>
{
	public void handleEvent(T event, Signaal signaal);

	public void postProcessLinkItem(LinkItem<Signaal> item, T event, Signaal signaal);
}
