package nl.topicus.eduarte.krdparticipatie.web.components.tables;

import nl.topicus.cobra.web.components.datapanel.CustomDataPanelContentDescription;
import nl.topicus.cobra.web.components.datapanel.columns.CustomPropertyColumn;
import nl.topicus.eduarte.entities.participatie.Waarneming;

public class OpenAbsentieWaarnemingen extends CustomDataPanelContentDescription<Waarneming>
{

	private static final long serialVersionUID = 1L;

	public OpenAbsentieWaarnemingen()
	{
		super("Open absentiewaarnemingen");
		createColumns();
	}

	private void createColumns()
	{

		addColumn(new CustomPropertyColumn<Waarneming>("Nummer", "Nummer",
			"deelnemer.deelnemernummer"));
		addColumn(new CustomPropertyColumn<Waarneming>("Roepnaam", "Roepnaam", "persoon.roepnaam",
			"deelnemer.persoon.roepnaam"));
		addColumn(new CustomPropertyColumn<Waarneming>("Voorvoegsel", "Voorvoegsel",
			"persoon.voorvoegsel", "deelnemer.persoon.voorvoegsel"));
		addColumn(new CustomPropertyColumn<Waarneming>("Achternaam", "Achternaam",
			"persoon.achternaam", "deelnemer.persoon.achternaam"));
		addColumn(new CustomPropertyColumn<Waarneming>("Begindatum", "Begindatum", "beginDatumTijd"));
		addColumn(new CustomPropertyColumn<Waarneming>("Einddatum", "Einddatum", "eindDatumTijd"));

		addColumn(new CustomPropertyColumn<Waarneming>("Groepen", "Groepen",
			"deelnemer.groepsnamenOpPeildatum").setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<Waarneming>("Opleiding", "Opleiding",
			"deelnemer.eersteInschrijvingOpPeildatum.opleiding").setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<Waarneming>("Organisatie-eenheid",
			"Organisatie-eenheid", "deelnemer.eersteInschrijvingOpPeildatum.organisatieEenheid")
			.setDefaultVisible(false));
	}

}
