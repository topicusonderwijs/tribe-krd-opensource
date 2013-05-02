package nl.topicus.cobra.web.components.datapanel.columns;

import nl.topicus.cobra.web.components.datapanel.CustomColumn;

public interface ExportHeaderColumn<T> extends CustomColumn<T>
{
	public String getExportHeader();
}
