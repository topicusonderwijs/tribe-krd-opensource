/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.web.pages.deelnemer;

import java.util.List;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.dataproviders.GeneralFilteredSortableDataProvider;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.web.components.datapanel.CustomDataPanelId;
import nl.topicus.cobra.web.components.datapanel.CustomDataPanelPageLinkRowFactory;
import nl.topicus.cobra.web.components.datapanel.DefaultCustomDataPanelId;
import nl.topicus.cobra.web.components.menu.AbstractMenuBar;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.cobra.web.components.panels.bottomrow.TerugButton;
import nl.topicus.eduarte.app.EduArteApp;
import nl.topicus.eduarte.app.security.actions.Instelling;
import nl.topicus.eduarte.app.security.actions.OrganisatieEenheid;
import nl.topicus.eduarte.app.security.actions.SearchImplementsActions;
import nl.topicus.eduarte.core.principals.deelnemer.DeelnemersZoeken;
import nl.topicus.eduarte.dao.helpers.VerbintenisDataAccessHelper;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis;
import nl.topicus.eduarte.web.components.factory.KRDModuleComponentFactory;
import nl.topicus.eduarte.web.components.menu.DeelnemerCollectiefMenu;
import nl.topicus.eduarte.web.components.menu.DeelnemerCollectiefMenuItem;
import nl.topicus.eduarte.web.components.menu.main.CoreMainMenuItem;
import nl.topicus.eduarte.web.components.panels.DeelnemerDataPanel;
import nl.topicus.eduarte.web.components.panels.datapanel.table.DeelnemerTable;
import nl.topicus.eduarte.web.components.panels.filter.AutoZoekFilterPanel;
import nl.topicus.eduarte.web.components.panels.filter.DeelnemerZoekFilterPanel;
import nl.topicus.eduarte.web.pages.SecurePage;
import nl.topicus.eduarte.web.pages.deelnemer.deelnemerkaart.DeelnemerkaartPage;
import nl.topicus.eduarte.zoekfilters.OrganisatieEenheidLocatieAuthorizationContext;
import nl.topicus.eduarte.zoekfilters.VerbintenisZoekFilter;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.repeater.Item;

/**
 * @author loite
 */
@PageInfo(title = "Deelnemer zoeken", menu = "Deelnemer")
@InPrincipal(DeelnemersZoeken.class)
@SearchImplementsActions( {Instelling.class, OrganisatieEenheid.class})
public class DeelnemerZoekenPage extends SecurePage
{
	private static final long serialVersionUID = 1L;

	private final AutoZoekFilterPanel<VerbintenisZoekFilter> filterPanel;

	private SecurePage returnPage;

	/**
	 * @return Default zoekfilter voor de ingelogde gebruiker.
	 */
	public static final VerbintenisZoekFilter getDefaultFilter()
	{
		VerbintenisZoekFilter filter = new VerbintenisZoekFilter();
		// Default sortering uitgezet ivm performance. Wordt automatisch gezet bij de
		// eerste zoekactie.
		// filter.addOrderByProperty("beeindigd");
		// filter.addOrderByProperty("deelnemer.deelnemernummer");
		// filter.addOrderByProperty("persoon.roepnaam");
		// filter.addOrderByProperty("persoon.achternaam");
		filter.setAscending(false);
		filter.setStandaardDeelnemerZoeken(true);

		return filter;
	}

	public DeelnemerZoekenPage()
	{
		this(getDefaultFilter());
	}

	public DeelnemerZoekenPage(VerbintenisZoekFilter filter)
	{
		this(filter, DeelnemerZoekenPage.class, new DeelnemerTable(), null);
	}

	public DeelnemerZoekenPage(VerbintenisZoekFilter filter, Class< ? > datapanelIdClass,
			DeelnemerTable table, SecurePage returnPage)
	{
		super(CoreMainMenuItem.Deelnemer);
		this.returnPage = returnPage;
		filter.setAuthorizationContext(new OrganisatieEenheidLocatieAuthorizationContext(this));
		if (filter.isMentorOfDocentRequired() && filter.getMentorOfDocent() == null)
		{
			geenMentorOrDocentError();
			createComponents();
			filterPanel = null;
			return;
		}
		GeneralFilteredSortableDataProvider<Verbintenis, VerbintenisZoekFilter> provider =
			GeneralFilteredSortableDataProvider.of(filter, VerbintenisDataAccessHelper.class);
		CustomDataPanelId panelId =
			new DefaultCustomDataPanelId(datapanelIdClass, "deelnemerDatapanel");
		final DeelnemerDataPanel datapanel =
			new DeelnemerDataPanel("deelnemerDatapanel", panelId, provider, table);
		datapanel.setRowFactory(new CustomDataPanelPageLinkRowFactory<Verbintenis>(
			DeelnemerkaartPage.class)
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick(Item<Verbintenis> item)
			{
				Verbintenis inschrijving = item.getModelObject();
				pushSearchResultToNavigationLevel(datapanel, item.getIndex());
				setResponsePage(new DeelnemerkaartPage(inschrijving.getDeelnemer(), inschrijving));
			}
		});
		datapanel.setItemsPerPage(20);
		add(datapanel);

		filterPanel = new DeelnemerZoekFilterPanel("filter", filter, datapanel);
		add(filterPanel);
		createComponents();
	}

	@Override
	public AbstractMenuBar createMenu(String id)
	{
		if (filterPanel.getZoekfilter() != null
			&& filterPanel.getZoekfilter().getMentorOfDocent() != null)
			return new DeelnemerCollectiefMenu(id, DeelnemerCollectiefMenuItem.MijnDeelnemers);
		return new DeelnemerCollectiefMenu(id, DeelnemerCollectiefMenuItem.DeelnemerZoeken);
	}

	@Override
	public void getBookmarkConstructorArguments(List<Class< ? >> ctorArgTypes,
			List<Object> ctorArgValues)
	{
		super.getBookmarkConstructorArguments(ctorArgTypes, ctorArgValues);
		ctorArgTypes.add(VerbintenisZoekFilter.class);
		ctorArgValues.add(filterPanel.getZoekfilter());
	}

	@Override
	protected void fillBottomRow(BottomRowPanel panel)
	{
		if (returnPage == null)
		{
			List<KRDModuleComponentFactory> factories =
				EduArteApp.get().getPanelFactories(KRDModuleComponentFactory.class);
			for (KRDModuleComponentFactory factory : factories)
			{
				factory.newIntakeWizardKnop(panel);
			}
		}
		else
		{
			panel.addButton(new TerugButton(panel, returnPage));
		}
	}

	private void geenMentorOrDocentError()
	{
		this
			.error("Geen medewerker aan account gekoppeld, Mijn deelnemers pagina niet beschikbaar");
		add(new WebMarkupContainer("datapanel").setVisible(false));
		add(new WebMarkupContainer("filter").setVisible(false));
	}

	@Override
	public String getName()
	{
		return EduArteApp.get().getDeelnemerTerm() + " zoeken";
	}
}
