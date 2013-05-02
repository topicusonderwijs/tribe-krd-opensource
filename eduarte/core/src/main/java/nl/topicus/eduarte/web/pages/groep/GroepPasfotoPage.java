/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.web.pages.groep;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.app.SkipClass;
import nl.topicus.cobra.dataproviders.GeneralFilteredSortableDataProvider;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.web.components.navigation.paging.CustomPagingNavigator;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.cobra.web.components.search.browser.SearchBrowserModel;
import nl.topicus.eduarte.app.EduArteRequestCycle;
import nl.topicus.eduarte.core.principals.groep.GroepPasfotos;
import nl.topicus.eduarte.dao.helpers.DeelnemerDataAccessHelper;
import nl.topicus.eduarte.entities.groep.Groep;
import nl.topicus.eduarte.entities.personen.Deelnemer;
import nl.topicus.eduarte.providers.GroepProvider;
import nl.topicus.eduarte.web.components.image.DeelnemerImage;
import nl.topicus.eduarte.web.components.menu.GroepMenuItem;
import nl.topicus.eduarte.web.components.panels.bottomrow.JasperReportBottomRowButton;
import nl.topicus.eduarte.web.pages.deelnemer.deelnemerkaart.DeelnemerkaartPage;
import nl.topicus.eduarte.zoekfilters.DeelnemerZoekFilter;

import org.apache.wicket.PageParameters;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.navigation.paging.PagingNavigator;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.GridView;
import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

/**
 * @author hoeve
 */
@PageInfo(title = "Groep Pasfotos", menu = "Groep > [groep] -> Pasfotos")
@SkipClass("Deze page doet meerdere concurrent resource requests (foto's) en heeft last van de race condition in de detach check")
@InPrincipal(GroepPasfotos.class)
public class GroepPasfotoPage extends AbstractGroepPage
{
	private static final long serialVersionUID = 1L;

	private GridView<Deelnemer> gridView;

	private DeelnemerZoekFilter getDefaultZoekFilter()
	{
		Groep groep = (Groep) getDefaultModelObject();
		DeelnemerZoekFilter filter = new DeelnemerZoekFilter();
		filter.setGroep(groep);
		filter.addOrderByProperty("deelnemernummer");
		filter.addOrderByProperty("persoon.roepnaam");
		filter.addOrderByProperty("persoon.achternaam");

		return filter;
	}

	public GroepPasfotoPage(PageParameters parameters)
	{
		this(AbstractGroepPage.getGroepFromPageParameters(parameters));
	}

	public GroepPasfotoPage(GroepProvider provider)
	{
		this(provider.getGroep());
	}

	public GroepPasfotoPage(Groep groep)
	{
		super(GroepMenuItem.Pasfotos, groep);

		WebMarkupContainer table = new WebMarkupContainer("table");
		table.setOutputMarkupId(true);
		add(table);

		gridView =
			new GridView<Deelnemer>("rows", GeneralFilteredSortableDataProvider.of(
				getDefaultZoekFilter(), DeelnemerDataAccessHelper.class))
			{
				private static final long serialVersionUID = 1L;

				@Override
				protected void populateEmptyItem(Item<Deelnemer> item)
				{
					WebMarkupContainer deelnemerLink = new WebMarkupContainer("deelnemerPanel");
					deelnemerLink.setVisible(false);
					deelnemerLink.add(new Image("foto", new Model<String>("geen foto"))
						.setVisible(false));
					deelnemerLink.add(new Label("naam", new Model<String>("geen deelnemer"))
						.setVisible(false));
					item.add(deelnemerLink);
				}

				@Override
				protected void populateItem(final Item<Deelnemer> item)
				{
					Link<Deelnemer> deelnemerLink =
						new Link<Deelnemer>("deelnemerPanel", item.getModel())
						{
							private static final long serialVersionUID = 1L;

							@Override
							public void onClick()
							{
								// bepaal het volgnummer in de zoekresultaten voor de zoek
								// resultaat
								// browser
								IDataProvider<Deelnemer> myProvider = gridView.getDataProvider();

								SearchBrowserModel<Deelnemer> searchBrowserModel =
									new SearchBrowserModel<Deelnemer>(myProvider);
								searchBrowserModel.setCurrentPage(gridView.getCurrentPage());
								searchBrowserModel.setItemsPerPage(gridView.getColumns()
									* gridView.getRows());
								searchBrowserModel.setItemCount(gridView.getRowCount());
								searchBrowserModel.setCurrentItem(item.getIndex());

								// voeg pagina toe aan navigatie //
								addNavigationLevel(searchBrowserModel);

								Deelnemer myDeelnemer = getModelObject();
								EduArteRequestCycle.get().setResponsePage(
									new DeelnemerkaartPage(myDeelnemer));
							}
						};

					deelnemerLink.add(new DeelnemerImage("foto", item.getModel()));
					deelnemerLink.add(new Label("naam", item.getModel()));
					item.add(deelnemerLink);
				}
			}.setColumns(7).setRows(5);
		table.add(gridView);

		WebMarkupContainer navigatorContainer = new WebMarkupContainer("navigatorContainer")
		{
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isVisible()
			{
				return gridView.getPageCount() > 1 && gridView.getRowCount() > 0;
			}
		};
		PagingNavigator navigator = new CustomPagingNavigator("navigator", gridView, null);
		navigatorContainer.add(navigator);
		table.add(navigatorContainer);

		createComponents();
	}

	@Override
	protected void fillBottomRow(BottomRowPanel panel)
	{
		super.fillBottomRow(panel);

		panel.addButton(new JasperReportBottomRowButton<Groep>(panel, "groeppasfotos.jrxml",
			getClass(), "groep")
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected IModel<Groep> getContextModel()
			{
				return getContextGroepModel();
			}
		});
	}
}
