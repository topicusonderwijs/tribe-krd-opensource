package nl.topicus.eduarte.web.components.panels.datapanel.table;

import nl.topicus.cobra.web.components.datapanel.CustomDataPanelContentDescription;
import nl.topicus.cobra.web.components.datapanel.columns.CustomPropertyColumn;
import nl.topicus.eduarte.app.EduArteApp;
import nl.topicus.eduarte.entities.bpv.BPVCriteriaOnderwijsproduct;

public class BPVCriteriaOnderwijsproductTable extends
		CustomDataPanelContentDescription<BPVCriteriaOnderwijsproduct>
{
	private static final long serialVersionUID = 1L;

	public BPVCriteriaOnderwijsproductTable()
	{
		super(EduArteApp.get().getBPVTerm() + "criteria");
		addColumn(new CustomPropertyColumn<BPVCriteriaOnderwijsproduct>("Naam", "Naam",
			"bpvCriteria.naam"));
		addColumn(new CustomPropertyColumn<BPVCriteriaOnderwijsproduct>("Omschrijving",
			"Omschrijving", "bpvCriteria.omschrijving"));
		addColumn(new CustomPropertyColumn<BPVCriteriaOnderwijsproduct>("Status", "Status",
			"status"));
	}
}
