package nl.topicus.eduarte.krdparticipatie.web.pages.beheer;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.dataproviders.GeneralDataProvider;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.web.components.datapanel.CustomDataPanelContentDescription;
import nl.topicus.cobra.web.components.datapanel.CustomDataPanelPageLinkRowFactory;
import nl.topicus.cobra.web.components.datapanel.columns.BooleanPropertyColumn;
import nl.topicus.cobra.web.components.datapanel.columns.CustomPropertyColumn;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.cobra.web.components.panels.bottomrow.ToevoegenButton;
import nl.topicus.eduarte.dao.participatie.helpers.AbsentieRedenDataAccessHelper;
import nl.topicus.eduarte.entities.participatie.AbsentieReden;
import nl.topicus.eduarte.krdparticipatie.principals.beheer.BeheerAbsentieredenen;
import nl.topicus.eduarte.krdparticipatie.web.components.menu.enums.ParticipatieBeheerMenuItem;
import nl.topicus.eduarte.krdparticipatie.web.components.panels.filter.OrganisatieEenheidLocatieActiefZoekFilterPanel;
import nl.topicus.eduarte.participatie.zoekfilters.AbsentieRedenZoekFilter;
import nl.topicus.eduarte.web.components.panels.EduArteDataPanel;
import nl.topicus.eduarte.web.components.panels.datapanel.columns.OrganisatieEenheidInheritanceColumn;
import nl.topicus.eduarte.web.pages.beheer.AbstractBeheerPage;
import nl.topicus.eduarte.zoekfilters.OrganisatieEenheidLocatieAuthorizationContext;

import org.apache.wicket.Page;
import org.apache.wicket.markup.html.link.IPageLink;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.IDataProvider;

/**
 * @author loite
 */
@PageInfo(title = "Absentieredenen", menu = "Beheer > Participatie > Absentieredenen")
@InPrincipal(BeheerAbsentieredenen.class)
public class AbsentieRedenenPage extends AbstractBeheerPage<Void>
{
	private static final long serialVersionUID = 1L;

	private OrganisatieEenheidLocatieActiefZoekFilterPanel<AbsentieReden, AbsentieRedenZoekFilter> filterPanel;

	private CustomDataPanelContentDescription<AbsentieReden> desc;

	private EduArteDataPanel<AbsentieReden> datapanel;

	private static AbsentieRedenZoekFilter getDefaultFilter()
	{
		AbsentieRedenZoekFilter filter = new AbsentieRedenZoekFilter();
		return filter;
	}

	public AbsentieRedenenPage()
	{
		this(getDefaultFilter());
	}

	public AbsentieRedenenPage(AbsentieRedenZoekFilter filter)
	{
		super(ParticipatieBeheerMenuItem.Absentieredenen);
		IDataProvider<AbsentieReden> provider =
			GeneralDataProvider.of(filter, AbsentieRedenDataAccessHelper.class);
		filter.setAuthorizationContext(new OrganisatieEenheidLocatieAuthorizationContext(this));

		createColumns(filter);
		createDataPanel(provider);
		createFilterPanel(filter);
		createComponents();
	}

	private void createFilterPanel(AbsentieRedenZoekFilter filter)
	{
		filterPanel =
			new OrganisatieEenheidLocatieActiefZoekFilterPanel<AbsentieReden, AbsentieRedenZoekFilter>(
				"filter", filter, datapanel);
		add(filterPanel);
	}

	private void createDataPanel(IDataProvider<AbsentieReden> provider)
	{
		datapanel = new EduArteDataPanel<AbsentieReden>("datapanel", provider, desc);

		datapanel.setRowFactory(new CustomDataPanelPageLinkRowFactory<AbsentieReden>(
			EditAbsentieRedenPage.class)
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick(Item<AbsentieReden> item)
			{
				AbsentieReden reden = item.getModelObject();
				setResponsePage(new EditAbsentieRedenPage(reden, filterPanel.getZoekfilter()));
			}
		});

		add(datapanel);
	}

	private void createColumns(AbsentieRedenZoekFilter filter)
	{
		desc = new CustomDataPanelContentDescription<AbsentieReden>("Absentiereden");

		desc.addColumn(new OrganisatieEenheidInheritanceColumn<AbsentieReden>("image", filter));
		desc.addColumn(new CustomPropertyColumn<AbsentieReden>("Afkorting", "Afkorting",
			"afkorting", "afkorting"));
		desc.addColumn(new CustomPropertyColumn<AbsentieReden>("Omschrijving", "Omschrijving",
			"omschrijving", "omschrijving"));
		desc.addColumn(new CustomPropertyColumn<AbsentieReden>("Soort", "Soort", "absentieSoort",
			"absentieSoort"));
		desc.addColumn(new CustomPropertyColumn<AbsentieReden>("Organisatie-eenheid",
			"Organisatie-eenheid", "organisatieEenheid", "organisatieEenheid.naam"));

		desc.addColumn(new CustomPropertyColumn<AbsentieReden>("Locatie", "Locatie", "locatie",
			"locatie.naam"));

		desc.addColumn(new BooleanPropertyColumn<AbsentieReden>("Actief", "Actief", "actief",
			"actief"));

	}

	@Override
	protected void fillBottomRow(BottomRowPanel panel)
	{
		panel.addButton(new ToevoegenButton(panel, new IPageLink()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public Page getPage()
			{
				return new EditAbsentieRedenPage(filterPanel.getZoekfilter());
			}

			@Override
			public Class< ? extends Page> getPageIdentity()
			{
				return EditAbsentieRedenPage.class;
			}

		}));
	}
}
