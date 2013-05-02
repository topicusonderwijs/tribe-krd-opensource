package nl.topicus.eduarte.krd.web.components.panels.datapanel.columns;

import nl.topicus.cobra.web.components.datapanel.columns.AjaxButtonColumn;
import nl.topicus.onderwijs.duo.bron.IBronBatch;

import org.apache.wicket.model.IModel;

public abstract class BronBatchVerwijderenButtonColumn extends AjaxButtonColumn<IBronBatch>
{
	private static final long serialVersionUID = 1L;

	public BronBatchVerwijderenButtonColumn()
	{
		super("verwijderen", "");
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
	protected String getConfirmationText()
	{
		return "Verwijderen van een BRON batch mag alleen als deze nog niet verzonden is naar "
			+ "de IB-Groep of afgekeurd is. Weet u zeker dat deze batch verwijderd kan worden?";
	}

	@Override
	public boolean isContentsVisible(IModel<IBronBatch> rowModel)
	{
		IBronBatch batch = rowModel.getObject();
		return batch.isVerwijderBatchMogelijk();
	}
}
