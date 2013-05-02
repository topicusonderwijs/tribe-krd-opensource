package nl.topicus.eduarte.web.components.panels.datapanel.columns;

import nl.topicus.cobra.entities.IdObject;
import nl.topicus.cobra.web.components.datapanel.columns.CustomPropertyColumn;
import nl.topicus.eduarte.entities.inschrijving.Plaatsing;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.model.IModel;

public class AlleenPlaatsingTonenEnVerbintenisAlsLegeCellCustomPropertyColumn<T extends IdObject>
		extends CustomPropertyColumn<T>
{
	private static final long serialVersionUID = 1L;

	public AlleenPlaatsingTonenEnVerbintenisAlsLegeCellCustomPropertyColumn(String id,
			String header, String propertyExpression)
	{
		super(id, header, propertyExpression);
	}

	@Override
	public void populateItem(WebMarkupContainer cell, String componentId, WebMarkupContainer row,
			IModel<T> rowModel, int span)
	{
		Object obj = rowModel.getObject();

		if (obj instanceof Plaatsing)
		{
			super.populateItem(cell, componentId, row, rowModel, span);
		}
		else if (obj instanceof Verbintenis)
		{
			cell.add(new WebMarkupContainer(componentId).setVisible(false));
		}
	}

}