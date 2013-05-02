package nl.topicus.eduarte.web.pages.onderwijs.onderwijscatalogus;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.dataproviders.GeneralFilteredSortableDataProvider;
import nl.topicus.cobra.modelsv2.ModelFactory;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.web.components.datapanel.CustomDataPanel;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.cobra.web.components.shortcut.CobraKeyAction;
import nl.topicus.eduarte.app.EduArteModuleKey;
import nl.topicus.eduarte.core.principals.onderwijs.OnderwijsproductVoorwaarden;
import nl.topicus.eduarte.dao.helpers.OnderwijsproductDataAccessHelper;
import nl.topicus.eduarte.entities.onderwijsproduct.Onderwijsproduct;
import nl.topicus.eduarte.entities.onderwijsproduct.OnderwijsproductVoorwaarde;
import nl.topicus.eduarte.web.components.menu.OnderwijsproductMenuItem;
import nl.topicus.eduarte.web.components.panels.EduArteDataPanel;
import nl.topicus.eduarte.web.components.panels.bottomrow.ModuleEditPageButton;
import nl.topicus.eduarte.web.components.panels.datapanel.columns.ModuleDependentDeleteColumn;
import nl.topicus.eduarte.web.components.panels.datapanel.table.OnderwijsproductTable;
import nl.topicus.eduarte.zoekfilters.OnderwijsproductZoekFilter;
import nl.topicus.eduarte.zoekfilters.OrganisatieEenheidLocatieAuthorizationContext;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.model.IModel;

/**
 * Pagina voor het tonen van de onderwijsproducten die een voorwaarde zijn voor dit
 * onderwijsproduct
 * 
 * @author vandekamp
 */
@PageInfo(title = "Onderwijsproduct Voorwaarden", menu = {"Onderwijs > Onderwijsproducten > [onderwijsproduct] > Voorwaarden"})
@InPrincipal(OnderwijsproductVoorwaarden.class)
public class OnderwijsproductVoorwaardenPage extends AbstractOnderwijsproductPage
{
	private static final long serialVersionUID = 1L;

	private final OnderwijsproductZoekFilter getDefaultFilter(Onderwijsproduct onderwijsproduct)
	{
		OnderwijsproductZoekFilter filter = new OnderwijsproductZoekFilter();
		filter.setAuthorizationContext(new OrganisatieEenheidLocatieAuthorizationContext(this));
		filter.setVoorwaardeVoor(onderwijsproduct);
		filter.addOrderByProperty("titel");
		filter.setResultCacheable(false);
		return filter;
	}

	public OnderwijsproductVoorwaardenPage(Onderwijsproduct onderwijsproduct)
	{
		super(OnderwijsproductMenuItem.Voorwaarden, ModelFactory.getModel(onderwijsproduct));
		OnderwijsproductZoekFilter filter = getDefaultFilter(onderwijsproduct);
		GeneralFilteredSortableDataProvider<Onderwijsproduct, OnderwijsproductZoekFilter> provider =
			GeneralFilteredSortableDataProvider.of(filter,
				OnderwijsproductDataAccessHelper.class);
		OnderwijsproductTable cols = new OnderwijsproductTable(true);
		cols.setTitle("Voorwaardelijke onderwijsproducten");
		cols.addColumn(new ModuleDependentDeleteColumn<Onderwijsproduct>("Verwijder", "Verwijder",
			EduArteModuleKey.ONDERWIJSCATALOGUS)
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick(WebMarkupContainer item, IModel<Onderwijsproduct> rowModel)
			{
				Onderwijsproduct voorwaarde = rowModel.getObject();
				Onderwijsproduct voorwaardeVoor = getContextOnderwijsproduct();
				voorwaardeVoor.deleteVoorwaarde(voorwaarde);
				voorwaardeVoor.commit();
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
		panel.addButton(new ModuleEditPageButton<OnderwijsproductVoorwaarde>(panel, "Toevoegen",
			CobraKeyAction.TOEVOEGEN, OnderwijsproductVoorwaarde.class,
			OnderwijsproductVoorwaardenPage.this)
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected OnderwijsproductVoorwaarde getEntity()
			{
				OnderwijsproductVoorwaarde voorwaarde = new OnderwijsproductVoorwaarde();
				Onderwijsproduct voorwaardeVoor =
					(Onderwijsproduct) OnderwijsproductVoorwaardenPage.this.getDefaultModelObject();
				voorwaarde.setVoorwaardeVoor(voorwaardeVoor);
				return voorwaarde;
			}

			@Override
			public boolean isVisible()
			{
				Onderwijsproduct onderwijsproduct =
					(Onderwijsproduct) OnderwijsproductVoorwaardenPage.this.getDefaultModelObject();
				return super.isVisible() && !onderwijsproduct.isStartonderwijsproduct();
			}

		});
	}
}
