package nl.topicus.eduarte.web.components.panels.taxonomie;

import java.util.List;

import nl.topicus.cobra.dataproviders.ListModelDataProvider;
import nl.topicus.cobra.modelsv2.ExtendedModel;
import nl.topicus.cobra.modelsv2.ModelFactory;
import nl.topicus.cobra.util.ComponentUtil;
import nl.topicus.cobra.web.components.datapanel.CustomDataPanel;
import nl.topicus.cobra.web.components.datapanel.CustomColumn.Positioning;
import nl.topicus.cobra.web.components.panels.TypedPanel;
import nl.topicus.cobra.web.components.panels.datapanel.columns.AjaxDeleteColumn;
import nl.topicus.eduarte.entities.onderwijsproduct.Onderwijsproduct;
import nl.topicus.eduarte.entities.onderwijsproduct.OnderwijsproductTaxonomie;
import nl.topicus.eduarte.entities.taxonomie.TaxonomieElement;
import nl.topicus.eduarte.web.components.modalwindow.taxonomie.TaxonomieElementSelectieModalWindow;
import nl.topicus.eduarte.web.components.panels.EduArteDataPanel;
import nl.topicus.eduarte.web.components.panels.datapanel.table.OnderwijsproductTaxonomieElementTable;
import nl.topicus.eduarte.zoekfilters.TaxonomieElementZoekFilter;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

public class TaxonomieElementToevoegenPanel extends TypedPanel<Onderwijsproduct>
{
	private static final long serialVersionUID = 1L;

	final TaxonomieElementSelectieModalWindow modalWindow;

	private CustomDataPanel<OnderwijsproductTaxonomie> datapanel;

	private WebMarkupContainer datapanelContainer;

	private class TaxonomieElementModel implements IModel<TaxonomieElement>
	{
		private static final long serialVersionUID = 1L;

		private IModel<TaxonomieElement> taxonomieElement;

		@Override
		public TaxonomieElement getObject()
		{
			if (taxonomieElement != null)
				return taxonomieElement.getObject();
			return null;
		}

		@Override
		public void setObject(TaxonomieElement object)
		{
			if (object != null)
				taxonomieElement = ModelFactory.getModel(object);
			else
				taxonomieElement = null;
		}

		@Override
		public void detach()
		{
			ComponentUtil.detachQuietly(taxonomieElement);
		}

	}

	public TaxonomieElementToevoegenPanel(String id,
			ExtendedModel<Onderwijsproduct> onderwijsproductModel)
	{
		super(id, onderwijsproductModel);

		datapanelContainer = new WebMarkupContainer("datapanelContainer");
		datapanelContainer.setOutputMarkupId(true);
		add(datapanelContainer);

		ListModelDataProvider<OnderwijsproductTaxonomie> provider =
			new ListModelDataProvider<OnderwijsproductTaxonomie>(
				new PropertyModel<List<OnderwijsproductTaxonomie>>(onderwijsproductModel,
					"onderwijsproductTaxonomieList"));

		OnderwijsproductTaxonomieElementTable table =
			new OnderwijsproductTaxonomieElementTable("Koppeling met taxonomie");
		table.addColumn(new AjaxDeleteColumn<OnderwijsproductTaxonomie>("delete", "")
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick(WebMarkupContainer item,
					IModel<OnderwijsproductTaxonomie> rowModel, AjaxRequestTarget target)
			{
				OnderwijsproductTaxonomie ondProdTax = rowModel.getObject();
				Onderwijsproduct ondPr = getModelObject();
				ondPr.getOnderwijsproductTaxonomieList().remove(ondProdTax);
				target.addComponent(datapanelContainer);
			}
		}.setPositioning(Positioning.FIXED_RIGHT));
		datapanel = new EduArteDataPanel<OnderwijsproductTaxonomie>("datapanel", provider, table);
		datapanelContainer.add(datapanel);

		modalWindow =
			new TaxonomieElementSelectieModalWindow("taxonomieElement",
				new TaxonomieElementModel(), new TaxonomieElementZoekFilter(TaxonomieElement.class));
		modalWindow.setWindowClosedCallback(new ModalWindow.WindowClosedCallback()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void onClose(AjaxRequestTarget target)
			{
				if (modalWindow.getDefaultModelObject() == null)
					return;
				TaxonomieElement element = (TaxonomieElement) modalWindow.getDefaultModelObject();
				Onderwijsproduct ondProd = getModelObject();
				if (ondProd.bestaatTaxonomieElementKoppeling(element))
				{
					return;
				}
				OnderwijsproductTaxonomie ondProdTax = new OnderwijsproductTaxonomie();
				ondProdTax.setTaxonomieElement(element);
				ondProdTax.setOnderwijsproduct(ondProd);
				ondProd.getOnderwijsproductTaxonomieList().add(ondProdTax);
				modalWindow.getDefaultModel().setObject(null);

				// Refresh het datapanel.
				target.addComponent(datapanelContainer);
			}
		});
		add(modalWindow);

		add(new AjaxLink<Void>("taxonomieElementToevoegenButton")
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick(AjaxRequestTarget target)
			{
				modalWindow.show(target);
			}
		});
	}
}
