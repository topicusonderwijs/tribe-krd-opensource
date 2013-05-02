package nl.topicus.eduarte.krd.web.pages.beheer.soortproductregel;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.dataproviders.GeneralFilteredSortableDataProvider;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.security.RechtenSoort;
import nl.topicus.cobra.security.RechtenSoorten;
import nl.topicus.cobra.web.components.datapanel.CustomDataPanelPageLinkRowFactory;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.cobra.web.components.panels.bottomrow.ToevoegenButton;
import nl.topicus.eduarte.dao.helpers.SoortProductregelDataAccessHelper;
import nl.topicus.eduarte.entities.organisatie.EntiteitContext;
import nl.topicus.eduarte.entities.productregel.SoortProductregel;
import nl.topicus.eduarte.krd.principals.onderwijs.BeheerSoortProductregels;
import nl.topicus.eduarte.krd.web.components.panels.filter.SoortProductregelZoekFilterPanel;
import nl.topicus.eduarte.web.components.menu.BeheerMenuItem;
import nl.topicus.eduarte.web.components.panels.EduArteDataPanel;
import nl.topicus.eduarte.web.components.panels.datapanel.table.SoortProductregelTable;
import nl.topicus.eduarte.web.pages.SecurePage;
import nl.topicus.eduarte.web.pages.beheer.AbstractBeheerPage;
import nl.topicus.eduarte.zoekfilters.SoortProductregelZoekFilter;

import org.apache.wicket.Page;
import org.apache.wicket.markup.html.link.IPageLink;

@PageInfo(title = "Soort productregels", menu = "Beheer > Onderwijs > Soort productregels")
@InPrincipal(BeheerSoortProductregels.class)
@RechtenSoorten( {RechtenSoort.INSTELLING, RechtenSoort.BEHEER})
public class SoortProductregelZoekenPage extends AbstractBeheerPage<Void>
{
	private static SoortProductregelZoekFilter getDefaultFilter()
	{
		SoortProductregelZoekFilter ret = new SoortProductregelZoekFilter();
		ret.addOrderByProperty("volgnummer");
		ret.addOrderByProperty("taxonomie.naam");
		ret.setActief(true);
		return ret;
	}

	public SoortProductregelZoekenPage()
	{
		super(BeheerMenuItem.SoortProductregels);
		SoortProductregelZoekFilter filter = getDefaultFilter();
		GeneralFilteredSortableDataProvider<SoortProductregel, SoortProductregelZoekFilter> dataprovider =
			GeneralFilteredSortableDataProvider.of(filter,
				SoortProductregelDataAccessHelper.class);

		EduArteDataPanel<SoortProductregel> datapanel =
			new EduArteDataPanel<SoortProductregel>("datapanel", dataprovider,
				new SoortProductregelTable());
		datapanel.setRowFactory(new CustomDataPanelPageLinkRowFactory<SoortProductregel>(
			SoortProductregelEditPage.class));
		add(datapanel);
		SoortProductregelZoekFilterPanel filterPanel =
			new SoortProductregelZoekFilterPanel("filter", filter, datapanel);
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
				SoortProductregel soortProductregel =
					new SoortProductregel(EntiteitContext.INSTELLING);
				soortProductregel.setVolgnummer(getVolgnummer());
				soortProductregel.setActief(true);
				return new SoortProductregelEditPage(soortProductregel,
					SoortProductregelZoekenPage.this);
			}

			@Override
			public Class< ? extends SecurePage> getPageIdentity()
			{
				return SoortProductregelEditPage.class;
			}
		}));

	}

	protected int getVolgnummer()
	{
		return DataAccessRegistry.getHelper(SoortProductregelDataAccessHelper.class)
			.getVolgendeVolgnummer();
	}
}
