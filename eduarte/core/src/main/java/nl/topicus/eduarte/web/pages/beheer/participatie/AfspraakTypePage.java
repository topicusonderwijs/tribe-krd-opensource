package nl.topicus.eduarte.web.pages.beheer.participatie;

import java.awt.Color;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.dataproviders.GeneralDataProvider;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.web.components.datapanel.CustomDataPanelPageLinkRowFactory;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.cobra.web.components.panels.bottomrow.ToevoegenButton;
import nl.topicus.eduarte.core.principals.beheer.participatie.AfspraaktypesWrite;
import nl.topicus.eduarte.dao.participatie.helpers.AfspraakTypeDataAccessHelper;
import nl.topicus.eduarte.entities.participatie.AfspraakType;
import nl.topicus.eduarte.participatie.zoekfilters.AfspraakTypeZoekFilter;
import nl.topicus.eduarte.web.components.menu.BeheerMenuItem;
import nl.topicus.eduarte.web.components.panels.EduArteDataPanel;
import nl.topicus.eduarte.web.components.panels.datapanel.table.AfspraakTypeTable;
import nl.topicus.eduarte.web.components.panels.filter.AfspraakTypeZoekFilterPanel;
import nl.topicus.eduarte.web.pages.beheer.AbstractBeheerPage;
import nl.topicus.eduarte.zoekfilters.OrganisatieEenheidLocatieAuthorizationContext;

import org.apache.wicket.Page;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.link.IPageLink;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.IDataProvider;

/**
 * @author papegaaij
 */
@PageInfo(title = "Afspraaktypes", menu = "Beheer > Participatie > Afspraaktypes")
@InPrincipal(AfspraaktypesWrite.class)
public class AfspraakTypePage extends AbstractBeheerPage<Void>
{

	private static final long serialVersionUID = 1L;

	private final AfspraakTypeZoekFilterPanel filterPanel;

	private static AfspraakTypeZoekFilter getDefaultFilter()
	{
		AfspraakTypeZoekFilter filter = new AfspraakTypeZoekFilter();
		filter.addOrderByProperty("naam");
		return filter;
	}

	public AfspraakTypePage()
	{
		this(getDefaultFilter());
	}

	public AfspraakTypePage(AfspraakTypeZoekFilter filter)
	{
		super(BeheerMenuItem.AfspraakTypes);
		filter.setAuthorizationContext(new OrganisatieEenheidLocatieAuthorizationContext(this));
		IDataProvider<AfspraakType> provider =
			GeneralDataProvider.of(filter, AfspraakTypeDataAccessHelper.class);

		EduArteDataPanel<AfspraakType> datapanel =
			new EduArteDataPanel<AfspraakType>("datapanel", provider, new AfspraakTypeTable(filter));
		datapanel.setRowFactory(new CustomDataPanelPageLinkRowFactory<AfspraakType>(
			EditAfspraakTypePage.class)
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick(Item<AfspraakType> item)
			{
				AfspraakType afspraakType = item.getModelObject();
				setResponsePage(new EditAfspraakTypePage(afspraakType, filterPanel.getZoekfilter()));
			}
		});

		add(datapanel);
		filterPanel = new AfspraakTypeZoekFilterPanel("filter", filter, datapanel);
		add(filterPanel);
		createComponents();
	}

	public AfspraakTypeZoekFilter getAfspraakTypeZoekFilter()
	{
		return filterPanel.getZoekfilter();
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
				AfspraakTypeZoekFilter afspraakTypeFilter = getAfspraakTypeZoekFilter();
				AfspraakType newAfspraakType = new AfspraakType();
				newAfspraakType.setOrganisatieEenheid(afspraakTypeFilter.getOrganisatieEenheid());
				newAfspraakType.setStandaardKleurObj(new Color(getRandomColorComponent(),
					getRandomColorComponent(), getRandomColorComponent()));
				newAfspraakType.setActief(true);
				return new EditAfspraakTypePage(newAfspraakType, afspraakTypeFilter);
			}

			private float getRandomColorComponent()
			{
				return (float) (Math.random() * 0.6 + 0.3);
			}

			@Override
			public Class< ? extends WebPage> getPageIdentity()
			{
				return EditAfspraakTypePage.class;
			}
		}));
	}

}
