package nl.topicus.eduarte.web.components.panels.datapanel.table;

import nl.topicus.cobra.web.components.datapanel.CustomDataPanelContentDescription;
import nl.topicus.cobra.web.components.datapanel.columns.BooleanPropertyColumn;
import nl.topicus.cobra.web.components.datapanel.columns.CustomPropertyColumn;
import nl.topicus.cobra.web.components.datapanel.columns.DatePropertyColumn;
import nl.topicus.eduarte.entities.contract.ContractVerplichting;

/**
 * 
 * 
 * @author vanharen
 */
public class MyContractVerplichtingTable extends
		CustomDataPanelContentDescription<ContractVerplichting>
{
	private static final long serialVersionUID = 1L;

	public MyContractVerplichtingTable()
	{
		super("Contractverplichtingen");
		createColumns();
	}

	private void createColumns()
	{
		addColumn(new CustomPropertyColumn<ContractVerplichting>("Omschrijving", "Omschrijving",
			"omschrijving", "omschrijving"));
		addColumn(new CustomPropertyColumn<ContractVerplichting>("Externe Organisatie",
			"Externe Organisatie", "contract.externeOrganisatie", "contract.externeOrganisatie")
			.setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<ContractVerplichting>("Beheerder", "Beheerder",
			"contract.beheerder", "contract.beheerder").setDefaultVisible(false));
		addColumn(new DatePropertyColumn<ContractVerplichting>("Begindatum", "Begindatum",
			"begindatum", "begindatum"));
		addColumn(new DatePropertyColumn<ContractVerplichting>("Deadline", "Deadline", "einddatum",
			"einddatum"));
		addColumn(new BooleanPropertyColumn<ContractVerplichting>("Uitgevoerd", "Uitgevoerd",
			"uitgevoerd", "uitgevoerd"));
		addColumn(new DatePropertyColumn<ContractVerplichting>("Uitgevoerd op", "Uitgevoerd op",
			"datumUitgevoerd"));
	}
}
