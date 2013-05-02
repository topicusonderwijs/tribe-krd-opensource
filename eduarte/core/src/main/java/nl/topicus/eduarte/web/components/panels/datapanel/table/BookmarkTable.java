package nl.topicus.eduarte.web.components.panels.datapanel.table;

import nl.topicus.cobra.web.components.datapanel.CustomDataPanelContentDescription;
import nl.topicus.cobra.web.components.datapanel.columns.CustomPropertyColumn;
import nl.topicus.eduarte.entities.sidebar.Bookmark;

public class BookmarkTable extends CustomDataPanelContentDescription<Bookmark>
{
	private static final long serialVersionUID = 1L;

	public BookmarkTable()
	{
		super("Favorieten");
		createColumns();
		createGroupProperties();
	}

	private void createColumns()
	{
		addColumn(new CustomPropertyColumn<Bookmark>("Omschrijving", "Favorieten", "omschrijving"));

	}

	private void createGroupProperties()
	{
		addGroupProperty(getDefaultGroupProperty());
	}

}
