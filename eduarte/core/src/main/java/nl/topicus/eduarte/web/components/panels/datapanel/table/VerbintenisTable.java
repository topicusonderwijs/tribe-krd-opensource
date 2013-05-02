package nl.topicus.eduarte.web.components.panels.datapanel.table;

import nl.topicus.cobra.util.TimeUtil;
import nl.topicus.cobra.web.components.datapanel.CustomDataPanelContentDescription;
import nl.topicus.cobra.web.components.datapanel.CustomColumn.Positioning;
import nl.topicus.cobra.web.components.datapanel.columns.AbstractCustomColumn;
import nl.topicus.cobra.web.components.datapanel.columns.BooleanPropertyColumn;
import nl.topicus.cobra.web.components.datapanel.columns.CustomPropertyColumn;
import nl.topicus.cobra.web.components.labels.TableHeaderDescriptionLabel;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis;

import org.apache.wicket.Component;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.IModel;

/**
 * Tabel met de mogelijke kolommen voor een Verbintenis.
 * 
 * @author idserda
 */
public class VerbintenisTable<T> extends CustomDataPanelContentDescription<T>
{
	private static final long serialVersionUID = 1L;

	public VerbintenisTable()
	{
		super("Verbintenissen");

		addColumn(new CustomPropertyColumn<T>("Volgnummer", "Volgnr.", "volgnummer", "volgnummer")
			.setPositioning(Positioning.FIXED_LEFT));
		addColumn(new CustomPropertyColumn<T>("Code", "Code", "opleiding.verbintenisgebied",
			"externeCode").setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<T>("Opleiding", "Opleiding", "opleiding", "opleiding"));
		addColumn(new CustomPropertyColumn<T>("Status", "Status", "status", "status"));
		addColumn(new CustomPropertyColumn<T>("Leerweg", "Leerweg", "opleiding.leerweg",
			"opleiding.leerweg"));
		addColumn(new BooleanPropertyColumn<T>("Bekostigd", "Bekostigd", "bekostigd",
			"bekostigdOpPeildatum")
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void populateItem(WebMarkupContainer cell, String componentId,
					WebMarkupContainer row, IModel<T> rowModel, int span)
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

		addColumn(new CustomPropertyColumn<T>("Van", "Van", "begindatum", "begindatum"));
		addColumn(new AbstractCustomColumn<T>("Tot", "Tot", "einddatum")
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void populateItem(WebMarkupContainer cell, String componentId,
					WebMarkupContainer row, IModel<T> rowModel, int span)
			{
				Object obj = rowModel.getObject();

				Verbintenis verbintenis = (Verbintenis) obj;

				if (verbintenis.getEinddatum() == null)
				{
					if (verbintenis.getGeplandeEinddatum() == null)
						cell.add(new Label(componentId, ""));
					else
						cell.add(new Label(componentId, "(verwacht "
							+ TimeUtil.getInstance().formatDate(verbintenis.getGeplandeEinddatum())
							+ ")"));
				}
				else
				{
					cell.add(new Label(componentId, TimeUtil.getInstance().formatDate(
						verbintenis.getEinddatum())));
				}
			}
		});
	}
}