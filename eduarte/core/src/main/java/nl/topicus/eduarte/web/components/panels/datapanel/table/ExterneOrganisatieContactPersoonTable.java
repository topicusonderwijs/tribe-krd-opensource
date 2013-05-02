package nl.topicus.eduarte.web.components.panels.datapanel.table;

import nl.topicus.cobra.web.components.datapanel.CustomDataPanelContentDescription;
import nl.topicus.cobra.web.components.datapanel.columns.CustomPropertyColumn;
import nl.topicus.eduarte.entities.personen.ExterneOrganisatieContactPersoon;

/**
 * Tabel met de mogelijke kolommen voor contactpersonen van een externe organisatie
 * 
 * @author idserda
 */
public class ExterneOrganisatieContactPersoonTable extends
		CustomDataPanelContentDescription<ExterneOrganisatieContactPersoon>
{
	private static final long serialVersionUID = 1L;

	public ExterneOrganisatieContactPersoonTable()
	{
		super("Contactpersonen");
		createColumns();
	}

	private void createColumns()
	{
		addColumn(new CustomPropertyColumn<ExterneOrganisatieContactPersoon>("Naam", "Naam", "naam"));
		addColumn(new CustomPropertyColumn<ExterneOrganisatieContactPersoon>("Telefoon",
			"Telefoon", "telefoon"));
		addColumn(new CustomPropertyColumn<ExterneOrganisatieContactPersoon>("Mobiel", "Mobiel",
			"mobiel").setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<ExterneOrganisatieContactPersoon>("Geslacht",
			"Geslacht", "geslacht").setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<ExterneOrganisatieContactPersoon>("E-mail", "E-mail",
			"emailadres"));
		addColumn(new CustomPropertyColumn<ExterneOrganisatieContactPersoon>("Rol", "Rol", "rol"));
		addColumn(new CustomPropertyColumn<ExterneOrganisatieContactPersoon>("Begindatum",
			"Begindatum", "begindatum", "begindatum").setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<ExterneOrganisatieContactPersoon>("Einddatum",
			"Einddatum", "einddatum", "einddatum").setDefaultVisible(false));

	}
}
