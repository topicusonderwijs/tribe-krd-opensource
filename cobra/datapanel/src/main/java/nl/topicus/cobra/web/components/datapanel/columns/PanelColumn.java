package nl.topicus.cobra.web.components.datapanel.columns;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

/**
 * @author vandekamp
 */
public abstract class PanelColumn<T> extends AbstractCustomColumn<T>
{
	private static final long serialVersionUID = 1L;

	/**
	 * @param id
	 * @param header
	 */
	public PanelColumn(String id, String header)
	{
		super(id, header);
	}

	/**
	 * @see nl.topicus.cobra.web.components.datapanel.columns.CustomPropertyColumn#populateItem(org.apache.wicket.markup.repeater.Item,
	 *      java.lang.String, org.apache.wicket.model.IModel)
	 */
	@Override
	public void populateItem(WebMarkupContainer cell, String componentId, WebMarkupContainer row,
			IModel<T> rowModel, int span)
	{
		cell.add(getPanel(componentId, row, rowModel));
	}

	protected abstract Panel getPanel(String componentId, WebMarkupContainer row, IModel<T> model);
}
