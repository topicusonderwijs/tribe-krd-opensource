package nl.topicus.eduarte.web.components.panels.datapanel.table;

import nl.topicus.cobra.web.components.datapanel.CustomDataPanelContentDescription;
import nl.topicus.cobra.web.components.datapanel.columns.CustomPropertyColumn;
import nl.topicus.eduarte.entities.kenmerk.DeelnemerKenmerk;

public class DeelnemerKenmerkTable extends CustomDataPanelContentDescription<DeelnemerKenmerk>
{

	private static final long serialVersionUID = 1L;

	public DeelnemerKenmerkTable()
	{
		super("Kenmerken");
		createColumns();
	}

	private void createColumns()
	{
		addColumn(new CustomPropertyColumn<DeelnemerKenmerk>("Categorie", "Categorie",
			"kenmerk.categorie.naam"));
		addColumn(new CustomPropertyColumn<DeelnemerKenmerk>("Kenmerk", "Kenmerk", "kenmerk.naam"));
		addColumn(new CustomPropertyColumn<DeelnemerKenmerk>("Toelichting", "Toelichting",
			"toelichting"));
		addColumn(new CustomPropertyColumn<DeelnemerKenmerk>("Begindatum", "Begindatum",
			"begindatum"));
		addColumn(new CustomPropertyColumn<DeelnemerKenmerk>("Einddatum", "Einddatum", "einddatum"));
	}
}