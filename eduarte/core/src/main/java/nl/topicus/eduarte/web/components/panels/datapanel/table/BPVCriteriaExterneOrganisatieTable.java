package nl.topicus.eduarte.web.components.panels.datapanel.table;

import nl.topicus.cobra.web.components.datapanel.CustomDataPanelContentDescription;
import nl.topicus.cobra.web.components.datapanel.columns.CustomPropertyColumn;
import nl.topicus.eduarte.app.EduArteApp;
import nl.topicus.eduarte.entities.bpv.BPVCriteriaExterneOrganisatie;

public class BPVCriteriaExterneOrganisatieTable extends
		CustomDataPanelContentDescription<BPVCriteriaExterneOrganisatie>
{
	private static final long serialVersionUID = 1L;

	public BPVCriteriaExterneOrganisatieTable()
	{
		super(EduArteApp.get().getBPVTerm() + "criteria");
		addColumn(new CustomPropertyColumn<BPVCriteriaExterneOrganisatie>("Naam", "Naam",
			"bpvCriteria.naam"));
		addColumn(new CustomPropertyColumn<BPVCriteriaExterneOrganisatie>("Omschrijving",
			"Omschrijving", "bpvCriteria.omschrijving"));
		addColumn(new CustomPropertyColumn<BPVCriteriaExterneOrganisatie>("Status", "Status",
			"status"));
	}
}