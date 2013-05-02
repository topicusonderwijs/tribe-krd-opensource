package nl.topicus.eduarte.web.components.panels.datapanel.table;

import nl.topicus.cobra.web.components.datapanel.CustomDataPanelContentDescription;
import nl.topicus.cobra.web.components.datapanel.columns.CustomPropertyColumn;
import nl.topicus.eduarte.entities.personen.OrganisatieEenheidContactPersoon;

/**
 * Tabel met de mogelijke kolommen voor contactpersonen van een externe organisatie
 * 
 * @author idserda
 */
public class OrganisatieEenheidContactPersoonTable extends
		CustomDataPanelContentDescription<OrganisatieEenheidContactPersoon>
{
	private static final long serialVersionUID = 1L;

	public OrganisatieEenheidContactPersoonTable()
	{
		super("Contactpersonen");
		createColumns();
	}

	private void createColumns()
	{
		addColumn(new CustomPropertyColumn<OrganisatieEenheidContactPersoon>("Naam", "Naam", "naam"));
		addColumn(new CustomPropertyColumn<OrganisatieEenheidContactPersoon>("Telefoon",
			"Telefoon", "telefoon"));
		addColumn(new CustomPropertyColumn<OrganisatieEenheidContactPersoon>("Mobiel", "Mobiel",
			"mobiel").setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<OrganisatieEenheidContactPersoon>("Geslacht",
			"Geslacht", "geslacht").setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<OrganisatieEenheidContactPersoon>("E-mail", "E-mail",
			"emailadres"));
		addColumn(new CustomPropertyColumn<OrganisatieEenheidContactPersoon>("Rol", "Rol",
			"rol.naam").setDefaultVisible(false));

	}
}
