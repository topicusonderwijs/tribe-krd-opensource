/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.web.pages.onderwijs.onderwijscatalogus;

import java.util.Date;
import java.util.List;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.web.components.choice.EnumCombobox;
import nl.topicus.cobra.web.components.choice.JaNeeCombobox;
import nl.topicus.cobra.web.components.menu.AbstractMenuBar;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.cobra.web.components.panels.bottomrow.OpslaanButton;
import nl.topicus.cobra.web.components.text.DatumField;
import nl.topicus.eduarte.app.EduArteApp;
import nl.topicus.eduarte.app.EduArteModuleKey;
import nl.topicus.eduarte.app.security.actions.Instelling;
import nl.topicus.eduarte.app.security.actions.OrganisatieEenheid;
import nl.topicus.eduarte.app.security.actions.SearchImplementsActions;
import nl.topicus.eduarte.core.principals.onderwijs.OnderwijsproductenUitgebreidZoeken;
import nl.topicus.eduarte.entities.onderwijsproduct.OnderwijsproductStatus;
import nl.topicus.eduarte.web.components.choice.SoortOnderwijsproductCombobox;
import nl.topicus.eduarte.web.components.choice.TaxonomieCombobox;
import nl.topicus.eduarte.web.components.choice.TaxonomieElementTypeCombobox;
import nl.topicus.eduarte.web.components.menu.OnderwijsCollectiefMenu;
import nl.topicus.eduarte.web.components.menu.OnderwijsCollectiefMenuItem;
import nl.topicus.eduarte.web.components.menu.main.CoreMainMenuItem;
import nl.topicus.eduarte.web.pages.SecurePage;
import nl.topicus.eduarte.zoekfilters.OnderwijsproductZoekFilter;
import nl.topicus.eduarte.zoekfilters.OrganisatieEenheidLocatieAuthorizationContext;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.CompoundPropertyModel;

/**
 * @author loite
 */
@PageInfo(title = "Onderwijsproduct uitgebreid zoeken", menu = "Onderwijs > Onderwijsproducten > Uitgebreid zoeken")
@InPrincipal(OnderwijsproductenUitgebreidZoeken.class)
@SearchImplementsActions( {Instelling.class, OrganisatieEenheid.class})
public class OnderwijsproductUitgebreidZoekenPage extends SecurePage
{
	private static final long serialVersionUID = 1L;

	private final Form<OnderwijsproductZoekFilter> form;

	private final DatumField peildatumField;

	private static final OnderwijsproductZoekFilter getDefaultFilter()
	{
		OnderwijsproductZoekFilter filter = new OnderwijsproductZoekFilter();
		filter.addOrderByProperty("titel");

		return filter;
	}

	public OnderwijsproductUitgebreidZoekenPage()
	{
		this(getDefaultFilter());
	}

	public OnderwijsproductUitgebreidZoekenPage(OnderwijsproductZoekFilter filter)
	{
		super(CoreMainMenuItem.Onderwijs);
		filter.setAuthorizationContext(new OrganisatieEenheidLocatieAuthorizationContext(this));

		form =
			new Form<OnderwijsproductZoekFilter>("form",
				new CompoundPropertyModel<OnderwijsproductZoekFilter>(filter))
			{
				private static final long serialVersionUID = 1L;

				@Override
				protected void onSubmit()
				{
					setResponsePage(new OnderwijsproductUitgebreidZoekenResultatenPage(getFilter()));
				}
			};
		add(form);
		peildatumField = new DatumField("peildatum")
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void onUpdate(AjaxRequestTarget target, Date newValue)
			{
				super.onUpdate(target, newValue);
				if (newValue == null)
				{
					info("Let op: geen (geldige) datum ingevoerd. De peildatum wordt niet meegenomen bij de zoekopdracht.");
				}
				((SecurePage) getPage()).refreshFeedback(target);
			}

		};
		form.add(peildatumField);
		form.add(new JaNeeCombobox("gearchiveerd").setNullValid(true));

		form.add(new TextField<String>("code"));
		form.add(new TextField<String>("titel"));
		form.add(new TextField<String>("zoekterm"));
		form.add(new TextField<String>("taxonomiecode"));
		TaxonomieCombobox taxCombo = new TaxonomieCombobox("taxonomie");
		taxCombo.setNullValid(true);
		form.add(taxCombo);
		TaxonomieElementTypeCombobox typeCombo =
			new TaxonomieElementTypeCombobox("taxonomieElementType", taxCombo, false);
		typeCombo.setNullValid(true);
		form.add(typeCombo);
		taxCombo.connectListForAjaxRefresh(typeCombo);
		form
			.add(new EnumCombobox<OnderwijsproductStatus>("status", OnderwijsproductStatus.values()));
		form.add(new SoortOnderwijsproductCombobox("soortOnderwijsproduct"));
		form.add(new JaNeeCombobox("bijIntake").setNullValid(true));

		WebMarkupContainer wmc = new WebMarkupContainer("creditsContainer");
		wmc.add(new TextField<Integer>("credits", Integer.class));
		wmc.add(new TextField<Integer>("mincredits", Integer.class));
		wmc.add(new TextField<Integer>("maxcredits", Integer.class));
		form.add(wmc.setVisible(EduArteApp.get().isModuleActive(EduArteModuleKey.HOGER_ONDERWIJS)));

		setRenderBodyOnly(true);
		createComponents();
	}

	@Override
	public AbstractMenuBar createMenu(String id)
	{
		return new OnderwijsCollectiefMenu(id,
			OnderwijsCollectiefMenuItem.OnderwijsproductenUitgebreidZoeken);
	}

	private OnderwijsproductZoekFilter getFilter()
	{
		return form.getModelObject();
	}

	@Override
	public void getBookmarkConstructorArguments(List<Class< ? >> ctorArgTypes,
			List<Object> ctorArgValues)
	{
		super.getBookmarkConstructorArguments(ctorArgTypes, ctorArgValues);
		ctorArgTypes.add(OnderwijsproductZoekFilter.class);
		ctorArgValues.add(getFilter());
	}

	@Override
	protected void fillBottomRow(BottomRowPanel panel)
	{
		panel.addButton(new OpslaanButton(panel, form, "Zoeken"));
	}
}
