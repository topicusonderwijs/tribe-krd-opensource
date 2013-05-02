package nl.topicus.eduarte.resultaten.web.pages.beheer;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.dataproviders.GeneralFilteredSortableDataProvider;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.web.components.datapanel.CustomDataPanelPageLinkRowFactory;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.cobra.web.components.panels.bottomrow.ToevoegenButton;
import nl.topicus.eduarte.core.principals.beheer.onderwijs.Schalen;
import nl.topicus.eduarte.dao.helpers.SchaalDataAccessHelper;
import nl.topicus.eduarte.entities.resultaatstructuur.Schaal;
import nl.topicus.eduarte.web.components.menu.BeheerMenuItem;
import nl.topicus.eduarte.web.components.panels.EduArteDataPanel;
import nl.topicus.eduarte.web.components.panels.datapanel.table.SchaalTable;
import nl.topicus.eduarte.web.components.panels.filter.NaamActiefZoekFilterPanel;
import nl.topicus.eduarte.web.pages.beheer.AbstractBeheerPage;
import nl.topicus.eduarte.zoekfilters.NaamActiefZoekFilter;

import org.apache.wicket.Page;
import org.apache.wicket.markup.html.link.IPageLink;
import org.apache.wicket.markup.repeater.data.IDataProvider;

@PageInfo(title = "Schalen", menu = "Beheer > Onderwijs > Schalen")
@InPrincipal(Schalen.class)
public class SchaalOverzichtPage extends AbstractBeheerPage<Void>
{
	public SchaalOverzichtPage()
	{
		super(BeheerMenuItem.Schalen);

		NaamActiefZoekFilter<Schaal> filter = createDefaultZoekFilter();
		IDataProvider<Schaal> provider =
			GeneralFilteredSortableDataProvider.of(filter, SchaalDataAccessHelper.class);
		EduArteDataPanel<Schaal> schalenPanel =
			new EduArteDataPanel<Schaal>("schalen", provider, new SchaalTable());
		schalenPanel.setRowFactory(new CustomDataPanelPageLinkRowFactory<Schaal>(
			SchaalEditPage.class));
		add(schalenPanel);

		add(new NaamActiefZoekFilterPanel("filterPanel", filter, schalenPanel));

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
				Schaal schaal = new Schaal();
				return new SchaalEditPage(schaal);
			}

			@Override
			public Class< ? extends Page> getPageIdentity()
			{
				return SchaalEditPage.class;
			}
		}));
	}

	private NaamActiefZoekFilter<Schaal> createDefaultZoekFilter()
	{
		NaamActiefZoekFilter<Schaal> ret = new NaamActiefZoekFilter<Schaal>(Schaal.class);
		ret.setActief(true);
		return ret;
	}
}
