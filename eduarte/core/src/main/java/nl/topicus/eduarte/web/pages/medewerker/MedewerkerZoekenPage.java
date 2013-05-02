package nl.topicus.eduarte.web.pages.medewerker;

import java.util.List;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.dataproviders.GeneralFilteredSortableDataProvider;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.web.components.datapanel.CustomDataPanel;
import nl.topicus.cobra.web.components.datapanel.CustomDataPanelPageLinkRowFactory;
import nl.topicus.cobra.web.components.menu.AbstractMenuBar;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.cobra.web.components.panels.bottomrow.ButtonAlignment;
import nl.topicus.cobra.web.components.panels.bottomrow.PageLinkButton;
import nl.topicus.cobra.web.components.shortcut.CobraKeyAction;
import nl.topicus.eduarte.core.principals.medewerker.MedewerkersZoeken;
import nl.topicus.eduarte.dao.helpers.MedewerkerDataAccessHelper;
import nl.topicus.eduarte.entities.personen.Medewerker;
import nl.topicus.eduarte.web.components.menu.MedewerkerCollectiefMenu;
import nl.topicus.eduarte.web.components.menu.MedewerkerCollectiefMenuItem;
import nl.topicus.eduarte.web.components.menu.main.CoreMainMenuItem;
import nl.topicus.eduarte.web.components.panels.EduArteDataPanel;
import nl.topicus.eduarte.web.components.panels.bottomrow.ModuleEditPageButton;
import nl.topicus.eduarte.web.components.panels.datapanel.table.MedewerkerTable;
import nl.topicus.eduarte.web.components.panels.filter.MedewerkerZoekFilterPanel;
import nl.topicus.eduarte.web.pages.SecurePage;
import nl.topicus.eduarte.zoekfilters.MedewerkerZoekFilter;
import nl.topicus.eduarte.zoekfilters.OrganisatieEenheidLocatieAuthorizationContext;

import org.apache.wicket.Page;
import org.apache.wicket.markup.html.link.IPageLink;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.Model;

/**
 * Zoekenpagina voor medewerkers.
 * 
 * @author loite
 */
@PageInfo(title = "Medewerker zoeken", menu = "Medewerker")
@InPrincipal(MedewerkersZoeken.class)
public class MedewerkerZoekenPage extends SecurePage
{
	private static final long serialVersionUID = 1L;

	private final MedewerkerZoekFilterPanel filterPanel;

	private static final MedewerkerZoekFilter getDefaultFilter()
	{
		MedewerkerZoekFilter filter = new MedewerkerZoekFilter();
		filter.addOrderByProperty("persoon.achternaam");

		return filter;
	}

	public MedewerkerZoekenPage()
	{
		this(getDefaultFilter());
	}

	public MedewerkerZoekenPage(MedewerkerZoekFilter filter)
	{
		super(CoreMainMenuItem.Medewerker);
		filter.setAuthorizationContext(new OrganisatieEenheidLocatieAuthorizationContext(this));
		IDataProvider<Medewerker> provider =
			GeneralFilteredSortableDataProvider.of(filter, MedewerkerDataAccessHelper.class);
		final CustomDataPanel<Medewerker> datapanel =
			new EduArteDataPanel<Medewerker>("datapanel", provider, new MedewerkerTable());
		datapanel.setRowFactory(new CustomDataPanelPageLinkRowFactory<Medewerker>(
			MedewerkerkaartPage.class)
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick(Item<Medewerker> item)
			{
				Medewerker medewerker = item.getModelObject();
				pushSearchResultToNavigationLevel(datapanel, item.getIndex());
				setResponsePage(new MedewerkerkaartPage(medewerker));
			}
		});
		datapanel.setItemsPerPage(20);
		add(datapanel);
		filterPanel = new MedewerkerZoekFilterPanel("filter", filter, datapanel, true);
		add(filterPanel);
		createComponents();
	}

	@Override
	public AbstractMenuBar createMenu(String id)
	{
		return new MedewerkerCollectiefMenu(id, MedewerkerCollectiefMenuItem.Zoeken);
	}

	@Override
	public void getBookmarkConstructorArguments(List<Class< ? >> ctorArgTypes,
			List<Object> ctorArgValues)
	{
		super.getBookmarkConstructorArguments(ctorArgTypes, ctorArgValues);
		ctorArgTypes.add(MedewerkerZoekFilter.class);
		ctorArgValues.add(filterPanel.getZoekfilter());
	}

	@Override
	protected void fillBottomRow(BottomRowPanel panel)
	{
		panel.addButton(new PageLinkButton(panel, "Medewerkers importeren", ButtonAlignment.RIGHT,
			new IPageLink()
			{
				private static final long serialVersionUID = 1L;

				@Override
				public Page getPage()
				{
					return new MedewerkersImporterenPage();
				}

				@Override
				public Class<MedewerkersImporterenPage> getPageIdentity()
				{
					return MedewerkersImporterenPage.class;
				}
			}));

		panel.addButton(new ModuleEditPageButton<Medewerker>(panel, "Nieuwe medewerker",
			CobraKeyAction.TOEVOEGEN, Medewerker.class, MedewerkerCollectiefMenuItem.Zoeken,
			MedewerkerZoekenPage.this, new Model<Medewerker>()));
	}
}