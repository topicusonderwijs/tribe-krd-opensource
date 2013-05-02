package nl.topicus.eduarte.web.components.modalwindow.stagemarktbedrijfsgegevens;

import nl.topicus.cobra.web.components.datapanel.CustomDataPanelContentDescription;
import nl.topicus.cobra.web.components.datapanel.columns.CustomPropertyColumn;
import nl.topicus.eduarte.web.components.factory.StagemarktBedrijfsgegevens;

public class StagemarktBedrijfsgegevensTable extends
		CustomDataPanelContentDescription<StagemarktBedrijfsgegevens>
{
	private static final long serialVersionUID = 1L;

	public StagemarktBedrijfsgegevensTable()
	{
		super("Stagemarkt bedrijfsgegevens");

		addColumn(new CustomPropertyColumn<StagemarktBedrijfsgegevens>("Bedrijfsnaam",
			"Bedrijfsnaam", "bedrijfsnaam", "bedrijfsnaam"));
		addColumn(new CustomPropertyColumn<StagemarktBedrijfsgegevens>("Postcode", "Postcode",
			"postcode", "postcode"));
		addColumn(new CustomPropertyColumn<StagemarktBedrijfsgegevens>("Huisnummer", "Huisnummer",
			"huisnummer", "huisnummer"));
		addColumn(new CustomPropertyColumn<StagemarktBedrijfsgegevens>("CodeLeerbedrijf",
			"CodeLeerbedrijf", "codeleerbedrijf"));
	}
}
