package nl.topicus.eduarte.web.components.panels.datapanel.table;

import nl.topicus.cobra.web.components.datapanel.CustomDataPanelContentDescription;
import nl.topicus.cobra.web.components.datapanel.GroupProperty;
import nl.topicus.cobra.web.components.datapanel.columns.CustomPropertyColumn;
import nl.topicus.eduarte.entities.onderwijsproduct.OnderwijsproductTaxonomie;

/**
 * Tabel met de mogelijke kolommen voor taxonomieelementzoekschermen.
 * 
 * @author loite
 */
public class OnderwijsproductTaxonomieElementTable extends
		CustomDataPanelContentDescription<OnderwijsproductTaxonomie>
{
	private static final long serialVersionUID = 1L;

	public OnderwijsproductTaxonomieElementTable(String title)
	{
		super(title);
		createColumns();
		createGroupProperties();
	}

	private void createColumns()
	{
		addColumn(new CustomPropertyColumn<OnderwijsproductTaxonomie>("Afkorting", "Afkorting",
			"taxonomieElement.afkorting", "taxonomieElement.afkorting"));
		addColumn(new CustomPropertyColumn<OnderwijsproductTaxonomie>("Naam", "Naam",
			"taxonomieElement.naam", "taxonomieElement.naam"));
		addColumn(new CustomPropertyColumn<OnderwijsproductTaxonomie>("Taxonomiecode",
			"Taxonomiecode", "taxonomieElement.sorteercode", "taxonomieElement.taxonomiecode"));
		addColumn(new CustomPropertyColumn<OnderwijsproductTaxonomie>("Externe code",
			"Externe code", "taxonomieElement.externeCode", "taxonomieElement.externeCode"));
		addColumn(new CustomPropertyColumn<OnderwijsproductTaxonomie>("Ouder", "Ouder",
			"taxonomieElement.parent.naam", "taxonomieElement.parent.naam")
			.setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<OnderwijsproductTaxonomie>("Taxonomie", "Taxonomie",
			"taxonomieElement.taxonomie.naam", "taxonomieElement.taxonomie.naam")
			.setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<OnderwijsproductTaxonomie>("Type", "Type",
			"taxonomieElement.taxonomieElementType.naam",
			"taxonomieElement.taxonomieElementType.naam").setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<OnderwijsproductTaxonomie>("Begindatum", "Begindatum",
			"taxonomieElement.begindatum", "taxonomieElement.begindatum").setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<OnderwijsproductTaxonomie>("Einddatum", "Einddatum",
			"taxonomieElement.einddatum", "taxonomieElement.einddatum").setDefaultVisible(false));
	}

	private void createGroupProperties()
	{
		addGroupProperty(new GroupProperty<OnderwijsproductTaxonomie>(
			"taxonomieElement.parent.naam", "Ouder", "taxonomieElement.parent.naam"));
		addGroupProperty(new GroupProperty<OnderwijsproductTaxonomie>(
			"taxonomieElement.taxonomieElementType.naam", "Type",
			"taxonomieElement.taxonomieElementType.naam"));
	}
}
