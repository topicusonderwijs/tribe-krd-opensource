package nl.topicus.eduarte.web.components.panels.datapanel.table;

import nl.topicus.cobra.web.components.datapanel.CustomDataPanelContentDescription;
import nl.topicus.cobra.web.components.datapanel.columns.BooleanPropertyColumn;
import nl.topicus.cobra.web.components.datapanel.columns.CustomPropertyColumn;
import nl.topicus.eduarte.entities.personen.ExterneOrganisatieContactPersoonRol;

public class ExterneOrganisatieContactPersoonRolTable extends
		CustomDataPanelContentDescription<ExterneOrganisatieContactPersoonRol>
{
	private static final long serialVersionUID = 1L;

	public ExterneOrganisatieContactPersoonRolTable()
	{
		super("Rollen");
		addColumn(new CustomPropertyColumn<ExterneOrganisatieContactPersoonRol>("naam", "Naam",
			"naam", "naam"));
		addColumn(new BooleanPropertyColumn<ExterneOrganisatieContactPersoonRol>(
			"Praktijkopleider BPV", "Praktijkopleider BPV", "praktijkopleiderBPV",
			"praktijkopleiderBPV"));
		addColumn(new BooleanPropertyColumn<ExterneOrganisatieContactPersoonRol>(
			"Contactpersoon BPV", "Contactpersoon BPV", "contactpersoonBPV", "contactpersoonBPV"));
		addColumn(new BooleanPropertyColumn<ExterneOrganisatieContactPersoonRol>("Actief",
			"Actief", "actief", "actief"));
	}
}
