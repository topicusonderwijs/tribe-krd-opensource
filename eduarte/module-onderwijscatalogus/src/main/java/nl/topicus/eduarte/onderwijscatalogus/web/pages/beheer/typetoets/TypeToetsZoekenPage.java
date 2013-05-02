package nl.topicus.eduarte.onderwijscatalogus.web.pages.beheer.typetoets;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.dataproviders.GeneralFilteredSortableDataProvider;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.web.components.datapanel.CustomDataPanelPageLinkRowFactory;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.cobra.web.components.panels.bottomrow.ToevoegenButton;
import nl.topicus.eduarte.dao.helpers.CodeNaamActiefLandelijkOfInstellingEntiteitDataAccessHelper;
import nl.topicus.eduarte.entities.onderwijsproduct.TypeToets;
import nl.topicus.eduarte.onderwijscatalogus.principals.beheer.TypeToetsen;
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

@PageInfo(title = "Type toetsen", menu = "Beheer > Onderwijs > Type toetsen")
@InPrincipal(TypeToetsen.class)
public class TypeToetsZoekenPage extends AbstractBeheerPage<Void>
{
	private static CodeNaamActiefZoekFilter<TypeToets> getDefaultFilter()
	{
		CodeNaamActiefZoekFilter<TypeToets> ret =
			new CodeNaamActiefZoekFilter<TypeToets>(TypeToets.class);
		ret.setActief(true);
		return ret;
	}

	public TypeToetsZoekenPage()
	{
		super(BeheerMenuItem.TypeToetsen);
		CodeNaamActiefZoekFilter<TypeToets> filter = getDefaultFilter();
		@SuppressWarnings("unchecked")
		IDataProvider<TypeToets> dataprovider =
			new GeneralFilteredSortableDataProvider(filter,
				CodeNaamActiefLandelijkOfInstellingEntiteitDataAccessHelper.class);

		EduArteDataPanel<TypeToets> datapanel =
			new EduArteDataPanel<TypeToets>("datapanel", dataprovider,
				new CodeNaamActiefTable<TypeToets>("Type toetsen"));
		datapanel.setRowFactory(new CustomDataPanelPageLinkRowFactory<TypeToets>(
			TypeToetsEditPage.class));
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
				TypeToets typeToets = new TypeToets();
				return new TypeToetsEditPage(typeToets, TypeToetsZoekenPage.this);
			}

			@Override
			public Class< ? extends SecurePage> getPageIdentity()
			{
				return TypeToetsEditPage.class;
			}
		}));
	}
}
