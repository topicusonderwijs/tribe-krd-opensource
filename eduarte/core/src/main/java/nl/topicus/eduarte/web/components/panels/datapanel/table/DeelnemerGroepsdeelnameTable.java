package nl.topicus.eduarte.web.components.panels.datapanel.table;

import nl.topicus.cobra.web.components.datapanel.columns.CustomPropertyColumn;
import nl.topicus.cobra.web.components.datapanel.columns.DatePropertyColumn;
import nl.topicus.eduarte.entities.groep.Groepsdeelname;
import nl.topicus.eduarte.entities.vrijevelden.VrijVeldCategorie;

public class DeelnemerGroepsdeelnameTable extends AbstractVrijVeldableTable<Groepsdeelname>
{

	private static final long serialVersionUID = 1L;

	public DeelnemerGroepsdeelnameTable()
	{
		super("Groepen");
		createColumns();
		createVrijVeldKolommen(VrijVeldCategorie.PLAATSING, "");
	}

	private void createColumns()
	{
		addColumn(new CustomPropertyColumn<Groepsdeelname>("Code", "Code", "groep.code",
			"groep.code"));
		addColumn(new CustomPropertyColumn<Groepsdeelname>("Naam", "Naam", "groep.naam",
			"groep.naam"));
		addColumn(new CustomPropertyColumn<Groepsdeelname>("Type", "Type", "groep.groepstype",
			"groep.groepstype").setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<Groepsdeelname>("Docenten", "Docenten",
			"groep.groepDocentenAfkortingenOpPeildatum"));
		addColumn(new CustomPropertyColumn<Groepsdeelname>("Mentoren", "Mentoren",
			"groep.groepMentorenAfkortingenOpPeildatum"));
		addColumn(new CustomPropertyColumn<Groepsdeelname>("Leerjaar", "Leerjaar",
			"groep.leerjaar", "groep.leerjaar"));
		addColumn(new CustomPropertyColumn<Groepsdeelname>("Organisatie-eenheid", "Organisatie",
			"groep.organisatieEenheid", "groep.organisatieEenheid.naam"));
		addColumn(new CustomPropertyColumn<Groepsdeelname>("Locatie", "Locatie", "groep.locatie",
			"groep.locatie.naam"));
		addColumn(new DatePropertyColumn<Groepsdeelname>("begindatum", "Begindatum", "begindatum",
			"begindatum"));
		addColumn(new DatePropertyColumn<Groepsdeelname>("einddatum", "Einddatum", "einddatum",
			"einddatum"));
	}
}
