package nl.topicus.eduarte.krdparticipatie.web.components.tables;

import nl.topicus.cobra.web.components.datapanel.CustomDataPanelContentDescription;
import nl.topicus.cobra.web.components.datapanel.columns.CustomPropertyColumn;
import nl.topicus.eduarte.entities.ibgverzuimloket.IbgVerzuimmelding;

public class CollectiviteitVerzuimmeldingTable extends
		CustomDataPanelContentDescription<IbgVerzuimmelding>
{
	private static final long serialVersionUID = 1L;

	public CollectiviteitVerzuimmeldingTable()
	{
		super("Verzuimmeldingen");
		addColumn(new CustomPropertyColumn<IbgVerzuimmelding>("Meldingsnummer", "Meldingsnummer",
			"meldingsnummer", "meldingsnummer"));
		addColumn(new CustomPropertyColumn<IbgVerzuimmelding>("Naam", "Naam", "naam",
			"verbintenis.deelnemer.persoon.achternaam"));
		addColumn(new CustomPropertyColumn<IbgVerzuimmelding>("BSN", "Bsn", "bsn",
			"verbintenis.deelnemer.persoon.bsn"));
		addColumn(new CustomPropertyColumn<IbgVerzuimmelding>("Deelnemernummer", "Nummer",
			"nummer", "verbintenis.deelnemer.deelnemernummer"));
		addColumn(new CustomPropertyColumn<IbgVerzuimmelding>("Startkwalificatieplichtig tot",
			"Startkwalificatieplichtig tot", "verbintenis.deelnemer.startkwalificatieplichtigTot"));
		addColumn(new CustomPropertyColumn<IbgVerzuimmelding>("Verzuimsoort", "Soort ", "soort",
			"verzuimsoort"));
		addColumn(new CustomPropertyColumn<IbgVerzuimmelding>("Melddatum", "Melddatum",
			"melddatum", "melddatumtijd"));
		addColumn(new CustomPropertyColumn<IbgVerzuimmelding>("Status", "Status", "status",
			"status"));
		addColumn(new CustomPropertyColumn<IbgVerzuimmelding>("Melder", "Melder", "melder",
			"aanduidingContactpersoon"));
	}
}
