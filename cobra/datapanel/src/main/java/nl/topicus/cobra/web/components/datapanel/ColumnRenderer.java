package nl.topicus.cobra.web.components.datapanel;

import org.apache.wicket.markup.html.form.IChoiceRenderer;

public class ColumnRenderer<T> implements IChoiceRenderer<CustomColumn<T>>
{
	private static final long serialVersionUID = 1L;

	public ColumnRenderer()
	{
	}

	@Override
	public Object getDisplayValue(CustomColumn<T> object)
	{
		CustomColumn<T> column = object;
		return column.getId();
	}

	@Override
	public String getIdValue(CustomColumn<T> object, int index)
	{
		CustomColumn<T> column = object;
		return column.getId();
	}

}
