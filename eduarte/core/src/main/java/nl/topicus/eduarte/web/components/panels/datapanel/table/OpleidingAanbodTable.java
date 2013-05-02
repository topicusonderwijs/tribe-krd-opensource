package nl.topicus.eduarte.web.components.panels.datapanel.table;

import nl.topicus.cobra.web.components.datapanel.GroupProperty;
import nl.topicus.cobra.web.components.datapanel.columns.CustomPropertyColumn;
import nl.topicus.eduarte.entities.opleiding.OpleidingAanbod;
import nl.topicus.eduarte.entities.vrijevelden.VrijVeldCategorie;

/**
 * 
 * 
 * @author vanharen
 */
public class OpleidingAanbodTable extends AbstractVrijVeldableTable<OpleidingAanbod>
{
	private static final long serialVersionUID = 1L;

	public OpleidingAanbodTable()
	{
		this("Opleidingen/locaties");
	}

	public OpleidingAanbodTable(String title)
	{
		super(title);
		createColumns();
		createOrganisatieColumns();
		createVrijVeldKolommen(VrijVeldCategorie.OPLEIDING, "opleiding");
		createGroupProperties();
	}

	private void createColumns()
	{
		addColumn(new CustomPropertyColumn<OpleidingAanbod>("Code", "Code", "opleiding.code",
			"opleiding.code", false));
		addColumn(new CustomPropertyColumn<OpleidingAanbod>("Naam", "Naam", "opleiding.naam",
			"opleiding.naam", false));
		addColumn(new CustomPropertyColumn<OpleidingAanbod>("Leerweg", "Leerweg",
			"opleiding.leerweg", "opleiding.leerweg", false));
		addColumn(new CustomPropertyColumn<OpleidingAanbod>("Default intensiteit",
			"Default intensiteit", "opleiding.defaultIntensiteit", "opleiding.defaultIntensiteit",
			false).setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<OpleidingAanbod>("Verbintenisgebied",
			"Verbintenisgebied", "verbintenisgebied.naam", "opleiding.verbintenisgebied.naam",
			false));
		addColumn(new CustomPropertyColumn<OpleidingAanbod>("Taxonomiecode", "Taxonomiecode",
			"verbintenisgebied.sorteercode", "opleiding.verbintenisgebied.taxonomiecode", false));
		addColumn(new CustomPropertyColumn<OpleidingAanbod>("Externe code", "Externe code",
			"verbintenisgebied.externeCode", "opleiding.verbintenisgebied.externeCode", false));
		addColumn(new CustomPropertyColumn<OpleidingAanbod>("Niveau (MBO)", "Niveau",
			"opleiding.verbintenisgebied.niveauNaam", false).setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<OpleidingAanbod>("Begindatum", "Begindatum",
			"opleiding.begindatum", "opleiding.begindatum", false).setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<OpleidingAanbod>("Einddatum", "Einddatum",
			"opleiding.einddatum", "opleiding.einddatum", false).setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<OpleidingAanbod>("Duur (maanden)", "Duur (maanden)",
			"opleiding.duurInMaanden", "opleiding.duurInMaanden", false).setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<OpleidingAanbod>("Leerjaar van", "Leerjaar van",
			"opleiding.beginLeerjaar", "opleiding.beginLeerjaar", false).setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<OpleidingAanbod>("Leerjaar tot", "Leerjaar tot",
			"opleiding.eindLeerjaar", "opleiding.eindLeerjaar", false).setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<OpleidingAanbod>("Diplomatekst 1", "Diplomatekst 1",
			"opleiding.diplomatekst1", "opleiding.diplomatekst1", false).setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<OpleidingAanbod>("Diplomatekst 2", "Diplomatekst 2",
			"opleiding.diplomatekst2", "opleiding.diplomatekst2", false).setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<OpleidingAanbod>("Diplomatekst 3", "Diplomatekst 3",
			"opleiding.diplomatekst3", "opleiding.diplomatekst3", false).setDefaultVisible(false));
	}

	private void createGroupProperties()
	{
		addGroupProperty(new GroupProperty<OpleidingAanbod>("opleiding.verbintenisgebied",
			"Verbintenisgebied", "opleiding.verbintenisgebied"));
		addGroupProperty(new GroupProperty<OpleidingAanbod>("opleiding.naam", "Opleiding",
			"opleiding.naam"));
	}

	private void createOrganisatieColumns()
	{
		addColumn(new CustomPropertyColumn<OpleidingAanbod>("Organisatie", "Organisatie",
			"organisatieEenheid.naam", "organisatieEenheid.naam"));
		addColumn(new CustomPropertyColumn<OpleidingAanbod>("Locatie", "Locatie", "locatie.naam",
			"locatie.naam"));
		addColumn(new CustomPropertyColumn<OpleidingAanbod>("Team", "Team", "team.naam",
			"team.naam").setDefaultVisible(false));
	}
}
