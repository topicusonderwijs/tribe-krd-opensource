package nl.topicus.eduarte.krd.web.components.modalwindow.bpvbedrijf;

import java.util.List;

import nl.topicus.cobra.modelsv2.ModelManager;
import nl.topicus.cobra.web.components.datapanel.CustomDataPanelContentDescription;
import nl.topicus.cobra.web.components.modal.toevoegen.AbstractToevoegenBewerkenModalWindow;
import nl.topicus.cobra.web.components.modal.toevoegen.AbstractToevoegenBewerkenModalWindowPanel;
import nl.topicus.eduarte.entities.bpv.BPVBedrijfsgegeven;
import nl.topicus.eduarte.web.components.modalwindow.AbstractEduArteToevoegenBewerkenPanel;

import org.apache.wicket.model.IModel;

public abstract class BPVBedrijfsgegevenEditPanel extends
		AbstractEduArteToevoegenBewerkenPanel<BPVBedrijfsgegeven>
{
	private static final long serialVersionUID = 1L;

	public BPVBedrijfsgegevenEditPanel(String id, IModel<List<BPVBedrijfsgegeven>> model,
			ModelManager manager, CustomDataPanelContentDescription<BPVBedrijfsgegeven> table)
	{
		super(id, model, manager, table);
	}

	@Override
	public AbstractToevoegenBewerkenModalWindowPanel<BPVBedrijfsgegeven> createModalWindowPanel(
			String id, AbstractToevoegenBewerkenModalWindow<BPVBedrijfsgegeven> modalWindow)
	{
		modalWindow.setInitialHeight(240);
		modalWindow.setInitialWidth(650);

		return new BPVBedrijfsgegevenModalWindowPanel(id, modalWindow, this);
	}

	@Override
	public String getToevoegenLabel()
	{
		return "BPV-bedrijfsgegeven toevoegen";
	}

	@Override
	public String getModalWindowTitle()
	{
		return "BPV-bedrijfsgegeven";
	}

	@Override
	public boolean isDeletable(BPVBedrijfsgegeven gegeven)
	{
		return !gegeven.isInGebruik();
	}
}
