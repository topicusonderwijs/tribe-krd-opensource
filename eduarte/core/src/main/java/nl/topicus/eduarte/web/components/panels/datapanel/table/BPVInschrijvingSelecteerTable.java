package nl.topicus.eduarte.web.components.panels.datapanel.table;

import nl.topicus.cobra.web.components.datapanel.CustomDataPanelContentDescription;
import nl.topicus.cobra.web.components.datapanel.columns.AjaxRadioColumn;
import nl.topicus.cobra.web.components.datapanel.columns.CustomPropertyColumn;
import nl.topicus.cobra.web.components.datapanel.columns.DatePropertyColumn;
import nl.topicus.eduarte.entities.bpv.BPVInschrijving;

public class BPVInschrijvingSelecteerTable extends
		CustomDataPanelContentDescription<BPVInschrijving>
{
	private static final long serialVersionUID = 1L;

	public BPVInschrijvingSelecteerTable()
	{
		super("Beroepspraktijkvorming");
		addColumn(new AjaxRadioColumn<BPVInschrijving>("selectie", "Selecteer")
		{
			private static final long serialVersionUID = 1L;

			@Override
			public String getCssClass()
			{
				// TODO Auto-generated method stub
				return "unit_60";
			}
		});
		addColumn(new CustomPropertyColumn<BPVInschrijving>("Nummer", "Nummer",
			"verbintenis.deelnemer.deelnemernummer"));
		addColumn(new DatePropertyColumn<BPVInschrijving>("Begindatum", "Begindatum", "begindatum",
			"begindatum"));
		addColumn(new CustomPropertyColumn<BPVInschrijving>("Einddatum", "Einddatum", "tot"));
		addColumn(new CustomPropertyColumn<BPVInschrijving>("BPV-bedrijf", "BPV-bedrijf",
			"bpvBedrijf.naam"));
		addColumn(new CustomPropertyColumn<BPVInschrijving>("Praktijkbegeleider",
			"Praktijkbegeleider", "praktijkbegeleider.persoon.volledigeNaam"));
		addColumn(new CustomPropertyColumn<BPVInschrijving>("Totale omvang", "Totale omvang",
			"totaleOmvang"));
		addColumn(new CustomPropertyColumn<BPVInschrijving>("Reden beëindiging",
			"Reden beëindiging", "redenUitschrijving"));
	}
}
