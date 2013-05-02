package nl.topicus.eduarte.resultaten.web.components.datapanel.columns;

import nl.topicus.cobra.web.components.datapanel.columns.ButtonColumn;
import nl.topicus.eduarte.entities.resultaatstructuur.Toets;

import org.apache.wicket.model.IModel;

public abstract class DeeltoetsToevoegenButtonColumn extends ButtonColumn<Toets>
{
	private static final long serialVersionUID = 1L;

	public DeeltoetsToevoegenButtonColumn()
	{
		super("Deeltoets toevoegen", "");
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
		return "newItem_grey";
	}

	@Override
	protected String getCssEnabled()
	{
		return "newItem";
	}

	@Override
	public boolean isContentsEnabled(IModel<Toets> rowModel)
	{
		Toets toets = rowModel.getObject();
		return toets.isSamengesteld();
	}
}
