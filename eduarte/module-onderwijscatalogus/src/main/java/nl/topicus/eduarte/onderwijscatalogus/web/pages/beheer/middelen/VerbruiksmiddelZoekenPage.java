package nl.topicus.eduarte.onderwijscatalogus.web.pages.beheer.middelen;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.dataproviders.GeneralFilteredSortableDataProvider;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.web.components.datapanel.CustomDataPanelPageLinkRowFactory;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.cobra.web.components.panels.bottomrow.ToevoegenButton;
import nl.topicus.eduarte.dao.helpers.CodeNaamActiefLandelijkOfInstellingEntiteitDataAccessHelper;
import nl.topicus.eduarte.entities.onderwijsproduct.Verbruiksmiddel;
import nl.topicus.eduarte.onderwijscatalogus.principals.beheer.Verbruiksmiddelen;
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

@PageInfo(title = "Verbruiksmiddelen", menu = "Beheer > Onderwijs > Verbruiksmiddelen")
@InPrincipal(Verbruiksmiddelen.class)
public class VerbruiksmiddelZoekenPage extends AbstractBeheerPage<Void>
{
	private static CodeNaamActiefZoekFilter<Verbruiksmiddel> getDefaultFilter()
	{
		CodeNaamActiefZoekFilter<Verbruiksmiddel> ret =
			new CodeNaamActiefZoekFilter<Verbruiksmiddel>(Verbruiksmiddel.class);
		ret.setActief(true);
		return ret;
	}

	public VerbruiksmiddelZoekenPage()
	{
		super(BeheerMenuItem.Verbruiksmiddelen);
		CodeNaamActiefZoekFilter<Verbruiksmiddel> filter = getDefaultFilter();
		@SuppressWarnings("unchecked")
		IDataProvider<Verbruiksmiddel> dataprovider =
			new GeneralFilteredSortableDataProvider(filter,
				CodeNaamActiefLandelijkOfInstellingEntiteitDataAccessHelper.class);

		EduArteDataPanel<Verbruiksmiddel> datapanel =
			new EduArteDataPanel<Verbruiksmiddel>("datapanel", dataprovider,
				new CodeNaamActiefTable<Verbruiksmiddel>("Verbruiksmiddelen"));
		datapanel.setRowFactory(new CustomDataPanelPageLinkRowFactory<Verbruiksmiddel>(
			VerbruiksmiddelEditPage.class));
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
				Verbruiksmiddel middel = new Verbruiksmiddel();
				return new VerbruiksmiddelEditPage(middel, VerbruiksmiddelZoekenPage.this);
			}

			@Override
			public Class< ? extends SecurePage> getPageIdentity()
			{
				return VerbruiksmiddelEditPage.class;
			}
		}));
	}
}
