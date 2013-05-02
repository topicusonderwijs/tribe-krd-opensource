package nl.topicus.eduarte.krd.web.components.panels.datapanel.table;

import nl.topicus.cobra.web.components.datapanel.CustomDataPanel;
import nl.topicus.cobra.web.components.datapanel.CustomDataPanelContentDescription;
import nl.topicus.cobra.web.components.datapanel.columns.AbstractCustomColumn;
import nl.topicus.cobra.web.components.datapanel.columns.BooleanPropertyColumn;
import nl.topicus.cobra.web.components.datapanel.columns.CustomPropertyColumn;
import nl.topicus.eduarte.krd.entities.bron.meldingen.aanlevering.BronAanleverMelding;
import nl.topicus.eduarte.krd.entities.bron.meldingen.aanlevering.IBronMelding;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

/**
 * Definieert de content voor het tonen van de BRON Batches in een {@link CustomDataPanel}
 * .
 */
public class DeelnemerBronMeldingTable extends CustomDataPanelContentDescription<IBronMelding>
{
	private static final long serialVersionUID = 1L;

	public DeelnemerBronMeldingTable()
	{
		super("Meldingen");

		createColumns();
	}

	private void createColumns()
	{
		addColumn(new CustomPropertyColumn<IBronMelding>("Onderwijs", "Onderwijs", "onderwijssoort"));
		addColumn(new CustomPropertyColumn<IBronMelding>("Batch", "Batch", "batch.batchNummer"));
		addColumn(new CustomPropertyColumn<IBronMelding>("Datum verzonden", "Datum verzonden",
			"createdAt"));
		addColumn(new CustomPropertyColumn<IBronMelding>("volgnummerVerbintenis",
			"Verbintenis nr.", "verbintenis.volgnummer"));
		addColumn(new AbstractCustomColumn<IBronMelding>("volgnummerBPV", "BPV nr.")
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void populateItem(WebMarkupContainer cell, String componentId,
					WebMarkupContainer row, IModel<IBronMelding> rowModel, int span)
			{
				Object obj = rowModel.getObject();
				if (BronAanleverMelding.class.equals(obj.getClass()))
				{
					cell.add(new Label(componentId, new Model<Integer>(((BronAanleverMelding) obj)
						.getBPVVolgnummer())));
				}
				else
				{
					cell.add(new WebMarkupContainer(componentId));
				}
			}
		});
		addColumn(new CustomPropertyColumn<IBronMelding>("Soort mutatie", "Soort",
			"soortMutatiesOmsch"));
		addColumn(new CustomPropertyColumn<IBronMelding>("Ingangsdatum", "Ingangsdatum",
			"ingangsDatum"));
		addColumn(new CustomPropertyColumn<IBronMelding>("Onderdeel", "Onderdeel",
			"bronMeldingOnderdelenOmsch"));

		addColumn(new CustomPropertyColumn<IBronMelding>("Organisatie-eenheid",
			"Organisatie-eenheid", "verbintenis.organisatieEenheid"));
		addColumn(new CustomPropertyColumn<IBronMelding>("Status", "Status", "bronMeldingStatus"));
		addColumn(new BooleanPropertyColumn<IBronMelding>("Geblokkeerd", "Geblokkeerd",
			"geblokkeerd", "geblokkeerd"));
		addColumn(new CustomPropertyColumn<IBronMelding>("Meldingnummer", "Nr.", "meldingnummer")
			.setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<IBronMelding>("Terugkoppelbestand",
			"Terugkoppelbestand", "terugkoppelbestandNummer").setDefaultVisible(false));
	}
}
