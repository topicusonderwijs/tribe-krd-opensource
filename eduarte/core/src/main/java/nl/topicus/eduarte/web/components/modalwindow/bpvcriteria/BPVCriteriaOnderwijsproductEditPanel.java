package nl.topicus.eduarte.web.components.modalwindow.bpvcriteria;

import java.util.List;

import nl.topicus.cobra.modelsv2.ModelManager;
import nl.topicus.cobra.web.components.datapanel.CustomDataPanelContentDescription;
import nl.topicus.cobra.web.components.modal.toevoegen.AbstractToevoegenBewerkenModalWindow;
import nl.topicus.cobra.web.components.modal.toevoegen.AbstractToevoegenBewerkenModalWindowPanel;
import nl.topicus.eduarte.entities.bpv.BPVCriteriaOnderwijsproduct;
import nl.topicus.eduarte.web.components.modalwindow.AbstractEduArteToevoegenBewerkenPanel;

import org.apache.wicket.model.IModel;

public abstract class BPVCriteriaOnderwijsproductEditPanel extends
		AbstractEduArteToevoegenBewerkenPanel<BPVCriteriaOnderwijsproduct>
{
	private static final long serialVersionUID = 1L;

	public BPVCriteriaOnderwijsproductEditPanel(String id,
			IModel<List<BPVCriteriaOnderwijsproduct>> model, ModelManager manager,
			CustomDataPanelContentDescription<BPVCriteriaOnderwijsproduct> table)
	{
		super(id, model, manager, table);
	}

	@Override
	public AbstractToevoegenBewerkenModalWindowPanel<BPVCriteriaOnderwijsproduct> createModalWindowPanel(
			String id, AbstractToevoegenBewerkenModalWindow<BPVCriteriaOnderwijsproduct> modalWindow)
	{
		modalWindow.setInitialHeight(300);
		modalWindow.setInitialWidth(650);

		return new BPVCriteriaOnderwijsproductModalWindowPanel(id, modalWindow, this);
	}

	@Override
	public String getToevoegenLabel()
	{
		return "Criteria toevoegen";
	}

	@Override
	public String getModalWindowTitle()
	{
		return "Criteria";
	}

	@Override
	public boolean isDeletable(BPVCriteriaOnderwijsproduct gegeven)
	{
		return !gegeven.isInGebruik();
	}
}
