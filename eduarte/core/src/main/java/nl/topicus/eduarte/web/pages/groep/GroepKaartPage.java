/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.web.pages.groep;

import java.util.List;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.dataproviders.GeneralFilteredSortableDataProvider;
import nl.topicus.cobra.dataproviders.ListModelDataProvider;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.web.components.datapanel.CustomDataPanel;
import nl.topicus.cobra.web.components.datapanel.CustomDataPanelPageLinkRowFactory;
import nl.topicus.cobra.web.components.form.AutoFieldSet;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.cobra.web.components.shortcut.CobraKeyAction;
import nl.topicus.eduarte.core.principals.groep.GroepInzien;
import nl.topicus.eduarte.dao.helpers.GroepsdeelnameDataAccessHelper;
import nl.topicus.eduarte.entities.groep.Groep;
import nl.topicus.eduarte.entities.groep.GroepDocent;
import nl.topicus.eduarte.entities.groep.GroepMentor;
import nl.topicus.eduarte.entities.groep.Groepsdeelname;
import nl.topicus.eduarte.entities.vrijevelden.GroepVrijVeld;
import nl.topicus.eduarte.providers.GroepProvider;
import nl.topicus.eduarte.web.components.menu.GroepMenuItem;
import nl.topicus.eduarte.web.components.panels.EduArteDataPanel;
import nl.topicus.eduarte.web.components.panels.VrijVeldEntiteitPanel;
import nl.topicus.eduarte.web.components.panels.bottomrow.ModuleEditPageButton;
import nl.topicus.eduarte.web.components.panels.datapanel.table.GroepDocentTable;
import nl.topicus.eduarte.web.components.panels.datapanel.table.GroepMentorTable;
import nl.topicus.eduarte.web.components.panels.datapanel.table.GroepsdeelnameTable;
import nl.topicus.eduarte.web.components.panels.filter.GroepsdeelnameZoekFilterPanel;
import nl.topicus.eduarte.web.pages.deelnemer.deelnemerkaart.DeelnemerkaartPage;
import nl.topicus.eduarte.web.pages.medewerker.MedewerkerkaartPage;
import nl.topicus.eduarte.zoekfilters.GroepsdeelnameZoekFilter;
import nl.topicus.eduarte.zoekfilters.OrganisatieEenheidLocatieAuthorizationContext;

import org.apache.wicket.PageParameters;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.PropertyModel;

/**
 * @author hoeve
 */
@PageInfo(title = "Groepkaart", menu = "Groep > [groep]")
@InPrincipal(GroepInzien.class)
public class GroepKaartPage extends AbstractGroepPage
{
	private static final long serialVersionUID = 1L;

	public GroepKaartPage(PageParameters parameters)
	{
		this(AbstractGroepPage.getGroepFromPageParameters(parameters));
	}

	public GroepKaartPage(GroepProvider provider)
	{
		this(provider.getGroep());
	}

	public GroepKaartPage(Groep groep)
	{
		super(GroepMenuItem.Groepkaart, groep);

		AutoFieldSet<Groep> autoFieldSet =
			new AutoFieldSet<Groep>("algemeen", getContextGroepModel());
		autoFieldSet.setPropertyNames("code", "groepstype", "naam", "leerjaar",
			"organisatieEenheid", "locatie", "begindatum", "einddatum");

		add(autoFieldSet);

		VrijVeldEntiteitPanel<GroepVrijVeld, Groep> vrijVeldenPanel =
			new VrijVeldEntiteitPanel<GroepVrijVeld, Groep>("vrijvelden", getContextGroepModel());
		add(vrijVeldenPanel);
		vrijVeldenPanel.setDossierScherm(true);

		add(createFieldsetDocenten());
		add(createFieldsetMentoren());
		add(createFieldsetDeelnemers(groep));

		createComponents();
	}

	private WebMarkupContainer createFieldsetDocenten()
	{
		WebMarkupContainer datapanelContainer = new WebMarkupContainer("panelDocenten");
		final CustomDataPanel<GroepDocent> datapanel =
			new EduArteDataPanel<GroepDocent>("datapanelDocenten",
				new ListModelDataProvider<GroepDocent>(new PropertyModel<List<GroepDocent>>(
					getDefaultModel(), "groepDocenten")), new GroepDocentTable(false));
		datapanel.setRowFactory(new CustomDataPanelPageLinkRowFactory<GroepDocent>(
			MedewerkerkaartPage.class)
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick(Item<GroepDocent> item)
			{
				pushSearchResultToNavigationLevel(datapanel, item.getIndex());
				super.onClick(item);
			}
		});

		datapanelContainer.setOutputMarkupId(true);
		datapanelContainer.add(datapanel);

		return datapanelContainer;
	}

	private WebMarkupContainer createFieldsetMentoren()
	{
		WebMarkupContainer datapanelContainer = new WebMarkupContainer("panelMentoren");
		final CustomDataPanel<GroepMentor> datapanel =
			new EduArteDataPanel<GroepMentor>("datapanelMentoren",
				new ListModelDataProvider<GroepMentor>(new PropertyModel<List<GroepMentor>>(
					getDefaultModel(), "groepMentoren")), new GroepMentorTable(false));
		datapanel.setRowFactory(new CustomDataPanelPageLinkRowFactory<GroepMentor>(
			MedewerkerkaartPage.class)
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick(Item<GroepMentor> item)
			{
				pushSearchResultToNavigationLevel(datapanel, item.getIndex());
				super.onClick(item);
			}
		});

		datapanelContainer.setOutputMarkupId(true);
		datapanelContainer.add(datapanel);

		return datapanelContainer;
	}

	private WebMarkupContainer createFieldsetDeelnemers(Groep groep)
	{
		WebMarkupContainer datapanelContainer = new WebMarkupContainer("panelDeelnemers");
		GroepsdeelnameZoekFilter filter = new GroepsdeelnameZoekFilter(groep);
		filter.setAuthorizationContext(new OrganisatieEenheidLocatieAuthorizationContext(this));
		filter.addOrderByProperty("deelnemer.deelnemernummer");
		filter.addOrderByProperty("persoon.roepnaam");
		filter.addOrderByProperty("persoon.achternaam");
		IDataProvider<Groepsdeelname> provider =
			GeneralFilteredSortableDataProvider.of(filter, GroepsdeelnameDataAccessHelper.class);
		final CustomDataPanel<Groepsdeelname> datapanel =
			new EduArteDataPanel<Groepsdeelname>("datapanelDeelnemers", provider,
				new GroepsdeelnameTable());
		datapanel.setRowFactory(new CustomDataPanelPageLinkRowFactory<Groepsdeelname>(
			DeelnemerkaartPage.class)
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick(Item<Groepsdeelname> item)
			{
				pushSearchResultToNavigationLevel(datapanel, item.getIndex());
				super.onClick(item);
			}
		});
		datapanel.setItemsPerPage(35);
		datapanelContainer.setOutputMarkupId(true);
		datapanelContainer.add(datapanel);

		GroepsdeelnameZoekFilterPanel filterPanel =
			new GroepsdeelnameZoekFilterPanel("filter", filter, datapanel);
		datapanelContainer.add(filterPanel);

		return datapanelContainer;
	}

	@Override
	protected void fillBottomRow(BottomRowPanel panel)
	{
		panel.addButton(new ModuleEditPageButton<Groep>(panel, "Bewerken", CobraKeyAction.BEWERKEN,
			Groep.class, GroepMenuItem.Groepkaart, GroepKaartPage.this, getContextGroepModel()));
	}
}
