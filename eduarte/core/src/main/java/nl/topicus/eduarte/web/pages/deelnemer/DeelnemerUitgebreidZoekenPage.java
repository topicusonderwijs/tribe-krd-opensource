/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.web.pages.deelnemer;

import java.util.List;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.web.components.menu.AbstractMenuBar;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.cobra.web.components.panels.bottomrow.ButtonAlignment;
import nl.topicus.cobra.web.components.panels.bottomrow.OpslaanButton;
import nl.topicus.cobra.web.components.shortcut.CobraKeyAction;
import nl.topicus.eduarte.app.security.actions.Instelling;
import nl.topicus.eduarte.app.security.actions.OrganisatieEenheid;
import nl.topicus.eduarte.app.security.actions.SearchImplementsActions;
import nl.topicus.eduarte.core.principals.deelnemer.DeelnemersZoekenUitgebreid;
import nl.topicus.eduarte.entities.rapportage.DeelnemerZoekOpdracht;
import nl.topicus.eduarte.web.components.menu.DeelnemerCollectiefMenu;
import nl.topicus.eduarte.web.components.menu.DeelnemerCollectiefMenuItem;
import nl.topicus.eduarte.web.components.menu.main.CoreMainMenuItem;
import nl.topicus.eduarte.web.components.panels.filter.uitgebreid.DeelnemerUitgebreidZoekFilterPanel;
import nl.topicus.eduarte.web.pages.SecurePage;
import nl.topicus.eduarte.web.pages.deelnemer.zoekopdrachten.DeelnemerZoekOpdrachtEditPage;
import nl.topicus.eduarte.web.pages.shared.AbstractDeelnemerSelectiePage;
import nl.topicus.eduarte.zoekfilters.VerbintenisZoekFilter;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.SubmitLink;

/**
 * @author loite
 */
@PageInfo(title = "Deelnemer uitgebreid zoeken", menu = "Deelnemer > Uitgebreid zoeken")
@InPrincipal(DeelnemersZoekenUitgebreid.class)
@SearchImplementsActions( {Instelling.class, OrganisatieEenheid.class})
public class DeelnemerUitgebreidZoekenPage extends SecurePage
{
	private static final long serialVersionUID = 1L;

	private AbstractDeelnemerSelectiePage< ? > selectieResponsePage;

	public static final VerbintenisZoekFilter getDefaultFilter()
	{
		VerbintenisZoekFilter filter = new VerbintenisZoekFilter();
		filter.addOrderByProperty("deelnemer.deelnemernummer");
		filter.addOrderByProperty("persoon.roepnaam");
		filter.addOrderByProperty("persoon.achternaam");
		return filter;
	}

	private Form<Void> form;

	protected DeelnemerUitgebreidZoekFilterPanel filterPanel;

	public DeelnemerUitgebreidZoekenPage()
	{
		this(getDefaultFilter());
	}

	public DeelnemerUitgebreidZoekenPage(VerbintenisZoekFilter filter)
	{
		this(filter, null, false);
	}

	public DeelnemerUitgebreidZoekenPage(VerbintenisZoekFilter filter, boolean openPanels)
	{
		this(filter, null, openPanels);
	}

	public DeelnemerUitgebreidZoekenPage(VerbintenisZoekFilter filter,
			AbstractDeelnemerSelectiePage< ? > selectieResponsePage, boolean openPanels)
	{
		super(CoreMainMenuItem.Deelnemer);
		add(form = new Form<Void>("form"));
		this.selectieResponsePage = selectieResponsePage;
		filterPanel = new DeelnemerUitgebreidZoekFilterPanel("filter", filter, openPanels);
		form.add(filterPanel);
		createComponents();
	}

	@Override
	public AbstractMenuBar createMenu(String id)
	{
		return new DeelnemerCollectiefMenu(id, DeelnemerCollectiefMenuItem.DeelnemerZoeken);
	}

	@Override
	public void getBookmarkConstructorArguments(List<Class< ? >> ctorArgTypes,
			List<Object> ctorArgValues)
	{
		super.getBookmarkConstructorArguments(ctorArgTypes, ctorArgValues);
		ctorArgTypes.add(VerbintenisZoekFilter.class);
		ctorArgValues.add(filterPanel.getFilter());
	}

	@Override
	protected void fillBottomRow(BottomRowPanel panel)
	{
		panel.addButton(new OpslaanButton(panel, form, "Zoeken")
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void onSubmit()
			{
				setResultatenResponsePage();
			}

			@Override
			protected WebMarkupContainer getLink(String linkId)
			{
				SubmitLink ret = (SubmitLink) super.getLink(linkId);
				form.setDefaultButton(ret);
				return ret;
			}
		});
		panel.addButton(new OpslaanButton(panel, form, "Zoekopdracht opslaan")
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void onSubmit()
			{
				DeelnemerZoekOpdracht nieuweOpdracht = new DeelnemerZoekOpdracht();
				nieuweOpdracht.setPersoonlijk(true);
				setResponsePage(new DeelnemerZoekOpdrachtEditPage(
					DeelnemerUitgebreidZoekenPage.this, nieuweOpdracht, filterPanel.getFilter()));
			}
		}.setAction(CobraKeyAction.TOEVOEGEN).setAlignment(ButtonAlignment.LEFT));
	}

	public DeelnemerUitgebreidZoekFilterPanel getFilterPanel()
	{
		return filterPanel;
	}

	protected void setResultatenResponsePage()
	{
		if (selectieResponsePage == null)
			setResponsePage(new DeelnemerUitgebreidZoekenResultatenPage(filterPanel.getFilter()));
		else
			setResponsePage(selectieResponsePage);
	}
}
