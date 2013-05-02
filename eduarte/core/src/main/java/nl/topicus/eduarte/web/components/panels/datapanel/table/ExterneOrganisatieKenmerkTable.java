package nl.topicus.eduarte.web.components.panels.datapanel.table;

import nl.topicus.cobra.web.components.datapanel.CustomDataPanelContentDescription;
import nl.topicus.cobra.web.components.datapanel.columns.CustomPropertyColumn;
import nl.topicus.eduarte.entities.kenmerk.ExterneOrganisatieKenmerk;

public class ExterneOrganisatieKenmerkTable extends
		CustomDataPanelContentDescription<ExterneOrganisatieKenmerk>
{

	private static final long serialVersionUID = 1L;

	public ExterneOrganisatieKenmerkTable()
	{
		super("Kenmerken");
		createColumns();
	}

	private void createColumns()
	{
		addColumn(new CustomPropertyColumn<ExterneOrganisatieKenmerk>("Categorie", "Categorie",
			"kenmerk.categorie.naam"));
		addColumn(new CustomPropertyColumn<ExterneOrganisatieKenmerk>("Kenmerk", "Kenmerk",
			"kenmerk.naam"));
		addColumn(new CustomPropertyColumn<ExterneOrganisatieKenmerk>("Toelichting", "Toelichting",
			"toelichting"));
		addColumn(new CustomPropertyColumn<ExterneOrganisatieKenmerk>("Begindatum", "Begindatum",
			"begindatum"));
		addColumn(new CustomPropertyColumn<ExterneOrganisatieKenmerk>("Einddatum", "Einddatum",
			"einddatum"));
	}
}