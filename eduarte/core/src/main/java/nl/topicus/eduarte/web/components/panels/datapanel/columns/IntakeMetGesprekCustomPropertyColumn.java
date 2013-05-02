package nl.topicus.eduarte.web.components.panels.datapanel.columns;

import nl.topicus.cobra.web.components.datapanel.columns.CustomPropertyColumn;
import nl.topicus.eduarte.entities.inschrijving.Intakegesprek;
import nl.topicus.eduarte.providers.VerbintenisProvider;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.model.IModel;

public class IntakeMetGesprekCustomPropertyColumn<T extends VerbintenisProvider> extends
		CustomPropertyColumn<T>
{
	private static final long serialVersionUID = 1L;

	private boolean hideCell;

	public IntakeMetGesprekCustomPropertyColumn(String id, String header, String propertyExpression)
	{
		this(id, header, propertyExpression, true);
	}

	public IntakeMetGesprekCustomPropertyColumn(String id, String header,
			String propertyExpression, boolean hideCell)
	{
		super(id, header, propertyExpression);
		this.hideCell = hideCell;
	}

	@Override
	public void populateItem(WebMarkupContainer cell, String componentId, WebMarkupContainer row,
			IModel<T> rowModel, int span)
	{
		Object obj = rowModel.getObject();

		if (obj instanceof Intakegesprek)
		{
			super.populateItem(cell, componentId, row, rowModel, span);
		}
		else
		{
			if (hideCell)
			{
				cell.setVisible(false);
			}
			else
			{
				cell.add(new WebMarkupContainer(componentId));
			}
		}
	}

}