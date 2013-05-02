package nl.topicus.eduarte.entities.signalering.settings;

import nl.topicus.cobra.web.components.datapanel.CustomDataPanelContentDescription;
import nl.topicus.cobra.web.components.datapanel.columns.BooleanPropertyColumn;
import nl.topicus.cobra.web.components.datapanel.columns.CustomPropertyColumn;
import nl.topicus.eduarte.entities.signalering.VerzuimTaakSignaalDefinitie;

public class VerzuimTaakSignaalDefinitieTable<T> extends
		CustomDataPanelContentDescription<VerzuimTaakSignaalDefinitie>
{
	private static final long serialVersionUID = 1L;

	public VerzuimTaakSignaalDefinitieTable(String title)
	{
		super(title);
		addColumn(new CustomPropertyColumn<VerzuimTaakSignaalDefinitie>("Naam", "Naam",
			"signaalNaam", "signaalNaam"));
		addColumn(new CustomPropertyColumn<VerzuimTaakSignaalDefinitie>("Omschrijving",
			"Omschrijving", "omschrijving", "omschrijving"));
		addColumn(new CustomPropertyColumn<VerzuimTaakSignaalDefinitie>("Soort Deelnemers",
			"Soort Deelnemers", "soortDeelnemer", "soortDeelnemer"));
		addColumn(new CustomPropertyColumn<VerzuimTaakSignaalDefinitie>("Aantal uren",
			"Aantal uren", "aantalklokuren", "aantalklokuren"));
		addColumn(new CustomPropertyColumn<VerzuimTaakSignaalDefinitie>("Looptijd", "Looptijd",
			"aantalWeken", "aantalWeken"));
		addColumn(new CustomPropertyColumn<VerzuimTaakSignaalDefinitie>("Aantal Weken",
			"Aantal Weken", "aantalWekenAanEen", "aantalWekenAanEen"));
		addColumn(new BooleanPropertyColumn<VerzuimTaakSignaalDefinitie>("Alleen ongeoorloofd",
			"Alleen ongeoorloofd", "ongeoorlooft", "ongeoorlooft"));
	}
}
