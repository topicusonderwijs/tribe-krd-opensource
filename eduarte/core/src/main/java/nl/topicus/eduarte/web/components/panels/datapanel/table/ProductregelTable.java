package nl.topicus.eduarte.web.components.panels.datapanel.table;

import nl.topicus.cobra.security.RechtenSoort;
import nl.topicus.cobra.util.ComponentUtil;
import nl.topicus.cobra.web.components.ComponentFactory;
import nl.topicus.cobra.web.components.datapanel.CustomDataPanelContentDescription;
import nl.topicus.cobra.web.components.datapanel.columns.AbstractCustomColumn;
import nl.topicus.cobra.web.components.datapanel.columns.CheckboxColumn;
import nl.topicus.cobra.web.components.datapanel.columns.CustomPropertyColumn;
import nl.topicus.eduarte.app.EduArteRequestCycle;
import nl.topicus.eduarte.entities.opleiding.Opleiding;
import nl.topicus.eduarte.entities.productregel.Productregel;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

/**
 * Columns voor tabellen van productregels
 * 
 * @author loite
 */
public class ProductregelTable extends CustomDataPanelContentDescription<Productregel>
{
	private static final long serialVersionUID = 1L;

	private static final class OnderwijsproductenColumn extends AbstractCustomColumn<Productregel>
	{
		private static final long serialVersionUID = 1L;

		private final IModel<Opleiding> opleidingModel;

		private OnderwijsproductenColumn(String id, String header, IModel<Opleiding> opleidingModel)
		{
			super(id, header);
			this.opleidingModel = opleidingModel;
		}

		@Override
		public void populateItem(WebMarkupContainer cellItem, String componentId,
				WebMarkupContainer row, IModel<Productregel> rowModel, int span)
		{
			Productregel regel = rowModel.getObject();
			if (regel.getAlleOnderwijsproductenToestaanVan() != null)
				cellItem.add(ComponentFactory.getDataLabel(componentId, "["
					+ regel.getAlleOnderwijsproductenToestaanVan().getNaam() + "]"));
			else
				cellItem.add(ComponentFactory.getDataLabel(componentId, regel
					.getOnderwijsproductCodes(opleidingModel.getObject())));

		}

		@Override
		public void detach()
		{
			super.detach();
			ComponentUtil.detachQuietly(opleidingModel);
		}

	}

	public ProductregelTable()
	{
		super("Productregels");
		createColumns();
	}

	public ProductregelTable(IModel<Opleiding> opleidingModel)
	{
		this();
		addColumn(new OnderwijsproductenColumn("Onderwijsproducten", "Onderwijsproducten",
			opleidingModel));
	}

	public static ProductregelTable createVerbintenisgebiedProductregelTable()
	{
		ProductregelTable table = new ProductregelTable();
		table.addColumn(new CustomPropertyColumn<Productregel>("Deelgebieden", "Deelgebieden",
			"deelgebiedExterneCodes"));
		if (EduArteRequestCycle.get().getAccountRechtenSoort() == RechtenSoort.BEHEER)
		{
			table.addColumn(new CheckboxColumn<Productregel>("Landelijk verplicht", "Verplicht",
				true)
			{
				private static final long serialVersionUID = 1L;

				@Override
				protected void onCheckboxSelectionChanged(IModel<Productregel> rowModel,
						Object newSelection)
				{
					Productregel productregel = rowModel.getObject();
					productregel.update();
					productregel.commit();
				}

				@Override
				protected IModel<Boolean> getCheckBoxModel(IModel<Productregel> rowModel)
				{
					return new PropertyModel<Boolean>(rowModel, "verplicht");
				}

			});
		}
		return table;
	}

	private void createColumns()
	{
		addColumn(new CustomPropertyColumn<Productregel>("Soort", "Soort", "soortProductregel",
			"soortProductregel"));
		addColumn(new CustomPropertyColumn<Productregel>("Diplomanaam", "Diplomanaam",
			"soortProductregel.diplomanaam").setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<Productregel>("Type", "Type", "typeProductregel",
			"typeProductregel").setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<Productregel>("Afkorting", "Afkorting", "afkorting",
			"afkorting"));
		addColumn(new CustomPropertyColumn<Productregel>("Naam", "Naam", "naam", "naam"));
		addColumn(new CustomPropertyColumn<Productregel>("Opleiding", "Opleiding",
			"opleiding.naam", "opleiding.naam").setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<Productregel>("Verbintenisgebied", "Verbintenisgebied",
			"verbintenisgebied.naam", "verbintenisgebied.naam").setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<Productregel>("Cohort", "Cohort", "cohort.naam",
			"cohort.naam").setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<Productregel>("Verplicht", "Verplicht", "verplicht",
			"verplichtOmschrijving").setDefaultVisible(false));
	}

}
