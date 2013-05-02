package nl.topicus.eduarte.web.pages.home;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.dataproviders.GeneralFilteredSortableDataProvider;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.web.components.datapanel.CustomDataPanelAjaxClickableRowFactory;
import nl.topicus.eduarte.app.EduArteContext;
import nl.topicus.eduarte.core.principals.Always;
import nl.topicus.eduarte.dao.helpers.ContractVerplichtingDataAccesHelper;
import nl.topicus.eduarte.entities.contract.ContractVerplichting;
import nl.topicus.eduarte.web.components.modalwindow.contract.MyContractVerplichtingEditPanel;
import nl.topicus.eduarte.web.components.panels.EduArteDataPanel;
import nl.topicus.eduarte.web.components.panels.datapanel.table.MyContractVerplichtingTable;
import nl.topicus.eduarte.web.components.panels.filter.MyContractVerplichtingenZoekFilterPanel;
import nl.topicus.eduarte.zoekfilters.ContractVerplichtingZoekFilter;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.IModel;

/**
 * 
 * 
 * @author vanharen
 */
@PageInfo(title = "Medewerker Contract Verplichtingen", menu = "Home > Te Doen > Contract Verplichtingen")
@InPrincipal(Always.class)
public class MyContractVerplichtingenPage extends AbstractHomePage<Void>
{
	private static final long serialVersionUID = 1L;

	private EduArteDataPanel<ContractVerplichting> dataPanel;

	private ContractVerplichtingZoekFilter filter;

	private IModel<ContractVerplichting> contractVerplichtingModel;

	private MyContractVerplichtingEditPanel window;

	private static final ContractVerplichtingZoekFilter getDefaultFilter()
	{
		ContractVerplichtingZoekFilter filter = new ContractVerplichtingZoekFilter();

		filter.setMedewerker(EduArteContext.get().getMedewerker());
		filter.addOrderByProperty("begindatum");

		return filter;
	}

	public MyContractVerplichtingenPage()
	{
		this(getDefaultFilter());
	}

	public MyContractVerplichtingenPage(ContractVerplichtingZoekFilter filter)
	{
		setFilter(filter);

		IDataProvider<ContractVerplichting> provider =
			GeneralFilteredSortableDataProvider.of(filter,
				ContractVerplichtingDataAccesHelper.class);

		dataPanel =
			new EduArteDataPanel<ContractVerplichting>("contractverplichtingen", provider,
				new MyContractVerplichtingTable());
		window =
			new MyContractVerplichtingEditPanel("window", contractVerplichtingModel, dataPanel);
		add(window);
		dataPanel.setRowFactory(new CustomDataPanelAjaxClickableRowFactory<ContractVerplichting>()
		{

			private static final long serialVersionUID = 1L;

			@Override
			protected void onClick(AjaxRequestTarget target, Item<ContractVerplichting> item)
			{
				window.setDefaultModel(item.getModel());
				window.show(target);
			}

		});

		dataPanel.setItemsPerPage(20);
		add(dataPanel);

		add(new MyContractVerplichtingenZoekFilterPanel("filter", filter, dataPanel));
		createComponents();
	}

	public ContractVerplichtingZoekFilter getFilter()
	{
		return filter;
	}

	public void setFilter(ContractVerplichtingZoekFilter filter)
	{
		this.filter = filter;
	}
}
