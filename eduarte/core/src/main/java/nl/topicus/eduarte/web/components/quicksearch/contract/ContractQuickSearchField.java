package nl.topicus.eduarte.web.components.quicksearch.contract;

import nl.topicus.cobra.util.ComponentUtil;
import nl.topicus.cobra.web.components.quicksearch.IdObjectRenderer;
import nl.topicus.cobra.web.components.quicksearch.QuickSearchModel;
import nl.topicus.cobra.web.components.wiquery.auto.QuickSearchField;
import nl.topicus.eduarte.dao.helpers.ContractDataAccessHelper;
import nl.topicus.eduarte.entities.contract.Contract;
import nl.topicus.eduarte.zoekfilters.ContractZoekFilter;

import org.apache.wicket.model.IModel;

public class ContractQuickSearchField extends QuickSearchField<Contract>
{
	private static final long serialVersionUID = 1L;

	private ContractZoekFilter filter;

	public ContractQuickSearchField(String id, IModel<Contract> model, ContractZoekFilter filter)
	{
		super(id, model, QuickSearchModel.of(ContractDataAccessHelper.class, filter),
			new IdObjectRenderer<Contract>());
		this.filter = filter;
	}

	@Override
	protected void onDetach()
	{
		super.onDetach();
		ComponentUtil.detachQuietly(filter);
	}

	@Override
	public Integer getWidth()
	{
		return 300;
	}
}
