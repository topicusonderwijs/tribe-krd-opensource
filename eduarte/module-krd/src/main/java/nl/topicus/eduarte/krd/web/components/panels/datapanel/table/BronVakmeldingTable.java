package nl.topicus.eduarte.krd.web.components.panels.datapanel.table;

import nl.topicus.cobra.web.components.datapanel.CustomDataPanelContentDescription;
import nl.topicus.cobra.web.components.datapanel.columns.CustomPropertyColumn;
import nl.topicus.eduarte.krd.entities.bron.meldingen.aanlevering.IVakMelding;

/**
 * Definieert de content voor het tonen van de {@link IVakMelding}. .
 */
public class BronVakmeldingTable extends CustomDataPanelContentDescription<IVakMelding>
{
	private static final long serialVersionUID = 1L;

	public BronVakmeldingTable()
	{
		super("Vakken in deze examenmelding");
		createColumns();
	}

	private void createColumns()
	{
		addColumn(new CustomPropertyColumn<IVakMelding>("Vakcode ", "Vak- code ", "examenvak"));
		addColumn(new CustomPropertyColumn<IVakMelding>("Diploma", "Diploma", "indicatieDiplomavak"));
		addColumn(new CustomPropertyColumn<IVakMelding>("ToepassingResultaat", "Toep. result",
			"toepassingResultaatOfBeoordelingExamenVak"));
		addColumn(new CustomPropertyColumn<IVakMelding>("IndicatieWerkstuk", "Ind. werkstuk",
			"indicatieWerkstuk"));
		addColumn(new CustomPropertyColumn<IVakMelding>("BeoordelingSE", "Beoord. SE",
			"beoordelingSchoolExamen"));
		addColumn(new CustomPropertyColumn<IVakMelding>("CijferSE", "SE-cijfer",
			"cijferSchoolExamen"));
		addColumn(new CustomPropertyColumn<IVakMelding>("CE1", "CE1", "cijferCE1"));
		addColumn(new CustomPropertyColumn<IVakMelding>("CE2", "CE2", "cijferCE2"));
		addColumn(new CustomPropertyColumn<IVakMelding>("CE3", "CE3", "cijferCE3"));
		addColumn(new CustomPropertyColumn<IVakMelding>("Eind1", "Eind1", "eersteEindcijfer"));
		addColumn(new CustomPropertyColumn<IVakMelding>("Eind2", "Eind2", "tweedeEindcijfer"));
		addColumn(new CustomPropertyColumn<IVakMelding>("Eind3", "Eind3", "derdeEindcijfer"));
		addColumn(new CustomPropertyColumn<IVakMelding>("CijferCijferlijst", "Cijfer- lijst",
			"cijferCijferlijst"));
		addColumn(new CustomPropertyColumn<IVakMelding>("VolgendeTijdvak", "Volgend tijdvak",
			"verwezenNaarVolgendeTijdvak"));
		addColumn(new CustomPropertyColumn<IVakMelding>("Certificaat", "Certificaat", "certificaat"));
		addColumn(new CustomPropertyColumn<IVakMelding>("IndCombinatieCijfer",
			"Ind. combi. cijfer", "indicatieCombinatieCijfer"));
		addColumn(new CustomPropertyColumn<IVakMelding>("HogerNiveau", "Hoger niv.", "hogerNiveau"));
		addColumn(new CustomPropertyColumn<IVakMelding>("VakcodeHogerNiveau", "Vakcode hoger niv.",
			"vakCodeHogerNiveau"));
	}
}
