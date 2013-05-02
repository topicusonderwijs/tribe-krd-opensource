package nl.topicus.eduarte.web.components.panels.datapanel.table;

import nl.topicus.cobra.web.components.datapanel.CustomDataPanelContentDescription;
import nl.topicus.cobra.web.components.datapanel.columns.CustomPropertyColumn;
import nl.topicus.eduarte.entities.criteriumbank.Criterium;

/**
 * Columns voor tabellen van criteria
 * 
 * @author loite
 */
public class CriteriumTable extends CustomDataPanelContentDescription<Criterium>
{
	private static final long serialVersionUID = 1L;

	public CriteriumTable()
	{
		super("Criteria");
		createColumns();
	}

	private void createColumns()
	{
		addColumn(new CustomPropertyColumn<Criterium>("Volgnummer", "Volgnummer", "volgnummer",
			"volgnummer"));
		addColumn(new CustomPropertyColumn<Criterium>("Naam", "Naam", "naam", "naam"));
		addColumn(new CustomPropertyColumn<Criterium>("Melding", "Melding", "melding", "melding"));
		addColumn(new CustomPropertyColumn<Criterium>("Formule", "Formule", "formule")
			.setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<Criterium>("Opleiding", "Opleiding", "opleiding.naam",
			"opleiding.naam").setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<Criterium>("Verbintenisgebied", "Verbintenisgebied",
			"verbintenisgebied.naam", "verbintenisgebied.naam").setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<Criterium>("Cohort", "Cohort", "cohort.naam",
			"cohort.naam").setDefaultVisible(false));
	}

}
