package nl.topicus.eduarte.web.components.panels.datapanel.table;

import nl.topicus.cobra.web.components.datapanel.CustomDataPanelContentDescription;
import nl.topicus.cobra.web.components.datapanel.columns.BooleanPropertyColumn;
import nl.topicus.cobra.web.components.datapanel.columns.CustomPropertyColumn;
import nl.topicus.eduarte.entities.resultaatstructuur.Schaalwaarde;

public class SchaalwaardeTable extends CustomDataPanelContentDescription<Schaalwaarde>
{
	private static final long serialVersionUID = 1L;

	public SchaalwaardeTable()
	{
		super("Schaalwaarden");
		addColumn(new CustomPropertyColumn<Schaalwaarde>("Naam", "Naam", "naam", "naam"));
		addColumn(new CustomPropertyColumn<Schaalwaarde>("Volgnummer", "Volgnummer", "volgnummer",
			"volgnummer"));
		addColumn(new CustomPropertyColumn<Schaalwaarde>("Interne waarde", "Interne waarde",
			"interneWaarde", "interneWaarde"));
		addColumn(new CustomPropertyColumn<Schaalwaarde>("Externe waarde", "Externe waarde",
			"externeWaarde", "externeWaarde"));
		addColumn(new BooleanPropertyColumn<Schaalwaarde>("Behaald", "Behaald", "behaald",
			"behaald").setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<Schaalwaarde>("Vanaf cijfer", "Vanaf", "vanafCijfer",
			"vanafCijfer").setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<Schaalwaarde>("T/m cijfer", "T/m", "totCijfer",
			"totCijfer").setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<Schaalwaarde>("Nominale waarde", "Nominaal",
			"nominaleWaarde", "nominaleWaarde").setDefaultVisible(false));
	}
}
