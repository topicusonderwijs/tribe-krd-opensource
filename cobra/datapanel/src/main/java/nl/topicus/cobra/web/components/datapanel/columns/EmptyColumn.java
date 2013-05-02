package nl.topicus.cobra.web.components.datapanel.columns;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.panel.EmptyPanel;
import org.apache.wicket.model.IModel;

public class EmptyColumn<T> extends AbstractCustomColumn<T>
{
	private static final long serialVersionUID = 1L;

	public EmptyColumn(String id, String header)
	{
		super(id, header);
	}

	@Override
	public void populateItem(WebMarkupContainer cell, String componentId, WebMarkupContainer row,
			IModel<T> rowModel, int span)
	{
		cell.add(new EmptyPanel(componentId).setVisible(isContentsVisible(rowModel)).setEnabled(
			isContentsEnabled(rowModel)));
	}

}