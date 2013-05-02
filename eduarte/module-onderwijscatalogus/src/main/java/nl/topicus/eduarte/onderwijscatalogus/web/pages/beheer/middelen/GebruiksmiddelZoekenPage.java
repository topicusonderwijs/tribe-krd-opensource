package nl.topicus.eduarte.onderwijscatalogus.web.pages.beheer.middelen;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.dataproviders.GeneralFilteredSortableDataProvider;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.web.components.datapanel.CustomDataPanelPageLinkRowFactory;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.cobra.web.components.panels.bottomrow.ToevoegenButton;
import nl.topicus.eduarte.dao.helpers.CodeNaamActiefLandelijkOfInstellingEntiteitDataAccessHelper;
import nl.topicus.eduarte.entities.onderwijsproduct.Gebruiksmiddel;
import nl.topicus.eduarte.onderwijscatalogus.principals.beheer.Gebruiksmiddelen;
import nl.topicus.eduarte.web.components.menu.BeheerMenuItem;
import nl.topicus.eduarte.web.components.panels.EduArteDataPanel;
import nl.topicus.eduarte.web.components.panels.datapanel.table.CodeNaamActiefTable;
import nl.topicus.eduarte.web.components.panels.filter.CodeNaamActiefZoekFilterPanel;
import nl.topicus.eduarte.web.pages.SecurePage;
import nl.topicus.eduarte.web.pages.beheer.AbstractBeheerPage;
import nl.topicus.eduarte.zoekfilters.CodeNaamActiefZoekFilter;

import org.apache.wicket.Page;
import org.apache.wicket.markup.html.link.IPageLink;
import org.apache.wicket.markup.repeater.data.IDataProvider;

@PageInfo(title = "Gebruiksmiddelen", menu = "Beheer > Onderwijs > Gebruiksmiddelen")
@InPrincipal(Gebruiksmiddelen.class)
public class GebruiksmiddelZoekenPage extends AbstractBeheerPage<Void>
{
	private static CodeNaamActiefZoekFilter<Gebruiksmiddel> getDefaultFilter()
	{
		CodeNaamActiefZoekFilter<Gebruiksmiddel> ret =
			new CodeNaamActiefZoekFilter<Gebruiksmiddel>(Gebruiksmiddel.class);
		ret.setActief(true);
		return ret;
	}

	public GebruiksmiddelZoekenPage()
	{
		super(BeheerMenuItem.Gebruiksmiddelen);
		CodeNaamActiefZoekFilter<Gebruiksmiddel> filter = getDefaultFilter();
		@SuppressWarnings("unchecked")
		IDataProvider<Gebruiksmiddel> dataprovider =
			new GeneralFilteredSortableDataProvider(filter,
				CodeNaamActiefLandelijkOfInstellingEntiteitDataAccessHelper.class);

		EduArteDataPanel<Gebruiksmiddel> datapanel =
			new EduArteDataPanel<Gebruiksmiddel>("datapanel", dataprovider,
				new CodeNaamActiefTable<Gebruiksmiddel>("Gebruiksmiddelen"));
		datapanel.setRowFactory(new CustomDataPanelPageLinkRowFactory<Gebruiksmiddel>(
			GebruiksmiddelEditPage.class));
		add(datapanel);
		CodeNaamActiefZoekFilterPanel filterPanel =
			new CodeNaamActiefZoekFilterPanel("filter", filter, datapanel);
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
				Gebruiksmiddel middel = new Gebruiksmiddel();
				return new GebruiksmiddelEditPage(middel, GebruiksmiddelZoekenPage.this);
			}

			@Override
			public Class< ? extends SecurePage> getPageIdentity()
			{
				return GebruiksmiddelEditPage.class;
			}
		}));
	}
}
