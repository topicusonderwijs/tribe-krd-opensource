package nl.topicus.eduarte.web.pages.onderwijs.opleiding;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.dataproviders.GeneralFilteredSortableDataProvider;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.web.components.datapanel.CustomDataPanelPageLinkRowFactory;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.cobra.web.components.panels.bottomrow.ToevoegenButton;
import nl.topicus.eduarte.core.principals.onderwijs.StandaardToetsfiltersBeheren;
import nl.topicus.eduarte.dao.helpers.StandaardToetsCodeFilterDataAccessHelper;
import nl.topicus.eduarte.entities.landelijk.Cohort;
import nl.topicus.eduarte.entities.opleiding.Opleiding;
import nl.topicus.eduarte.entities.resultaatstructuur.StandaardToetsCodeFilter;
import nl.topicus.eduarte.providers.OpleidingProvider;
import nl.topicus.eduarte.web.components.menu.OpleidingMenuItem;
import nl.topicus.eduarte.web.components.panels.EduArteDataPanel;
import nl.topicus.eduarte.web.components.panels.datapanel.table.StandaardToetsCodeFilterTable;
import nl.topicus.eduarte.zoekfilters.StandaardToetsCodeFilterZoekFilter;

import org.apache.wicket.Page;
import org.apache.wicket.markup.html.link.IPageLink;

/**
 * @author loite
 */
@PageInfo(title = "Opleiding Toetsfilters", menu = {"Onderwijs > [opleiding] > Toetsfilters"})
@InPrincipal(StandaardToetsfiltersBeheren.class)
public class OpleidingToetsfiltersOverzichtPage extends AbstractOpleidingPage
{
	private static final long serialVersionUID = 1L;

	public OpleidingToetsfiltersOverzichtPage(OpleidingProvider provider)
	{
		this(provider.getOpleiding());
	}

	public OpleidingToetsfiltersOverzichtPage(Opleiding opleiding)
	{
		super(OpleidingMenuItem.Toetsfilters, opleiding);

		StandaardToetsCodeFilterZoekFilter filter = new StandaardToetsCodeFilterZoekFilter();
		filter.setOpleiding(opleiding);
		filter.addOrderByProperty("cohort.naam");

		EduArteDataPanel<StandaardToetsCodeFilter> datapanel =
			new EduArteDataPanel<StandaardToetsCodeFilter>("datapanel",
				GeneralFilteredSortableDataProvider.of(filter,
					StandaardToetsCodeFilterDataAccessHelper.class),
				new StandaardToetsCodeFilterTable());
		datapanel.setRowFactory(new CustomDataPanelPageLinkRowFactory<StandaardToetsCodeFilter>(
			OpleidingToetsfilterEditPage.class));
		add(datapanel);

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
				StandaardToetsCodeFilter newFilter = new StandaardToetsCodeFilter();
				newFilter.setOpleiding(getContextOpleiding());
				newFilter.setCohort(Cohort.getHuidigCohort());
				return new OpleidingToetsfilterEditPage(newFilter);
			}

			@Override
			public Class< ? extends Page> getPageIdentity()
			{
				return OpleidingToetsfilterEditPage.class;
			}
		}));
	}
}
