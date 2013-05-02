package nl.topicus.eduarte.web.components.modalwindow.bpvcriteria;

import java.util.List;

import nl.topicus.cobra.modelsv2.ModelManager;
import nl.topicus.cobra.web.components.datapanel.CustomDataPanelContentDescription;
import nl.topicus.cobra.web.components.modal.toevoegen.AbstractToevoegenBewerkenModalWindow;
import nl.topicus.cobra.web.components.modal.toevoegen.AbstractToevoegenBewerkenModalWindowPanel;
import nl.topicus.eduarte.entities.bpv.BPVCriteriaExterneOrganisatie;
import nl.topicus.eduarte.web.components.modalwindow.AbstractEduArteToevoegenBewerkenPanel;

import org.apache.wicket.model.IModel;

public abstract class BPVCriteriaExterneOrganisatieEditPanel extends
		AbstractEduArteToevoegenBewerkenPanel<BPVCriteriaExterneOrganisatie>
{
	private static final long serialVersionUID = 1L;

	public BPVCriteriaExterneOrganisatieEditPanel(String id,
			IModel<List<BPVCriteriaExterneOrganisatie>> model, ModelManager manager,
			CustomDataPanelContentDescription<BPVCriteriaExterneOrganisatie> table)
	{
		super(id, model, manager, table);
	}

	@Override
	public AbstractToevoegenBewerkenModalWindowPanel<BPVCriteriaExterneOrganisatie> createModalWindowPanel(
			String id,
			AbstractToevoegenBewerkenModalWindow<BPVCriteriaExterneOrganisatie> modalWindow)
	{
		modalWindow.setInitialHeight(300);
		modalWindow.setInitialWidth(650);

		return new BPVCriteriaExterneOrganisatieModalWindowPanel(id, modalWindow, this);
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
	public boolean isDeletable(BPVCriteriaExterneOrganisatie gegeven)
	{
		return !gegeven.isInGebruik();
	}
}
