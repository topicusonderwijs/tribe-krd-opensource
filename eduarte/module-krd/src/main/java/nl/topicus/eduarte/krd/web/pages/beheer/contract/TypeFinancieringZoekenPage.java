package nl.topicus.eduarte.krd.web.pages.beheer.contract;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.dataproviders.GeneralFilteredSortableDataProvider;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.web.components.datapanel.CustomDataPanelPageLinkRowFactory;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.cobra.web.components.panels.bottomrow.ToevoegenButton;
import nl.topicus.eduarte.dao.helpers.TypeFinancieringDataAccessHelper;
import nl.topicus.eduarte.entities.contract.TypeFinanciering;
import nl.topicus.eduarte.krd.principals.beheer.TypeFinancieringen;
import nl.topicus.eduarte.web.components.menu.BeheerMenuItem;
import nl.topicus.eduarte.web.components.panels.EduArteDataPanel;
import nl.topicus.eduarte.web.components.panels.datapanel.table.TypeFinancieringTable;
import nl.topicus.eduarte.web.components.panels.filter.CodeNaamActiefZoekFilterPanel;
import nl.topicus.eduarte.web.pages.beheer.AbstractBeheerPage;
import nl.topicus.eduarte.zoekfilters.TypeFinancieringZoekFilter;

import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.IDataProvider;

@PageInfo(title = "Soort financieringen", menu = "Beheer > Beheer tabellen > Soort financieringen")
@InPrincipal(TypeFinancieringen.class)
public class TypeFinancieringZoekenPage extends AbstractBeheerPage<Void>
{
	private static TypeFinancieringZoekFilter getDefaultFilter()
	{
		TypeFinancieringZoekFilter ret = new TypeFinancieringZoekFilter();
		ret.setActief(true);
		return ret;
	}

	public TypeFinancieringZoekenPage()
	{
		super(BeheerMenuItem.TypeFinancieringen);

		TypeFinancieringZoekFilter filter = getDefaultFilter();
		IDataProvider<TypeFinanciering> dataprovider =
			GeneralFilteredSortableDataProvider.of(filter,
				TypeFinancieringDataAccessHelper.class);

		EduArteDataPanel<TypeFinanciering> datapanel =
			new EduArteDataPanel<TypeFinanciering>("datapanel", dataprovider,
				new TypeFinancieringTable());
		datapanel.setRowFactory(new CustomDataPanelPageLinkRowFactory<TypeFinanciering>(
			TypeFinancieringEditPage.class)
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void onClick(Item<TypeFinanciering> item)
			{
				setResponsePage(new TypeFinancieringEditPage(item.getModel(),
					TypeFinancieringZoekenPage.this));
			}
		});
		add(datapanel);

		CodeNaamActiefZoekFilterPanel filterPanel =
			new CodeNaamActiefZoekFilterPanel("filter", filter, datapanel);
		add(filterPanel);

		createComponents();
	}

	@Override
	protected void fillBottomRow(BottomRowPanel panel)
	{
		panel.addButton(new ToevoegenButton(panel, "Nieuwe soort financiering",
			TypeFinancieringEditPage.class, TypeFinancieringZoekenPage.this));
	}
}
