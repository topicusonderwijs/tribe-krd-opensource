package nl.topicus.eduarte.web.components.panels.datapanel.table;

import nl.topicus.cobra.web.components.datapanel.CustomDataPanelContentDescription;
import nl.topicus.cobra.web.components.datapanel.columns.CustomPropertyColumn;
import nl.topicus.eduarte.entities.kenmerk.MedewerkerKenmerk;

public class MedewerkerKenmerkTable extends CustomDataPanelContentDescription<MedewerkerKenmerk>
{

	private static final long serialVersionUID = 1L;

	public MedewerkerKenmerkTable()
	{
		super("Kenmerken");
		createColumns();
	}

	private void createColumns()
	{
		addColumn(new CustomPropertyColumn<MedewerkerKenmerk>("Categorie", "Categorie",
			"kenmerk.categorie.naam"));
		addColumn(new CustomPropertyColumn<MedewerkerKenmerk>("Kenmerk", "Kenmerk", "kenmerk.naam"));
		addColumn(new CustomPropertyColumn<MedewerkerKenmerk>("Toelichting", "Toelichting",
			"toelichting"));
		addColumn(new CustomPropertyColumn<MedewerkerKenmerk>("Begindatum", "Begindatum",
			"begindatum"));
		addColumn(new CustomPropertyColumn<MedewerkerKenmerk>("Einddatum", "Einddatum", "einddatum"));
	}
}