package nl.topicus.eduarte.web.pages.onderwijs.onderwijscatalogus;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.dataproviders.GeneralFilteredSortableDataProvider;
import nl.topicus.cobra.modelsv2.ModelFactory;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.web.components.datapanel.CustomDataPanel;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.cobra.web.components.shortcut.CobraKeyAction;
import nl.topicus.eduarte.app.EduArteModuleKey;
import nl.topicus.eduarte.core.principals.onderwijs.OnderwijsproductOpvolgers;
import nl.topicus.eduarte.dao.helpers.OnderwijsproductDataAccessHelper;
import nl.topicus.eduarte.entities.onderwijsproduct.Onderwijsproduct;
import nl.topicus.eduarte.entities.onderwijsproduct.OnderwijsproductOpvolger;
import nl.topicus.eduarte.web.components.menu.OnderwijsproductMenuItem;
import nl.topicus.eduarte.web.components.panels.EduArteDataPanel;
import nl.topicus.eduarte.web.components.panels.bottomrow.ModuleEditPageButton;
import nl.topicus.eduarte.web.components.panels.datapanel.columns.ModuleDependentDeleteColumn;
import nl.topicus.eduarte.web.components.panels.datapanel.table.OnderwijsproductTable;
import nl.topicus.eduarte.zoekfilters.OnderwijsproductZoekFilter;
import nl.topicus.eduarte.zoekfilters.OrganisatieEenheidLocatieAuthorizationContext;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.IModel;

/**
 * Pagina voor het tonen van de onderwijsproducten die de opvolgers zijn voor dit
 * onderwijsproduct
 * 
 * @author loite
 */
@PageInfo(title = "Onderwijsproduct opvolgers", menu = {"Onderwijs > Onderwijsproducten > [onderwijsproduct] > Opvolgers"})
@InPrincipal(OnderwijsproductOpvolgers.class)
public class OnderwijsproductOpvolgersPage extends AbstractOnderwijsproductPage
{
	private static final long serialVersionUID = 1L;

	private final OnderwijsproductZoekFilter getDefaultFilter(Onderwijsproduct onderwijsproduct)
	{
		OnderwijsproductZoekFilter filter = new OnderwijsproductZoekFilter();
		filter.setAuthorizationContext(new OrganisatieEenheidLocatieAuthorizationContext(this));
		filter.setOpvolgerVan(onderwijsproduct);
		filter.addOrderByProperty("titel");
		filter.setResultCacheable(false);
		return filter;
	}

	/**
	 * Constructor
	 * 
	 * @param onderwijsproduct
	 */
	public OnderwijsproductOpvolgersPage(Onderwijsproduct onderwijsproduct)
	{
		super(OnderwijsproductMenuItem.Opvolgers, ModelFactory.getModel(onderwijsproduct));
		OnderwijsproductZoekFilter filter = getDefaultFilter(onderwijsproduct);
		IDataProvider<Onderwijsproduct> provider =
			GeneralFilteredSortableDataProvider.of(filter, OnderwijsproductDataAccessHelper.class);
		OnderwijsproductTable cols = new OnderwijsproductTable(true);
		cols.setTitle("Opvolger");
		cols.addColumn(new ModuleDependentDeleteColumn<Onderwijsproduct>("Verwijder", "Verwijder",
			EduArteModuleKey.ONDERWIJSCATALOGUS)
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick(WebMarkupContainer item, IModel<Onderwijsproduct> rowModel)
			{
				Onderwijsproduct opvolger = rowModel.getObject();
				Onderwijsproduct opvolgerVoor = getContextOnderwijsproduct();
				opvolgerVoor.deleteOpvolger(opvolger);
				opvolgerVoor.commit();
			}
		});
		CustomDataPanel<Onderwijsproduct> datapanel =
			new EduArteDataPanel<Onderwijsproduct>("datapanel", provider, cols);
		add(datapanel);
		createComponents();

	}

	@Override
	protected void fillBottomRow(BottomRowPanel panel)
	{
		super.fillBottomRow(panel);
		panel.addButton(new ModuleEditPageButton<OnderwijsproductOpvolger>(panel, "Toevoegen",
			CobraKeyAction.TOEVOEGEN, OnderwijsproductOpvolger.class,
			OnderwijsproductOpvolgersPage.this)
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected OnderwijsproductOpvolger getEntity()
			{
				OnderwijsproductOpvolger opvolger = new OnderwijsproductOpvolger();
				Onderwijsproduct opvolgerVan =
					(Onderwijsproduct) OnderwijsproductOpvolgersPage.this.getDefaultModelObject();
				opvolger.setOudProduct(opvolgerVan);
				return opvolger;
			}

		});
	}
}
