package nl.topicus.eduarte.web.components.panels.datapanel.table;

import nl.topicus.cobra.entities.IdObject;
import nl.topicus.cobra.util.TimeUtil;
import nl.topicus.cobra.web.components.datapanel.CollapsableGroupRowFactoryDecorator;
import nl.topicus.cobra.web.components.datapanel.CustomDataPanelContentDescription;
import nl.topicus.cobra.web.components.datapanel.CustomColumn.Positioning;
import nl.topicus.cobra.web.components.datapanel.columns.AbstractCustomColumn;
import nl.topicus.cobra.web.components.datapanel.columns.AjaxRadioColumn;
import nl.topicus.cobra.web.components.datapanel.columns.CustomPropertyColumn;
import nl.topicus.eduarte.entities.inschrijving.Plaatsing;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis;
import nl.topicus.eduarte.web.components.panels.datapanel.columns.AlleenPlaatsingenEnNietVerbintenisTonenCustomPropertyColumn;
import nl.topicus.eduarte.web.components.panels.datapanel.columns.PlaatsingTreeColumn;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.IModel;

/**
 * Tabel die alle verbintenissen van een deelnemer toont. Bij VAVO en VO verbintennis
 * worden ook de plaatsingen getoond.
 * 
 * @author idserda
 */
public class VerbintenisPlaatsingBronTable extends CustomDataPanelContentDescription<IdObject>
{
	private static final long serialVersionUID = 1L;

	public VerbintenisPlaatsingBronTable(CollapsableGroupRowFactoryDecorator<IdObject> rowFactory)
	{
		super("Verbintenissen en plaatsingen");

		addColumn(new AjaxRadioColumn<IdObject>("verbintenisSelecteren", null)
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void onUpdate(AjaxRequestTarget target, IModel<IdObject> selectedModel)
			{
				onChoiceUpdate(target, selectedModel);
			}

			@Override
			public void populateItem(WebMarkupContainer cellItem, String componentId,
					WebMarkupContainer row, IModel<IdObject> rowModel, int span)
			{
				Object obj = rowModel.getObject();

				if (obj instanceof Verbintenis)
				{
					Verbintenis verbintenis = (Verbintenis) obj;
					if (verbintenis.isVAVOVerbintenis() || verbintenis.isVOVerbintenis())
					{
						// Bij VAVO/VO verbintenissen is het alleen mogelijk een
						// onderliggende plaatsing te selecteren
						cellItem.add(new WebMarkupContainer(componentId));
					}
					else
					{
						super.populateItem(cellItem, componentId, row, rowModel, span);
					}
				}
				else
				{
					super.populateItem(cellItem, componentId, row, rowModel, span);
				}
			}

			@Override
			public String getCssClass()
			{
				return "unit_20";
			}

		});

		addColumn(new PlaatsingTreeColumn<IdObject>(rowFactory, "4"));
		addColumn(new AlleenPlaatsingenEnNietVerbintenisTonenCustomPropertyColumn<IdObject>(
			"Schooljaar", "Schooljaar", "verbintenis.cohort.naam")
		{
			private static final long serialVersionUID = 1L;

			@Override
			public String getCssClass()
			{
				return "unit_40";
			}
		});

		addColumn(new AlleenPlaatsingenEnNietVerbintenisTonenCustomPropertyColumn<IdObject>(
			"Leerjaar", "Leerjaar", "leerjaar"));
		addColumn(new AlleenPlaatsingenEnNietVerbintenisTonenCustomPropertyColumn<IdObject>(
			"Stamgroep", "Stamgroep", "groep.code"));

		addColumn(new CustomPropertyColumn<IdObject>("Van", "Van", "begindatum"));
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
		}.setPositioning(Positioning.FIXED_RIGHT));
	}

	@SuppressWarnings("unused")
	protected void onChoiceUpdate(AjaxRequestTarget target, IModel<IdObject> selectedModel)
	{
	}

}