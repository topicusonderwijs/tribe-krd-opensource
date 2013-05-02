package nl.topicus.eduarte.web.components.panels.datapanel.table;

import nl.topicus.cobra.web.components.datapanel.CustomDataPanelContentDescription;
import nl.topicus.cobra.web.components.datapanel.columns.AjaxRadioColumn;
import nl.topicus.cobra.web.components.datapanel.columns.CustomPropertyColumn;
import nl.topicus.eduarte.entities.onderwijsproduct.OnderwijsproductAfnameContext;

/**
 * @author idserda
 */
public class OnderwijsproductAfnameContextSelectTable extends
		CustomDataPanelContentDescription<OnderwijsproductAfnameContext>
{
	private static final long serialVersionUID = 1L;

	public OnderwijsproductAfnameContextSelectTable()
	{
		super("Laatst afgenomen onderwijsproducten");
		addAllColumns();
	}

	private void addAllColumns()
	{
		addColumn(new AjaxRadioColumn<OnderwijsproductAfnameContext>("selectie", "Selecteer")
		{
			private static final long serialVersionUID = 1L;

			@Override
			public String getCssClass()
			{
				// TODO Auto-generated method stub
				return "unit_60";
			}

		});
		addColumn(new CustomPropertyColumn<OnderwijsproductAfnameContext>("Code", "Code",
			"onderwijsproductAfname.onderwijsproduct.code",
			"onderwijsproductAfname.onderwijsproduct.code"));
		addColumn(new CustomPropertyColumn<OnderwijsproductAfnameContext>("Titel", "Titel",
			"onderwijsproductAfname.onderwijsproduct.titel",
			"onderwijsproductAfname.onderwijsproduct.titel"));
		addColumn(new CustomPropertyColumn<OnderwijsproductAfnameContext>("Begindatum",
			"Begindatum", "onderwijsproductAfname.begindatum", "onderwijsproductAfname.begindatum"));
	}
}
