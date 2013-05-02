package nl.topicus.eduarte.onderwijscatalogus.web.pages.onderwijsproduct;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.dataproviders.GeneralFilteredSortableDataProvider;
import nl.topicus.cobra.modelsv2.DefaultModelManager;
import nl.topicus.cobra.modelsv2.ModelFactory;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.util.ComponentUtil;
import nl.topicus.cobra.web.components.datapanel.CustomDataPanel;
import nl.topicus.cobra.web.components.datapanel.CustomDataPanelPageLinkRowFactory;
import nl.topicus.cobra.web.components.panels.bottomrow.AnnulerenButton;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.eduarte.dao.helpers.TaxonomieElementDataAccessHelper;
import nl.topicus.eduarte.entities.onderwijsproduct.OnderwijsproductTaxonomie;
import nl.topicus.eduarte.entities.taxonomie.TaxonomieElement;
import nl.topicus.eduarte.onderwijscatalogus.principals.onderwijs.OnderwijsproductTaxonomieWrite;
import nl.topicus.eduarte.web.components.menu.OnderwijsproductMenuItem;
import nl.topicus.eduarte.web.components.panels.EduArteDataPanel;
import nl.topicus.eduarte.web.components.panels.datapanel.table.TaxonomieElementTable;
import nl.topicus.eduarte.web.components.panels.filter.TaxonomieElementZoekFilterPanel;
import nl.topicus.eduarte.web.pages.IModuleEditPage;
import nl.topicus.eduarte.web.pages.SecurePage;
import nl.topicus.eduarte.web.pages.onderwijs.onderwijscatalogus.AbstractOnderwijsproductPage;
import nl.topicus.eduarte.zoekfilters.TaxonomieElementZoekFilter;

import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;

/**
 * Pagina voor toevoegen van een onderwijsproduct taxonomie element
 * 
 * @author vandekamp
 */
@PageInfo(title = "Onderwijsproduct taxonomie element toevoegen", menu = {"Onderwijs > Onderwijsproducten > [Onderwijsproduct] > Taxonomie > Toevoegen"})
@InPrincipal(OnderwijsproductTaxonomieWrite.class)
public class OnderwijsproductTaxonomieToevoegenPage extends AbstractOnderwijsproductPage implements
		IModuleEditPage<OnderwijsproductTaxonomie>
{
	private static final long serialVersionUID = 1L;

	private final SecurePage returnToPage;

	private IModel<OnderwijsproductTaxonomie> onderwijsproductTaxonomieModel;

	private static final TaxonomieElementZoekFilter getDefaultFilter()
	{
		TaxonomieElementZoekFilter filter = new TaxonomieElementZoekFilter(TaxonomieElement.class);
		return filter;
	}

	public OnderwijsproductTaxonomieToevoegenPage(
			OnderwijsproductTaxonomie onderwijsproductTaxonomie, SecurePage returnToPage)
	{
		super(OnderwijsproductMenuItem.Taxonomie, ModelFactory.getModel(onderwijsproductTaxonomie
			.getOnderwijsproduct()));
		onderwijsproductTaxonomieModel =
			ModelFactory.getModel(onderwijsproductTaxonomie, new DefaultModelManager(
				OnderwijsproductTaxonomie.class));
		this.returnToPage = returnToPage;
		TaxonomieElementZoekFilter filter = getDefaultFilter();
		GeneralFilteredSortableDataProvider<TaxonomieElement, TaxonomieElementZoekFilter> provider =
			GeneralFilteredSortableDataProvider.of(filter,
				TaxonomieElementDataAccessHelper.class);
		CustomDataPanel<TaxonomieElement> datapanel =
			new EduArteDataPanel<TaxonomieElement>("datapanel", provider,
				new TaxonomieElementTable("Selecteer taxonomie-element"));

		datapanel.setRowFactory(new CustomDataPanelPageLinkRowFactory<TaxonomieElement>(
			OnderwijsproductTaxonomieToevoegenPage.class)
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick(Item<TaxonomieElement> item)
			{
				TaxonomieElement taxonomieElement = item.getModelObject();
				OnderwijsproductTaxonomie onderwijsproductTax =
					onderwijsproductTaxonomieModel.getObject();
				if (onderwijsproductTax.getOnderwijsproduct().bestaatTaxonomieElementKoppeling(
					taxonomieElement))
				{
					error("Dit taxonomie-element is al gekoppeld aan dit onderwijsproduct");
				}
				else
				{
					onderwijsproductTax.setTaxonomieElement(taxonomieElement);
					onderwijsproductTax.getOnderwijsproduct().getOnderwijsproductTaxonomieList()
						.add(onderwijsproductTax);
					onderwijsproductTax.getOnderwijsproduct().update();
					onderwijsproductTax.saveOrUpdate();
					onderwijsproductTax.commit();
					setResponsePage(OnderwijsproductTaxonomieToevoegenPage.this.returnToPage);
				}
			}
		});
		datapanel.setItemsPerPage(20);
		add(datapanel);
		add(new TaxonomieElementZoekFilterPanel("filter", filter, datapanel, true));
		createComponents();
	}

	@Override
	protected void fillBottomRow(BottomRowPanel panel)
	{
		super.fillBottomRow(panel);
		panel.addButton(new AnnulerenButton(panel, returnToPage));
	}

	@Override
	protected void onDetach()
	{
		ComponentUtil.detachQuietly(onderwijsproductTaxonomieModel);
		super.onDetach();
	}
}
