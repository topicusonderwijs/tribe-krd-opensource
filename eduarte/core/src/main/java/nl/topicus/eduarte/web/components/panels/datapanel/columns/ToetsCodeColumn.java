package nl.topicus.eduarte.web.components.panels.datapanel.columns;

import nl.topicus.cobra.util.ComponentUtil;
import nl.topicus.cobra.web.components.datapanel.columns.CustomPropertyColumn;
import nl.topicus.cobra.web.components.datapanel.columns.ISpanningColumn;
import nl.topicus.eduarte.entities.resultaatstructuur.Toets;

import org.apache.wicket.extensions.markup.html.repeater.data.table.IStyledColumn;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.model.IModel;

public class ToetsCodeColumn extends CustomPropertyColumn<Toets> implements ISpanningColumn<Toets>,
		IStyledColumn<Toets>
{
	private static final long serialVersionUID = 1L;

	private IModel<Integer> spanCountModel;

	public ToetsCodeColumn(IModel<Integer> spanCountModel)
	{
		super("Toetscode als boom", "Code", "boom", "code");
		this.spanCountModel = spanCountModel;
	}

	@Override
	public int getSpan()
	{
		return spanCountModel.getObject();
	}

	@Override
	public void populateItem(WebMarkupContainer cell, String componentId, WebMarkupContainer row,
			IModel<Toets> rowModel, int span)
	{
		Toets toets = rowModel.getObject();
		if (toets.getDepth() == span)
			super.populateItem(cell, componentId, row, rowModel, span);
		else
			cell.add(new WebMarkupContainer(componentId).setVisible(false));
	}

	@Override
	public String getCssClass()
	{
		return "unit_30";
	}

	@Override
	public void detach()
	{
		super.detach();
		ComponentUtil.detachQuietly(spanCountModel);
	}
}
