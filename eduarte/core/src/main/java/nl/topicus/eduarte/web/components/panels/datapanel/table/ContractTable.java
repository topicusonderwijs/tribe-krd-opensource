package nl.topicus.eduarte.web.components.panels.datapanel.table;

import nl.topicus.cobra.web.components.datapanel.columns.BooleanPropertyColumn;
import nl.topicus.cobra.web.components.datapanel.columns.CurrencyColumn;
import nl.topicus.cobra.web.components.datapanel.columns.CustomPropertyColumn;
import nl.topicus.eduarte.entities.contract.Contract;
import nl.topicus.eduarte.entities.vrijevelden.VrijVeldCategorie;

public class ContractTable extends AbstractVrijVeldableTable<Contract>
{
	private static final long serialVersionUID = 1L;

	public ContractTable()
	{
		super("Contracten");
		addColumn(new CustomPropertyColumn<Contract>("Naam", "Naam", "naam", "naam"));
		addColumn(new BooleanPropertyColumn<Contract>("Actief", "Actief", "actief"));
		addColumn(new CustomPropertyColumn<Contract>("Code", "Code", "code", "code"));
		addColumn(new CustomPropertyColumn<Contract>("Organisatie-eenheid", "Organisatie-eenheid",
			"organisatieEenheid", "organisatieEenheid"));
		addColumn(new CustomPropertyColumn<Contract>("Onderaanneming", "Onderaanneming",
			"onderaanneming", "onderaanneming").setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<Contract>("Onderaanneming bij", "Onderaanneming bij",
			"onderaannemingBij", "onderaannemingBij").setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<Contract>("Externe organisatie", "Externe organisatie",
			"externeOrganisatie.naam", "externeOrganisatie.naam"));
		addColumn(new CustomPropertyColumn<Contract>("Nummer", "Nummer", "externNummer",
			"externNummer"));
		addColumn(new CustomPropertyColumn<Contract>("Soort", "Soort", "soortContract",
			"soortContract"));
		addColumn(new CurrencyColumn<Contract>("Kostprijs", "Kostprijs", "kostprijs", "kostprijs")
			.setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<Contract>("Begindatum", "Begindatum", "begindatum",
			"begindatum"));
		addColumn(new CustomPropertyColumn<Contract>("Einddatum", "Einddatum", "einddatum",
			"einddatum"));
		createVrijVeldKolommen(VrijVeldCategorie.CONTRACT, "");
	}
}
