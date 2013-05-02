/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.web.pages.groep;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.dataproviders.GeneralFilteredSortableDataProvider;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.web.components.datapanel.CustomDataPanel;
import nl.topicus.cobra.web.components.datapanel.CustomDataPanelContentDescription;
import nl.topicus.cobra.web.components.datapanel.columns.AbstractCustomColumn;
import nl.topicus.cobra.web.components.datapanel.columns.BorderColumn;
import nl.topicus.cobra.web.components.datapanel.columns.CustomPropertyColumn;
import nl.topicus.eduarte.core.principals.groep.GroepDocumenten;
import nl.topicus.eduarte.dao.helpers.GroepsdeelnameDataAccessHelper;
import nl.topicus.eduarte.entities.groep.Groep;
import nl.topicus.eduarte.entities.groep.Groepsdeelname;
import nl.topicus.eduarte.providers.GroepProvider;
import nl.topicus.eduarte.web.components.menu.GroepMenuItem;
import nl.topicus.eduarte.web.components.panels.EduArteDataPanel;
import nl.topicus.eduarte.web.components.panels.filter.GroepsdeelnameZoekFilterPanel;
import nl.topicus.eduarte.zoekfilters.GroepsdeelnameZoekFilter;
import nl.topicus.eduarte.zoekfilters.OrganisatieEenheidLocatieAuthorizationContext;

import org.apache.wicket.Component;
import org.apache.wicket.PageParameters;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

@PageInfo(title = "Hokjeslijst", menu = "Groep > [groep] -> Dossiers")
@InPrincipal(GroepDocumenten.class)
public class GroepHokjeslijstPage extends AbstractGroepPage
{
	private static final long serialVersionUID = 1L;

	private static final class HokjeColumn extends AbstractCustomColumn<Groepsdeelname> implements
			BorderColumn<Groepsdeelname>
	{
		private static final long serialVersionUID = 1L;

		public HokjeColumn(int nummer)
		{
			super("Hokje " + nummer, "");
		}

		@Override
		public void populateItem(WebMarkupContainer cell, String componentId,
				WebMarkupContainer row, IModel<Groepsdeelname> rowModel, int span)
		{
			cell.add(new Label(componentId, " ").setEscapeModelStrings(false));
		}

		@Override
		public boolean hasBorder()
		{
			return true;
		}

	}

	/**
	 * Bookmarkable constructor.
	 * 
	 * @see AbstractGroepPage#getGroepFromPageParameters(PageParameters)
	 */
	public GroepHokjeslijstPage(PageParameters parameters)
	{
		this(AbstractGroepPage.getGroepFromPageParameters(parameters));
	}

	public GroepHokjeslijstPage(GroepProvider provider)
	{
		this(provider.getGroep());
	}

	/**
	 * Constructor
	 * 
	 * @param groep
	 */
	public GroepHokjeslijstPage(Groep groep)
	{
		super(GroepMenuItem.Dossieroverzicht, groep);

		GroepsdeelnameZoekFilter deelnameFilter = new GroepsdeelnameZoekFilter(groep);
		deelnameFilter.addOrderByProperty("deelnemer.deelnemernummer");
		deelnameFilter.addOrderByProperty("persoon.roepnaam");
		deelnameFilter.addOrderByProperty("persoon.achternaam");
		deelnameFilter.setAuthorizationContext(new OrganisatieEenheidLocatieAuthorizationContext(
			this));
		IDataProvider<Groepsdeelname> provider =
			GeneralFilteredSortableDataProvider.of(deelnameFilter,
				GroepsdeelnameDataAccessHelper.class);
		CustomDataPanelContentDescription<Groepsdeelname> table =
			new CustomDataPanelContentDescription<Groepsdeelname>("Dossierstukken");
		table.addColumn(new CustomPropertyColumn<Groepsdeelname>("Deelnemernummer", "Nummer",
			"deelnemer.deelnemernummer", "deelnemer.deelnemernummer"));
		table.addColumn(new CustomPropertyColumn<Groepsdeelname>("Naam", "Naam",
			"persoon.achternaam", "deelnemer.persoon.volledigeNaam"));
		for (int nummer = 1; nummer <= 80; nummer++)
		{
			table.addColumn(new HokjeColumn(nummer));
		}

		final CustomDataPanel<Groepsdeelname> datapanel =
			new EduArteDataPanel<Groepsdeelname>("datapanel", provider, table);
		datapanel.setItemsPerPage(35);
		add(datapanel);

		GroepsdeelnameZoekFilterPanel filterPanel =
			new GroepsdeelnameZoekFilterPanel("filter", deelnameFilter, datapanel);
		add(filterPanel);

		createComponents();
	}

	@Override
	public Component createTitle(String id)
	{
		return new Label(id, new PropertyModel<String>(getDefaultModel(), "naam"));
	}
}
