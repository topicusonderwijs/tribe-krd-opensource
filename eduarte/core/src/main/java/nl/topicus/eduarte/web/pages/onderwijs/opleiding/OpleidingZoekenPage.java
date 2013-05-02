package nl.topicus.eduarte.web.pages.onderwijs.opleiding;

import java.util.List;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.dataproviders.GeneralFilteredSortableDataProvider;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.web.components.datapanel.CustomDataPanel;
import nl.topicus.cobra.web.components.datapanel.CustomDataPanelPageLinkRowFactory;
import nl.topicus.cobra.web.components.datapanel.TitleModel;
import nl.topicus.cobra.web.components.menu.AbstractMenuBar;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.cobra.web.components.shortcut.CobraKeyAction;
import nl.topicus.cobra.web.security.DisableSecurityCheckMarker;
import nl.topicus.eduarte.app.security.actions.Instelling;
import nl.topicus.eduarte.app.security.actions.OrganisatieEenheid;
import nl.topicus.eduarte.app.security.actions.SearchImplementsActions;
import nl.topicus.eduarte.app.security.checks.OrganisatieEenheidLocatieKoppelbaarSecurityCheck;
import nl.topicus.eduarte.core.principals.onderwijs.OpleidingenZoeken;
import nl.topicus.eduarte.dao.helpers.OpleidingAanbodDataAccessHelper;
import nl.topicus.eduarte.entities.opleiding.Opleiding;
import nl.topicus.eduarte.entities.opleiding.OpleidingAanbod;
import nl.topicus.eduarte.web.components.datapanel.ActiefRowFactoryDecorator;
import nl.topicus.eduarte.web.components.menu.OnderwijsCollectiefMenu;
import nl.topicus.eduarte.web.components.menu.OnderwijsCollectiefMenuItem;
import nl.topicus.eduarte.web.components.menu.main.CoreMainMenuItem;
import nl.topicus.eduarte.web.components.panels.EduArteDataPanel;
import nl.topicus.eduarte.web.components.panels.bottomrow.ModuleEditPageButton;
import nl.topicus.eduarte.web.components.panels.datapanel.table.OpleidingAanbodTable;
import nl.topicus.eduarte.web.components.panels.filter.OpleidingAanbodZoekFilterPanel;
import nl.topicus.eduarte.web.pages.SecurePage;
import nl.topicus.eduarte.zoekfilters.OpleidingAanbodZoekFilter;

import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;

/**
 * @author loite
 */
@PageInfo(title = "Opleiding zoeken", menu = "Onderwijs")
@InPrincipal(OpleidingenZoeken.class)
@SearchImplementsActions( {Instelling.class, OrganisatieEenheid.class})
public class OpleidingZoekenPage extends SecurePage
{
	private static final long serialVersionUID = 1L;

	private final OpleidingAanbodZoekFilterPanel filterPanel;

	private static final OpleidingAanbodZoekFilter getDefaultFilter()
	{
		OpleidingAanbodZoekFilter filter = new OpleidingAanbodZoekFilter();
		filter.addOrderByProperty("opleiding.code");
		return filter;
	}

	public OpleidingZoekenPage()
	{
		this(getDefaultFilter());
	}

	public OpleidingZoekenPage(OpleidingAanbodZoekFilter filter)
	{
		super(CoreMainMenuItem.Onderwijs);
		GeneralFilteredSortableDataProvider<OpleidingAanbod, OpleidingAanbodZoekFilter> provider =
			GeneralFilteredSortableDataProvider.of(filter, OpleidingAanbodDataAccessHelper.class);
		final CustomDataPanel<OpleidingAanbod> datapanel =
			new EduArteDataPanel<OpleidingAanbod>("datapanel", provider, new OpleidingAanbodTable())
			{
				private static final long serialVersionUID = 1L;

				/**
				 * Geeft de opleiding van het opleidingaanbod terug terug ipv het
				 * opleidingaanbod. Dit zorgt ervoor dat een opleiding niet herhaald wordt
				 * op de volgende regel als het om dezelfde opleiding gaat.
				 * 
				 * @see nl.topicus.cobra.web.components.datapanel.CustomDataPanel#getRowValue(Object
				 *      rowValue)
				 */
				@Override
				protected Object getRowValue(OpleidingAanbod aanbod)
				{
					if (aanbod != null)
					{
						return aanbod.getOpleiding();
					}
					return null;
				}

				@Override
				protected IModel<String> createTitleModel(String title)
				{
					return new TitleModel(title, this)
					{
						private static final long serialVersionUID = 1L;

						@Override
						public String getObject()
						{
							@SuppressWarnings("unchecked")
							GeneralFilteredSortableDataProvider< ? extends OpleidingAanbod, OpleidingAanbodZoekFilter> prov =
								(GeneralFilteredSortableDataProvider< ? extends OpleidingAanbod, OpleidingAanbodZoekFilter>) getDataProvider();
							OpleidingAanbodZoekFilter zoekFilter = prov.getFilterState();
							long opleidingCount =
								DataAccessRegistry.getHelper(OpleidingAanbodDataAccessHelper.class)
									.getOpleidingCount(zoekFilter);

							String superTitle = super.getObject() + "(";
							StringBuilder buffer = new StringBuilder(superTitle.length() + 20);
							buffer.append(superTitle).replace(buffer.length() - 2, buffer.length(),
								" opleidingen/locaties, ").append(opleidingCount).append(
								" opleidingen)");

							return buffer.toString();
						}
					};
				}
			};
		datapanel.setRowFactory(new ActiefRowFactoryDecorator<OpleidingAanbod>(
			new CustomDataPanelPageLinkRowFactory<OpleidingAanbod>(OpleidingkaartPage.class)
			{
				private static final long serialVersionUID = 1L;

				@Override
				public void onClick(Item<OpleidingAanbod> item)
				{
					OpleidingAanbod aanbod = item.getModelObject();
					pushSearchResultToNavigationLevel(datapanel, item.getIndex());
					setResponsePage(new OpleidingkaartPage(aanbod.getOpleiding()));
				}
			})
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected WebMarkupContainer applyActiefCssClass(WebMarkupContainer rowWMC,
					final Item<OpleidingAanbod> item)
			{
				rowWMC.add(new AttributeAppender("class", new AbstractReadOnlyModel<String>()
				{
					private static final long serialVersionUID = 1L;

					@Override
					public String getObject()
					{
						OpleidingAanbod aanbod = item.getModelObject();

						if (!aanbod.getOpleiding().isActief())
							return "inactive";
						return null;
					}
				}, " "));
				return rowWMC;
			}

		});
		datapanel.setItemsPerPage(20);
		add(datapanel);
		filterPanel = new OpleidingAanbodZoekFilterPanel("filter", filter, datapanel);
		add(filterPanel);
		createComponents();
	}

	@Override
	protected void fillBottomRow(BottomRowPanel panel)
	{
		super.fillBottomRow(panel);
		ModuleEditPageButton<Opleiding> toevoegen =
			new ModuleEditPageButton<Opleiding>(panel, "Toevoegen", CobraKeyAction.TOEVOEGEN,
				Opleiding.class, OnderwijsCollectiefMenuItem.OpleidingenZoeken,
				OpleidingZoekenPage.this)
			{
				private static final long serialVersionUID = 1L;

				@Override
				protected Opleiding getEntity()
				{
					return new Opleiding();
				}
			};
		panel.addButton(toevoegen);

		DisableSecurityCheckMarker.place(toevoegen,
			OrganisatieEenheidLocatieKoppelbaarSecurityCheck.class);
	}

	@Override
	public AbstractMenuBar createMenu(String id)
	{
		return new OnderwijsCollectiefMenu(id, OnderwijsCollectiefMenuItem.OpleidingenZoeken);
	}

	@Override
	public void getBookmarkConstructorArguments(List<Class< ? >> ctorArgTypes,
			List<Object> ctorArgValues)
	{
		super.getBookmarkConstructorArguments(ctorArgTypes, ctorArgValues);
		ctorArgTypes.add(OpleidingAanbodZoekFilter.class);
		ctorArgValues.add(filterPanel.getZoekfilter());
	}
}
