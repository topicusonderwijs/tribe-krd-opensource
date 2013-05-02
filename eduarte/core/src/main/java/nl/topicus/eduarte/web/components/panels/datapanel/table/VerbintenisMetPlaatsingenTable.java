package nl.topicus.eduarte.web.components.panels.datapanel.table;

import nl.topicus.cobra.entities.IdObject;
import nl.topicus.cobra.util.TimeUtil;
import nl.topicus.cobra.web.components.datapanel.CollapsableRowFactoryDecorator;
import nl.topicus.cobra.web.components.datapanel.CustomDataPanelContentDescription;
import nl.topicus.cobra.web.components.datapanel.CustomColumn.Positioning;
import nl.topicus.cobra.web.components.datapanel.columns.AbstractCustomColumn;
import nl.topicus.cobra.web.components.datapanel.columns.BooleanPropertyColumn;
import nl.topicus.cobra.web.components.datapanel.columns.CustomPropertyColumn;
import nl.topicus.cobra.web.components.labels.TableHeaderDescriptionLabel;
import nl.topicus.eduarte.app.EduArteApp;
import nl.topicus.eduarte.app.EduArteModuleKey;
import nl.topicus.eduarte.entities.inschrijving.Plaatsing;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis;
import nl.topicus.eduarte.web.components.panels.datapanel.columns.AlleenPlaatsingTonenEnVerbintenisAlsLegeCellCustomPropertyColumn;
import nl.topicus.eduarte.web.components.panels.datapanel.columns.AlleenPlaatsingenEnNietVerbintenisTonenCustomPropertyColumn;
import nl.topicus.eduarte.web.components.panels.datapanel.columns.AlleenVerbintenisTonenEnPlaatsingenAlsLegeCellCustomPropertyColumn;
import nl.topicus.eduarte.web.components.panels.datapanel.columns.PlaatsingTreeColumn;

import org.apache.wicket.Component;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.IModel;

/**
 * Tabel met de mogelijke kolommen voor een Verbintenis.
 * 
 * @author idserda
 */
public class VerbintenisMetPlaatsingenTable extends CustomDataPanelContentDescription<IdObject>
{
	private static final long serialVersionUID = 1L;

	public VerbintenisMetPlaatsingenTable(CollapsableRowFactoryDecorator<IdObject> rowFactory)
	{
		super("Verbintenissen en plaatsingen");

		addColumn(new AlleenVerbintenisTonenEnPlaatsingenAlsLegeCellCustomPropertyColumn<IdObject>(
			"Volgnummer", "Volgnr.", "volgnummer").setPositioning(Positioning.FIXED_LEFT));

		final boolean hogerOnderwijs =
			EduArteApp.get().isModuleActive(EduArteModuleKey.HOGER_ONDERWIJS);
		addColumn(new PlaatsingTreeColumn<IdObject>(rowFactory, hogerOnderwijs ? "3" : "5")
			.setPositioning(Positioning.FIXED_LEFT));

		addColumn(new AlleenPlaatsingenEnNietVerbintenisTonenCustomPropertyColumn<IdObject>(
			"Schooljaar", "Schooljaar", "SchoolJaar")
		{
			private static final long serialVersionUID = 1L;

			@Override
			public String getCssClass()
			{
				return "unit_40";
			}
		}.setPositioning(Positioning.FIXED_LEFT));
		if (!hogerOnderwijs)
		{
			addColumn(new AlleenPlaatsingenEnNietVerbintenisTonenCustomPropertyColumn<IdObject>(
				"Leerjaar", "Leerjaar", "leerjaar").setPositioning(Positioning.FIXED_LEFT));
			addColumn(new AlleenPlaatsingenEnNietVerbintenisTonenCustomPropertyColumn<IdObject>(
				"Praktijkjaar", "Praktijkjaar", "jarenPraktijkonderwijs")
				.setPositioning(Positioning.FIXED_LEFT));
		}
		addColumn(new AlleenPlaatsingenEnNietVerbintenisTonenCustomPropertyColumn<IdObject>(
			"Plaatsingsgroep", "Plaatsingsgroep", "groep.code")
			.setPositioning(Positioning.FIXED_LEFT));

		addColumn(new AlleenVerbintenisTonenEnPlaatsingenAlsLegeCellCustomPropertyColumn<IdObject>(
			"Status", "Status", "status"));
		addColumn(new AlleenVerbintenisTonenEnPlaatsingenAlsLegeCellCustomPropertyColumn<IdObject>(
			"Code", "Code", "externeCode"));
		addColumn(new AlleenVerbintenisTonenEnPlaatsingenAlsLegeCellCustomPropertyColumn<IdObject>(
			"Reden beëindigen", "Reden beëindigen", "redenUitschrijving").setDefaultVisible(false));
		addColumn(new AlleenVerbintenisTonenEnPlaatsingenAlsLegeCellCustomPropertyColumn<IdObject>(
			"Leerweg", "Leerweg", "verbintenis.opleiding.leerweg"));
		addColumn(new CustomPropertyColumn<IdObject>("Van", "Van", "begindatum"));
		if (hogerOnderwijs)
		{
			addColumn(new AlleenPlaatsingTonenEnVerbintenisAlsLegeCellCustomPropertyColumn<IdObject>(
				"Fase", "Fase", "fase.naam"));
			addColumn(new AlleenPlaatsingTonenEnVerbintenisAlsLegeCellCustomPropertyColumn<IdObject>(
				"Inschrijvingsvorm", "Inschrijvingsvorm", "inschrijvingsVorm"));
		}
		addColumn(new BooleanPropertyColumn<IdObject>("Bekostigd", "Bekostigd", "bekostigd",
			"bekostigdOpPeildatum")
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void populateItem(WebMarkupContainer cell, String componentId,
					WebMarkupContainer row, IModel<IdObject> rowModel, int span)
			{
				Object obj = rowModel.getObject();

				if (obj instanceof Verbintenis)
				{
					if (((Verbintenis) obj).isActief())
					{
						super.populateItem(cell, componentId, row, rowModel, span);
					}
					else
					{
						cell.add(new WebMarkupContainer(componentId).setVisible(false));
					}
				}
				else if (obj instanceof Plaatsing)
				{
					cell.add(new WebMarkupContainer(componentId).setVisible(false));
				}
			}

			@Override
			public Component getHeader(String componentId)
			{
				TableHeaderDescriptionLabel header =
					new TableHeaderDescriptionLabel(componentId, "Bekostigd",
						"Bekostigd - De bekostiging van de verbintenis op de peildatum.");
				return header;
			}

			@Override
			public String getCssClass()
			{
				return "unit_75";
			}
		});

		addColumn(new AbstractCustomColumn<IdObject>("Tot", "Tot")
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void populateItem(WebMarkupContainer cell, String componentId,
					WebMarkupContainer row, IModel<IdObject> rowModel, int span)
			{
				Object obj = rowModel.getObject();

				if (obj instanceof Verbintenis)
				{
					Verbintenis verbintenis = (Verbintenis) obj;

					if (verbintenis.getEinddatum() == null)
					{
						if (verbintenis.getGeplandeEinddatum() == null)
							cell.add(new Label(componentId, ""));
						else
							cell.add(new Label(componentId, "(verwacht "
								+ TimeUtil.getInstance().formatDate(
									verbintenis.getGeplandeEinddatum()) + ")"));
					}
					else
					{
						cell.add(new Label(componentId, TimeUtil.getInstance().formatDate(
							verbintenis.getEinddatum())));
					}
				}
				else if (obj instanceof Plaatsing)
				{
					Plaatsing plaatsing = (Plaatsing) obj;

					cell.add(new Label(componentId, TimeUtil.getInstance().formatDate(
						plaatsing.getEinddatum())));
				}

			}
		});
	}
}