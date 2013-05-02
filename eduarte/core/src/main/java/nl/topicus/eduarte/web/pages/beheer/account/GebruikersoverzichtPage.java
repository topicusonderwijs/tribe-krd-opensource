package nl.topicus.eduarte.web.pages.beheer.account;

import java.util.List;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.dataproviders.GeneralFilteredSortableDataProvider;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.web.components.datapanel.CustomDataPanelPageLinkRowFactory;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.cobra.web.components.panels.bottomrow.ButtonAlignment;
import nl.topicus.cobra.web.components.panels.bottomrow.PageLinkButton;
import nl.topicus.cobra.web.components.panels.bottomrow.ToevoegenButton;
import nl.topicus.eduarte.app.EduArteApp;
import nl.topicus.eduarte.app.EduArteContext;
import nl.topicus.eduarte.core.principals.beheer.account.AccountsRead;
import nl.topicus.eduarte.dao.helpers.AccountDataAccessHelper;
import nl.topicus.eduarte.entities.security.authentication.Account;
import nl.topicus.eduarte.entities.security.authentication.MedewerkerAccount;
import nl.topicus.eduarte.entities.security.authorization.AuthorisatieNiveau;
import nl.topicus.eduarte.web.components.factory.NieuweAccountButtonFactory;
import nl.topicus.eduarte.web.components.menu.BeheerMenuItem;
import nl.topicus.eduarte.web.components.panels.EduArteDataPanel;
import nl.topicus.eduarte.web.components.panels.datapanel.table.AccountTable;
import nl.topicus.eduarte.web.components.panels.filter.AccountZoekFilterPanel;
import nl.topicus.eduarte.web.pages.beheer.AbstractBeheerPage;
import nl.topicus.eduarte.web.pages.shared.AccountEditPage;
import nl.topicus.eduarte.zoekfilters.AccountZoekFilter;

import org.apache.wicket.Page;
import org.apache.wicket.markup.html.link.IPageLink;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.IDataProvider;

@PageInfo(title = "Gebruikers", menu = "Beheer > Gebruikers")
@InPrincipal(AccountsRead.class)
public class GebruikersoverzichtPage extends AbstractBeheerPage<Void>
{

	private static AccountZoekFilter getDefaultFilter()
	{
		AccountZoekFilter ret = new AccountZoekFilter();
		ret.setAuthorisatieNiveau(EduArteContext.get().getAccount().getAuthorisatieNiveau());
		ret.addOrderByProperty("gebruikersnaam");
		return ret;
	}

	public GebruikersoverzichtPage()
	{
		super(BeheerMenuItem.Gebruikers);

		AccountZoekFilter filter = getDefaultFilter();
		IDataProvider<Account> dataprovider =
			GeneralFilteredSortableDataProvider.of(filter, AccountDataAccessHelper.class);

		final EduArteDataPanel<Account> datapanel =
			new EduArteDataPanel<Account>("datapanel", dataprovider, new AccountTable());
		datapanel.setRowFactory(new CustomDataPanelPageLinkRowFactory<Account>(
			AccountOverviewPage.class)
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void onClick(Item<Account> item)
			{
				pushSearchResultToNavigationLevel(datapanel, item.getIndex());
				setResponsePage(new AccountOverviewPage(item.getModelObject()));
			}
		});
		add(datapanel);

		AccountZoekFilterPanel filterPanel =
			new AccountZoekFilterPanel("filter", filter, datapanel);
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
				MedewerkerAccount account = new MedewerkerAccount();
				account.setAuthorisatieNiveau(AuthorisatieNiveau.REST);
				account.setActief(true);
				return new AccountEditPage(GebruikersoverzichtPage.this, account);
			}

			@Override
			public Class< ? extends Page> getPageIdentity()
			{
				return AccountEditPage.class;
			}
		}));
		panel.addButton(new PageLinkButton(panel, "Accounts importeren",
			AccountsImporterenPage.class).setAlignment(ButtonAlignment.LEFT));
		List<NieuweAccountButtonFactory> factories =
			EduArteApp.get().getPanelFactories(NieuweAccountButtonFactory.class);
		for (NieuweAccountButtonFactory factory : factories)
		{
			factory.createNieuweAccountButton(panel, GebruikersoverzichtPage.this);
		}
	}
}
