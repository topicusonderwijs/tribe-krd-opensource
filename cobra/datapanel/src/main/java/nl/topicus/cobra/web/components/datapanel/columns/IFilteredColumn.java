package nl.topicus.cobra.web.components.datapanel.columns;

import java.util.List;

import nl.topicus.cobra.web.components.datapanel.CustomColumn;

public interface IFilteredColumn<T> extends CustomColumn<T>
{
	public List<String> getFilterProperties();
}
