package nl.topicus.eduarte.web.components.panels.datapanel.table;

import nl.topicus.cobra.web.components.datapanel.CustomDataPanelContentDescription;
import nl.topicus.cobra.web.components.datapanel.columns.CustomPropertyColumn;
import nl.topicus.eduarte.entities.taxonomie.TaxonomieElementType;

/**
 * Tabel met de mogelijke kolommen voor taxonomieelementzoekschermen.
 * 
 * @author loite
 */
public class TaxonomieElementTypeTable extends
		CustomDataPanelContentDescription<TaxonomieElementType>
{
	private static final long serialVersionUID = 1L;

	public TaxonomieElementTypeTable()
	{
		super("Taxonomie-elementtypes");
		createColumns();
	}

	private void createColumns()
	{
		addColumn(new CustomPropertyColumn<TaxonomieElementType>("Naam", "Naam", "naam"));
		addColumn(new CustomPropertyColumn<TaxonomieElementType>("Parent", "Parent", "parent.naam"));
		addColumn(new CustomPropertyColumn<TaxonomieElementType>("Soort", "Soort", "soort"));
		addColumn(new CustomPropertyColumn<TaxonomieElementType>("Inschrijfbaar", "Inschrijfbaar",
			"inschrijfbaarOmschrijving"));
		addColumn(new CustomPropertyColumn<TaxonomieElementType>("Diplomeerbaar", "Diplomeerbaar",
			"diplomeerbaarOmschrijving"));
	}
}
