package nl.topicus.eduarte.krd.web.components.modalwindow.bpvopmerking;

import java.util.List;

import nl.topicus.cobra.modelsv2.ModelManager;
import nl.topicus.cobra.web.components.datapanel.CustomDataPanelContentDescription;
import nl.topicus.cobra.web.components.modal.toevoegen.AbstractToevoegenBewerkenModalWindow;
import nl.topicus.cobra.web.components.modal.toevoegen.AbstractToevoegenBewerkenModalWindowPanel;
import nl.topicus.eduarte.entities.organisatie.ExterneOrganisatieOpmerking;
import nl.topicus.eduarte.web.components.modalwindow.AbstractEduArteToevoegenBewerkenPanel;

import org.apache.wicket.model.IModel;

public abstract class BPVOpmerkingEditPanel extends
		AbstractEduArteToevoegenBewerkenPanel<ExterneOrganisatieOpmerking>
{
	private static final long serialVersionUID = 1L;

	public BPVOpmerkingEditPanel(String id, IModel<List<ExterneOrganisatieOpmerking>> model,
			ModelManager manager,
			CustomDataPanelContentDescription<ExterneOrganisatieOpmerking> table)
	{
		super(id, model, manager, table);
	}

	@Override
	public AbstractToevoegenBewerkenModalWindowPanel<ExterneOrganisatieOpmerking> createModalWindowPanel(
			String id, AbstractToevoegenBewerkenModalWindow<ExterneOrganisatieOpmerking> modalWindow)
	{
		modalWindow.setInitialHeight(300);
		modalWindow.setInitialWidth(650);

		return new BPVOpmerkingModalWindowPanel(id, modalWindow, this);
	}

	@Override
	public String getToevoegenLabel()
	{
		return "BPV-opmerking toevoegen";
	}

	@Override
	public String getModalWindowTitle()
	{
		return "BPV-opmerking";
	}

	@Override
	public boolean isDeletable(ExterneOrganisatieOpmerking gegeven)
	{
		return !gegeven.isInGebruik();
	}
}
