/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.web.pages.groep;

import java.util.List;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.dataproviders.GeneralFilteredSortableDataProvider;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.util.TimeUtil;
import nl.topicus.cobra.web.components.datapanel.CustomDataPanel;
import nl.topicus.cobra.web.components.datapanel.CustomDataPanelPageLinkRowFactory;
import nl.topicus.cobra.web.components.menu.AbstractMenuBar;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.cobra.web.components.shortcut.CobraKeyAction;
import nl.topicus.cobra.web.security.DisableSecurityCheckMarker;
import nl.topicus.eduarte.app.security.actions.Instelling;
import nl.topicus.eduarte.app.security.actions.OrganisatieEenheid;
import nl.topicus.eduarte.app.security.actions.SearchImplementsActions;
import nl.topicus.eduarte.app.security.checks.GroepSecurityCheck;
import nl.topicus.eduarte.core.principals.groep.GroepenZoeken;
import nl.topicus.eduarte.dao.helpers.GroepDataAccessHelper;
import nl.topicus.eduarte.entities.groep.Groep;
import nl.topicus.eduarte.web.components.menu.GroepCollectiefMenu;
import nl.topicus.eduarte.web.components.menu.GroepCollectiefMenuItem;
import nl.topicus.eduarte.web.components.menu.main.CoreMainMenuItem;
import nl.topicus.eduarte.web.components.panels.EduArteDataPanel;
import nl.topicus.eduarte.web.components.panels.bottomrow.ModuleEditPageButton;
import nl.topicus.eduarte.web.components.panels.datapanel.table.GroepTable;
import nl.topicus.eduarte.web.components.panels.filter.GroepZoekFilterPanel;
import nl.topicus.eduarte.web.pages.SecurePage;
import nl.topicus.eduarte.zoekfilters.GroepZoekFilter;
import nl.topicus.eduarte.zoekfilters.OrganisatieEenheidLocatieAuthorizationContext;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.security.actions.Enable;
import org.apache.wicket.security.swarm.checks.DataSecurityCheck;

/**
 * @author hoeve
 */
@PageInfo(title = "Groep zoeken", menu = "Groep")
@InPrincipal(GroepenZoeken.class)
@SearchImplementsActions( {Instelling.class, OrganisatieEenheid.class})
public class GroepZoekenPage extends SecurePage
{
	private static final long serialVersionUID = 1L;

	private final GroepZoekFilterPanel filterPanel;

	public static final GroepZoekFilter getDefaultFilter()
	{
		GroepZoekFilter filter = new GroepZoekFilter();
		filter.addOrderByProperty("einddatum");
		filter.addOrderByProperty("begindatum");
		filter.addOrderByProperty("naam");
		filter.addOrderByProperty("code");

		return filter;
	}

	public GroepZoekenPage()
	{
		this(getDefaultFilter());
	}

	public GroepZoekenPage(GroepZoekFilter filter)
	{
		super(CoreMainMenuItem.Groep);
		filter.setAuthorizationContext(new OrganisatieEenheidLocatieAuthorizationContext(this));

		if (filter.isMentorOrDocentRequired() && filter.getMentorOrDocent() == null)
		{
			geenMentorOrDocentError();
			createComponents();
			filterPanel = null;
			return;
		}

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
				pushSearchResultToNavigationLevel(datapanel, item.getIndex());
				super.onClick(item);
			}
		});
		datapanel.setItemsPerPage(20);
		add(datapanel);
		filterPanel = new GroepZoekFilterPanel("filter", filter, datapanel);
		add(filterPanel);
		createComponents();
	}

	private void geenMentorOrDocentError()
	{
		this.error("Geen medewerker aan account gekoppeld, Mijn groepen pagina niet beschikbaar");

		add(new WebMarkupContainer("datapanel").setVisible(false));
		add(new WebMarkupContainer("filter").setVisible(false));
	}

	@Override
	public AbstractMenuBar createMenu(String id)
	{
		if (getCurrentZoekFilter() != null && getCurrentZoekFilter().getMentorOrDocent() != null)
			return new GroepCollectiefMenu(id, GroepCollectiefMenuItem.MijnGroepen);

		return new GroepCollectiefMenu(id, GroepCollectiefMenuItem.GroepZoeken);
	}

	@Override
	public Label createTitle(String id)
	{
		if (getCurrentZoekFilter() != null && getCurrentZoekFilter().getMentorOrDocent() != null)
			return new Label(id, "Mijn Groepen");

		return new Label(id, "Groep zoeken");

	}

	@Override
	public void getBookmarkConstructorArguments(List<Class< ? >> ctorArgTypes,
			List<Object> ctorArgValues)
	{
		super.getBookmarkConstructorArguments(ctorArgTypes, ctorArgValues);
		ctorArgTypes.add(GroepZoekFilter.class);
		ctorArgValues.add(getCurrentZoekFilter());
	}

	@Override
	protected void fillBottomRow(BottomRowPanel panel)
	{
		if (new DataSecurityCheck(Groep.GROEP_WRITE).isActionAuthorized(Enable.class))
		{
			ModuleEditPageButton<Groep> toevoegenButton =
				new ModuleEditPageButton<Groep>(panel, "Nieuwe groep", CobraKeyAction.TOEVOEGEN,
					Groep.class, GroepCollectiefMenuItem.GroepZoeken, GroepZoekenPage.this)
				{
					private static final long serialVersionUID = 1L;

					@Override
					protected Groep getEntity()
					{
						Groep groep = new Groep();
						groep.setBegindatum(TimeUtil.getInstance().currentDate());
						return groep;
					}
				};
			panel.addButton(toevoegenButton);
			DisableSecurityCheckMarker.place(toevoegenButton, GroepSecurityCheck.class);
		}
	}

	public GroepZoekFilter getCurrentZoekFilter()
	{
		return filterPanel.getZoekfilter();
	}
}
