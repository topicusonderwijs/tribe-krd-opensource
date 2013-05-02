package nl.topicus.eduarte.krd.web.components.panels.datapanel.columns;

import java.util.ArrayList;
import java.util.List;

import nl.topicus.cobra.web.components.panels.TypedPanel;
import nl.topicus.eduarte.entities.onderwijsproduct.Onderwijsproduct;
import nl.topicus.eduarte.entities.onderwijsproduct.OnderwijsproductAfnameContext;
import nl.topicus.eduarte.entities.productregel.Productregel;
import nl.topicus.eduarte.krd.web.components.choice.OnderwijsproductComboBox;
import nl.topicus.eduarte.krd.web.pages.deelnemer.onderwijs.OnderwijsproductAfnameContextListModel;

import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

public class ProductKeuzeDatumColumnPanel extends TypedPanel<List<OnderwijsproductAfnameContext>>
{
	private static final long serialVersionUID = 1L;

	public ProductKeuzeDatumColumnPanel(String id, OnderwijsproductAfnameContextListModel model,
			Productregel regel)
	{
		super(id, model);
		IModel<OnderwijsproductAfnameContext> context = model.getContextWithRegel(regel);
		OnderwijsproductComboBox onderwijsproductComboBox =
			new OnderwijsproductComboBox("onderwijsproduct", new PropertyModel<Onderwijsproduct>(
				context, "onderwijsproductAfname.onderwijsproduct"), new ChoiceModel(context));

		OnderwijsproductAfnameContext afnameContext = context.getObject();

		onderwijsproductComboBox.setNullValid(!afnameContext.getOnderwijsproductAfname().isSaved());
		add(onderwijsproductComboBox);
		setRenderBodyOnly(true);
	}

	private class ChoiceModel extends AbstractReadOnlyModel<List<Onderwijsproduct>>
	{
		private static final long serialVersionUID = 1L;

		private IModel<OnderwijsproductAfnameContext> model;

		private ChoiceModel(IModel<OnderwijsproductAfnameContext> model)
		{
			this.model = model;
		}

		@Override
		public List<Onderwijsproduct> getObject()
		{
			OnderwijsproductAfnameContext context = model.getObject();
			List<Onderwijsproduct> choices = new ArrayList<Onderwijsproduct>();
			choices.addAll(context.getProductregel().getOnderwijsproducten(
				context.getVerbintenis().getOpleiding()));
			return choices;
		}

	}
}
