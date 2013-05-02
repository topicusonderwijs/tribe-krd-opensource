/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.web.pages.deelnemer;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.dao.helpers.DatabaseSelection;
import nl.topicus.cobra.dao.hibernate.HibernateSelection;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.web.components.menu.AbstractMenuBar;
import nl.topicus.cobra.zoekfilters.DetachableZoekFilter;
import nl.topicus.eduarte.app.security.actions.Instelling;
import nl.topicus.eduarte.app.security.actions.OrganisatieEenheid;
import nl.topicus.eduarte.app.security.actions.SearchImplementsActions;
import nl.topicus.eduarte.core.principals.deelnemer.DeelnemerRapportages;
import nl.topicus.eduarte.dao.helpers.VerbintenisDataAccessHelper;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis;
import nl.topicus.eduarte.entities.rapportage.DocumentTemplate;
import nl.topicus.eduarte.entities.rapportage.DocumentTemplateCategorie;
import nl.topicus.eduarte.entities.rapportage.DocumentTemplateContext;
import nl.topicus.eduarte.web.components.link.IRapportageReturnPage;
import nl.topicus.eduarte.web.components.menu.DeelnemerCollectiefMenu;
import nl.topicus.eduarte.web.components.menu.DeelnemerCollectiefMenuItem;
import nl.topicus.eduarte.web.components.menu.main.CoreMainMenuItem;
import nl.topicus.eduarte.web.components.panels.sidebar.rapportage.RapportageSideBar;
import nl.topicus.eduarte.web.components.panels.templates.RapportagesPanel;
import nl.topicus.eduarte.web.pages.SecurePage;
import nl.topicus.eduarte.web.pages.shared.SelectieTarget;
import nl.topicus.eduarte.zoekfilters.VerbintenisZoekFilter;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxSelfUpdatingTimerBehavior;
import org.apache.wicket.util.time.Duration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Pagina waar we datapanels van {@link DocumentTemplate}s tonen met links om een
 * rapportage te genereren.
 * 
 * @author hoeve
 */
@PageInfo(title = "Deelnemer rapportages", menu = "Deelnemer > Rapportages")
@InPrincipal(DeelnemerRapportages.class)
@SearchImplementsActions({Instelling.class, OrganisatieEenheid.class})
public class DeelnemerRapportagesPage extends SecurePage implements
		IRapportageReturnPage<Verbintenis, Verbintenis, VerbintenisZoekFilter>
{
	private static final long serialVersionUID = 1L;

	protected static Logger log = LoggerFactory.getLogger(DeelnemerRapportagesPage.class);

	/**
	 * Constructor
	 */
	public DeelnemerRapportagesPage()
	{
		super(CoreMainMenuItem.Deelnemer);
		add(new RapportagesPanel<Verbintenis, Verbintenis, VerbintenisZoekFilter>("panel",
			DocumentTemplateContext.Verbintenis, DocumentTemplateCategorie.getValues()));
		createComponents();
	}

	/**
	 * @see nl.topicus.eduarte.web.pages.SecurePage#createMenu(java.lang.String)
	 */
	@Override
	public AbstractMenuBar createMenu(String id)
	{
		return new DeelnemerCollectiefMenu(id, DeelnemerCollectiefMenuItem.Rapportages);
	}

	@Override
	public void rapportageInvoked()
	{
		info("De taak is op de achtergrond gestart. (Het kan enkele seconden duren voordat de voortgang in de overzichten zichtbaar is)");
		Component sideBar = this.get(ID_LAYSIDE + ":" + ID_RAPPORTAGESIDEBAR);
		if (sideBar != null && sideBar instanceof RapportageSideBar)
		{
			((RapportageSideBar) sideBar)
				.add(new AjaxSelfUpdatingTimerBehavior(Duration.seconds(5)));
		}
	}

	@Override
	public Class<VerbintenisDataAccessHelper> getDataAccessHelperClass()
	{
		return VerbintenisDataAccessHelper.class;
	}

	@Override
	public DetachableZoekFilter<Verbintenis> getDefaultFilter()
	{
		return VerbintenisZoekFilter.getDefaultFilter();
	}

	@Override
	public DatabaseSelection<Verbintenis, Verbintenis> createSelection()
	{
		return new HibernateSelection<Verbintenis>(Verbintenis.class);
	}

	@Override
	public void setResponsePage(DatabaseSelection<Verbintenis, Verbintenis> selection,
			SelectieTarget<Verbintenis, Verbintenis> target)
	{
		setResponsePage(new RapportageVerbintenisSelectiePage(DeelnemerRapportagesPage.this,
			(VerbintenisZoekFilter) getDefaultFilter(), selection, target));
	}
}
