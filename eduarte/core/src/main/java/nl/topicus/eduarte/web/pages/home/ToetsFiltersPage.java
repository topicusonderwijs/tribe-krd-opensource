/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.web.pages.home;

import nl.topicus.cobra.app.INavigationBasePage;
import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.dataproviders.GeneralFilteredSortableDataProvider;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.security.RechtenSoort;
import nl.topicus.cobra.security.RechtenSoorten;
import nl.topicus.cobra.web.components.datapanel.CustomColumn.Positioning;
import nl.topicus.cobra.web.components.datapanel.CustomDataPanelPageLinkRowFactory;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.cobra.web.components.panels.bottomrow.ToevoegenButton;
import nl.topicus.cobra.web.components.panels.datapanel.columns.AjaxDeleteColumn;
import nl.topicus.eduarte.app.security.actions.Begeleider;
import nl.topicus.eduarte.app.security.actions.Docent;
import nl.topicus.eduarte.app.security.actions.Instelling;
import nl.topicus.eduarte.app.security.actions.OrganisatieEenheid;
import nl.topicus.eduarte.app.security.actions.SearchImplementsActions;
import nl.topicus.eduarte.core.principals.deelnemer.resultaten.DeelnemerResultatenboom;
import nl.topicus.eduarte.core.principals.deelnemer.resultaten.DeelnemerResultatenmatrix;
import nl.topicus.eduarte.dao.helpers.ToetsCodeFilterDataAccessHelper;
import nl.topicus.eduarte.entities.resultaatstructuur.ToetsCodeFilter;
import nl.topicus.eduarte.web.components.menu.HomeMenuItem;
import nl.topicus.eduarte.web.components.panels.EduArteDataPanel;
import nl.topicus.eduarte.web.components.panels.datapanel.table.ToetsCodeFilterTable;
import nl.topicus.eduarte.web.pages.shared.ToetsFilterEditPage;
import nl.topicus.eduarte.zoekfilters.OrganisatieEenheidLocatieAuthorizationContext;
import nl.topicus.eduarte.zoekfilters.ToetsCodeFilterZoekFilter;

import org.apache.wicket.Page;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.link.IPageLink;
import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.IModel;

@PageInfo(title = "Toetsfilters", menu = "Home > Instellingen > Toetsfilters")
@InPrincipal({DeelnemerResultatenboom.class, DeelnemerResultatenmatrix.class})
@RechtenSoorten(RechtenSoort.INSTELLING)
@SearchImplementsActions({Instelling.class, OrganisatieEenheid.class, Begeleider.class,
	Docent.class})
public class ToetsFiltersPage extends AbstractHomePage<Void> implements INavigationBasePage
{
	private static final long serialVersionUID = 1L;

	private EduArteDataPanel<ToetsCodeFilter> datapanel;

	public ToetsFiltersPage()
	{
		super(HomeMenuItem.Toetsfilters);

		ToetsCodeFilterZoekFilter filter = ToetsCodeFilterZoekFilter.createDefaultFilter();
		filter.setAuthorizationContext(new OrganisatieEenheidLocatieAuthorizationContext(this));
		filter.setMedewerker(getIngelogdeMedewerker());
		filter.setPersoonlijk(true);

		IDataProvider<ToetsCodeFilter> dataProvider =
			GeneralFilteredSortableDataProvider.of(filter, ToetsCodeFilterDataAccessHelper.class);
		ToetsCodeFilterTable table = new ToetsCodeFilterTable();
		table.addColumn(new AjaxDeleteColumn<ToetsCodeFilter>("Verwijderen", "")
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick(WebMarkupContainer item, IModel<ToetsCodeFilter> rowModel,
					AjaxRequestTarget target)
			{
				super.onClick(item, rowModel, target);
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
	protected void fillBottomRow(BottomRowPanel panel)
	{
		panel.addButton(new ToevoegenButton(panel, new IPageLink()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public Page getPage()
			{
				ToetsCodeFilter newFilter = new ToetsCodeFilter();
				newFilter.setMedewerker(getIngelogdeMedewerker());
				return new ToetsFilterEditPage(newFilter, ToetsFiltersPage.this);
			}

			@Override
			public Class< ? extends Page> getPageIdentity()
			{
				return ToetsFilterEditPage.class;
			}
		}));
	}
}
