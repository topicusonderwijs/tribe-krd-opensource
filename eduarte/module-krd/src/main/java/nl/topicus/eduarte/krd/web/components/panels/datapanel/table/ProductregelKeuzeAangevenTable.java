package nl.topicus.eduarte.krd.web.components.panels.datapanel.table;

import java.util.List;

import nl.topicus.cobra.web.components.datapanel.CustomDataPanelContentDescription;
import nl.topicus.cobra.web.components.datapanel.columns.PanelColumn;
import nl.topicus.eduarte.entities.productregel.Productregel;
import nl.topicus.eduarte.krd.web.components.panels.datapanel.columns.DeelnemerNaamColumnPanel;
import nl.topicus.eduarte.krd.web.components.panels.datapanel.columns.ProductregelPanelColumn;
import nl.topicus.eduarte.krd.web.pages.deelnemer.onderwijs.OnderwijsproductAfnameContextListModel;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

public class ProductregelKeuzeAangevenTable extends
		CustomDataPanelContentDescription<OnderwijsproductAfnameContextListModel>
{
	private static final long serialVersionUID = 1L;

	public ProductregelKeuzeAangevenTable(List<Productregel> productregels)
	{
		super("Selecteer productregels om in te vullen");
		addColumn(new PanelColumn<OnderwijsproductAfnameContextListModel>("Naam", "Naam")
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected Panel getPanel(String componentId, WebMarkupContainer row,
					IModel<OnderwijsproductAfnameContextListModel> model)
			{
				return new DeelnemerNaamColumnPanel(componentId, model);
			}

		});
		for (Productregel regel : productregels)
		{
			addColumn(new ProductregelPanelColumn(regel.getAfkorting(), regel.getAfkorting(), regel));
		}
	}

	@Override
	public void detach()
	{
		super.detach();
	}
}
