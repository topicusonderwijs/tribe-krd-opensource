package nl.topicus.eduarte.web.pages.beheer.contract;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.dataproviders.GeneralFilteredSortableDataProvider;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.web.components.datapanel.CustomDataPanelPageLinkRowFactory;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.cobra.web.components.shortcut.CobraKeyAction;
import nl.topicus.eduarte.core.principals.beheer.contract.ContractenRead;
import nl.topicus.eduarte.dao.helpers.ContractDataAccessHelper;
import nl.topicus.eduarte.entities.contract.Contract;
import nl.topicus.eduarte.web.components.menu.RelatieBeheerMenuItem;
import nl.topicus.eduarte.web.components.panels.EduArteDataPanel;
import nl.topicus.eduarte.web.components.panels.bottomrow.ModuleEditPageButton;
import nl.topicus.eduarte.web.components.panels.datapanel.table.ContractTable;
import nl.topicus.eduarte.web.components.panels.filter.ContractZoekFilterPanel;
import nl.topicus.eduarte.web.pages.beheer.relatie.AbstractRelatieBeheerPage;
import nl.topicus.eduarte.zoekfilters.ContractZoekFilter;

import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.IDataProvider;

@PageInfo(title = "Contracten", menu = "Relatie > Contracten")
@InPrincipal(ContractenRead.class)
public class ContractZoekenPage extends AbstractRelatieBeheerPage<Void>
{
	public ContractZoekenPage()
	{
		super(RelatieBeheerMenuItem.Contracten);

		ContractZoekFilter filter = new ContractZoekFilter();
		filter.addOrderByProperty("begindatum");
		filter.setAscending(false);

		IDataProvider<Contract> dataprovider =
			GeneralFilteredSortableDataProvider.of(filter, ContractDataAccessHelper.class);

		EduArteDataPanel<Contract> datapanel =
			new EduArteDataPanel<Contract>("datapanel", dataprovider, new ContractTable());
		datapanel.setRowFactory(new CustomDataPanelPageLinkRowFactory<Contract>(
			ContractOverzichtPage.class)
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void onClick(Item<Contract> item)
			{
				Contract contract = item.getModelObject();
				setResponsePage(new ContractOverzichtPage(contract));
			}
		});
		add(datapanel);

		ContractZoekFilterPanel filterPanel =
			new ContractZoekFilterPanel("filter", filter, datapanel);
		add(filterPanel);

		createComponents();
	}

	@Override
	protected void fillBottomRow(BottomRowPanel panel)
	{
		super.fillBottomRow(panel);
		panel.addButton(new ModuleEditPageButton<Contract>(panel, "Nieuw contract",
			CobraKeyAction.TOEVOEGEN, Contract.class, RelatieBeheerMenuItem.Contracten,
			ContractZoekenPage.this)
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected Contract getEntity()
			{
				return new Contract();
			}
		});
	}
}
