package nl.topicus.eduarte.web.components.choice.multiple;

import java.util.Collection;
import java.util.List;

import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.web.components.choice.render.ToStringRenderer;
import nl.topicus.eduarte.dao.helpers.ContractDataAccessHelper;
import nl.topicus.eduarte.entities.contract.Contract;
import nl.topicus.eduarte.zoekfilters.ContractZoekFilter;

import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;

public class ContractMultipleChoice extends UitgebreidZoekMultipleChoice<Contract>
{
	private static final long serialVersionUID = 1L;

	private static final class ListModel extends LoadableDetachableModel<List<Contract>>
	{
		private static final long serialVersionUID = 1L;

		@Override
		protected List<Contract> load()
		{
			ContractZoekFilter filter = new ContractZoekFilter();
			filter.addOrderByProperty("naam");
			return DataAccessRegistry.getHelper(ContractDataAccessHelper.class).list(filter);
		}
	}

	public ContractMultipleChoice(String id, IModel<Collection<Contract>> model)
	{
		super(id, model, new ListModel(), new ToStringRenderer());
	}

}
