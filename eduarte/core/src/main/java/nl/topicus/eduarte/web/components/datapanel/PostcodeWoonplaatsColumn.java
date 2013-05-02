package nl.topicus.eduarte.web.components.datapanel;

import nl.topicus.cobra.web.components.datapanel.columns.CustomPropertyColumn;
import nl.topicus.eduarte.web.components.label.PostcodeWoonplaatsLabel;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.model.IModel;

public class PostcodeWoonplaatsColumn<T> extends CustomPropertyColumn<T>
{
	private static final long serialVersionUID = 1L;

	public PostcodeWoonplaatsColumn(String id, String header, String propertyExpression)
	{
		super(id, header, propertyExpression);
	}

	public PostcodeWoonplaatsColumn(String id, String header, String propertyExpression,
			boolean repeatWhenEqualToPrevRow)
	{
		super(id, header, null, propertyExpression, repeatWhenEqualToPrevRow);
	}

	public PostcodeWoonplaatsColumn(String id, String header, String sortProperty,
			String propertyExpression)
	{
		super(id, header, sortProperty, propertyExpression, true);
	}

	public PostcodeWoonplaatsColumn(String id, String header, String sortProperty,
			String propertyExpression, boolean repeatWhenEqualToPrevRow)
	{
		super(id, header, sortProperty, propertyExpression, repeatWhenEqualToPrevRow);
	}

	@Override
	public void populateItem(WebMarkupContainer cell, String componentId, WebMarkupContainer row,
			IModel<T> rowModel, int span)
	{
		cell.add(new PostcodeWoonplaatsLabel(componentId, createLabelModel(rowModel))
			.setRenderBodyOnly(true));
	}
}
