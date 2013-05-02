package nl.topicus.eduarte.krd.web.components.panels.datapanel.table;

import nl.topicus.cobra.web.components.datapanel.CustomDataPanel;
import nl.topicus.cobra.web.components.datapanel.CustomDataPanelContentDescription;
import nl.topicus.cobra.web.components.datapanel.GroupProperty;
import nl.topicus.cobra.web.components.datapanel.columns.CustomPropertyColumn;
import nl.topicus.eduarte.krd.entities.bron.BronAanleverpuntLocatie;

/**
 * Definieert de content voor het tonen van de BRON aanleverpunten van een instelling in
 * een {@link CustomDataPanel}.
 */
public class BronAanleverpuntTable extends
		CustomDataPanelContentDescription<BronAanleverpuntLocatie>
{
	private static final long serialVersionUID = 1L;

	public BronAanleverpuntTable()
	{
		super("BRON aanleverpunten");

		createColumns();
		createGroupProperties();

	}

	private void createColumns()
	{
		addColumn(new CustomPropertyColumn<BronAanleverpuntLocatie>("Aanleverpunt", "Aanleverpunt",
			"aanleverpunt.emptyString"));
		addColumn(new CustomPropertyColumn<BronAanleverpuntLocatie>("Locatie", "Locatie",
			"locatie.naam"));
		addColumn(new CustomPropertyColumn<BronAanleverpuntLocatie>("Laatste BatchNr BO",
			"Laatste BatchNr BO", "aanleverpunt.laatsteBatchNrBO"));
		addColumn(new CustomPropertyColumn<BronAanleverpuntLocatie>("Laatste BatchNr ED",
			"Laatste BatchNr ED", "aanleverpunt.laatsteBatchNrED"));
		addColumn(new CustomPropertyColumn<BronAanleverpuntLocatie>("Laatste BatchNr VAVO",
			"Laatste BatchNr VAVO", "aanleverpunt.laatsteBatchNrVAVO"));
		addColumn(new CustomPropertyColumn<BronAanleverpuntLocatie>("Laatste BatchNr VO",
			"Laatste BatchNr VO", "aanleverpunt.laatsteBatchNrVO"));
	}

	private void createGroupProperties()
	{
		GroupProperty<BronAanleverpuntLocatie> groupProperty =
			new GroupProperty<BronAanleverpuntLocatie>("aanleverpunt.nummer", "Aanleverpunt",
				"aanleverpunt");
		addGroupProperty(groupProperty);
		setDefaultGroupProperty(groupProperty);
	}
}
