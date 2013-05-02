package nl.topicus.eduarte.krd.web.pages.beheer;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.dataproviders.GeneralFilteredSortableDataProvider;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.web.components.datapanel.CustomDataPanelPageLinkRowFactory;
import nl.topicus.cobra.web.components.datapanel.columns.CustomPropertyColumn;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.cobra.web.components.panels.bottomrow.ToevoegenButton;
import nl.topicus.eduarte.dao.helpers.KenmerkDataAccessHelper;
import nl.topicus.eduarte.entities.kenmerk.Kenmerk;
import nl.topicus.eduarte.krd.principals.beheer.systeemtabellen.KenmerkPrincipal;
import nl.topicus.eduarte.web.components.menu.BeheerMenuItem;
import nl.topicus.eduarte.web.components.panels.EduArteDataPanel;
import nl.topicus.eduarte.web.components.panels.datapanel.table.CodeNaamActiefTable;
import nl.topicus.eduarte.web.components.panels.filter.CodeNaamActiefZoekFilterPanel;
import nl.topicus.eduarte.web.pages.beheer.AbstractBeheerPage;
import nl.topicus.eduarte.zoekfilters.KenmerkZoekFilter;

import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.IDataProvider;

@PageInfo(title = "Kenmerken", menu = "Beheer > Beheer tabellen > Kenmerken")
@InPrincipal(KenmerkPrincipal.class)
public class KenmerkZoekenPage extends AbstractBeheerPage<Void>
{
	private static KenmerkZoekFilter getDefaultFilter()
	{
		KenmerkZoekFilter ret = new KenmerkZoekFilter();
		ret.setActief(Boolean.TRUE);
		return ret;
	}

	public KenmerkZoekenPage()
	{
		super(BeheerMenuItem.Kenmerken);

		KenmerkZoekFilter filter = getDefaultFilter();
		IDataProvider<Kenmerk> dataprovider =
			GeneralFilteredSortableDataProvider.of(filter, KenmerkDataAccessHelper.class);

		EduArteDataPanel<Kenmerk> datapanel =
			new EduArteDataPanel<Kenmerk>("datapanel", dataprovider,
				new CodeNaamActiefTable<Kenmerk>("Kenmerken")
					.addColumn(new CustomPropertyColumn<Kenmerk>("Categorie", "Categorie",
						"categorie.naam", "categorie.naam")));
		datapanel.setRowFactory(new CustomDataPanelPageLinkRowFactory<Kenmerk>(
			KenmerkEditPage.class)
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void onClick(Item<Kenmerk> item)
			{
				setResponsePage(new KenmerkEditPage(item.getModel(), KenmerkZoekenPage.this));
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
		panel.addButton(new ToevoegenButton(panel, "Nieuw kenmerk", KenmerkEditPage.class,
			KenmerkZoekenPage.this));
	}
}
