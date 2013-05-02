package nl.topicus.eduarte.web.components.panels.datapanel.table;

import nl.topicus.cobra.web.components.datapanel.CustomDataPanelContentDescription;
import nl.topicus.cobra.web.components.datapanel.GroupProperty;
import nl.topicus.cobra.web.components.datapanel.columns.CustomPropertyColumn;
import nl.topicus.eduarte.entities.taxonomie.TaxonomieElement;

/**
 * Tabel met de mogelijke kolommen voor taxonomieelementzoekschermen.
 * 
 * @author loite
 */
public class TaxonomieElementTable extends CustomDataPanelContentDescription<TaxonomieElement>
{
	private static final long serialVersionUID = 1L;

	public TaxonomieElementTable(String title)
	{
		super(title);
		createColumns();
		createGroupProperties();
	}

	private void createColumns()
	{
		addColumn(new CustomPropertyColumn<TaxonomieElement>("Afkorting", "Afkorting", "afkorting",
			"afkorting"));
		addColumn(new CustomPropertyColumn<TaxonomieElement>("Naam", "Naam", "naam", "naam"));
		addColumn(new CustomPropertyColumn<TaxonomieElement>("Taxonomiecode", "Taxonomiecode",
			"sorteercode", "taxonomiecode"));
		addColumn(new CustomPropertyColumn<TaxonomieElement>("Externe code", "Externe code",
			"externeCode", "externeCode"));
		addColumn(new CustomPropertyColumn<TaxonomieElement>("Ouder", "Ouder", "parent.naam",
			"parent.naam"));
		addColumn(new CustomPropertyColumn<TaxonomieElement>("Taxonomie", "Taxonomie",
			"taxonomie.naam", "taxonomie.naam").setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<TaxonomieElement>("Type", "Type",
			"taxonomieElementType.naam", "taxonomieElementType.naam").setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<TaxonomieElement>("Begindatum", "Begindatum",
			"begindatum", "begindatum").setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<TaxonomieElement>("Einddatum", "Einddatum", "einddatum",
			"einddatum").setDefaultVisible(false));

		// MBO-specifieke properties.
		addColumn(new CustomPropertyColumn<TaxonomieElement>("Leerwegen", "Leerwegen",
			"leerwegCodes").setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<TaxonomieElement>("Soort opleiding", "Soort opleiding",
			"soortOpleidingNaam").setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<TaxonomieElement>("Niveau", "Niveau", "niveauNaam")
			.setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<TaxonomieElement>("Prijsfactor", "Prijsfactor",
			"prijsfactor").setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<TaxonomieElement>("Studiebelastingsuren",
			"Studiebelastingsuren", "studiebelastingsuren").setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<TaxonomieElement>("Wettelijke eisen",
			"Wettelijke eisen", "wettelijkeEisenOmschrijving").setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<TaxonomieElement>("Bron wetteljke eisen",
			"Bron wettelijke eisen", "bronWettelijkeEisen").setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<TaxonomieElement>("Brin kenniscentrum",
			"Brin kenniscentrum", "brinKenniscentrum").setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<TaxonomieElement>("Naam kenniscentrum",
			"Naam kenniscentrum", "naamKenniscentrum").setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<TaxonomieElement>("Code coordinatiepunt",
			"Code coordinatiepunt", "codeCoordinatiepunt").setDefaultVisible(false));
	}

	private void createGroupProperties()
	{
		addGroupProperty(new GroupProperty<TaxonomieElement>("parent.naam", "Ouder", "parent.naam"));
		addGroupProperty(new GroupProperty<TaxonomieElement>("taxonomieElementType.naam", "Type",
			"taxonomieElementType.naam"));
	}
}
