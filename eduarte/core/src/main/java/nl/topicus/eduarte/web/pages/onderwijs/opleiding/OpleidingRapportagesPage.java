/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.web.pages.onderwijs.opleiding;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.dao.helpers.DatabaseSelection;
import nl.topicus.cobra.dao.hibernate.HibernateSelection;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.web.components.menu.AbstractMenuBar;
import nl.topicus.cobra.zoekfilters.DetachableZoekFilter;
import nl.topicus.eduarte.app.security.actions.Instelling;
import nl.topicus.eduarte.app.security.actions.OrganisatieEenheid;
import nl.topicus.eduarte.app.security.actions.SearchImplementsActions;
import nl.topicus.eduarte.core.principals.onderwijs.OpleidingRapportages;
import nl.topicus.eduarte.dao.helpers.OpleidingDataAccessHelper;
import nl.topicus.eduarte.entities.opleiding.Opleiding;
import nl.topicus.eduarte.entities.rapportage.DocumentTemplateCategorie;
import nl.topicus.eduarte.entities.rapportage.DocumentTemplateContext;
import nl.topicus.eduarte.web.components.link.IRapportageReturnPage;
import nl.topicus.eduarte.web.components.menu.OnderwijsCollectiefMenu;
import nl.topicus.eduarte.web.components.menu.OnderwijsCollectiefMenuItem;
import nl.topicus.eduarte.web.components.menu.main.CoreMainMenuItem;
import nl.topicus.eduarte.web.components.panels.sidebar.rapportage.RapportageSideBar;
import nl.topicus.eduarte.web.components.panels.templates.RapportagesPanel;
import nl.topicus.eduarte.web.pages.SecurePage;
import nl.topicus.eduarte.web.pages.shared.SelectieTarget;
import nl.topicus.eduarte.zoekfilters.OpleidingZoekFilter;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxSelfUpdatingTimerBehavior;
import org.apache.wicket.util.time.Duration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Pagina waarop de rapportages voor opleidingen/onderwijsproducten getoond worden
 * 
 * @author vandekamp
 */
@PageInfo(title = "Opleiding rapportages", menu = "Onderwijs > Rapportage -> Opleiding rapportages")
@InPrincipal(OpleidingRapportages.class)
@SearchImplementsActions({Instelling.class, OrganisatieEenheid.class})
public class OpleidingRapportagesPage extends SecurePage implements
		IRapportageReturnPage<Opleiding, Opleiding, OpleidingZoekFilter>
{
	private static final long serialVersionUID = 1L;

	protected static Logger log = LoggerFactory.getLogger(OpleidingRapportagesPage.class);

	public OpleidingRapportagesPage()
	{
		super(CoreMainMenuItem.Onderwijs);

		add(new RapportagesPanel<Opleiding, Opleiding, OpleidingZoekFilter>("panel",
			DocumentTemplateContext.Opleiding, DocumentTemplateCategorie.Identiteit));

		createComponents();
	}

	@Override
	public AbstractMenuBar createMenu(String id)
	{
		return new OnderwijsCollectiefMenu(id, OnderwijsCollectiefMenuItem.OpleidingRapportages);
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
	public Class<OpleidingDataAccessHelper> getDataAccessHelperClass()
	{
		return OpleidingDataAccessHelper.class;
	}

	@Override
	public DetachableZoekFilter<Opleiding> getDefaultFilter()
	{
		return new OpleidingZoekFilter();
	}

	@Override
	public DatabaseSelection<Opleiding, Opleiding> createSelection()
	{
		return new HibernateSelection<Opleiding>(Opleiding.class);
	}

	@Override
	public void setResponsePage(DatabaseSelection<Opleiding, Opleiding> selection,
			SelectieTarget<Opleiding, Opleiding> target)
	{
		setResponsePage(new RapportageOpleidingSelectiePage(OpleidingRapportagesPage.this,
			new OpleidingZoekFilter(), selection, target));
	}
}
