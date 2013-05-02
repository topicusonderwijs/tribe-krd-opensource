package nl.topicus.eduarte.web.components.panels.datapanel.table;

import nl.topicus.cobra.web.components.datapanel.CustomDataPanelContentDescription;
import nl.topicus.cobra.web.components.datapanel.columns.CustomPropertyColumn;
import nl.topicus.eduarte.app.EduArteApp;
import nl.topicus.eduarte.entities.organisatie.ExterneOrganisatiePraktijkbegeleider;

public class ExterneOrganisatiePraktijkbegeleiderTable extends
		CustomDataPanelContentDescription<ExterneOrganisatiePraktijkbegeleider>
{
	private static final long serialVersionUID = 1L;

	public ExterneOrganisatiePraktijkbegeleiderTable()
	{
		super(EduArteApp.get().getBPVTerm() + "begeleider(s)");
		addColumn(new CustomPropertyColumn<ExterneOrganisatiePraktijkbegeleider>("Naam", "Naam",
			"medewerker.persoon"));
	}
}