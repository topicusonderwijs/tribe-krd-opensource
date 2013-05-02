package nl.topicus.eduarte.web.pages.onderwijs.onderwijscatalogus;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.dataproviders.GeneralFilteredSortableDataProvider;
import nl.topicus.cobra.modelsv2.ModelFactory;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.web.components.datapanel.CustomDataPanel;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.cobra.web.components.shortcut.CobraKeyAction;
import nl.topicus.eduarte.app.EduArteModuleKey;
import nl.topicus.eduarte.core.principals.onderwijs.OnderwijsproductVerbruiksmiddelen;
import nl.topicus.eduarte.dao.helpers.VerbruiksmiddelDataAccessHelper;
import nl.topicus.eduarte.entities.onderwijsproduct.Onderwijsproduct;
import nl.topicus.eduarte.entities.onderwijsproduct.Verbruiksmiddel;
import nl.topicus.eduarte.web.components.menu.OnderwijsproductMenuItem;
import nl.topicus.eduarte.web.components.panels.EduArteDataPanel;
import nl.topicus.eduarte.web.components.panels.bottomrow.ModuleEditPageButton;
import nl.topicus.eduarte.web.components.panels.datapanel.columns.ModuleDependentDeleteColumn;
import nl.topicus.eduarte.web.components.panels.datapanel.table.CodeNaamActiefTable;
import nl.topicus.eduarte.zoekfilters.VerbruiksmiddelZoekFilter;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.model.IModel;

/**
 * Koppeling van het onderwijsproduct met verbruiksmiddelen
 * 
 * @author vandekamp
 */
@PageInfo(title = "Onderwijsproduct Verbruiksmiddelen", menu = {"Onderwijs > Onderwijsproducten > [onderwijsproduct] > Verbruiksmiddel"})
@InPrincipal(OnderwijsproductVerbruiksmiddelen.class)
public class OnderwijsproductVerbruiksmiddelPage extends AbstractOnderwijsproductPage
{
	private static final long serialVersionUID = 1L;

	private static final VerbruiksmiddelZoekFilter getDefaultFilter(
			Onderwijsproduct onderwijsproduct)
	{
		VerbruiksmiddelZoekFilter filter = new VerbruiksmiddelZoekFilter();
		filter.setOnderwijsproduct(onderwijsproduct);
		filter.addOrderByProperty("code");
		filter.setResultCacheable(false);

		return filter;
	}

	public OnderwijsproductVerbruiksmiddelPage(Onderwijsproduct onderwijsproduct)
	{
		super(OnderwijsproductMenuItem.Verbruiksmiddelen, ModelFactory.getModel(onderwijsproduct));
		VerbruiksmiddelZoekFilter filter = getDefaultFilter(onderwijsproduct);
		GeneralFilteredSortableDataProvider<Verbruiksmiddel, VerbruiksmiddelZoekFilter> provider =
			GeneralFilteredSortableDataProvider.of(filter,
				VerbruiksmiddelDataAccessHelper.class);
		CodeNaamActiefTable<Verbruiksmiddel> cols =
			new CodeNaamActiefTable<Verbruiksmiddel>("Verbruiksmiddelen");
		cols.addColumn(new ModuleDependentDeleteColumn<Verbruiksmiddel>("Verwijder", "Verwijder",
			EduArteModuleKey.ONDERWIJSCATALOGUS)
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick(WebMarkupContainer item, IModel<Verbruiksmiddel> rowModel)
			{
				Verbruiksmiddel verbruiksmiddel = rowModel.getObject();
				Onderwijsproduct onderwijspr = getContextOnderwijsproduct();
				onderwijspr.deleteVerbruiksmiddel(verbruiksmiddel);
				onderwijspr.commit();
			}
		});

		CustomDataPanel<Verbruiksmiddel> datapanel =
			new EduArteDataPanel<Verbruiksmiddel>("datapanel", provider, cols);
		add(datapanel);
		createComponents();

	}

	@Override
	protected void fillBottomRow(BottomRowPanel panel)
	{
		super.fillBottomRow(panel);

		panel.addButton(new ModuleEditPageButton<Onderwijsproduct>(panel, "Toevoegen",
			CobraKeyAction.TOEVOEGEN, Onderwijsproduct.class,
			OnderwijsproductMenuItem.Verbruiksmiddelen, OnderwijsproductVerbruiksmiddelPage.this,
			getContextOnderwijsproductModel()));
	}
}
