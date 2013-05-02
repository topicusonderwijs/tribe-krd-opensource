package nl.topicus.eduarte.krd.web.pages.beheer.organisatie.extern;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.dataproviders.GeneralFilteredSortableDataProvider;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.web.components.datapanel.CustomDataPanelPageLinkRowFactory;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.cobra.web.components.panels.bottomrow.ToevoegenButton;
import nl.topicus.eduarte.dao.helpers.CodeNaamActiefLandelijkOfInstellingEntiteitDataAccessHelper;
import nl.topicus.eduarte.entities.organisatie.SoortExterneOrganisatie;
import nl.topicus.eduarte.krd.principals.beheer.SoortExterneOrganisaties;
import nl.topicus.eduarte.web.components.menu.BeheerMenuItem;
import nl.topicus.eduarte.web.components.panels.EduArteDataPanel;
import nl.topicus.eduarte.web.components.panels.datapanel.table.SoortExterneOrganisatieTable;
import nl.topicus.eduarte.web.components.panels.filter.CodeNaamZoekFilterPanel;
import nl.topicus.eduarte.web.pages.SecurePage;
import nl.topicus.eduarte.web.pages.beheer.AbstractBeheerPage;
import nl.topicus.eduarte.zoekfilters.CodeNaamActiefZoekFilter;

import org.apache.wicket.Page;
import org.apache.wicket.markup.html.link.IPageLink;
import org.apache.wicket.markup.repeater.data.IDataProvider;

@PageInfo(title = "Soort externe organisaties", menu = "Beheer > Beheer tabellen > Soort externe organisaties")
@InPrincipal(SoortExterneOrganisaties.class)
public class SoortExterneOrganisatieZoekenPage extends AbstractBeheerPage<Void>
{
	private static CodeNaamActiefZoekFilter<SoortExterneOrganisatie> getDefaultFilter()
	{
		CodeNaamActiefZoekFilter<SoortExterneOrganisatie> ret =
			new CodeNaamActiefZoekFilter<SoortExterneOrganisatie>(SoortExterneOrganisatie.class);
		return ret;
	}

	@SuppressWarnings("unchecked")
	public SoortExterneOrganisatieZoekenPage()
	{
		super(BeheerMenuItem.SoortExterneOrganisaties);
		CodeNaamActiefZoekFilter<SoortExterneOrganisatie> filter = getDefaultFilter();
		IDataProvider<SoortExterneOrganisatie> dataprovider =
			new GeneralFilteredSortableDataProvider(filter,
				CodeNaamActiefLandelijkOfInstellingEntiteitDataAccessHelper.class);

		EduArteDataPanel<SoortExterneOrganisatie> datapanel =
			new EduArteDataPanel<SoortExterneOrganisatie>("datapanel", dataprovider,
				new SoortExterneOrganisatieTable("Soort externe organisaties"));
		datapanel.setRowFactory(new CustomDataPanelPageLinkRowFactory<SoortExterneOrganisatie>(
			SoortExterneOrganisatieEditPage.class));
		add(datapanel);
		CodeNaamZoekFilterPanel filterPanel =
			new CodeNaamZoekFilterPanel("filter", filter, datapanel);
		add(filterPanel);
		createComponents();
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
				SoortExterneOrganisatie soortExterneOrganisatie = new SoortExterneOrganisatie();
				return new SoortExterneOrganisatieEditPage(soortExterneOrganisatie,
					SoortExterneOrganisatieZoekenPage.this);
			}

			@Override
			public Class< ? extends SecurePage> getPageIdentity()
			{
				return SoortExterneOrganisatieEditPage.class;
			}
		}));
	}
}
