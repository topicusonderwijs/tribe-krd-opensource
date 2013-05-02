package nl.topicus.eduarte.krdparticipatie.web.components.tables;

import nl.topicus.cobra.web.components.datapanel.CustomDataPanelContentDescription;
import nl.topicus.cobra.web.components.datapanel.columns.BooleanPropertyColumn;
import nl.topicus.cobra.web.components.datapanel.columns.CustomPropertyColumn;
import nl.topicus.eduarte.entities.participatie.AbsentieMelding;

public class OpenAbsentieMeldingen extends CustomDataPanelContentDescription<AbsentieMelding>
{

	private static final long serialVersionUID = 1L;

	public OpenAbsentieMeldingen()
	{
		super("Open absentiemeldingen");
		createColumns();
	}

	private void createColumns()
	{

		addColumn(new CustomPropertyColumn<AbsentieMelding>("Nummer", "Nummer",
			"deelnemer.deelnemernummer"));
		addColumn(new CustomPropertyColumn<AbsentieMelding>("Roepnaam", "Roepnaam",
			"persoon.roepnaam", "deelnemer.persoon.roepnaam"));
		addColumn(new CustomPropertyColumn<AbsentieMelding>("Voorvoegsel", "Voorvoegsel",
			"persoon.voorvoegsel", "deelnemer.persoon.voorvoegsel"));
		addColumn(new CustomPropertyColumn<AbsentieMelding>("Achternaam", "Achternaam",
			"persoon.achternaam", "deelnemer.persoon.achternaam"));
		addColumn(new BooleanPropertyColumn<AbsentieMelding>("Afgehandeld", "Afgehandeld",
			"afgehandeld", "afgehandeld").setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<AbsentieMelding>("Begindatum", "Begindatum",
			"beginDatumTijd"));
		addColumn(new CustomPropertyColumn<AbsentieMelding>("Einddatum", "Einddatum",
			"eindDatumTijd"));

		addColumn(new CustomPropertyColumn<AbsentieMelding>("Absentiereden", "Absentiereden",
			"absentieReden.omschrijving"));

		addColumn(new CustomPropertyColumn<AbsentieMelding>("Groepen", "Groepen",
			"deelnemer.groepsnamenOpPeildatum").setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<AbsentieMelding>("Opleiding", "Opleiding",
			"deelnemer.eersteInschrijvingOpPeildatum.opleiding").setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<AbsentieMelding>("Organisatie-eenheid",
			"Organisatie-eenheid", "deelnemer.eersteInschrijvingOpPeildatum.organisatieEenheid")
			.setDefaultVisible(false));
	}

}
