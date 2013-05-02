package nl.topicus.eduarte.web.components.quicksearch.contract;

import nl.topicus.cobra.util.ComponentUtil;
import nl.topicus.cobra.web.components.quicksearch.AbstractSearchEditor;
import nl.topicus.cobra.web.components.wiquery.auto.QuickSearchField;
import nl.topicus.cobra.zoekfilters.ZoekFilterCopyManager;
import nl.topicus.eduarte.entities.contract.Contract;
import nl.topicus.eduarte.web.components.modalwindow.AbstractZoekenModalWindow;
import nl.topicus.eduarte.web.components.modalwindow.contract.ContractSelectieModalWindow;
import nl.topicus.eduarte.zoekfilters.ContractZoekFilter;

import org.apache.wicket.model.IModel;

public class ContractSearchEditor extends AbstractSearchEditor<Contract>
{
	private static final long serialVersionUID = 1L;

	private static final ContractZoekFilter getDefaultFilter()
	{
		ContractZoekFilter filter = new ContractZoekFilter();
		filter.addOrderByProperty("naam");
		return filter;
	}

	private ContractZoekFilter filter;

	public ContractSearchEditor(String id, IModel<Contract> model)
	{
		this(id, model, getDefaultFilter());
	}

	public ContractSearchEditor(String id, IModel<Contract> model, ContractZoekFilter filter)
	{
		super(id, model);
		this.filter = filter;
	}

	@Override
	public AbstractZoekenModalWindow<Contract> createModelWindow(String id, IModel<Contract> model)
	{
		return new ContractSelectieModalWindow(id, model, filter);
	}

	@Override
	public QuickSearchField<Contract> createSearchField(String id, IModel<Contract> model)
	{
		return new ContractQuickSearchField(id, model, new ZoekFilterCopyManager()
			.copyObject(filter));
	}

	@Override
	protected void onDetach()
	{
		super.onDetach();
		ComponentUtil.detachQuietly(filter);
	}
}
