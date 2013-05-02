package nl.topicus.eduarte.krdparticipatie.web.components.tables;

import nl.topicus.cobra.web.components.datapanel.CustomDataPanelContentDescription;
import nl.topicus.cobra.web.components.datapanel.columns.CustomPropertyColumn;
import nl.topicus.cobra.web.components.datapanel.columns.LinkColumn;
import nl.topicus.eduarte.app.EduArteRequestCycle;
import nl.topicus.eduarte.krdparticipatie.web.pages.verzuimloket.DeelnemerVerzuimloketPage;
import nl.topicus.eduarte.rapportage.leerplicht.LeerplichtRapportage;

import org.apache.wicket.model.IModel;

public class LeerplichtRapportageTable extends
		CustomDataPanelContentDescription<LeerplichtRapportage>
{
	private static final long serialVersionUID = 1L;

	public LeerplichtRapportageTable()
	{
		super("Deelnemers");

		addColumn(new CustomPropertyColumn<LeerplichtRapportage>("Nummer", "Nummer",
			"deelnemer.deelnemernummer", "deelnemer.deelnemernummer"));
		addColumn(new CustomPropertyColumn<LeerplichtRapportage>("Roepnaam", "Roepnaam",
			"deelnemer.persoon.roepnaam", "deelnemer.persoon.roepnaam"));
		addColumn(new CustomPropertyColumn<LeerplichtRapportage>("Voorvoegsel", "Voorvoegsel",
			"deelnemer.persoon.voorvoegsel", "deelnemer.persoon.voorvoegsel"));
		addColumn(new CustomPropertyColumn<LeerplichtRapportage>("Achternaam", "Achternaam",
			"deelnemer.persoon.achternaam", "deelnemer.persoon.achternaam"));
		addColumn(new CustomPropertyColumn<LeerplichtRapportage>("Organisatie-eenheid",
			"Organisatie-eenheid",
			"deelnemer.eersteInschrijvingOpPeildatum.organisatieEenheid.naam"));
		addColumn(new CustomPropertyColumn<LeerplichtRapportage>("Locatie", "Locatie",
			"deelnemer.eersteInschrijvingOpPeildatum.locatie.naam"));
		addColumn(new CustomPropertyColumn<LeerplichtRapportage>("klokuren absent",
			"klokuren absent", "aantalUrenAbsent", "aantalUrenAbsent"));
		addColumn(new CustomPropertyColumn<LeerplichtRapportage>("Weken absent", "Weken absent",
			"aantalWekenAbsent", "aantalWekenAbsent"));
		addColumn(new LinkColumn<LeerplichtRapportage>("Verzuimloket", "Verzuimloket",
			"Naar verzuimloket", "Naar verzuimloketmeldingen van deelnemer", "")
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void onClick(IModel<LeerplichtRapportage> rowModel)
			{
				EduArteRequestCycle.get().setResponsePage(
					new DeelnemerVerzuimloketPage(rowModel.getObject().getDeelnemer()));
			}
		});
	}
}
