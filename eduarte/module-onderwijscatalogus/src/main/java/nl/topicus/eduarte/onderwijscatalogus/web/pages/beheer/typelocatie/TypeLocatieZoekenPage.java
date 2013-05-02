package nl.topicus.eduarte.onderwijscatalogus.web.pages.beheer.typelocatie;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.dataproviders.GeneralFilteredSortableDataProvider;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.web.components.datapanel.CustomDataPanelPageLinkRowFactory;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.cobra.web.components.panels.bottomrow.ToevoegenButton;
import nl.topicus.eduarte.dao.helpers.CodeNaamActiefLandelijkOfInstellingEntiteitDataAccessHelper;
import nl.topicus.eduarte.entities.onderwijsproduct.TypeLocatie;
import nl.topicus.eduarte.onderwijscatalogus.principals.beheer.TypeLocaties;
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

@PageInfo(title = "Type locaties", menu = "Beheer > Onderwijs > Type locaties")
@InPrincipal(TypeLocaties.class)
public class TypeLocatieZoekenPage extends AbstractBeheerPage<Void>
{
	private static CodeNaamActiefZoekFilter<TypeLocatie> getDefaultFilter()
	{
		CodeNaamActiefZoekFilter<TypeLocatie> ret =
			new CodeNaamActiefZoekFilter<TypeLocatie>(TypeLocatie.class);
		ret.setActief(true);
		return ret;
	}

	public TypeLocatieZoekenPage()
	{
		super(BeheerMenuItem.TypeLocaties);
		CodeNaamActiefZoekFilter<TypeLocatie> filter = getDefaultFilter();
		@SuppressWarnings("unchecked")
		IDataProvider<TypeLocatie> dataprovider =
			new GeneralFilteredSortableDataProvider(filter,
				CodeNaamActiefLandelijkOfInstellingEntiteitDataAccessHelper.class);

		EduArteDataPanel<TypeLocatie> datapanel =
			new EduArteDataPanel<TypeLocatie>("datapanel", dataprovider,
				new CodeNaamActiefTable<TypeLocatie>("Type locaties"));
		datapanel.setRowFactory(new CustomDataPanelPageLinkRowFactory<TypeLocatie>(
			TypeLocatieEditPage.class));
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
				TypeLocatie typeLocatie = new TypeLocatie();
				return new TypeLocatieEditPage(typeLocatie, TypeLocatieZoekenPage.this);
			}

			@Override
			public Class< ? extends SecurePage> getPageIdentity()
			{
				return TypeLocatieEditPage.class;
			}
		}));
	}
}
