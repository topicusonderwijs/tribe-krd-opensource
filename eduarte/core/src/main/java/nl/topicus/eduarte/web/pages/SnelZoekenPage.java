/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.web.pages;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.dataproviders.GeneralFilteredSortableDataProvider;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.util.ComponentUtil;
import nl.topicus.cobra.web.components.datapanel.CustomDataPanel;
import nl.topicus.cobra.web.components.datapanel.CustomDataPanelPageLinkRowFactory;
import nl.topicus.cobra.web.components.menu.AbstractMenuBar;
import nl.topicus.cobra.web.components.wiquery.CollapsablePanel;
import nl.topicus.cobra.web.security.TargetBasedSecurePageLink;
import nl.topicus.eduarte.app.security.actions.Instelling;
import nl.topicus.eduarte.app.security.actions.OrganisatieEenheid;
import nl.topicus.eduarte.app.security.actions.SearchImplementsActions;
import nl.topicus.eduarte.core.principals.Snelzoeken;
import nl.topicus.eduarte.dao.helpers.GroepDataAccessHelper;
import nl.topicus.eduarte.dao.helpers.MedewerkerDataAccessHelper;
import nl.topicus.eduarte.dao.helpers.VerbintenisDataAccessHelper;
import nl.topicus.eduarte.entities.groep.Groep;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis;
import nl.topicus.eduarte.entities.personen.Medewerker;
import nl.topicus.eduarte.web.components.menu.DeelnemerCollectiefMenu;
import nl.topicus.eduarte.web.components.menu.DeelnemerCollectiefMenuItem;
import nl.topicus.eduarte.web.components.menu.main.CoreMainMenuItem;
import nl.topicus.eduarte.web.components.panels.DeelnemerDataPanel;
import nl.topicus.eduarte.web.components.panels.EduArteDataPanel;
import nl.topicus.eduarte.web.components.panels.datapanel.table.DeelnemerTable;
import nl.topicus.eduarte.web.components.panels.datapanel.table.GroepTable;
import nl.topicus.eduarte.web.components.panels.datapanel.table.MedewerkerTable;
import nl.topicus.eduarte.web.pages.deelnemer.DeelnemerZoekenPage;
import nl.topicus.eduarte.web.pages.deelnemer.deelnemerkaart.DeelnemerkaartPage;
import nl.topicus.eduarte.web.pages.groep.GroepKaartPage;
import nl.topicus.eduarte.web.pages.groep.GroepZoekenPage;
import nl.topicus.eduarte.web.pages.medewerker.MedewerkerZoekenPage;
import nl.topicus.eduarte.web.pages.medewerker.MedewerkerkaartPage;
import nl.topicus.eduarte.zoekfilters.GroepZoekFilter;
import nl.topicus.eduarte.zoekfilters.MedewerkerZoekFilter;
import nl.topicus.eduarte.zoekfilters.VerbintenisZoekFilter;

import org.apache.wicket.Page;
import org.apache.wicket.markup.html.link.IPageLink;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.security.checks.ClassSecurityCheck;

/**
 * @author loite
 */
@PageInfo(title = "Snelzoeken", menu = "Home")
@InPrincipal(Snelzoeken.class)
@SearchImplementsActions( {Instelling.class, OrganisatieEenheid.class})
public class SnelZoekenPage extends SecurePage
{
	private static final long serialVersionUID = 1L;

	private class VerbintenisPanel extends CollapsablePanel<Void>
	{
		private static final long serialVersionUID = 1L;

		private VerbintenisZoekFilter verbintenisFilter;

		public VerbintenisPanel(String id, long deelnemerCount,
				VerbintenisZoekFilter verbintenisFilter)
		{
			super(id, deelnemerCount + " deelnemers");
			this.verbintenisFilter = verbintenisFilter;
			ComponentUtil.setSecurityCheck(this, new ClassSecurityCheck(DeelnemerkaartPage.class));
		}

		@Override
		protected void createContents()
		{
			GeneralFilteredSortableDataProvider<Verbintenis, VerbintenisZoekFilter> verbintenisProvider =
				GeneralFilteredSortableDataProvider.of(verbintenisFilter,
					VerbintenisDataAccessHelper.class);
			final DeelnemerDataPanel verbintenisDatapanel =
				new DeelnemerDataPanel("verbintenisDatapanel", verbintenisProvider,
					new DeelnemerTable());
			verbintenisDatapanel.setRowFactory(new CustomDataPanelPageLinkRowFactory<Verbintenis>(
				DeelnemerkaartPage.class)
			{
				private static final long serialVersionUID = 1L;

				@Override
				public void onClick(Item<Verbintenis> item)
				{
					Verbintenis inschrijving = item.getModelObject();
					pushSearchResultToNavigationLevel(verbintenisDatapanel, item.getIndex());
					setResponsePage(new DeelnemerkaartPage(inschrijving.getDeelnemer(),
						inschrijving));
				}
			});
			verbintenisDatapanel.setItemsPerPage(5);
			verbintenisDatapanel.setNavigatorVisible(false);
			add(verbintenisDatapanel);
			add(new TargetBasedSecurePageLink<Void>("verbintenisTotalenTonenButton",
				new IPageLink()
				{

					private static final long serialVersionUID = 1L;

					@Override
					public Page getPage()
					{
						return new DeelnemerZoekenPage(verbintenisFilter);
					}

					@Override
					public Class< ? extends Page> getPageIdentity()
					{
						return DeelnemerZoekenPage.class;
					}
				}));
		}

		@Override
		protected void onDetach()
		{
			super.onDetach();
			verbintenisFilter.detach();
		}
	}

	private class GroepPanel extends CollapsablePanel<Void>
	{
		private static final long serialVersionUID = 1L;

		private GroepZoekFilter groepFilter;

		public GroepPanel(String id, int groepCount, GroepZoekFilter groepFilter)
		{
			super(id, groepCount + " groepen");
			this.groepFilter = groepFilter;
			ComponentUtil.setSecurityCheck(this, new ClassSecurityCheck(GroepKaartPage.class));
		}

		@Override
		protected void createContents()
		{
			GeneralFilteredSortableDataProvider<Groep, GroepZoekFilter> groepProvider =
				GeneralFilteredSortableDataProvider.of(groepFilter, GroepDataAccessHelper.class);
			final CustomDataPanel<Groep> groepDatapanel =
				new EduArteDataPanel<Groep>("groepDatapanel", groepProvider, new GroepTable());
			groepDatapanel.setRowFactory(new CustomDataPanelPageLinkRowFactory<Groep>(
				GroepKaartPage.class)
			{
				private static final long serialVersionUID = 1L;

				@Override
				public void onClick(Item<Groep> item)
				{
					Groep groep = item.getModelObject();
					pushSearchResultToNavigationLevel(groepDatapanel, item.getIndex());
					setResponsePage(new GroepKaartPage(groep));
				}
			});
			groepDatapanel.setItemsPerPage(5);
			groepDatapanel.setNavigatorVisible(false);
			add(groepDatapanel);
			add(new TargetBasedSecurePageLink<Void>("groepTotalenTonenButton", new IPageLink()
			{

				private static final long serialVersionUID = 1L;

				@Override
				public Page getPage()
				{
					return new GroepZoekenPage(groepFilter);
				}

				@Override
				public Class< ? extends Page> getPageIdentity()
				{
					return GroepZoekenPage.class;
				}
			}));
		}

		@Override
		protected void onDetach()
		{
			super.onDetach();
			groepFilter.detach();
		}
	}

	private class MedewerkerPanel extends CollapsablePanel<Void>
	{
		private static final long serialVersionUID = 1L;

		private MedewerkerZoekFilter medewerkerFilter;

		public MedewerkerPanel(String id, int medewerkerCount, MedewerkerZoekFilter medewerkerFilter)
		{
			super(id, medewerkerCount + " medewerkers");
			this.medewerkerFilter = medewerkerFilter;
			ComponentUtil.setSecurityCheck(this, new ClassSecurityCheck(MedewerkerkaartPage.class));
		}

		@Override
		protected void createContents()
		{
			GeneralFilteredSortableDataProvider<Medewerker, MedewerkerZoekFilter> medewerkerProvider =
				GeneralFilteredSortableDataProvider.of(medewerkerFilter,
					MedewerkerDataAccessHelper.class);
			final CustomDataPanel<Medewerker> medewerkerDatapanel =
				new EduArteDataPanel<Medewerker>("medewerkerDatapanel", medewerkerProvider,
					new MedewerkerTable());
			medewerkerDatapanel.setRowFactory(new CustomDataPanelPageLinkRowFactory<Medewerker>(
				MedewerkerkaartPage.class)
			{
				private static final long serialVersionUID = 1L;

				@Override
				public void onClick(Item<Medewerker> item)
				{
					Medewerker medewerker = item.getModelObject();
					pushSearchResultToNavigationLevel(medewerkerDatapanel, item.getIndex());
					setResponsePage(new MedewerkerkaartPage(medewerker));
				}
			});
			medewerkerDatapanel.setItemsPerPage(5);
			medewerkerDatapanel.setNavigatorVisible(false);
			add(medewerkerDatapanel);

			add(new TargetBasedSecurePageLink<Void>("medewerkerTotalenTonenButton", new IPageLink()
			{

				private static final long serialVersionUID = 1L;

				@Override
				public Page getPage()
				{
					return new MedewerkerZoekenPage(medewerkerFilter);
				}

				@Override
				public Class< ? extends Page> getPageIdentity()
				{
					return MedewerkerZoekenPage.class;
				}
			}));
		}

		@Override
		protected void onDetach()
		{
			super.onDetach();
			medewerkerFilter.detach();
		}
	}

	public SnelZoekenPage(final VerbintenisZoekFilter verbintenisFilter,
			final GroepZoekFilter groepFilter, final MedewerkerZoekFilter medewerkerFilter,
			long deelnemerCount, int groepCount, int medewerkerCount)
	{
		super(CoreMainMenuItem.Deelnemer);

		add(new VerbintenisPanel("verbintenis", deelnemerCount, verbintenisFilter));
		add(new GroepPanel("groep", groepCount, groepFilter));
		add(new MedewerkerPanel("medewerker", medewerkerCount, medewerkerFilter));

		createComponents();
	}

	@Override
	public AbstractMenuBar createMenu(String id)
	{
		return new DeelnemerCollectiefMenu(id,
			DeelnemerCollectiefMenuItem.DeelnemerUitgebreidZoeken);
	}

	@Override
	public boolean supportsBookmarks()
	{
		return false;
	}
}
