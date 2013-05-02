package nl.topicus.eduarte.web.pages.onderwijs.onderwijscatalogus;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.dataproviders.GeneralFilteredSortableDataProvider;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.web.components.datapanel.CustomDataPanelPageLinkRowFactory;
import nl.topicus.cobra.web.components.datapanel.CustomColumn.Positioning;
import nl.topicus.cobra.web.components.menu.AbstractMenuBar;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.cobra.web.components.panels.bottomrow.ToevoegenButton;
import nl.topicus.cobra.web.components.panels.datapanel.columns.AjaxDeleteColumn;
import nl.topicus.eduarte.core.principals.onderwijs.ToetsCodeFilters;
import nl.topicus.eduarte.dao.helpers.ToetsCodeFilterDataAccessHelper;
import nl.topicus.eduarte.entities.resultaatstructuur.StandaardToetsCodeFilter;
import nl.topicus.eduarte.entities.resultaatstructuur.ToetsCodeFilter;
import nl.topicus.eduarte.entities.resultaatstructuur.ToetsCodeFilterOrganisatieEenheidLocatie;
import nl.topicus.eduarte.web.components.menu.OnderwijsCollectiefMenu;
import nl.topicus.eduarte.web.components.menu.OnderwijsCollectiefMenuItem;
import nl.topicus.eduarte.web.components.menu.main.CoreMainMenuItem;
import nl.topicus.eduarte.web.components.panels.EduArteDataPanel;
import nl.topicus.eduarte.web.components.panels.datapanel.table.ToetsCodeFilterTable;
import nl.topicus.eduarte.web.pages.SecurePage;
import nl.topicus.eduarte.web.pages.shared.ToetsFilterEditPage;
import nl.topicus.eduarte.zoekfilters.OrganisatieEenheidLocatieAuthorizationContext;
import nl.topicus.eduarte.zoekfilters.ToetsCodeFilterZoekFilter;

import org.apache.wicket.Page;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.link.IPageLink;
import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.IModel;

/**
 * Pagina voor het zoeken naar onderwijsproducten.
 * 
 * @author loite
 */
@PageInfo(title = "Toetscodefilters beheren", menu = "Onderwijs > Onderwijsproducten > Toetsfilters")
@InPrincipal(ToetsCodeFilters.class)
public class ToetsCodeFiltersOverzichtPage extends SecurePage
{
	private static final long serialVersionUID = 1L;

	private EduArteDataPanel<ToetsCodeFilter> datapanel;

	public ToetsCodeFiltersOverzichtPage()
	{
		super(CoreMainMenuItem.Onderwijs);

		ToetsCodeFilterZoekFilter filter = ToetsCodeFilterZoekFilter.createDefaultFilter();
		filter.setMedewerker(getIngelogdeMedewerker());
		filter.setPersoonlijk(false);
		filter.setAuthorizationContext(new OrganisatieEenheidLocatieAuthorizationContext(this));

		IDataProvider<ToetsCodeFilter> dataProvider =
			GeneralFilteredSortableDataProvider.of(filter,
				ToetsCodeFilterDataAccessHelper.class);
		ToetsCodeFilterTable table = new ToetsCodeFilterTable();
		table.addColumn(new AjaxDeleteColumn<ToetsCodeFilter>("Verwijderen", "")
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick(WebMarkupContainer item, IModel<ToetsCodeFilter> rowModel,
					AjaxRequestTarget target)
			{
				ToetsCodeFilter itemFilter = rowModel.getObject();
				for (ToetsCodeFilterOrganisatieEenheidLocatie koppel : itemFilter
					.getOrganisatieEenhedenLocaties())
				{
					koppel.delete();
				}
				for (StandaardToetsCodeFilter koppel : itemFilter.getStandaardSelelecties())
				{
					koppel.delete();
				}
				itemFilter.delete();
				itemFilter.commit();
				target.addComponent(datapanel);
			}
		}.setPositioning(Positioning.FIXED_RIGHT));
		datapanel =
			new EduArteDataPanel<ToetsCodeFilter>("filterDataPanel", dataProvider,
				new ToetsCodeFilterTable());
		datapanel.setRowFactory(new CustomDataPanelPageLinkRowFactory<ToetsCodeFilter>(
			ToetsFilterEditPage.class));
		add(datapanel);
		createComponents();
	}

	@Override
	public AbstractMenuBar createMenu(String id)
	{
		return new OnderwijsCollectiefMenu(id, OnderwijsCollectiefMenuItem.Toetsfilters);
	}

	@Override
	protected void fillBottomRow(BottomRowPanel panel)
	{
		panel.addButton(new ToevoegenButton(panel, new IPageLink()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public Page getPage()
			{
				ToetsCodeFilter newFilter = new ToetsCodeFilter();
				return new ToetsFilterEditPage(newFilter, ToetsCodeFiltersOverzichtPage.this);
			}

			@Override
			public Class< ? extends Page> getPageIdentity()
			{
				return ToetsFilterEditPage.class;
			}
		}));
	}
}
