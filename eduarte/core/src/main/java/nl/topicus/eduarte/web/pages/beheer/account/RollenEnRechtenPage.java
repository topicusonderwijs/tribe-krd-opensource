package nl.topicus.eduarte.web.pages.beheer.account;

import java.util.List;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.dataproviders.GeneralFilteredSortableDataProvider;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.security.RechtenSoort;
import nl.topicus.cobra.web.components.datapanel.CollapsableGroupRowFactoryDecorator;
import nl.topicus.cobra.web.components.datapanel.CustomDataPanelPageLinkRowFactory;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.cobra.web.components.panels.bottomrow.ToevoegenButton;
import nl.topicus.eduarte.app.EduArteApp;
import nl.topicus.eduarte.core.principals.beheer.account.RightsRead;
import nl.topicus.eduarte.dao.helpers.RolDataAccessHelper;
import nl.topicus.eduarte.entities.security.authorization.AuthorisatieNiveau;
import nl.topicus.eduarte.entities.security.authorization.Rol;
import nl.topicus.eduarte.web.components.factory.NieuweRolButtonFactory;
import nl.topicus.eduarte.web.components.menu.BeheerMenuItem;
import nl.topicus.eduarte.web.components.panels.EduArteDataPanel;
import nl.topicus.eduarte.web.components.panels.datapanel.table.RolTable;
import nl.topicus.eduarte.web.components.panels.filter.RolZoekFilterPanel;
import nl.topicus.eduarte.web.pages.beheer.AbstractBeheerPage;
import nl.topicus.eduarte.zoekfilters.RolZoekFilter;

import org.apache.wicket.Page;
import org.apache.wicket.markup.html.link.IPageLink;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.IDataProvider;

/**
 * Pagina voor het maken en bewerken van rollen
 * 
 * @author papegaaij
 */
@PageInfo(title = "Rollen", menu = "Beheer > Accountbeheer > Rollen en rechten")
@InPrincipal(RightsRead.class)
public class RollenEnRechtenPage extends AbstractBeheerPage<Void>
{
	public RollenEnRechtenPage()
	{
		super(BeheerMenuItem.RollenEnRechten);

		RolZoekFilter filter = createDefaultZoekFilter();
		IDataProvider<Rol> dataProvider =
			GeneralFilteredSortableDataProvider.of(filter, RolDataAccessHelper.class);
		final EduArteDataPanel<Rol> datapanel =
			new EduArteDataPanel<Rol>("rollen", dataProvider, new RolTable());

		datapanel.setRowFactory(new CollapsableGroupRowFactoryDecorator<Rol>(
			new CustomDataPanelPageLinkRowFactory<Rol>(RolOverviewPage.class)
			{
				private static final long serialVersionUID = 1L;

				@Override
				protected void onClick(Item<Rol> item)
				{
					pushSearchResultToNavigationLevel(datapanel, item.getIndex());
					setResponsePage(new RolOverviewPage(item.getModelObject()));
				}
			}));
		add(datapanel);

		add(new RolZoekFilterPanel("filterPanel", filter, datapanel));

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
				Rol rol = new Rol();
				rol.setRechtenSoort(RechtenSoort.INSTELLING);
				rol.setAuthorisatieNiveau(AuthorisatieNiveau.REST);
				return new RolEditPage(rol);
			}

			@Override
			public Class< ? extends Page> getPageIdentity()
			{
				return RolEditPage.class;
			}
		}));

		List<NieuweRolButtonFactory> factories =
			EduArteApp.get().getPanelFactories(NieuweRolButtonFactory.class);
		for (NieuweRolButtonFactory factory : factories)
		{
			factory.createNieuweRolButton(panel);
		}
	}

	private RolZoekFilter createDefaultZoekFilter()
	{
		return new RolZoekFilter();
	}
}
