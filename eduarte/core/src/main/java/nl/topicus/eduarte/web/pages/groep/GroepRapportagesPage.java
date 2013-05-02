/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.web.pages.groep;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.dao.helpers.DatabaseSelection;
import nl.topicus.cobra.dao.hibernate.HibernateSelection;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.web.components.menu.AbstractMenuBar;
import nl.topicus.cobra.zoekfilters.DetachableZoekFilter;
import nl.topicus.eduarte.app.security.actions.Instelling;
import nl.topicus.eduarte.app.security.actions.OrganisatieEenheid;
import nl.topicus.eduarte.app.security.actions.SearchImplementsActions;
import nl.topicus.eduarte.core.principals.groep.GroepRapportages;
import nl.topicus.eduarte.dao.helpers.GroepDataAccessHelper;
import nl.topicus.eduarte.entities.groep.Groep;
import nl.topicus.eduarte.entities.rapportage.DocumentTemplate;
import nl.topicus.eduarte.entities.rapportage.DocumentTemplateCategorie;
import nl.topicus.eduarte.entities.rapportage.DocumentTemplateContext;
import nl.topicus.eduarte.web.components.link.IRapportageReturnPage;
import nl.topicus.eduarte.web.components.menu.GroepCollectiefMenu;
import nl.topicus.eduarte.web.components.menu.GroepCollectiefMenuItem;
import nl.topicus.eduarte.web.components.menu.main.CoreMainMenuItem;
import nl.topicus.eduarte.web.components.panels.sidebar.rapportage.RapportageSideBar;
import nl.topicus.eduarte.web.components.panels.templates.RapportagesPanel;
import nl.topicus.eduarte.web.pages.SecurePage;
import nl.topicus.eduarte.web.pages.shared.SelectieTarget;
import nl.topicus.eduarte.zoekfilters.GroepZoekFilter;

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
@PageInfo(title = "Groep rapportages", menu = "Groep > Rapportages")
@InPrincipal(GroepRapportages.class)
@SearchImplementsActions({Instelling.class, OrganisatieEenheid.class})
public class GroepRapportagesPage extends SecurePage implements
		IRapportageReturnPage<Groep, Groep, GroepZoekFilter>
{
	private static final long serialVersionUID = 1L;

	protected static Logger log = LoggerFactory.getLogger(GroepRapportagesPage.class);

	public GroepRapportagesPage()
	{
		super(CoreMainMenuItem.Groep);

		add(new RapportagesPanel<Groep, Groep, GroepZoekFilter>("panel",
			DocumentTemplateContext.Groep, DocumentTemplateCategorie.getValues()));

		createComponents();
	}

	@Override
	public AbstractMenuBar createMenu(String id)
	{
		return new GroepCollectiefMenu(id, GroepCollectiefMenuItem.Rapportages);
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
	public Class<GroepDataAccessHelper> getDataAccessHelperClass()
	{
		return GroepDataAccessHelper.class;
	}

	@Override
	public DetachableZoekFilter<Groep> getDefaultFilter()
	{
		return GroepZoekFilter.createDefaultFilter();
	}

	@Override
	public void setResponsePage(DatabaseSelection<Groep, Groep> selection,
			SelectieTarget<Groep, Groep> target)
	{
		this.setResponsePage(new RapportageGroepSelectiePage(GroepRapportagesPage.this,
			(GroepZoekFilter) getDefaultFilter(), selection, target));
	}

	@Override
	public DatabaseSelection<Groep, Groep> createSelection()
	{
		return new HibernateSelection<Groep>(Groep.class);
	}
}
