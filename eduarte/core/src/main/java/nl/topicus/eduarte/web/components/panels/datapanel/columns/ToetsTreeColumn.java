package nl.topicus.eduarte.web.components.panels.datapanel.columns;

import java.util.HashMap;
import java.util.Map;

import nl.topicus.cobra.util.ModelObjectKey;
import nl.topicus.cobra.web.components.datapanel.CollapsableRowFactoryDecorator;
import nl.topicus.eduarte.entities.resultaatstructuur.Resultaatstructuur;
import nl.topicus.eduarte.entities.resultaatstructuur.Toets;
import nl.topicus.eduarte.web.components.resultaat.StructuurLinkColumn;

import org.apache.wicket.extensions.markup.html.repeater.data.table.IStyledColumn;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

public class ToetsTreeColumn extends StructuurLinkColumn<Toets> implements IStyledColumn<Toets>
{
	private static final long serialVersionUID = 1L;

	private Map<ModelObjectKey, WebMarkupContainer> toetsRows =
		new HashMap<ModelObjectKey, WebMarkupContainer>();

	private CollapsableRowFactoryDecorator<Toets> rowFactory;

	private boolean editMode;

	public ToetsTreeColumn(CollapsableRowFactoryDecorator<Toets> rowFactory, boolean editMode)
	{
		super("Toetscode", "Toetscode", "code", "codeOfOnderwijsproduct");
		this.rowFactory = rowFactory;
		this.editMode = editMode;
	}

	@Override
	public void populateItem(WebMarkupContainer cell, String componentId, WebMarkupContainer row,
			IModel<Toets> rowModel, int span)
	{
		row.setOutputMarkupId(true);
		toetsRows.put(new ModelObjectKey(rowModel), row);
		Toets toets = rowModel.getObject();
		if (toets.getParent() != null)
		{
			WebMarkupContainer parentRow =
				toetsRows.get(new ModelObjectKey(new Model<Toets>(toets.getParent())));
			if (parentRow != null)
			{
				rowFactory.makeChild(parentRow, row);
			}
		}
		if (!toets.getChildren().isEmpty())
		{
			rowFactory.makeParent(row, rowModel);
		}
		super.populateItem(cell, componentId, row, rowModel, span);
	}

	@Override
	protected Resultaatstructuur getResultaatstructuur(IModel<Toets> rowModel)
	{
		return editMode ? null : rowModel.getObject().getResultaatstructuur();
	}

	@Override
	public String getCssClass()
	{
		return "";
	}

	@Override
	public void detach()
	{
		super.detach();
		for (ModelObjectKey curKey : toetsRows.keySet())
			curKey.detach();
	}
}
