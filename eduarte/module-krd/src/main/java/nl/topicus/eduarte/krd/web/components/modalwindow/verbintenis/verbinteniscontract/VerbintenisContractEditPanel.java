package nl.topicus.eduarte.krd.web.components.modalwindow.verbintenis.verbinteniscontract;

import java.util.List;

import nl.topicus.cobra.modelsv2.ModelManager;
import nl.topicus.cobra.web.components.datapanel.CustomDataPanelContentDescription;
import nl.topicus.cobra.web.components.modal.toevoegen.AbstractToevoegenBewerkenModalWindow;
import nl.topicus.cobra.web.components.modal.toevoegen.AbstractToevoegenBewerkenModalWindowPanel;
import nl.topicus.eduarte.entities.inschrijving.VerbintenisContract;
import nl.topicus.eduarte.web.components.modalwindow.AbstractEduArteToevoegenBewerkenPanel;

import org.apache.wicket.model.IModel;

public abstract class VerbintenisContractEditPanel extends
		AbstractEduArteToevoegenBewerkenPanel<VerbintenisContract>
{
	private static final long serialVersionUID = 1L;

	public VerbintenisContractEditPanel(String id, IModel<List<VerbintenisContract>> model,
			ModelManager manager, CustomDataPanelContentDescription<VerbintenisContract> table)
	{
		super(id, model, manager, table);
	}

	@Override
	public AbstractToevoegenBewerkenModalWindowPanel<VerbintenisContract> createModalWindowPanel(
			String id, AbstractToevoegenBewerkenModalWindow<VerbintenisContract> modalWindow)
	{
		modalWindow.setInitialHeight(250);
		modalWindow.setInitialWidth(450);
		return new VerbintenisContractModalWindowPanel(id, modalWindow, this);
	}

	@Override
	public String getToevoegenLabel()
	{
		return "Contract toevoegen";
	}

	@Override
	public String getModalWindowTitle()
	{
		return "Contract";
	}
}
