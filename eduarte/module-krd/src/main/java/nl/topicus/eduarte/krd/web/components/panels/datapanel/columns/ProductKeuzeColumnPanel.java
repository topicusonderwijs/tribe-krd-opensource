package nl.topicus.eduarte.krd.web.components.panels.datapanel.columns;

import java.util.ArrayList;
import java.util.List;

import nl.topicus.cobra.web.components.panels.TypedPanel;
import nl.topicus.eduarte.entities.onderwijsproduct.Onderwijsproduct;
import nl.topicus.eduarte.entities.productregel.Productregel;
import nl.topicus.eduarte.krd.web.components.choice.OnderwijsproductComboBox;
import nl.topicus.eduarte.krd.web.pages.deelnemer.onderwijs.DeelnemerCollectieveAfnameContext;
import nl.topicus.eduarte.web.components.quicksearch.onderwijsproduct.OnderwijsproductSearchEditor;
import nl.topicus.eduarte.zoekfilters.OnderwijsproductZoekFilter;
import nl.topicus.eduarte.zoekfilters.OrganisatieEenheidLocatieAuthorizationContext;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

public class ProductKeuzeColumnPanel extends TypedPanel<DeelnemerCollectieveAfnameContext>
{
	private static final long serialVersionUID = 1L;

	public ProductKeuzeColumnPanel(String id, IModel<DeelnemerCollectieveAfnameContext> model,
			WebMarkupContainer row)
	{
		super(id, model);
		PropertyModel<Onderwijsproduct> onderwijsproductModel =
			new PropertyModel<Onderwijsproduct>(model, "onderwijsproduct");
		Productregel productregel = model.getObject().getProductregel();

		OnderwijsproductComboBox onderwijsproductComboBox =
			new OnderwijsproductComboBox("onderwijsproduct", onderwijsproductModel,
				new ChoiceModel(model));
		onderwijsproductComboBox.connectListForAjaxRefresh(row);
		onderwijsproductComboBox.setNullValid(true);
		onderwijsproductComboBox
			.setVisible(productregel.getAlleOnderwijsproductenToestaanVan() == null);
		add(onderwijsproductComboBox);

		OnderwijsproductZoekFilter filter =
			new OnderwijsproductZoekFilter(productregel.getAlleOnderwijsproductenToestaanVan());
		filter.setStaOrganisatieEenheidAanpassingToe(false);
		filter.setAuthorizationContext(new OrganisatieEenheidLocatieAuthorizationContext(this));

		OnderwijsproductSearchEditor onderwijsproductSearchEditor =
			new OnderwijsproductSearchEditor("searchEditor", onderwijsproductModel, filter);
		onderwijsproductSearchEditor
			.setVisible(productregel.getAlleOnderwijsproductenToestaanVan() != null);
		add(onderwijsproductSearchEditor);

		setRenderBodyOnly(true);
	}

	private class ChoiceModel extends AbstractReadOnlyModel<List<Onderwijsproduct>>
	{
		private static final long serialVersionUID = 1L;

		private IModel<DeelnemerCollectieveAfnameContext> model;

		private ChoiceModel(IModel<DeelnemerCollectieveAfnameContext> model)
		{
			this.model = model;
		}

		@Override
		public List<Onderwijsproduct> getObject()
		{
			DeelnemerCollectieveAfnameContext context = model.getObject();
			List<Onderwijsproduct> choices = new ArrayList<Onderwijsproduct>();
			choices.addAll(context.getProductregel().getOnderwijsproducten(context.getOpleiding(),
				false, false));
			return choices;
		}

	}
}
