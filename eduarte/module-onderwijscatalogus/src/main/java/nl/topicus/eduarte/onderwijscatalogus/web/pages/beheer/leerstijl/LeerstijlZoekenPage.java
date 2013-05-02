package nl.topicus.eduarte.onderwijscatalogus.web.pages.beheer.leerstijl;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.dataproviders.GeneralFilteredSortableDataProvider;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.web.components.datapanel.CustomDataPanelPageLinkRowFactory;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.cobra.web.components.panels.bottomrow.ToevoegenButton;
import nl.topicus.eduarte.dao.helpers.CodeNaamActiefLandelijkOfInstellingEntiteitDataAccessHelper;
import nl.topicus.eduarte.entities.onderwijsproduct.Leerstijl;
import nl.topicus.eduarte.onderwijscatalogus.principals.beheer.Leerstijlen;
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

@PageInfo(title = "Leerstijlen", menu = "Beheer > Onderwijs > Leerstijlen")
@InPrincipal(Leerstijlen.class)
public class LeerstijlZoekenPage extends AbstractBeheerPage<Void>
{
	private static CodeNaamActiefZoekFilter<Leerstijl> getDefaultFilter()
	{
		CodeNaamActiefZoekFilter<Leerstijl> ret =
			new CodeNaamActiefZoekFilter<Leerstijl>(Leerstijl.class);
		ret.setActief(true);
		return ret;
	}

	public LeerstijlZoekenPage()
	{
		super(BeheerMenuItem.Leerstijlen);
		CodeNaamActiefZoekFilter<Leerstijl> filter = getDefaultFilter();
		@SuppressWarnings("unchecked")
		IDataProvider<Leerstijl> dataprovider =
			new GeneralFilteredSortableDataProvider(filter,
				CodeNaamActiefLandelijkOfInstellingEntiteitDataAccessHelper.class);

		EduArteDataPanel<Leerstijl> datapanel =
			new EduArteDataPanel<Leerstijl>("datapanel", dataprovider,
				new CodeNaamActiefTable<Leerstijl>("Leerstijlen"));
		datapanel.setRowFactory(new CustomDataPanelPageLinkRowFactory<Leerstijl>(
			LeerstijlEditPage.class));
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
				Leerstijl leerstijl = new Leerstijl();
				return new LeerstijlEditPage(leerstijl, LeerstijlZoekenPage.this);
			}

			@Override
			public Class< ? extends SecurePage> getPageIdentity()
			{
				return LeerstijlEditPage.class;
			}
		}));
	}
}
