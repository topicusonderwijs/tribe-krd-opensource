package nl.topicus.eduarte.web.components.panels.datapanel.table;

import nl.topicus.cobra.web.components.datapanel.CustomDataPanelContentDescription;
import nl.topicus.cobra.web.components.datapanel.columns.CustomPropertyColumn;
import nl.topicus.eduarte.app.EduArteApp;
import nl.topicus.eduarte.entities.organisatie.ExterneOrganisatieOpmerking;

/**
 * Tabel met de mogelijke kolommen voor {@link ExterneOrganisatieOpmerking}
 * 
 * @author vandekamp
 */
public class BPVOpmerkingTable extends
		CustomDataPanelContentDescription<ExterneOrganisatieOpmerking>
{
	private static final long serialVersionUID = 1L;

	public BPVOpmerkingTable()
	{
		super(EduArteApp.get().getBPVTerm() + "opmerkingen");
		addColumn(new CustomPropertyColumn<ExterneOrganisatieOpmerking>("Datum", "Datum", "datum"));
		addColumn(new CustomPropertyColumn<ExterneOrganisatieOpmerking>("Opmerking", "Opmerking",
			"opmerking"));
		addColumn(new CustomPropertyColumn<ExterneOrganisatieOpmerking>("Auteur", "Auteur",
			"auteur"));
		addColumn(new CustomPropertyColumn<ExterneOrganisatieOpmerking>("Tonen bij matching",
			"Tonen bij matching", "tonenBijMatching"));

	}
}
