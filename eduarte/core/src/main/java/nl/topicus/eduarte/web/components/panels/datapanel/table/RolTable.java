package nl.topicus.eduarte.web.components.panels.datapanel.table;

import nl.topicus.cobra.web.components.datapanel.CustomDataPanelContentDescription;
import nl.topicus.cobra.web.components.datapanel.GroupProperty;
import nl.topicus.cobra.web.components.datapanel.columns.CustomPropertyColumn;
import nl.topicus.eduarte.entities.security.authorization.Rol;

public class RolTable extends CustomDataPanelContentDescription<Rol>
{
	private static final long serialVersionUID = 1L;

	public RolTable()
	{
		super("Rollen");
		createColumns();
		createGroupProperties();
	}

	private void createColumns()
	{
		addColumn(new CustomPropertyColumn<Rol>("Naam", "Naam", "naam", "naam"));
		addColumn(new CustomPropertyColumn<Rol>("Categorie", "Categorie", "categorie", "categorie"));
		addColumn(new CustomPropertyColumn<Rol>("Type", "Type", "authorisatieNiveau",
			"authorisatieNiveau"));
	}

	private void createGroupProperties()
	{
		addGroupProperty(getDefaultGroupProperty());
		addGroupProperty(new GroupProperty<Rol>("authorisatieNiveau", "Type", "authorisatieNiveau"));
	}

	@Override
	public GroupProperty<Rol> getDefaultGroupProperty()
	{
		return new GroupProperty<Rol>("categorie", "Categorie", "categorie");
	}
}
