package nl.topicus.eduarte.krdparticipatie.web.components.tables;

import nl.topicus.cobra.web.components.datapanel.CustomDataPanelContentDescription;
import nl.topicus.cobra.web.components.datapanel.columns.CustomPropertyColumn;
import nl.topicus.eduarte.entities.ibgverzuimloket.IbgVerzuimmelding;

public class DeelnemerVerzuimmeldingTable extends
		CustomDataPanelContentDescription<IbgVerzuimmelding>
{
	private static final long serialVersionUID = 1L;

	public DeelnemerVerzuimmeldingTable()
	{
		super("Verzuimmeldingen");
		addColumn(new CustomPropertyColumn<IbgVerzuimmelding>("meldingsnummer", "Meldingsnummer",
			"meldingsnummer", "meldingsnummer"));
		addColumn(new CustomPropertyColumn<IbgVerzuimmelding>("soort", "Soort", "soort",
			"verzuimsoort"));
		addColumn(new CustomPropertyColumn<IbgVerzuimmelding>("vanafDatum", "Datum/tijd",
			"vanafDatum", "begindatum"));
		addColumn(new CustomPropertyColumn<IbgVerzuimmelding>("status", "Status", "status",
			"status"));
		addColumn(new CustomPropertyColumn<IbgVerzuimmelding>("melder", "Melder", "melder",
			"aanduidingContactpersoon"));
	}
}
