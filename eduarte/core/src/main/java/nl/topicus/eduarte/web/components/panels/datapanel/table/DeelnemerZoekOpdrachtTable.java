package nl.topicus.eduarte.web.components.panels.datapanel.table;

import nl.topicus.cobra.web.components.datapanel.CustomDataPanelContentDescription;
import nl.topicus.cobra.web.components.datapanel.GroupProperty;
import nl.topicus.cobra.web.components.datapanel.columns.BooleanPropertyColumn;
import nl.topicus.cobra.web.components.datapanel.columns.CustomPropertyColumn;
import nl.topicus.eduarte.entities.rapportage.DeelnemerZoekOpdracht;

public class DeelnemerZoekOpdrachtTable extends
		CustomDataPanelContentDescription<DeelnemerZoekOpdracht>
{
	private static final long serialVersionUID = 1L;

	public DeelnemerZoekOpdrachtTable()
	{
		super("Opgeslagen zoekopdrachten");
		addColumn(new CustomPropertyColumn<DeelnemerZoekOpdracht>("Omschrijving", "Omschrijving",
			"omschrijving", "omschrijving"));
		addColumn(new BooleanPropertyColumn<DeelnemerZoekOpdracht>("Persoonlijk", "Persoonlijk",
			"account", "persoonlijk"));

		GroupProperty<DeelnemerZoekOpdracht> group =
			new GroupProperty<DeelnemerZoekOpdracht>("persoonlijk", "Persoonlijk", "account");
		addGroupProperty(group);
		setDefaultGroupProperty(group);
	}
}
