package nl.topicus.eduarte.web.components.panels.datapanel.table;

import nl.topicus.cobra.web.components.datapanel.CustomDataPanelContentDescription;
import nl.topicus.cobra.web.components.datapanel.columns.CustomPropertyColumn;
import nl.topicus.eduarte.entities.personen.PersoonExterneOrganisatieContactPersoon;

public class PersoonExterneOrganisatieContactPersoonTable extends
		CustomDataPanelContentDescription<PersoonExterneOrganisatieContactPersoon>
{
	private static final long serialVersionUID = 1L;

	public PersoonExterneOrganisatieContactPersoonTable()
	{
		super("Contactpersonen");
		createColumns();
	}

	private void createColumns()
	{
		addColumn(new CustomPropertyColumn<PersoonExterneOrganisatieContactPersoon>("Naam", "Naam",
			"externeOrganisatieContactPersoon.naam"));
		addColumn(new CustomPropertyColumn<PersoonExterneOrganisatieContactPersoon>("Telefoon",
			"Telefoon", "externeOrganisatieContactPersoon.telefoon"));
		addColumn(new CustomPropertyColumn<PersoonExterneOrganisatieContactPersoon>("Mobiel",
			"Mobiel", "externeOrganisatieContactPersoon.mobiel").setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<PersoonExterneOrganisatieContactPersoon>("Geslacht",
			"Geslacht", "externeOrganisatieContactPersoon.geslacht").setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<PersoonExterneOrganisatieContactPersoon>("E-mail",
			"E-mail", "externeOrganisatieContactPersoon.emailadres"));
		addColumn(new CustomPropertyColumn<PersoonExterneOrganisatieContactPersoon>("Rol", "Rol",
			"externeOrganisatieContactPersoon.rol"));
	}
}
