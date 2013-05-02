package nl.topicus.eduarte.krd.web.components.modalwindow.contract.contractverplichting;

import java.util.List;

import nl.topicus.cobra.modelsv2.ExtendedModel;
import nl.topicus.cobra.web.components.datapanel.CustomDataPanelContentDescription;
import nl.topicus.cobra.web.components.modal.toevoegen.AbstractToevoegenBewerkenModalWindow;
import nl.topicus.cobra.web.components.modal.toevoegen.AbstractToevoegenBewerkenModalWindowPanel;
import nl.topicus.eduarte.entities.contract.Contract;
import nl.topicus.eduarte.entities.contract.ContractVerplichting;
import nl.topicus.eduarte.web.components.modalwindow.AbstractEduArteToevoegenBewerkenPanel;

import org.apache.wicket.model.IModel;

public abstract class ContractVerplichtingEditPanel extends
		AbstractEduArteToevoegenBewerkenPanel<ContractVerplichting>
{
	private static final long serialVersionUID = 1L;

	private IModel<Contract> contractModel;

	public ContractVerplichtingEditPanel(String id, IModel<List<ContractVerplichting>> model,
			CustomDataPanelContentDescription<ContractVerplichting> table,
			ExtendedModel<Contract> contractModel)
	{
		super(id, model, contractModel.getManager(), table);
		this.contractModel = contractModel;
	}

	@Override
	public AbstractToevoegenBewerkenModalWindowPanel<ContractVerplichting> createModalWindowPanel(
			String id, AbstractToevoegenBewerkenModalWindow<ContractVerplichting> modalWindow)
	{
		modalWindow.setInitialHeight(250);
		modalWindow.setInitialWidth(450);
		return new ContractVerplichtingModalWindowPanel(id, modalWindow, this, contractModel);

	}

	@Override
	public OpslaanButtonType getOpslaanButtonType()
	{
		return OpslaanButtonType.OpslaanEnToevoegen;
	}

	@Override
	public String getToevoegenLabel()
	{
		return "Contractverplichting toevoegen";
	}

	@Override
	public String getModalWindowTitle()
	{
		return "Contractverplichting";
	}
}
