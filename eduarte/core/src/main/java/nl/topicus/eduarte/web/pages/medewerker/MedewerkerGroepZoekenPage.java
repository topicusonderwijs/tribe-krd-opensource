/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.web.pages.medewerker;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.dataproviders.GeneralFilteredSortableDataProvider;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.web.components.datapanel.CustomDataPanel;
import nl.topicus.cobra.web.components.datapanel.CustomDataPanelPageLinkRowFactory;
import nl.topicus.eduarte.core.principals.medewerker.MedewerkerGroepenRead;
import nl.topicus.eduarte.dao.helpers.GroepDataAccessHelper;
import nl.topicus.eduarte.entities.groep.Groep;
import nl.topicus.eduarte.entities.personen.Medewerker;
import nl.topicus.eduarte.providers.MedewerkerProvider;
import nl.topicus.eduarte.web.components.menu.MedewerkerMenuItem;
import nl.topicus.eduarte.web.components.panels.EduArteDataPanel;
import nl.topicus.eduarte.web.components.panels.datapanel.table.GroepTable;
import nl.topicus.eduarte.web.components.panels.filter.GroepZoekFilterPanel;
import nl.topicus.eduarte.web.pages.groep.GroepKaartPage;
import nl.topicus.eduarte.zoekfilters.GroepZoekFilter;
import nl.topicus.eduarte.zoekfilters.OrganisatieEenheidLocatieAuthorizationContext;

import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.IDataProvider;

/**
 * @author hoeve
 */
@PageInfo(title = "Groep zoeken", menu = "Medewerker > [Medewerker] > Groepen")
@InPrincipal(MedewerkerGroepenRead.class)
public class MedewerkerGroepZoekenPage extends AbstractMedewerkerPage
{
	private static final long serialVersionUID = 1L;

	private final GroepZoekFilterPanel filterPanel;

	/**
	 * @return Default zoekfilter voor de geselecteerde gebruiker.
	 */
	private final GroepZoekFilter getDefaultFilter(Medewerker medewerker)
	{
		GroepZoekFilter filter = new GroepZoekFilter();
		filter.setAuthorizationContext(new OrganisatieEenheidLocatieAuthorizationContext(this));
		filter.setMentorOrDocent(medewerker);
		filter.addOrderByProperty("code");
		filter.addOrderByProperty("begindatum");
		filter.addOrderByProperty("einddatum");

		return filter;
	}

	public MedewerkerGroepZoekenPage(MedewerkerProvider provider)
	{
		this(provider.getMedewerker());
	}

	public MedewerkerGroepZoekenPage(Medewerker medewerker)
	{
		super(MedewerkerMenuItem.Groepen, medewerker);

		GroepZoekFilter filter = getDefaultFilter(medewerker);
		IDataProvider<Groep> provider =
			GeneralFilteredSortableDataProvider.of(filter, GroepDataAccessHelper.class);
		final CustomDataPanel<Groep> datapanel =
			new EduArteDataPanel<Groep>("datapanel", provider, new GroepTable());
		datapanel.setRowFactory(new CustomDataPanelPageLinkRowFactory<Groep>(GroepKaartPage.class)
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick(Item<Groep> item)
			{
				Groep groep = item.getModelObject();
				pushSearchResultToNavigationLevel(datapanel, item.getIndex());
				setResponsePage(new GroepKaartPage(groep));
			}
		});
		datapanel.setItemsPerPage(20);
		add(datapanel);
		filterPanel = new GroepZoekFilterPanel("filter", filter, datapanel);
		add(filterPanel);
		createComponents();
	}
}
