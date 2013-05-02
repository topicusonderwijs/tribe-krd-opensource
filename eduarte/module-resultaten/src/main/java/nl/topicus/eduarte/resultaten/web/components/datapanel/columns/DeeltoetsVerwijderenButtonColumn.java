package nl.topicus.eduarte.resultaten.web.components.datapanel.columns;

import nl.topicus.cobra.web.components.datapanel.columns.AjaxButtonColumn;
import nl.topicus.eduarte.entities.resultaatstructuur.Toets;

import org.apache.wicket.model.IModel;

public abstract class DeeltoetsVerwijderenButtonColumn extends AjaxButtonColumn<Toets>
{
	private static final long serialVersionUID = 1L;

	public DeeltoetsVerwijderenButtonColumn()
	{
		super("Deeltoets verwijderen", "");
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
		return "deleteItem_grey";
	}

	@Override
	protected String getCssEnabled()
	{
		return "deleteItem";
	}

	@Override
	public boolean isContentsEnabled(IModel<Toets> rowModel)
	{
		Toets toets = rowModel.getObject();
		return toets.getChildren().isEmpty() && toets.getParent() != null
			&& !toets.getHeeftResultaten();
	}
}
