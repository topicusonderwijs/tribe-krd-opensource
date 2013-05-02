package nl.topicus.eduarte.web.components.modalwindow.contract;

import nl.topicus.cobra.web.components.datapanel.CustomDataPanel;
import nl.topicus.cobra.web.components.modal.CobraModalWindow;
import nl.topicus.eduarte.dao.helpers.ContractDataAccessHelper;
import nl.topicus.eduarte.entities.contract.Contract;
import nl.topicus.eduarte.web.components.modalwindow.AbstractZoekenPanel;
import nl.topicus.eduarte.web.components.panels.datapanel.table.ContractTable;
import nl.topicus.eduarte.web.components.panels.filter.CodeNaamZoekFilterPanel;
import nl.topicus.eduarte.zoekfilters.ContractZoekFilter;

import org.apache.wicket.Component;

public class ContractSelectiePanel extends AbstractZoekenPanel<Contract, ContractZoekFilter>
{
	private static final long serialVersionUID = 1L;

	private static final ContractZoekFilter getDefaultFilter()
	{
		ContractZoekFilter filter = new ContractZoekFilter();
		filter.addOrderByProperty("naam");
		return filter;
	}

	public ContractSelectiePanel(String id, CobraModalWindow<Contract> window,
			ContractZoekFilter filter)
	{
		super(id, window, filter == null ? getDefaultFilter() : filter,
			ContractDataAccessHelper.class, new ContractTable());
	}

	@Override
	protected Component createFilterPanel(String id, ContractZoekFilter filter,
			CustomDataPanel<Contract> datapanel)
	{
		return new CodeNaamZoekFilterPanel(id, filter, datapanel);
	}
}
