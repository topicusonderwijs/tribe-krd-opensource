package nl.topicus.eduarte.krd.web.components.panels.datapanel.columns;

import nl.topicus.cobra.modelsv2.ModelFactory;
import nl.topicus.cobra.util.ComponentUtil;
import nl.topicus.cobra.web.components.datapanel.columns.AbstractCustomColumn;
import nl.topicus.eduarte.entities.productregel.Productregel;
import nl.topicus.eduarte.krd.web.pages.deelnemer.onderwijs.OnderwijsproductAfnameContextListModel;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.model.IModel;

/**
 * @author vandekamp
 */
public class ProductregelPanelColumn extends
		AbstractCustomColumn<OnderwijsproductAfnameContextListModel>
{
	private static final long serialVersionUID = 1L;

	private IModel<Productregel> productregelModel;

	public ProductregelPanelColumn(String id, String header, Productregel regel)
	{
		super(id, header);
		this.productregelModel = ModelFactory.getModel(regel);
	}

	@Override
	public void populateItem(WebMarkupContainer item, String componentId, WebMarkupContainer row,
			IModel<OnderwijsproductAfnameContextListModel> model, int span)
	{
		item.add(new ProductKeuzeDatumColumnPanel(componentId, model.getObject(), productregelModel
			.getObject()));
	}

	@Override
	public void detach()
	{
		ComponentUtil.detachQuietly(productregelModel);
		super.detach();
	}
}
