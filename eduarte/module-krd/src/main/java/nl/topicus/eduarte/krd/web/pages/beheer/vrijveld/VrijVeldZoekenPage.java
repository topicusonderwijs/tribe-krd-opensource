package nl.topicus.eduarte.krd.web.pages.beheer.vrijveld;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.dataproviders.GeneralFilteredSortableDataProvider;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.web.components.datapanel.CustomDataPanelPageLinkRowFactory;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.cobra.web.components.panels.bottomrow.ToevoegenButton;
import nl.topicus.eduarte.dao.helpers.VrijVeldDataAccessHelper;
import nl.topicus.eduarte.entities.vrijevelden.VrijVeld;
import nl.topicus.eduarte.krd.principals.beheer.systeemtabellen.VrijVeldenPrincipal;
import nl.topicus.eduarte.krd.web.components.panels.datapanel.table.VrijVeldTable;
import nl.topicus.eduarte.krd.web.components.panels.filter.VrijVeldZoekFilterPanel;
import nl.topicus.eduarte.web.components.menu.BeheerMenuItem;
import nl.topicus.eduarte.web.components.panels.EduArteDataPanel;
import nl.topicus.eduarte.web.pages.beheer.AbstractBeheerPage;
import nl.topicus.eduarte.zoekfilters.VrijVeldZoekFilter;

import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.IDataProvider;

@PageInfo(title = "Vrij velden", menu = "Beheer > Beheer tabellen > Vrije velden")
@InPrincipal(VrijVeldenPrincipal.class)
public class VrijVeldZoekenPage extends AbstractBeheerPage<Void>
{
	private static VrijVeldZoekFilter getDefaultFilter()
	{
		VrijVeldZoekFilter ret = new VrijVeldZoekFilter();
		ret.setActief(true);
		return ret;
	}

	public VrijVeldZoekenPage()
	{
		super(BeheerMenuItem.VrijeVelden);

		VrijVeldZoekFilter filter = getDefaultFilter();
		IDataProvider<VrijVeld> dataprovider =
			GeneralFilteredSortableDataProvider.of(filter,
				VrijVeldDataAccessHelper.class);

		EduArteDataPanel<VrijVeld> datapanel =
			new EduArteDataPanel<VrijVeld>("datapanel", dataprovider, new VrijVeldTable());
		datapanel.setRowFactory(new CustomDataPanelPageLinkRowFactory<VrijVeld>(
			VrijVeldEditPage.class)
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void onClick(Item<VrijVeld> item)
			{
				setResponsePage(new VrijVeldEditPage(item.getModel(), VrijVeldZoekenPage.this));
			}
		});
		add(datapanel);

		VrijVeldZoekFilterPanel filterPanel =
			new VrijVeldZoekFilterPanel("filter", filter, datapanel);
		add(filterPanel);

		createComponents();
	}

	@Override
	protected void fillBottomRow(BottomRowPanel panel)
	{
		panel.addButton(new ToevoegenButton(panel, "Nieuwe vrij veld", VrijVeldEditPage.class,
			VrijVeldZoekenPage.this));
	}
}
