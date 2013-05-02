package nl.topicus.eduarte.onderwijscatalogus.web.pages.beheer.soortOnderwijsproduct;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.dataproviders.GeneralFilteredSortableDataProvider;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.web.components.datapanel.CustomDataPanelPageLinkRowFactory;
import nl.topicus.cobra.web.components.datapanel.columns.BooleanPropertyColumn;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.cobra.web.components.panels.bottomrow.ToevoegenButton;
import nl.topicus.eduarte.dao.helpers.CodeNaamActiefLandelijkOfInstellingEntiteitDataAccessHelper;
import nl.topicus.eduarte.entities.onderwijsproduct.SoortOnderwijsproduct;
import nl.topicus.eduarte.onderwijscatalogus.principals.beheer.SoortOnderwijsproducten;
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

@PageInfo(title = "Soort onderwijsproducten", menu = "Beheer > Onderwijs > Soort onderwijsproducten")
@InPrincipal(SoortOnderwijsproducten.class)
public class SoortOnderwijsproductZoekenPage extends AbstractBeheerPage<Void>
{
	private static CodeNaamActiefZoekFilter<SoortOnderwijsproduct> getDefaultFilter()
	{
		CodeNaamActiefZoekFilter<SoortOnderwijsproduct> ret =
			new CodeNaamActiefZoekFilter<SoortOnderwijsproduct>(SoortOnderwijsproduct.class);
		ret.setActief(true);
		return ret;
	}

	public SoortOnderwijsproductZoekenPage()
	{
		super(BeheerMenuItem.SoortOnderwijsproducten);
		CodeNaamActiefZoekFilter<SoortOnderwijsproduct> filter = getDefaultFilter();
		@SuppressWarnings("unchecked")
		IDataProvider<SoortOnderwijsproduct> dataprovider =
			new GeneralFilteredSortableDataProvider(filter,
				CodeNaamActiefLandelijkOfInstellingEntiteitDataAccessHelper.class);

		CodeNaamActiefTable<SoortOnderwijsproduct> table =
			new CodeNaamActiefTable<SoortOnderwijsproduct>("Soort onderwijsproduct");
		table.addColumn(new BooleanPropertyColumn<SoortOnderwijsproduct>("Summatief", "Summatief",
			"summatief", "summatief"));
		table.addColumn(new BooleanPropertyColumn<SoortOnderwijsproduct>("Stage", "Stage", "stage",
			"stage"));
		EduArteDataPanel<SoortOnderwijsproduct> datapanel =
			new EduArteDataPanel<SoortOnderwijsproduct>("datapanel", dataprovider, table);
		datapanel.setRowFactory(new CustomDataPanelPageLinkRowFactory<SoortOnderwijsproduct>(
			SoortOnderwijsproductEditPage.class));
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
				SoortOnderwijsproduct soortOnderwijsproduct = new SoortOnderwijsproduct();
				return new SoortOnderwijsproductEditPage(soortOnderwijsproduct,
					SoortOnderwijsproductZoekenPage.this);
			}

			@Override
			public Class< ? extends SecurePage> getPageIdentity()
			{
				return SoortOnderwijsproductEditPage.class;
			}
		}));
	}
}
