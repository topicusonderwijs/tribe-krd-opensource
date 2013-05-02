package nl.topicus.eduarte.web.components.modalwindow.contract;

import nl.topicus.cobra.util.ComponentUtil;
import nl.topicus.cobra.web.components.modal.CobraModalWindowBasePanel;
import nl.topicus.eduarte.entities.contract.Contract;
import nl.topicus.eduarte.web.components.modalwindow.AbstractZoekenModalWindow;
import nl.topicus.eduarte.zoekfilters.ContractZoekFilter;

import org.apache.wicket.model.IModel;

public class ContractSelectieModalWindow extends AbstractZoekenModalWindow<Contract>
{
	private static final long serialVersionUID = 1L;

	private ContractZoekFilter filter;

	public ContractSelectieModalWindow(String id)
	{
		this(id, null, null);
	}

	public ContractSelectieModalWindow(String id, IModel<Contract> model)
	{
		this(id, model, null);
	}

	public ContractSelectieModalWindow(String id, IModel<Contract> model, ContractZoekFilter filter)
	{
		super(id, model, filter);
		this.filter = filter;
		setTitle("Contract selecteren");
	}

	@Override
	protected CobraModalWindowBasePanel<Contract> createContents(String id)
	{
		return new ContractSelectiePanel(id, this, filter);
	}

	@Override
	protected void onDetach()
	{
		super.onDetach();
		ComponentUtil.detachQuietly(filter);
	}
}
