package nl.topicus.eduarte.web.pages.beheer.contract;

import nl.topicus.cobra.web.components.datapanel.CustomDataPanelContentDescription;
import nl.topicus.cobra.web.components.datapanel.columns.CurrencyColumn;
import nl.topicus.cobra.web.components.datapanel.columns.CustomPropertyColumn;
import nl.topicus.cobra.web.components.datapanel.columns.DatePropertyColumn;
import nl.topicus.eduarte.entities.contract.ContractOnderdeel;

public class ContractOnderdeelTable extends CustomDataPanelContentDescription<ContractOnderdeel>
{
	private static final long serialVersionUID = 1L;

	public ContractOnderdeelTable()
	{
		super("Contractonderdeel");
		createColumns();
	}

	private void createColumns()
	{
		addColumn(new CustomPropertyColumn<ContractOnderdeel>("Naam", "Naam", "naam", "naam"));
		addColumn(new CustomPropertyColumn<ContractOnderdeel>("Min. aantal deelnemers",
			"Min. aantal deelnemers", "minimumAantalDeelnemers", "minimumAantalDeelnemers"));
		addColumn(new CustomPropertyColumn<ContractOnderdeel>("Max. aantal deelnemers",
			"Max. aantal deelnemers", "maximumAantalDeelnemers", "maximumAantalDeelnemers"));
		addColumn(new CurrencyColumn<ContractOnderdeel>("prijs", "Prijs", "prijs"));
		addColumn(new DatePropertyColumn<ContractOnderdeel>("Geldig vanaf", "Geldig vanaf",
			"begindatum"));
		addColumn(new DatePropertyColumn<ContractOnderdeel>("Geldig tot", "Geldig tot", "einddatum"));
		addColumn(new CustomPropertyColumn<ContractOnderdeel>("Freq aanwezigheid",
			"Freq aanwezigheid", "frequentieAanwezigheid", "frequentieAanwezigheid")
			.setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<ContractOnderdeel>("Groepsgrootte", "Groepsgrootte",
			"groepsgrootte", "groepsgrootte").setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<ContractOnderdeel>("Begleidings intensiteit",
			"Begleidings intensiteit", "begeleidingsintensiteit", "begeleidingsintensiteit")
			.setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<ContractOnderdeel>("Studiebelasting", "Studiebelasting",
			"studiebelasting", "studiebelasting").setDefaultVisible(false));
	}
}
