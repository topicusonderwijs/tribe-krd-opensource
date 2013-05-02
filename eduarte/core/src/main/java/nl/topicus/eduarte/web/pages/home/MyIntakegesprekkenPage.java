/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.web.pages.home;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.dataproviders.GeneralFilteredSortableDataProvider;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.util.TimeUtil;
import nl.topicus.cobra.web.components.datapanel.CustomDataPanel;
import nl.topicus.cobra.web.components.datapanel.CustomDataPanelPageLinkRowFactory;
import nl.topicus.eduarte.app.EduArteContext;
import nl.topicus.eduarte.core.principals.Always;
import nl.topicus.eduarte.dao.helpers.IntakegesprekDataAccessHelper;
import nl.topicus.eduarte.entities.inschrijving.Intakegesprek;
import nl.topicus.eduarte.entities.inschrijving.Intakegesprek.IntakegesprekStatus;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis;
import nl.topicus.eduarte.web.components.menu.HomeMenuItem;
import nl.topicus.eduarte.web.components.panels.EduArteDataPanel;
import nl.topicus.eduarte.web.components.panels.datapanel.table.IntakegesprekTable;
import nl.topicus.eduarte.web.components.panels.filter.MyIntakegesprekkenZoekFilterPanel;
import nl.topicus.eduarte.web.pages.deelnemer.intake.DeelnemerIntakePage;
import nl.topicus.eduarte.zoekfilters.IntakegesprekZoekFilter;

import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.IDataProvider;

/**
 * Pagina met een lijst van intakegesprekken welke de medewerker nog moet afnemen.
 */
@PageInfo(title = "Medewerker intakegesprekken", menu = "Home > Te Doen > Intakegesprekken")
@InPrincipal(Always.class)
public class MyIntakegesprekkenPage extends AbstractHomePage<Void>
{
	private static final long serialVersionUID = 1L;

	private final MyIntakegesprekkenZoekFilterPanel filterPanel;

	private CustomDataPanel<Intakegesprek> datapanel;

	/**
	 * @return Default zoekfilter voor de ingelogde gebruiker.
	 */
	private static final IntakegesprekZoekFilter getDefaultFilter()
	{
		IntakegesprekZoekFilter filter = new IntakegesprekZoekFilter();
		filter.setIntaker(EduArteContext.get().getMedewerker());
		filter.setDatumTijd(TimeUtil.getInstance().currentDate());
		filter.setStatus(IntakegesprekStatus.Uitvoeren);
		filter.addOrderByProperty("datumTijd");

		return filter;
	}

	public MyIntakegesprekkenPage()
	{
		this(getDefaultFilter());
	}

	@SuppressWarnings("unchecked")
	public MyIntakegesprekkenPage(IntakegesprekZoekFilter filter)
	{
		super(HomeMenuItem.Intakegesprekken);

		IDataProvider<Intakegesprek> provider =
			GeneralFilteredSortableDataProvider.of(filter, IntakegesprekDataAccessHelper.class);

		CustomDataPanelPageLinkRowFactory<Intakegesprek> rowFactory =
			new CustomDataPanelPageLinkRowFactory<Intakegesprek>(DeelnemerIntakePage.class)
			{
				private static final long serialVersionUID = 1L;

				@Override
				public void onClick(Item<Intakegesprek> item)
				{
					Intakegesprek intakegesprek = item.getModelObject();
					Verbintenis verbintenis = intakegesprek.getVerbintenis();

					pushSearchResultToNavigationLevel(datapanel, item.getIndex());

					setResponsePage(new DeelnemerIntakePage(verbintenis, intakegesprek));
				}
			};

		datapanel =
			new EduArteDataPanel<Intakegesprek>("datapanel", provider, new IntakegesprekTable(null,
				true));
		datapanel.setRowFactory(rowFactory);
		datapanel.setItemsPerPage(20);
		add(datapanel);
		filterPanel = new MyIntakegesprekkenZoekFilterPanel("filter", filter, datapanel);
		add(filterPanel);
		createComponents();
	}
}