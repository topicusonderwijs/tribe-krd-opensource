package nl.topicus.eduarte.web.pages.beheer.account;

import java.util.Collections;
import java.util.List;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.dataproviders.SortableListModelDataProvider;
import nl.topicus.cobra.modelsv2.ModelFactory;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.cobra.web.components.panels.bottomrow.TerugButton;
import nl.topicus.eduarte.app.EduArteApp;
import nl.topicus.eduarte.app.security.PrincipalGroup;
import nl.topicus.eduarte.core.principals.beheer.account.AccountsRead;
import nl.topicus.eduarte.entities.security.authentication.Account;
import nl.topicus.eduarte.web.components.menu.BeheerMenuItem;
import nl.topicus.eduarte.web.components.panels.EduArteDataPanel;
import nl.topicus.eduarte.web.components.panels.datapanel.table.RechtenTable;
import nl.topicus.eduarte.web.pages.beheer.AbstractBeheerPage;

import org.apache.wicket.Page;
import org.apache.wicket.markup.html.link.IPageLink;
import org.apache.wicket.model.util.ListModel;

@InPrincipal(AccountsRead.class)
@PageInfo(title = "Rechten van de gebruiker", menu = "Beheer > Gebruikers > [Account] > Toon rechten")
public class ToonRechtenPage extends AbstractBeheerPage<Account>
{
	public ToonRechtenPage(Account account)
	{
		super(ModelFactory.getModel(account), BeheerMenuItem.Gebruikers);

		List<PrincipalGroup> groups = EduArteApp.get().getPrincipalGroups();
		Collections.sort(groups);

		EduArteDataPanel<PrincipalGroup> datapanel =
			new EduArteDataPanel<PrincipalGroup>("rechten",
				new SortableListModelDataProvider<PrincipalGroup>(new ListModel<PrincipalGroup>(
					groups)), new RechtenTable(account));
		datapanel.getHeaderToolbars().add(0, new ActionToolbar(datapanel));
		datapanel.setSelecteerKolommenButtonVisible(false);

		add(datapanel);
		createComponents();
	}

	@Override
	protected void fillBottomRow(BottomRowPanel panel)
	{
		panel.addButton(new TerugButton(panel, new IPageLink()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public Class< ? extends Page> getPageIdentity()
			{
				return AccountOverviewPage.class;
			}

			@Override
			public Page getPage()
			{
				return new AccountOverviewPage(getAccount());
			}
		}));
	}

	@Override
	public void getBookmarkConstructorArguments(List<Class< ? >> ctorArgTypes,
			List<Object> ctorArgValues)
	{
		super.getBookmarkConstructorArguments(ctorArgTypes, ctorArgValues);
		ctorArgTypes.add(Account.class);
		ctorArgValues.add(getDefaultModel());
	}

	@Override
	public String getContextOmschrijving()
	{
		return "Rechten voor " + getAccount().getGebruikersnaam();
	}

	private Account getAccount()
	{
		return getContextModelObject();
	}
}
