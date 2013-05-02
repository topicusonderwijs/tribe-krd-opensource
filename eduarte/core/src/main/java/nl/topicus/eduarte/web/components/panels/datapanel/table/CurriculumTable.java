package nl.topicus.eduarte.web.components.panels.datapanel.table;

import nl.topicus.cobra.web.components.datapanel.CustomDataPanelContentDescription;
import nl.topicus.cobra.web.components.datapanel.columns.CustomPropertyColumn;
import nl.topicus.eduarte.entities.curriculum.Curriculum;

public class CurriculumTable extends CustomDataPanelContentDescription<Curriculum>
{
	private static final long serialVersionUID = 1L;

	public CurriculumTable()
	{
		this(false);
	}

	public CurriculumTable(boolean opleidingTonen)
	{
		super("Curriculum per cohort");
		createColumns(opleidingTonen);
	}

	private void createColumns(boolean opleidingTonen)
	{
		addColumn(new CustomPropertyColumn<Curriculum>("Organisatie-eenheid",
			"Organisatie-eenheid", "organisatieEenheid", "organisatieEenheid"));
		addColumn(new CustomPropertyColumn<Curriculum>("Locatie", "Locatie", "locatie", "locatie"));
		addColumn(new CustomPropertyColumn<Curriculum>("Opleiding", "Opleiding", "opleiding",
			"opleiding").setDefaultVisible(opleidingTonen));
		addColumn(new CustomPropertyColumn<Curriculum>("Cohort", "Cohort", "cohort", "cohort"));
		addColumn(new CustomPropertyColumn<Curriculum>("AantalOnderwijsproducten", "Aantal regels",
			"aantalGekoppeldeOnderwijsproducten"));
	}
}