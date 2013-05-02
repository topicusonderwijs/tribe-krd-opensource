package nl.topicus.eduarte.web.pages.beheer.contract;

import nl.topicus.cobra.web.components.datapanel.CustomDataPanelContentDescription;
import nl.topicus.cobra.web.components.datapanel.columns.BooleanPropertyColumn;
import nl.topicus.cobra.web.components.datapanel.columns.CustomPropertyColumn;
import nl.topicus.cobra.web.components.datapanel.columns.DatePropertyColumn;
import nl.topicus.eduarte.entities.contract.ContractVerplichting;

public class ContractVerplichtingTable extends
		CustomDataPanelContentDescription<ContractVerplichting>
{
	private static final long serialVersionUID = 1L;

	public ContractVerplichtingTable()
	{
		super("Contractverplichting");
		createColumns();
	}

	private void createColumns()
	{
		addColumn(new CustomPropertyColumn<ContractVerplichting>("Omschrijving", "Omschrijving",
			"omschrijving", "omschrijving"));
		addColumn(new CustomPropertyColumn<ContractVerplichting>("Medewerker", "Medewerker",
			"medewerker", "medewerker"));
		addColumn(new DatePropertyColumn<ContractVerplichting>("Begindatum", "Begindatum",
			"begindatum"));
		addColumn(new DatePropertyColumn<ContractVerplichting>("Deadline", "Deadline", "einddatum"));
		addColumn(new BooleanPropertyColumn<ContractVerplichting>("Uitgevoerd", "Uitgevoerd",
			"uitgevoerd", "uitgevoerd"));
		addColumn(new DatePropertyColumn<ContractVerplichting>("Uitgevoerd op", "Uitgevoerd op",
			"datumUitgevoerd"));
	}
}
