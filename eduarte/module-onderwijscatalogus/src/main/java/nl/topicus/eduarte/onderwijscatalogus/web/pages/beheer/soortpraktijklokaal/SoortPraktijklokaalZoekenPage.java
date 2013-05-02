package nl.topicus.eduarte.onderwijscatalogus.web.pages.beheer.soortpraktijklokaal;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.dataproviders.GeneralFilteredSortableDataProvider;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.web.components.datapanel.CustomDataPanelPageLinkRowFactory;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.cobra.web.components.panels.bottomrow.ToevoegenButton;
import nl.topicus.eduarte.dao.helpers.CodeNaamActiefLandelijkOfInstellingEntiteitDataAccessHelper;
import nl.topicus.eduarte.entities.onderwijsproduct.SoortPraktijklokaal;
import nl.topicus.eduarte.onderwijscatalogus.principals.beheer.SoortPraktijklokalen;
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

@PageInfo(title = "Soort praktijklokalen", menu = "Beheer > Onderwijs > Soort praktijklokalen")
@InPrincipal(SoortPraktijklokalen.class)
public class SoortPraktijklokaalZoekenPage extends AbstractBeheerPage<Void>
{
	private static CodeNaamActiefZoekFilter<SoortPraktijklokaal> getDefaultFilter()
	{
		CodeNaamActiefZoekFilter<SoortPraktijklokaal> ret =
			new CodeNaamActiefZoekFilter<SoortPraktijklokaal>(SoortPraktijklokaal.class);
		ret.setActief(true);
		return ret;
	}

	public SoortPraktijklokaalZoekenPage()
	{
		super(BeheerMenuItem.SoortPraktijklokalen);
		CodeNaamActiefZoekFilter<SoortPraktijklokaal> filter = getDefaultFilter();
		@SuppressWarnings("unchecked")
		IDataProvider<SoortPraktijklokaal> dataprovider =
			new GeneralFilteredSortableDataProvider(filter,
				CodeNaamActiefLandelijkOfInstellingEntiteitDataAccessHelper.class);

		EduArteDataPanel<SoortPraktijklokaal> datapanel =
			new EduArteDataPanel<SoortPraktijklokaal>("datapanel", dataprovider,
				new CodeNaamActiefTable<SoortPraktijklokaal>("Soort praktijklokalen"));
		datapanel.setRowFactory(new CustomDataPanelPageLinkRowFactory<SoortPraktijklokaal>(
			SoortPraktijklokaalEditPage.class));
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
				SoortPraktijklokaal soortPraktijklokaal = new SoortPraktijklokaal();
				return new SoortPraktijklokaalEditPage(soortPraktijklokaal,
					SoortPraktijklokaalZoekenPage.this);
			}

			@Override
			public Class< ? extends SecurePage> getPageIdentity()
			{
				return SoortPraktijklokaalEditPage.class;
			}
		}));
	}
}
