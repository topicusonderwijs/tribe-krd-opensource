package nl.topicus.eduarte.resultaten.web.components.datapanel.columns;

import nl.topicus.cobra.web.components.datapanel.columns.AjaxButtonColumn;
import nl.topicus.eduarte.entities.resultaatstructuur.Toets;

import org.apache.wicket.model.IModel;

public abstract class DeeltoetsOmhoogVerplaatsenButtonColumn extends AjaxButtonColumn<Toets>
{
	private static final long serialVersionUID = 1L;

	public DeeltoetsOmhoogVerplaatsenButtonColumn()
	{
		super("Deeltoets omhoog verplaatsen", "");
		setPositioning(Positioning.FIXED_RIGHT);
	}

	@Override
	public String getCssClass()
	{
		return "unit_20";
	}

	@Override
	protected String getCssDisabled()
	{
		return "";
	}

	@Override
	protected String getCssEnabled()
	{
		return "ui-icon ui-icon-circle-arrow-n";
	}

	@Override
	public boolean isContentsEnabled(IModel<Toets> rowModel)
	{
		Toets toets = rowModel.getObject();
		return toets.getParent() != null && !toets.isEersteChild();
	}
}
