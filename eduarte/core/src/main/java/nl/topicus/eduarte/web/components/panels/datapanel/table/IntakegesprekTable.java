package nl.topicus.eduarte.web.components.panels.datapanel.table;

import nl.topicus.cobra.web.components.datapanel.CollapsableRowFactoryDecorator;
import nl.topicus.cobra.web.components.datapanel.CustomColumn.Positioning;
import nl.topicus.cobra.web.components.datapanel.columns.CustomPropertyColumn;
import nl.topicus.cobra.web.components.datapanel.columns.DatePropertyColumn;
import nl.topicus.cobra.web.components.datapanel.columns.TimePropertyColumn;
import nl.topicus.eduarte.entities.inschrijving.Intakegesprek;
import nl.topicus.eduarte.entities.vrijevelden.VrijVeldCategorie;
import nl.topicus.eduarte.providers.VerbintenisProvider;
import nl.topicus.eduarte.web.components.panels.datapanel.columns.IntakeMetGesprekCustomPropertyColumn;
import nl.topicus.eduarte.web.components.panels.datapanel.columns.IntakegesprekTreeColumn;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.model.IModel;

/**
 * Tabel met de mogelijke kolommen voor een Intakegesprek.
 * 
 * @author hoeve
 * @author hop
 */
public class IntakegesprekTable<T extends VerbintenisProvider> extends AbstractVrijVeldableTable<T>
{
	private static final long serialVersionUID = 1L;

	public IntakegesprekTable(CollapsableRowFactoryDecorator<T> rowFactory, boolean toonDeelnemer)
	{
		super("Intakes en intakegesprekken");

		if (toonDeelnemer)
		{
			addColumn(new CustomPropertyColumn<T>("Deelnemer", "Deelnemer",
				"deelnemer.deelnemernummer", "deelnemer.deelnemernummer"));
			addColumn(new CustomPropertyColumn<T>("Naam", "Naam", "persoon.achternaam",
				"deelnemer.persoon.volledigeNaam"));
		}

		if (rowFactory != null)
			/*
			 * Colspan van deze row is fixed. Daarom default properties allemaal vaste
			 * positie geven zodat deze niet weggehaald kunnen worden. Properties die wel
			 * gekozen kunnen worden (IntakeMetGesprekCustomPropertyColumn's) 'false'
			 * meegeven aan de constructor, zodat hiervoor een lege cell in deze row komt.
			 */
			addColumn(new IntakegesprekTreeColumn<T>(rowFactory)
				.setPositioning(Positioning.FIXED_LEFT));

		addColumn(new DatePropertyColumn<T>("Datum", "Datum", "datumTijd", "datumTijd")
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void populateItem(WebMarkupContainer item, String componentId,
					WebMarkupContainer row, IModel<T> model, int span)
			{
				Object obj = model.getObject();
				if (obj instanceof Intakegesprek)
					super.populateItem(item, componentId, row, model, span);
				else
					item.setVisible(false);
			}
		}.setPositioning(Positioning.FIXED_LEFT));

		addColumn(new TimePropertyColumn<T>("Tijd", "Tijd", "datumTijd")
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void populateItem(WebMarkupContainer item, String componentId,
					WebMarkupContainer row, IModel<T> model, int span)
			{
				Object obj = model.getObject();
				if (obj instanceof Intakegesprek)
					super.populateItem(item, componentId, row, model, span);
				else
					item.setVisible(false);
			}
		}.setPositioning(Positioning.FIXED_LEFT));

		if (!toonDeelnemer)
		{
			addColumn(new IntakeMetGesprekCustomPropertyColumn<T>("Intaker", "Intaker", "intaker")
				.setPositioning(Positioning.FIXED_LEFT));
		}
		addColumn(new IntakeMetGesprekCustomPropertyColumn<T>("Locatie", "Locatie", "locatie")
			.setPositioning(Positioning.FIXED_LEFT));
		addColumn(new IntakeMetGesprekCustomPropertyColumn<T>("Kamer", "Kamer", "kamer")
			.setPositioning(Positioning.FIXED_LEFT));
		addColumn(new IntakeMetGesprekCustomPropertyColumn<T>("Status", "Status", "status")
			.setPositioning(Positioning.FIXED_LEFT));
		addColumn(new IntakeMetGesprekCustomPropertyColumn<T>("Uitkomst", "Uitkomst", "uitkomst",
			false).setDefaultVisible(false));
		addColumn(new IntakeMetGesprekCustomPropertyColumn<T>("Organisatie-eenheid",
			"Organisatie-eenheid", "verbintenis.organisatieEenheid", false)
			.setDefaultVisible(false));
		addColumn(new IntakeMetGesprekCustomPropertyColumn<T>("Gewenste opleiding",
			"Gewenste opleiding", "gewensteOpleiding", false).setDefaultVisible(false));
		addColumn(new IntakeMetGesprekCustomPropertyColumn<T>("Gewenste locatie",
			"Gewenste locatie", "gewensteLocatie", false).setDefaultVisible(false));
		addColumn(new IntakeMetGesprekCustomPropertyColumn<T>("Gewenste begindatum",
			"Gewenste begindatum", "gewensteBegindatum", false).setDefaultVisible(false));
		addColumn(new IntakeMetGesprekCustomPropertyColumn<T>("Gewenste einddatum",
			"Gewenste einddatum", "gewensteEinddatum", false).setDefaultVisible(false));
		addColumn(new IntakeMetGesprekCustomPropertyColumn<T>("Gewenste BPV", "Gewenste BPV",
			"gewensteBPV", false).setDefaultVisible(false));
		createVrijVeldKolommen(VrijVeldCategorie.INTAKE, "");

	}
}