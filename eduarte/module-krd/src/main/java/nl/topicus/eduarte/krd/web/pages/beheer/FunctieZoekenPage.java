package nl.topicus.eduarte.krd.web.pages.beheer;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.dataproviders.GeneralFilteredSortableDataProvider;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.web.components.datapanel.CustomDataPanelPageLinkRowFactory;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.cobra.web.components.panels.bottomrow.ToevoegenButton;
import nl.topicus.eduarte.dao.helpers.FunctieDataAccessHelper;
import nl.topicus.eduarte.entities.personen.Functie;
import nl.topicus.eduarte.krd.principals.beheer.systeemtabellen.FunctiePrincipal;
import nl.topicus.eduarte.web.components.menu.BeheerMenuItem;
import nl.topicus.eduarte.web.components.panels.EduArteDataPanel;
import nl.topicus.eduarte.web.components.panels.datapanel.table.CodeNaamActiefTable;
import nl.topicus.eduarte.web.components.panels.filter.CodeNaamActiefZoekFilterPanel;
import nl.topicus.eduarte.web.pages.beheer.AbstractBeheerPage;
import nl.topicus.eduarte.zoekfilters.FunctieZoekFilter;

import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.IDataProvider;

@PageInfo(title = "Functies", menu = "Beheer > Functies")
@InPrincipal(FunctiePrincipal.class)
public class FunctieZoekenPage extends AbstractBeheerPage<Void>
{
	private static FunctieZoekFilter getDefaultFilter()
	{
		FunctieZoekFilter ret = new FunctieZoekFilter();
		ret.setActief(true);
		return ret;
	}

	public FunctieZoekenPage()
	{
		super(BeheerMenuItem.Functies);

		FunctieZoekFilter filter = getDefaultFilter();
		IDataProvider<Functie> dataprovider =
			GeneralFilteredSortableDataProvider.of(filter, FunctieDataAccessHelper.class);

		EduArteDataPanel<Functie> datapanel =
			new EduArteDataPanel<Functie>("datapanel", dataprovider,
				new CodeNaamActiefTable<Functie>("Functie"));
		datapanel.setRowFactory(new CustomDataPanelPageLinkRowFactory<Functie>(
			FunctieEditPage.class)
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void onClick(Item<Functie> item)
			{
				setResponsePage(new FunctieEditPage(item.getModel(), FunctieZoekenPage.this));
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
		panel.addButton(new ToevoegenButton(panel, "Nieuwe functie", FunctieEditPage.class,
			FunctieZoekenPage.this));
	}
}
