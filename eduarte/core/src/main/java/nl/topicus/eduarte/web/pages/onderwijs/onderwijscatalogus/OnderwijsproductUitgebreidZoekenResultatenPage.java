/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.web.pages.onderwijs.onderwijscatalogus;

import java.util.List;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.dataproviders.GeneralFilteredSortableDataProvider;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.web.components.datapanel.CustomDataPanel;
import nl.topicus.cobra.web.components.datapanel.CustomDataPanelPageLinkRowFactory;
import nl.topicus.cobra.web.components.datapanel.DefaultCustomDataPanelId;
import nl.topicus.cobra.web.components.menu.AbstractMenuBar;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.cobra.web.components.panels.bottomrow.TerugButton;
import nl.topicus.eduarte.app.security.actions.Instelling;
import nl.topicus.eduarte.app.security.actions.OrganisatieEenheid;
import nl.topicus.eduarte.app.security.actions.SearchImplementsActions;
import nl.topicus.eduarte.core.principals.onderwijs.OnderwijsproductenUitgebreidZoeken;
import nl.topicus.eduarte.dao.helpers.OnderwijsproductDataAccessHelper;
import nl.topicus.eduarte.entities.onderwijsproduct.Onderwijsproduct;
import nl.topicus.eduarte.web.components.menu.OnderwijsCollectiefMenu;
import nl.topicus.eduarte.web.components.menu.OnderwijsCollectiefMenuItem;
import nl.topicus.eduarte.web.components.menu.main.CoreMainMenuItem;
import nl.topicus.eduarte.web.components.panels.EduArteDataPanel;
import nl.topicus.eduarte.web.components.panels.datapanel.table.OnderwijsproductTable;
import nl.topicus.eduarte.web.pages.SecurePage;
import nl.topicus.eduarte.zoekfilters.OnderwijsproductZoekFilter;

import org.apache.wicket.Page;
import org.apache.wicket.markup.html.link.IPageLink;
import org.apache.wicket.markup.repeater.Item;

/**
 * @author loite
 */
@PageInfo(title = "Onderwijsproduct uitgebreid zoeken resultaten", menu = "Onderwijs > Onderwijsproducten > Uitgebreid zoeken")
@InPrincipal(OnderwijsproductenUitgebreidZoeken.class)
@SearchImplementsActions( {Instelling.class, OrganisatieEenheid.class})
public class OnderwijsproductUitgebreidZoekenResultatenPage extends SecurePage
{
	private static final long serialVersionUID = 1L;

	private final OnderwijsproductZoekFilter filter;

	/**
	 * Constructor
	 * 
	 * @param filter
	 */
	public OnderwijsproductUitgebreidZoekenResultatenPage(OnderwijsproductZoekFilter filter)
	{
		super(CoreMainMenuItem.Onderwijs);
		this.filter = filter;
		if (filter.getPeildatum() == null)
		{
			info("Let op: geen (geldige) datum ingevoerd. De peildatum is niet meegenomen bij de zoekopdracht.");
		}

		GeneralFilteredSortableDataProvider<Onderwijsproduct, OnderwijsproductZoekFilter> provider =
			GeneralFilteredSortableDataProvider.of(filter,
				OnderwijsproductDataAccessHelper.class);

		final CustomDataPanel<Onderwijsproduct> datapanel =
			new EduArteDataPanel<Onderwijsproduct>("datapanel", new DefaultCustomDataPanelId(
				OnderwijsproductZoekenPage.class, "datapanel"), provider,
				new OnderwijsproductTable(true));
		datapanel.setRowFactory(new CustomDataPanelPageLinkRowFactory<Onderwijsproduct>(
			OnderwijsproductKaartPage.class)
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick(Item<Onderwijsproduct> item)
			{
				Onderwijsproduct onderwijsproduct = item.getModelObject();
				pushSearchResultToNavigationLevel(datapanel, item.getIndex());
				setResponsePage(new OnderwijsproductKaartPage(onderwijsproduct));
			}
		});
		datapanel.setItemsPerPage(20);
		add(datapanel);

		createComponents();
	}

	@Override
	protected void fillBottomRow(BottomRowPanel panel)
	{
		TerugButton terug = new TerugButton(panel, new IPageLink()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public Page getPage()
			{
				return new OnderwijsproductUitgebreidZoekenPage(
					OnderwijsproductUitgebreidZoekenResultatenPage.this.filter);
			}

			@Override
			public Class< ? extends SecurePage> getPageIdentity()
			{
				return OnderwijsproductUitgebreidZoekenPage.class;
			}

		});
		terug.setLabel("Zoekopdracht aanpassen");
		panel.addButton(terug);
	}

	@Override
	public AbstractMenuBar createMenu(String id)
	{
		return new OnderwijsCollectiefMenu(id,
			OnderwijsCollectiefMenuItem.OnderwijsproductenUitgebreidZoeken);
	}

	@Override
	public void detachModels()
	{
		super.detachModels();
		filter.detach();
	}

	@Override
	public void getBookmarkConstructorArguments(List<Class< ? >> ctorArgTypes,
			List<Object> ctorArgValues)
	{
		super.getBookmarkConstructorArguments(ctorArgTypes, ctorArgValues);
		ctorArgTypes.add(OnderwijsproductZoekFilter.class);
		ctorArgValues.add(filter);
	}
}
