/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.web.pages.deelnemer;

import java.util.List;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.dataproviders.GeneralFilteredSortableDataProvider;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.util.ComponentUtil;
import nl.topicus.cobra.web.components.datapanel.CustomDataPanelId;
import nl.topicus.cobra.web.components.datapanel.CustomDataPanelPageLinkRowFactory;
import nl.topicus.cobra.web.components.datapanel.DefaultCustomDataPanelId;
import nl.topicus.cobra.web.components.menu.AbstractMenuBar;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.cobra.web.components.panels.bottomrow.TerugButton;
import nl.topicus.eduarte.app.security.actions.Instelling;
import nl.topicus.eduarte.app.security.actions.OrganisatieEenheid;
import nl.topicus.eduarte.app.security.actions.SearchImplementsActions;
import nl.topicus.eduarte.core.principals.deelnemer.DeelnemersZoeken;
import nl.topicus.eduarte.dao.helpers.VerbintenisDataAccessHelper;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis;
import nl.topicus.eduarte.entities.rapportage.DeelnemerZoekOpdracht;
import nl.topicus.eduarte.web.components.menu.DeelnemerCollectiefMenu;
import nl.topicus.eduarte.web.components.menu.DeelnemerCollectiefMenuItem;
import nl.topicus.eduarte.web.components.menu.main.CoreMainMenuItem;
import nl.topicus.eduarte.web.components.panels.DeelnemerDataPanel;
import nl.topicus.eduarte.web.components.panels.datapanel.table.DeelnemerTable;
import nl.topicus.eduarte.web.components.panels.filter.DeelnemerPeildatumZoekFilterPanel;
import nl.topicus.eduarte.web.pages.SecurePage;
import nl.topicus.eduarte.web.pages.deelnemer.deelnemerkaart.DeelnemerkaartPage;
import nl.topicus.eduarte.zoekfilters.OrganisatieEenheidLocatieAuthorizationContext;
import nl.topicus.eduarte.zoekfilters.VerbintenisZoekFilter;

import org.apache.wicket.Page;
import org.apache.wicket.markup.html.link.IPageLink;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;

/**
 * @author loite
 */
@PageInfo(title = "Deelnemer uitgebreid zoeken resultaten", menu = "Deelnemer > Uitgebreid zoeken")
@InPrincipal(DeelnemersZoeken.class)
@SearchImplementsActions({Instelling.class, OrganisatieEenheid.class})
public class DeelnemerUitgebreidZoekenResultatenPage extends SecurePage
{
	private static final long serialVersionUID = 1L;

	private IModel<DeelnemerZoekOpdracht> zoekOpdracht;

	private final VerbintenisZoekFilter filter;

	private DeelnemerDataPanel datapanel;

	public DeelnemerUitgebreidZoekenResultatenPage(DeelnemerZoekOpdracht zoekOpdracht)
	{
		this(zoekOpdracht.deserializeFilter(), zoekOpdracht.isKolommenVastzetten() ? zoekOpdracht
			.createDataPanelId() : new DefaultCustomDataPanelId(DeelnemerZoekenPage.class,
			"datapanel"), zoekOpdracht.isKolommenVastzetten(), zoekOpdracht.isPeildatumVastzetten());
	}

	public DeelnemerUitgebreidZoekenResultatenPage(VerbintenisZoekFilter filter)
	{
		this(filter, new DefaultCustomDataPanelId(DeelnemerZoekenPage.class, "datapanel"), false,
			false);
	}

	private DeelnemerUitgebreidZoekenResultatenPage(VerbintenisZoekFilter filter,
			CustomDataPanelId panelId, boolean kolommenVastzetten, boolean peildatumVastzetten)
	{
		super(CoreMainMenuItem.Deelnemer);
		filter.setAuthorizationContext(new OrganisatieEenheidLocatieAuthorizationContext(this));
		this.filter = filter;

		if (filter.getPeildatum() == null)
		{
			error("Let op: geen (geldige) datum ingevoerd. De peildatum is niet meegenomen bij de zoekopdracht.");
		}

		GeneralFilteredSortableDataProvider<Verbintenis, VerbintenisZoekFilter> provider =
			GeneralFilteredSortableDataProvider.of(filter, VerbintenisDataAccessHelper.class);

		datapanel = new DeelnemerDataPanel("datapanel", panelId, provider, new DeelnemerTable());
		datapanel.setGroeperenButtonVisible(!kolommenVastzetten);
		datapanel.setSelecteerKolommenButtonVisible(!kolommenVastzetten);
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
		add(new DeelnemerPeildatumZoekFilterPanel("filter", filter, datapanel, peildatumVastzetten));
		createComponents();
	}

	public VerbintenisZoekFilter getFilter()
	{
		return filter;
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
				return new DeelnemerUitgebreidZoekenPage(
					DeelnemerUitgebreidZoekenResultatenPage.this.filter, true);
			}

			@Override
			public Class< ? extends SecurePage> getPageIdentity()
			{
				return DeelnemerUitgebreidZoekenPage.class;
			}

		});
		terug.setLabel("Zoekopdracht aanpassen");
		panel.addButton(terug);
	}

	@Override
	public AbstractMenuBar createMenu(String id)
	{
		return new DeelnemerCollectiefMenu(id,
			DeelnemerCollectiefMenuItem.DeelnemerUitgebreidZoeken);
	}

	@Override
	public void detachModels()
	{
		super.detachModels();
		filter.detach();
		ComponentUtil.detachQuietly(zoekOpdracht);
	}

	@Override
	public void getBookmarkConstructorArguments(List<Class< ? >> ctorArgTypes,
			List<Object> ctorArgValues)
	{
		super.getBookmarkConstructorArguments(ctorArgTypes, ctorArgValues);
		ctorArgTypes.add(VerbintenisZoekFilter.class);
		ctorArgValues.add(filter);
	}
}
