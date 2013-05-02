package nl.topicus.eduarte.web.components.panels.datapanel.table;

import nl.topicus.cobra.web.components.datapanel.CustomDataPanelContentDescription;
import nl.topicus.cobra.web.components.datapanel.columns.CustomPropertyColumn;
import nl.topicus.eduarte.entities.resultaatstructuur.Resultaatstructuur;

public class ResultaatstructuurTable extends CustomDataPanelContentDescription<Resultaatstructuur>
{
	private static final long serialVersionUID = 1L;

	public ResultaatstructuurTable()
	{
		super("Resultaatstructuren");

		addColumn(new CustomPropertyColumn<Resultaatstructuur>("Type", "Type", "type", "type"));
		addColumn(new CustomPropertyColumn<Resultaatstructuur>("Categorie", "Categorie",
			"categorie.naam", "categorie.naam"));
		addColumn(new CustomPropertyColumn<Resultaatstructuur>("Cohort", "Cohort", "cohort",
			"cohort"));
		addColumn(new CustomPropertyColumn<Resultaatstructuur>("Code", "Code", "code", "code"));
		addColumn(new CustomPropertyColumn<Resultaatstructuur>("Naam", "Naam", "Naam", "Naam"));
		addColumn(new CustomPropertyColumn<Resultaatstructuur>("Productcode", "Productcode",
			"onderwijsproduct.code", "onderwijsproduct.code"));
		addColumn(new CustomPropertyColumn<Resultaatstructuur>("Producttitel", "Producttitel",
			"onderwijsproduct.titel", "onderwijsproduct.titel").setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<Resultaatstructuur>("Omschrijving", "Omschrijving",
			"onderwijsproduct.omschrijving", "onderwijsproduct.omschrijving")
			.setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<Resultaatstructuur>("Soort product", "Soort",
			"onderwijsproduct.soortProduct.naam", "onderwijsproduct.soortProduct.naam")
			.setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<Resultaatstructuur>("Auteur", "Auteur", "auteur"));
		addColumn(new CustomPropertyColumn<Resultaatstructuur>("Status", "Status", "status",
			"status"));
	}
}
