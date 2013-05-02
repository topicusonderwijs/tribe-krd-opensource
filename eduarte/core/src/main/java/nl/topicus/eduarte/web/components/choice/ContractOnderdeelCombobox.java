package nl.topicus.eduarte.web.components.choice;

import java.util.Collections;
import java.util.List;

import nl.topicus.cobra.util.ComponentUtil;
import nl.topicus.cobra.web.components.choice.AbstractAjaxDropDownChoice;
import nl.topicus.eduarte.entities.contract.Contract;
import nl.topicus.eduarte.entities.contract.ContractOnderdeel;
import nl.topicus.eduarte.web.components.choice.renderer.EntiteitToStringRenderer;

import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;

/**
 * Combobox voor contract onderdelen, welke een listmodel nodig heeft om de opties te
 * renderen.
 * 
 * @author hoeve
 */
public class ContractOnderdeelCombobox extends AbstractAjaxDropDownChoice<ContractOnderdeel>
{
	private static final long serialVersionUID = 1L;

	public static final class ContractOnderdelenListModel extends
			LoadableDetachableModel<List<ContractOnderdeel>>
	{
		private static final long serialVersionUID = 1L;

		private final IModel<Contract> contractModel;

		public ContractOnderdelenListModel(IModel<Contract> contractModel)
		{
			this.contractModel = contractModel;
		}

		@Override
		protected List<ContractOnderdeel> load()
		{
			if (getContract() == null)
				return Collections.emptyList();
			return getContract().getContractOnderdelen();
		}

		private Contract getContract()
		{
			return contractModel.getObject();
		}

		@Override
		protected void onDetach()
		{
			super.onDetach();
			ComponentUtil.detachQuietly(contractModel);
		}

	}

	public ContractOnderdeelCombobox(String id,
			IModel< ? extends List< ? extends ContractOnderdeel>> listModel)
	{
		super(id, null, listModel, new EntiteitToStringRenderer());
	}

	public ContractOnderdeelCombobox(String id, IModel<ContractOnderdeel> model,
			IModel< ? extends List< ? extends ContractOnderdeel>> listModel)
	{
		super(id, model, listModel, new EntiteitToStringRenderer());
	}
}
