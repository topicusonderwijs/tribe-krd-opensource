package nl.topicus.eduarte.web.components.panels.datapanel.table;

import nl.topicus.cobra.web.components.datapanel.GroupProperty;
import nl.topicus.cobra.web.components.datapanel.columns.BooleanPropertyColumn;
import nl.topicus.cobra.web.components.datapanel.columns.CustomPropertyColumn;
import nl.topicus.eduarte.app.EduArteApp;
import nl.topicus.eduarte.app.EduArteModuleKey;
import nl.topicus.eduarte.entities.onderwijsproduct.Onderwijsproduct;
import nl.topicus.eduarte.entities.vrijevelden.VrijVeldCategorie;

/**
 * @author loite
 */
public class OnderwijsproductTable extends AbstractVrijVeldableTable<Onderwijsproduct>
{
	private static final long serialVersionUID = 1L;

	/**
	 * @param isHelePagina
	 *            Wordt de tabel op de helft van een pagina getoond, of over een hele
	 *            pagina?
	 */
	public OnderwijsproductTable(boolean isHelePagina)
	{
		super("Onderwijsproducten");
		addAllColumns(isHelePagina);
		createVrijVeldKolommen(VrijVeldCategorie.ONDERWIJSPRODUCT, "");
		addGroupProperties();

		if (EduArteApp.get().isModuleActive(EduArteModuleKey.ONDERWIJSCATALOGUS_AMARANTIS))
			addAmarantisColumns();
	}

	private void addGroupProperties()
	{
		addGroupProperty(new GroupProperty<Onderwijsproduct>("soortProduct.naam", "Soort product",
			"soortProduct.naam"));
		addGroupProperty(new GroupProperty<Onderwijsproduct>("status", "Status", "status"));
		addGroupProperty(new GroupProperty<Onderwijsproduct>("aggregatieniveau",
			"Aggregatieniveau", "aggregatieniveau"));
		addGroupProperty(new GroupProperty<Onderwijsproduct>("leerstijl.naam", "Leerstijl",
			"leerstijl.naam"));
		addGroupProperty(new GroupProperty<Onderwijsproduct>("typeToets.naam", "Type toets",
			"typeToets.naam"));
		addGroupProperty(new GroupProperty<Onderwijsproduct>("typeLocatie.naam", "Type locatie",
			"typeLocatie.naam"));
	}

	private void addAmarantisColumns()
	{
		addColumn(new BooleanPropertyColumn<Onderwijsproduct>("Individueel", "Individueel",
			"individueel", "individueel").setDefaultVisible(false));
		addColumn(new BooleanPropertyColumn<Onderwijsproduct>("Onafhankelijk", "Onafhankelijk",
			"Onafhankelijk", "Onafhankelijk").setDefaultVisible(false));
		addColumn(new BooleanPropertyColumn<Onderwijsproduct>("Begeleid", "Begeleid", "begeleid",
			"begeleid").setDefaultVisible(false));

		addColumn(new CustomPropertyColumn<Onderwijsproduct>("Vereiste voorkennis",
			"Vereiste voorkennis", "vereisteVoorkennis", "vereisteVoorkennis")
			.setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<Onderwijsproduct>("Niveauaanduiding",
			"Niveauaanduiding", "niveauaanduiding", "niveauaanduiding").setDefaultVisible(false));

		addColumn(new CustomPropertyColumn<Onderwijsproduct>("Belasting EC", "Belasting EC",
			"belastingEC", "belastingEC").setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<Onderwijsproduct>("Belasting Overig",
			"Belasting Overig", "belastingOverig", "belastingOverig").setDefaultVisible(false));

		addColumn(new CustomPropertyColumn<Onderwijsproduct>("Leerstofdrager", "Leerstofdrager",
			"leerstofdrager", "leerstofdrager").setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<Onderwijsproduct>("Literatuur", "Literatuur",
			"literatuur", "literatuur").setDefaultVisible(false));

		addColumn(new CustomPropertyColumn<Onderwijsproduct>("Hulpmiddelen", "Hulpmiddelen",
			"hulpmiddelen", "hulpmiddelen").setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<Onderwijsproduct>("Docentactiviteiten",
			"Docentactiviteiten", "docentactiviteiten", "docentactiviteiten")
			.setDefaultVisible(false));

		addColumn(new CustomPropertyColumn<Onderwijsproduct>("FrequentiePerWeek",
			"FrequentiePerWeek", "FrequentiePerWeek", "FrequentiePerWeek").setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<Onderwijsproduct>("Tijd Per Eenheid",
			"Tijd Per Eenheid", "tijdPerEenheid", "tijdPerEenheid").setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<Onderwijsproduct>("Aantal Weken", "Aantal Weken",
			"aantalWeken", "aantalWeken"));
	}

	private void addAllColumns(boolean isHelePagina)
	{
		addColumn(new CustomPropertyColumn<Onderwijsproduct>("Code", "Code", "code", "code"));
		addColumn(new CustomPropertyColumn<Onderwijsproduct>("Titel", "Titel", "titel", "titel"));
		addColumn(new CustomPropertyColumn<Onderwijsproduct>("Omschrijving", "Omschrijving",
			"omschrijving", "omschrijving").setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<Onderwijsproduct>("Soort product", "Soort",
			"soortProduct.naam", "soortProduct.naam"));
		addColumn(new CustomPropertyColumn<Onderwijsproduct>("Zoektermen", "Zoektermen",
			"zoektermenAlsString").setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<Onderwijsproduct>("Status", "Status", "status", "status")
			.setDefaultVisible(isHelePagina));
		addColumn(new CustomPropertyColumn<Onderwijsproduct>("Begindatum", "Begindatum",
			"begindatum", "begindatum").setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<Onderwijsproduct>("Einddatum", "Einddatum", "einddatum",
			"einddatum").setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<Onderwijsproduct>("Kalender", "Kalender", "kalender",
			"kalender").setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<Onderwijsproduct>("Uitvoeringsfrequentie",
			"Uitvoeringsfrequentie", "uitvoeringsfrequentie", "uitvoeringsfrequentie")
			.setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<Onderwijsproduct>("Credits", "Credits", "credits",
			"credits").setDefaultVisible(EduArteApp.get().isModuleActive(
			EduArteModuleKey.HOGER_ONDERWIJS)));
		addColumn(new CustomPropertyColumn<Onderwijsproduct>("Omvang", "Omvang", "omvang", "omvang")
			.setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<Onderwijsproduct>("Belasting", "Belasting", "belasting",
			"belasting").setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<Onderwijsproduct>("Aggregatieniveau",
			"Aggregativeniveau", "aggregatieniveau", "aggregatieniveau").setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<Onderwijsproduct>("Startonderwijsproduct",
			"Startonderwijsproduct", "startonderwijsproduct", "startonderwijsproductOmschrijving")
			.setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<Onderwijsproduct>("Leerstijl", "Leerstijl",
			"leerstijl.naam", "leerstijl.naam").setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<Onderwijsproduct>("Toegankelijkheid",
			"Toegankelijkheid", "toegankelijkheid").setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<Onderwijsproduct>("Minimum aantal deelnemers",
			"Min aant. deeln.", "minimumAantalDeelnemers", "minimumAantalDeelnemers")
			.setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<Onderwijsproduct>("Maximum aantal deelnemers",
			"Max aant. deeln.", "maximumAantalDeelnemers", "maximumAantalDeelnemers")
			.setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<Onderwijsproduct>("Juridisch eigenaar",
			"Juridisch eigenaar", "juridischEigenaar", "juridischEigenaar")
			.setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<Onderwijsproduct>("Gebruiksrecht", "Gebruiksrecht",
			"gebruiksrecht", "gebruiksrecht").setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<Onderwijsproduct>("Soort praktijklokaal",
			"Soort praktijklokaal", "soortPraktijklokaal.naam", "soortPraktijklokaal.naam")
			.setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<Onderwijsproduct>("Type toets", "Type toets",
			"typeToets.naam", "typeToets.naam").setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<Onderwijsproduct>("Personeelcompetenties",
			"Personeelcompetenties", "personeelCompetenties", "personeelCompetenties")
			.setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<Onderwijsproduct>("Personeel kennisgebied en -niveau",
			"Personeel kennisgebied en -niveau", "personeelKennisgebiedEnNiveau",
			"personeelKennisgebiedEnNiveau").setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<Onderwijsproduct>("Personeel - Wettelijke vereisten",
			"Wettelijke vereisten", "personeelWettelijkeVereisten", "personeelWettelijkeVereisten")
			.setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<Onderwijsproduct>("Personeelbevoegdheid",
			"Personeelbevoegdheid", "personeelBevoegdheid", "personeelBevoegdheid")
			.setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<Onderwijsproduct>("Type locatie", "Type locatie",
			"typeLocatie.naam", "typeLocatie.naam").setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<Onderwijsproduct>("Gebruiksmiddelen",
			"Gebruiksmiddelen", "gebruiksmiddelenAlsString").setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<Onderwijsproduct>("Verbruiksmiddelen",
			"Verbruiksmiddelen", "verbruiksmiddelenAlsString").setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<Onderwijsproduct>("Kostprijs", "Kostprijs", "kostprijs",
			"kostprijs").setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<Onderwijsproduct>("Taxonomiecodes", "Taxonomiecodes",
			"taxonomiecodes").setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<Onderwijsproduct>("Aanbod", "Aanbod", "aanbodAlsString")
			.setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<Onderwijsproduct>("Paklijst", "Paklijst",
			"onderdelenAlsString").setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<Onderwijsproduct>("Onderdeel van", "Onderdeel van",
			"onderdeelVanAlsString").setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<Onderwijsproduct>("Voorwaardelijke onderwijsproducten",
			"Voorwaarden", "voorwaardenAlsString").setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<Onderwijsproduct>("Voorwaardelijk voor",
			"Voorwaardelijk voor", "voorwaardelijkVoorAlsString").setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<Onderwijsproduct>("Schalen", "Schalen", "schaalNamen")
			.setDefaultVisible(false));
		addColumn(new BooleanPropertyColumn<Onderwijsproduct>("BijIntake", "Bij Intake",
			"bijIntake", "bijIntake").setDefaultVisible(false));
	}
}
