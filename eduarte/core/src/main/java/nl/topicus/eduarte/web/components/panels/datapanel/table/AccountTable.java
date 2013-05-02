package nl.topicus.eduarte.web.components.panels.datapanel.table;

import nl.topicus.cobra.web.components.datapanel.CustomDataPanelContentDescription;
import nl.topicus.cobra.web.components.datapanel.columns.BooleanPropertyColumn;
import nl.topicus.cobra.web.components.datapanel.columns.CustomPropertyColumn;
import nl.topicus.eduarte.entities.security.authentication.Account;

public class AccountTable extends CustomDataPanelContentDescription<Account>
{
	private static final long serialVersionUID = 1L;

	public AccountTable()
	{
		super("Gebruikers");
		addColumn(new CustomPropertyColumn<Account>("Gebruikersnaam", "Gebruikersnaam",
			"gebruikersnaam", "gebruikersnaam"));
		addColumn(new CustomPropertyColumn<Account>("Eigenaar", "Eigenaar", "eigenaar"));
		addColumn(new CustomPropertyColumn<Account>("Rollen", "Rollen", "rolNamen"));
		addColumn(new BooleanPropertyColumn<Account>("Actief", "Actief", "actief", "actief"));
	}
}
