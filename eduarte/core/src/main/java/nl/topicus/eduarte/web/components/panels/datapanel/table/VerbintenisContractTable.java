package nl.topicus.eduarte.web.components.panels.datapanel.table;

import nl.topicus.cobra.web.components.datapanel.CustomDataPanelContentDescription;
import nl.topicus.cobra.web.components.datapanel.columns.CustomPropertyColumn;
import nl.topicus.cobra.web.components.datapanel.columns.DatePropertyColumn;
import nl.topicus.eduarte.entities.inschrijving.VerbintenisContract;

public class VerbintenisContractTable extends
		CustomDataPanelContentDescription<VerbintenisContract>
{
	private static final long serialVersionUID = 1L;

	public VerbintenisContractTable()
	{
		super("Contracten");
		addColumn(new CustomPropertyColumn<VerbintenisContract>("Contract", "Contract",
			"contract.naam", "contract.naam"));
		addColumn(new CustomPropertyColumn<VerbintenisContract>("Onderdeel", "Onderdeel",
			"onderdeel.naam", "onderdeel.naam"));
		addColumn(new CustomPropertyColumn<VerbintenisContract>("Contactpersoon", "Contactpersoon",
			"externeOrganisatieContactPersoon", "externeOrganisatieContactPersoon"));
		addColumn(new DatePropertyColumn<VerbintenisContract>("Begindatum", "Begindatum",
			"begindatum"));
		addColumn(new DatePropertyColumn<VerbintenisContract>("Einddatum", "Einddatum", "einddatum"));
		addColumn(new CustomPropertyColumn<VerbintenisContract>("Extern nummer", "Extern nummer",
			"externNummer", "externNummer"));
		addColumn(new CustomPropertyColumn<VerbintenisContract>("Externe organisatie",
			"Externe organisatie", "externeOrganisatie").setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<VerbintenisContract>("Datum beschikking",
			"Datum beschikking", "datumBeschikking").setDefaultVisible(false));
	}
}
