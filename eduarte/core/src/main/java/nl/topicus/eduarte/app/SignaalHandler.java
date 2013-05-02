package nl.topicus.eduarte.app;

import nl.topicus.eduarte.entities.signalering.Signaal;

import org.apache.wicket.Component;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.repeater.Item;

/**
 * @author loite
 * @param <T>
 */
public interface SignaalHandler<T extends Signaal>
{
	/**
	 * @param signaal
	 * @param context
	 */
	public void handleSignaal(T signaal, Component context);

	/**
	 * @param signaal
	 * @param id
	 * @param item
	 * @return row voor het gegeven signaal voor een datapanel.
	 */
	public WebMarkupContainer getSignaalRow(T signaal, String id, Item<Signaal> item);
}
