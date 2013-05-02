package nl.topicus.cobra.web.components.listview;

import java.util.List;

import nl.topicus.cobra.entities.IdObject;

import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.model.IModel;

/**
 * @author loite
 */
public abstract class ClickableCompoundIdObjectListView<T extends IdObject> extends
		CompoundIdObjectListView<T> implements IClickableListView<T>
{
	private static final long serialVersionUID = 1L;

	public ClickableCompoundIdObjectListView(String id)
	{
		super(id);
	}

	public ClickableCompoundIdObjectListView(String id, IModel<List<T>> model)
	{
		super(id, model);
	}

	public abstract void onClick(ListItem<T> item);

	@Override
	protected ListItem<T> newItem(int index)
	{
		return new LinkItem<T>(index, getListItemModel(getModel(), index));
	}
}
