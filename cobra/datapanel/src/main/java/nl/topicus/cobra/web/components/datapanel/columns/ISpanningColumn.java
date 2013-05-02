package nl.topicus.cobra.web.components.datapanel.columns;

import nl.topicus.cobra.web.components.datapanel.CustomColumn;

public interface ISpanningColumn<T> extends CustomColumn<T>
{
	public int getSpan();
}
