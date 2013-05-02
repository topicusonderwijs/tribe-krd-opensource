package nl.topicus.eduarte.web.components.panels.datapanel.table;

import nl.topicus.cobra.web.components.datapanel.CustomDataPanelContentDescription;
import nl.topicus.cobra.web.components.datapanel.columns.CustomPropertyColumn;
import nl.topicus.eduarte.entities.bpv.BPVCriteria;

public class BPVCriteriaTable extends CustomDataPanelContentDescription<BPVCriteria>
{
	private static final long serialVersionUID = 1L;

	public BPVCriteriaTable()
	{
		super("Criteria");
		addColumn(new CustomPropertyColumn<BPVCriteria>("Naam", "Naam", "naam"));
		addColumn(new CustomPropertyColumn<BPVCriteria>("Omschrijving", "Omschrijving",
			"omschrijving"));
	}
}
