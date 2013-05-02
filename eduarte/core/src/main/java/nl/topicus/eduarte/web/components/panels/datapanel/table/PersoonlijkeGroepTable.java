package nl.topicus.eduarte.web.components.panels.datapanel.table;

import nl.topicus.cobra.web.components.datapanel.CustomDataPanelContentDescription;
import nl.topicus.cobra.web.components.datapanel.columns.CustomPropertyColumn;
import nl.topicus.eduarte.entities.participatie.PersoonlijkeGroep;

public class PersoonlijkeGroepTable extends CustomDataPanelContentDescription<PersoonlijkeGroep>
{
	private static final long serialVersionUID = 1L;

	public PersoonlijkeGroepTable()
	{
		super("Persoonlijke groepen");
		createColumns();
	}

	private void createColumns()
	{
		addColumn(new CustomPropertyColumn<PersoonlijkeGroep>("Code", "Code", "code", "code"));
		addColumn(new CustomPropertyColumn<PersoonlijkeGroep>("Omschrijving", "Omschrijving",
			"omschrijving", "omschrijving"));
		addColumn(new CustomPropertyColumn<PersoonlijkeGroep>("Eigenaar", "Eigenaar", "eigenaar"));
		addColumn(new CustomPropertyColumn<PersoonlijkeGroep>("Organisatie", "Organisatie",
			"organisatieEenheid", "organisatieEenheid.naam"));
		addColumn(new CustomPropertyColumn<PersoonlijkeGroep>("Begindatum", "Begindatum",
			"beginDatum", "beginDatum"));
		addColumn(new CustomPropertyColumn<PersoonlijkeGroep>("Einddatum", "Einddatum",
			"eindDatum", "eindDatum"));
		addColumn(new CustomPropertyColumn<PersoonlijkeGroep>("Publiek", "Publiek", "gedeeld",
			"gedeeldOmschrijving"));
	}
}
