package nl.topicus.cobra.web.components.datapanel.columns;

import nl.topicus.cobra.web.components.datapanel.CustomColumn;

public interface HideableColumn<T> extends CustomColumn<T>
{
	/**
	 * Geeft aan of de column getoond moet worden
	 */
	public boolean isColumnVisible();

	/**
	 * Geeft aan of de column in een export moet komen (als ook isColumnVisible true
	 * geeft)
	 */
	public boolean isColumnVisibleInExport();
}
