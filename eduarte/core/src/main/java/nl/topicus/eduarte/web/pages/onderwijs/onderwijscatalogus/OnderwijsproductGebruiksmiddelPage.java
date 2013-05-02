package nl.topicus.eduarte.web.pages.onderwijs.onderwijscatalogus;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.dataproviders.GeneralFilteredSortableDataProvider;
import nl.topicus.cobra.modelsv2.ModelFactory;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.web.components.datapanel.CustomDataPanel;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.cobra.web.components.shortcut.CobraKeyAction;
import nl.topicus.eduarte.app.EduArteModuleKey;
import nl.topicus.eduarte.core.principals.onderwijs.OnderwijsproductGebruiksmiddelen;
import nl.topicus.eduarte.dao.helpers.GebruiksmiddelDataAccessHelper;
import nl.topicus.eduarte.entities.onderwijsproduct.Gebruiksmiddel;
import nl.topicus.eduarte.entities.onderwijsproduct.Onderwijsproduct;
import nl.topicus.eduarte.web.components.menu.OnderwijsproductMenuItem;
import nl.topicus.eduarte.web.components.panels.EduArteDataPanel;
import nl.topicus.eduarte.web.components.panels.bottomrow.ModuleEditPageButton;
import nl.topicus.eduarte.web.components.panels.datapanel.columns.ModuleDependentDeleteColumn;
import nl.topicus.eduarte.web.components.panels.datapanel.table.CodeNaamActiefTable;
import nl.topicus.eduarte.zoekfilters.GebruiksmiddelZoekFilter;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.IModel;

/**
 * Koppeling van het onderwijsproduct met gebruiksmiddelen
 * 
 * @author vandekamp
 */
@PageInfo(title = "Gebruiksmiddelen", menu = {"Onderwijs > Onderwijsproducten > [onderwijsproduct] > Gebruiksmiddel"})
@InPrincipal(OnderwijsproductGebruiksmiddelen.class)
public class OnderwijsproductGebruiksmiddelPage extends AbstractOnderwijsproductPage
{
	private static final long serialVersionUID = 1L;

	private static final GebruiksmiddelZoekFilter getDefaultFilter(Onderwijsproduct onderwijsproduct)
	{
		GebruiksmiddelZoekFilter filter = new GebruiksmiddelZoekFilter();
		filter.setOnderwijsproduct(onderwijsproduct);
		filter.addOrderByProperty("code");
		filter.setResultCacheable(false);

		return filter;
	}

	public OnderwijsproductGebruiksmiddelPage(Onderwijsproduct onderwijsproduct)
	{
		super(OnderwijsproductMenuItem.Gebruiksmiddelen, ModelFactory.getModel(onderwijsproduct));
		GebruiksmiddelZoekFilter filter = getDefaultFilter(onderwijsproduct);
		IDataProvider<Gebruiksmiddel> provider =
			GeneralFilteredSortableDataProvider.of(filter, GebruiksmiddelDataAccessHelper.class);
		CodeNaamActiefTable<Gebruiksmiddel> cols =
			new CodeNaamActiefTable<Gebruiksmiddel>("Gebruiksmiddelen");
		cols.addColumn(new ModuleDependentDeleteColumn<Gebruiksmiddel>("Verwijder", "Verwijder",
			EduArteModuleKey.ONDERWIJSCATALOGUS)
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick(WebMarkupContainer item, IModel<Gebruiksmiddel> rowModel)
			{
				Gebruiksmiddel gebruiksmiddel = rowModel.getObject();
				Onderwijsproduct onderwijspr = getContextOnderwijsproduct();
				onderwijspr.deleteGebruiksmiddel(gebruiksmiddel);
				onderwijspr.commit();
			}
		});

		CustomDataPanel<Gebruiksmiddel> datapanel =
			new EduArteDataPanel<Gebruiksmiddel>("datapanel", provider, cols);
		add(datapanel);
		createComponents();

	}

	@Override
	protected void fillBottomRow(BottomRowPanel panel)
	{
		super.fillBottomRow(panel);

		panel.addButton(new ModuleEditPageButton<Onderwijsproduct>(panel, "Toevoegen",
			CobraKeyAction.TOEVOEGEN, Onderwijsproduct.class,
			OnderwijsproductMenuItem.Gebruiksmiddelen, OnderwijsproductGebruiksmiddelPage.this,
			getContextOnderwijsproductModel()));
	}
}
