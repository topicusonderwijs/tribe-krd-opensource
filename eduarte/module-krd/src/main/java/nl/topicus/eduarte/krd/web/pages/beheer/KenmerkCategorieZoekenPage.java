package nl.topicus.eduarte.krd.web.pages.beheer;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.dataproviders.GeneralFilteredSortableDataProvider;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.web.components.datapanel.CustomDataPanelPageLinkRowFactory;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.cobra.web.components.panels.bottomrow.ToevoegenButton;
import nl.topicus.eduarte.dao.helpers.KenmerkCategorieDataAccessHelper;
import nl.topicus.eduarte.entities.kenmerk.KenmerkCategorie;
import nl.topicus.eduarte.krd.principals.beheer.systeemtabellen.KenmerkPrincipal;
import nl.topicus.eduarte.web.components.menu.BeheerMenuItem;
import nl.topicus.eduarte.web.components.panels.EduArteDataPanel;
import nl.topicus.eduarte.web.components.panels.datapanel.table.CodeNaamActiefTable;
import nl.topicus.eduarte.web.components.panels.filter.CodeNaamActiefZoekFilterPanel;
import nl.topicus.eduarte.web.pages.beheer.AbstractBeheerPage;
import nl.topicus.eduarte.zoekfilters.KenmerkCategorieZoekFilter;

import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.IDataProvider;

@PageInfo(title = "Kenmerkcategorieën", menu = "Beheer > Beheer tabellen > Kenmerkcategorieën")
@InPrincipal(KenmerkPrincipal.class)
public class KenmerkCategorieZoekenPage extends AbstractBeheerPage<Void>
{
	private static KenmerkCategorieZoekFilter getDefaultFilter()
	{
		KenmerkCategorieZoekFilter ret = new KenmerkCategorieZoekFilter();
		ret.setActief(Boolean.TRUE);
		return ret;
	}

	public KenmerkCategorieZoekenPage()
	{
		super(BeheerMenuItem.Kenmerkcategorien);

		KenmerkCategorieZoekFilter filter = getDefaultFilter();
		IDataProvider<KenmerkCategorie> dataprovider =
			GeneralFilteredSortableDataProvider.of(filter,
				KenmerkCategorieDataAccessHelper.class);

		EduArteDataPanel<KenmerkCategorie> datapanel =
			new EduArteDataPanel<KenmerkCategorie>("datapanel", dataprovider,
				new CodeNaamActiefTable<KenmerkCategorie>("Kenmerkcategorie"));
		datapanel.setRowFactory(new CustomDataPanelPageLinkRowFactory<KenmerkCategorie>(
			KenmerkCategorieEditPage.class)
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void onClick(Item<KenmerkCategorie> item)
			{
				setResponsePage(new KenmerkCategorieEditPage(item.getModel(),
					KenmerkCategorieZoekenPage.this));
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
		panel.addButton(new ToevoegenButton(panel, "Nieuwe categorie",
			KenmerkCategorieEditPage.class, KenmerkCategorieZoekenPage.this));
	}
}
