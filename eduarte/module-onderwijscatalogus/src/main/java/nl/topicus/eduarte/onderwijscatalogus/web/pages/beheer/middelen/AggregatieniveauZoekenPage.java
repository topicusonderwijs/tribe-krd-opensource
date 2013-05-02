package nl.topicus.eduarte.onderwijscatalogus.web.pages.beheer.middelen;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.dataproviders.GeneralFilteredSortableDataProvider;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.web.components.datapanel.CustomDataPanelPageLinkRowFactory;
import nl.topicus.cobra.web.components.datapanel.columns.CustomPropertyColumn;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.cobra.web.components.panels.bottomrow.ToevoegenButton;
import nl.topicus.eduarte.dao.helpers.CodeNaamActiefLandelijkOfInstellingEntiteitDataAccessHelper;
import nl.topicus.eduarte.entities.onderwijsproduct.Aggregatieniveau;
import nl.topicus.eduarte.onderwijscatalogus.principals.beheer.Aggregatieniveaus;
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

@PageInfo(title = "Aggregatieniveau's", menu = "Beheer > Onderwijs > Aggregatieniveau's")
@InPrincipal(Aggregatieniveaus.class)
public class AggregatieniveauZoekenPage extends AbstractBeheerPage<Void>
{
	private static CodeNaamActiefZoekFilter<Aggregatieniveau> getDefaultFilter()
	{
		CodeNaamActiefZoekFilter<Aggregatieniveau> ret =
			new CodeNaamActiefZoekFilter<Aggregatieniveau>(Aggregatieniveau.class);
		ret.setActief(true);
		return ret;
	}

	public AggregatieniveauZoekenPage()
	{
		super(BeheerMenuItem.Aggregatieniveaus);
		CodeNaamActiefZoekFilter<Aggregatieniveau> filter = getDefaultFilter();
		@SuppressWarnings("unchecked")
		IDataProvider<Aggregatieniveau> dataprovider =
			new GeneralFilteredSortableDataProvider(filter,
				CodeNaamActiefLandelijkOfInstellingEntiteitDataAccessHelper.class);

		CodeNaamActiefTable<Aggregatieniveau> table =
			new CodeNaamActiefTable<Aggregatieniveau>("Aggregatieniveau's");
		table.addColumn(new CustomPropertyColumn<Aggregatieniveau>("Niveau", "Niveau", "niveau",
			"niveau"));
		EduArteDataPanel<Aggregatieniveau> datapanel =
			new EduArteDataPanel<Aggregatieniveau>("datapanel", dataprovider, table);
		datapanel.setRowFactory(new CustomDataPanelPageLinkRowFactory<Aggregatieniveau>(
			AggregatieniveauEditPage.class));
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
				Aggregatieniveau niveau = new Aggregatieniveau();
				return new AggregatieniveauEditPage(niveau, AggregatieniveauZoekenPage.this);
			}

			@Override
			public Class< ? extends SecurePage> getPageIdentity()
			{
				return AggregatieniveauEditPage.class;
			}
		}));
	}
}
