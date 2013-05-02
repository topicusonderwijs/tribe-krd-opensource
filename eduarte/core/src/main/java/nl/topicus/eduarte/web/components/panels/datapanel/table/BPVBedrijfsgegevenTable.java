package nl.topicus.eduarte.web.components.panels.datapanel.table;

import nl.topicus.cobra.web.components.datapanel.CustomDataPanelContentDescription;
import nl.topicus.cobra.web.components.datapanel.columns.CustomPropertyColumn;
import nl.topicus.eduarte.app.EduArteApp;
import nl.topicus.eduarte.entities.bpv.BPVBedrijfsgegeven;

/**
 * Tabel met de mogelijke kolommen voor BPVBedrijfsgegens
 * 
 * @author vandekamp
 */
public class BPVBedrijfsgegevenTable extends CustomDataPanelContentDescription<BPVBedrijfsgegeven>
{
	private static final long serialVersionUID = 1L;

	public BPVBedrijfsgegevenTable(boolean addBedrijf)
	{
		super(EduArteApp.get().getBPVTerm() + "bedrijfsgegevens");
		createColumns(addBedrijf);
	}

	private void createColumns(boolean addBedrijf)
	{
		if (addBedrijf)
		{
			addColumn(new CustomPropertyColumn<BPVBedrijfsgegeven>("Soort", "Soort",
				"externeOrganisatie.soortExterneOrganisatie",
				"externeOrganisatie.soortExterneOrganisatie"));
			addColumn(new CustomPropertyColumn<BPVBedrijfsgegeven>("Naam", "Naam",
				"externeOrganisatie.naam", "externeOrganisatie.naam"));
			addColumn(new CustomPropertyColumn<BPVBedrijfsgegeven>("Plaats", "Plaats",
				"externeOrganisatie.fysiekAdres.adres.plaats"));
			addColumn(new CustomPropertyColumn<BPVBedrijfsgegeven>("Adres", "Adres",
				"externeOrganisatie.fysiekAdres.adres.volledigAdresOp1Regel")
				.setDefaultVisible(false));
		}
		addColumn(new CustomPropertyColumn<BPVBedrijfsgegeven>("Kenniscentrum", "Kenniscentrum",
			"kenniscentrum.naam"));
		addColumn(new CustomPropertyColumn<BPVBedrijfsgegeven>("Code leerbedrijf",
			"Code leerbedrijf", "codeLeerbedrijf"));
		addColumn(new CustomPropertyColumn<BPVBedrijfsgegeven>("Herkomst code", "Herkomst code",
			"herkomstCode").setDefaultVisible(!addBedrijf));
		addColumn(new CustomPropertyColumn<BPVBedrijfsgegeven>("Relatienummer", "Relatienummer",
			"relatienummer").setDefaultVisible(!addBedrijf));

	}
}
