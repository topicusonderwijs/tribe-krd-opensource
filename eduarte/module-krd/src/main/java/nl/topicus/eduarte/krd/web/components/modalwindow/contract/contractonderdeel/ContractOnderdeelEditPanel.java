package nl.topicus.eduarte.krd.web.components.modalwindow.contract.contractonderdeel;

import java.util.List;

import nl.topicus.cobra.modelsv2.ExtendedModel;
import nl.topicus.cobra.web.components.datapanel.CustomDataPanelContentDescription;
import nl.topicus.cobra.web.components.modal.toevoegen.AbstractToevoegenBewerkenModalWindow;
import nl.topicus.cobra.web.components.modal.toevoegen.AbstractToevoegenBewerkenModalWindowPanel;
import nl.topicus.eduarte.entities.contract.Contract;
import nl.topicus.eduarte.entities.contract.ContractOnderdeel;
import nl.topicus.eduarte.web.components.modalwindow.AbstractEduArteToevoegenBewerkenPanel;

import org.apache.wicket.model.IModel;

public abstract class ContractOnderdeelEditPanel extends
		AbstractEduArteToevoegenBewerkenPanel<ContractOnderdeel>
{
	private static final long serialVersionUID = 1L;

	private IModel<Contract> contractModel;

	public ContractOnderdeelEditPanel(String id, IModel<List<ContractOnderdeel>> model,
			CustomDataPanelContentDescription<ContractOnderdeel> table,
			ExtendedModel<Contract> contractModel)
	{
		super(id, model, contractModel.getManager(), table);
		this.contractModel = contractModel;
	}

	@Override
	public AbstractToevoegenBewerkenModalWindowPanel<ContractOnderdeel> createModalWindowPanel(
			String id, AbstractToevoegenBewerkenModalWindow<ContractOnderdeel> modalWindow)
	{
		modalWindow.setInitialHeight(350);
		modalWindow.setInitialWidth(450);
		return new ContractOnderdeelModalWindowPanel(id, modalWindow, this, contractModel);
	}

	@Override
	public OpslaanButtonType getOpslaanButtonType()
	{
		return OpslaanButtonType.OpslaanEnToevoegen;
	}

	@Override
	public String getToevoegenLabel()
	{
		return "Contractonderdeel toevoegen";
	}

	@Override
	public String getModalWindowTitle()
	{
		return "Contractonderdeel";
	}
}
