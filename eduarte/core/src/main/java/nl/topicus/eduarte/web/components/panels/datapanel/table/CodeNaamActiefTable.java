package nl.topicus.eduarte.web.components.panels.datapanel.table;

import nl.topicus.cobra.web.components.datapanel.columns.CustomPropertyColumn;

/**
 * @author vandekamp
 */
public class CodeNaamActiefTable<T> extends NaamActiefTable<T>
{
	private static final long serialVersionUID = 1L;

	public CodeNaamActiefTable(String title)
	{
		super(title);
		addColumn(new CustomPropertyColumn<T>("Code", "Code", "code", "code"));
	}
}
