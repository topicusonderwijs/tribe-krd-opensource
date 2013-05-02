package nl.topicus.eduarte.web.pages.onderwijs.onderwijscatalogus;

import java.util.ArrayList;
import java.util.List;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.dataproviders.GeneralFilteredSortableDataProvider;
import nl.topicus.cobra.modelsv2.ModelFactory;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.util.StringUtil;
import nl.topicus.cobra.web.components.datapanel.CustomDataPanel;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.cobra.web.components.shortcut.CobraKeyAction;
import nl.topicus.eduarte.app.EduArteModuleKey;
import nl.topicus.eduarte.dao.helpers.TaxonomieElementDataAccessHelper;
import nl.topicus.eduarte.entities.onderwijsproduct.Onderwijsproduct;
import nl.topicus.eduarte.entities.onderwijsproduct.OnderwijsproductTaxonomie;
import nl.topicus.eduarte.entities.taxonomie.TaxonomieElement;
import nl.topicus.eduarte.web.components.menu.OnderwijsproductMenuItem;
import nl.topicus.eduarte.web.components.panels.EduArteDataPanel;
import nl.topicus.eduarte.web.components.panels.bottomrow.ModuleEditPageButton;
import nl.topicus.eduarte.web.components.panels.datapanel.columns.ModuleDependentDeleteColumn;
import nl.topicus.eduarte.web.components.panels.datapanel.table.TaxonomieElementTable;
import nl.topicus.eduarte.zoekfilters.TaxonomieElementZoekFilter;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.model.IModel;

/**
 * Koppeling van het onderwijsproduct met taxonomie
 * 
 * @author vandekamp
 */
@PageInfo(title = "Onderwijsproduct Taxonomie", menu = {"Onderwijs > Onderwijsproducten > [onderwijsproduct] > Taxonomie"})
@InPrincipal(nl.topicus.eduarte.core.principals.onderwijs.OnderwijsproductTaxonomie.class)
public class OnderwijsproductTaxonomiePage extends AbstractOnderwijsproductPage
{
	private static final long serialVersionUID = 1L;

	private static final TaxonomieElementZoekFilter getDefaultFilter(
			Onderwijsproduct onderwijsproduct)
	{
		TaxonomieElementZoekFilter filter = new TaxonomieElementZoekFilter(TaxonomieElement.class);
		filter.setGekoppeldAanOnderwijsProduct(onderwijsproduct);
		filter.setResultCacheable(false);

		return filter;
	}

	public OnderwijsproductTaxonomiePage(Onderwijsproduct onderwijsproduct)
	{
		super(OnderwijsproductMenuItem.Taxonomie, ModelFactory.getModel(onderwijsproduct));
		TaxonomieElementZoekFilter filter = getDefaultFilter(onderwijsproduct);
		GeneralFilteredSortableDataProvider<TaxonomieElement, TaxonomieElementZoekFilter> provider =
			GeneralFilteredSortableDataProvider.of(filter,
				TaxonomieElementDataAccessHelper.class);
		TaxonomieElementTable cols = new TaxonomieElementTable("Koppelingen met taxonomie");
		cols.addColumn(new ModuleDependentDeleteColumn<TaxonomieElement>("Verwijder", "Verwijder",
			EduArteModuleKey.ONDERWIJSCATALOGUS)
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick(WebMarkupContainer item, IModel<TaxonomieElement> rowModel)
			{
				TaxonomieElement element = rowModel.getObject();
				Onderwijsproduct onderwijspr = getContextOnderwijsproduct();
				onderwijspr.deleteOnderwijsproductTaxonomie(element);
				onderwijspr.commit();
			}
		});

		CustomDataPanel<TaxonomieElement> datapanel =
			new EduArteDataPanel<TaxonomieElement>("datapanel", provider, cols);
		add(datapanel);
		createComponents();

	}

	@Override
	protected void fillBottomRow(BottomRowPanel panel)
	{
		super.fillBottomRow(panel);

		panel.addButton(new ModuleEditPageButton<OnderwijsproductTaxonomie>(panel, "Toevoegen",
			CobraKeyAction.TOEVOEGEN, OnderwijsproductTaxonomie.class,
			OnderwijsproductTaxonomiePage.this)
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected OnderwijsproductTaxonomie getEntity()
			{
				Onderwijsproduct onderwijsproduct =
					(Onderwijsproduct) OnderwijsproductTaxonomiePage.this.getDefaultModelObject();
				OnderwijsproductTaxonomie onderwijsproductTaxonomie =
					new OnderwijsproductTaxonomie();
				onderwijsproductTaxonomie.setOnderwijsproduct(onderwijsproduct);
				return onderwijsproductTaxonomie;
			}
		});
	}

	@Override
	protected void onBeforeRender()
	{
		if (heeftMeerdereExterneCodes())
		{
			warn("Let op: Dit onderwijsproduct is gekoppeld aan meerdere taxonomieën met verschillende externe codes. Hierdoor wordt dit onderwijsproduct NIET met BRON gecommuniceerd.");
		}
		super.onBeforeRender();
	}

	private boolean heeftMeerdereExterneCodes()
	{
		Onderwijsproduct product = (Onderwijsproduct) getDefaultModelObject();
		if (product != null)
		{
			List<String> codes = new ArrayList<String>();

			for (OnderwijsproductTaxonomie onderwijsproductTaxonomie : product
				.getOnderwijsproductTaxonomieList())
			{
				String externeCode =
					onderwijsproductTaxonomie.getTaxonomieElement().getExterneCode();

				if (StringUtil.isNotEmpty(externeCode) && !codes.contains(externeCode))
				{
					codes.add(externeCode);
				}
			}

			return (codes.size() > 1);
		}
		return false;
	}
}
