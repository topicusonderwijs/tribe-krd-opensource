package nl.topicus.cobra.web.components.listview;

import org.apache.wicket.markup.html.list.ListItem;

/**
 * Interface voor listviews met clickable items
 * 
 * @author loite
 */
public interface IClickableListView<T>
{
	public void onClick(ListItem<T> item);
}
