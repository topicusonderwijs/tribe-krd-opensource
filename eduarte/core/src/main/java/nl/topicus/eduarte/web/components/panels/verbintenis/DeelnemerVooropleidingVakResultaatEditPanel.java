package nl.topicus.eduarte.web.components.panels.verbintenis;

import java.util.List;

import nl.topicus.cobra.modelsv2.ModelManager;
import nl.topicus.cobra.web.components.datapanel.CustomDataPanelContentDescription;
import nl.topicus.cobra.web.components.modal.toevoegen.AbstractToevoegenBewerkenModalWindow;
import nl.topicus.cobra.web.components.modal.toevoegen.AbstractToevoegenBewerkenModalWindowPanel;
import nl.topicus.eduarte.entities.hogeronderwijs.VooropleidingVakResultaat;
import nl.topicus.eduarte.web.components.modalwindow.AbstractEduArteToevoegenBewerkenPanel;
import nl.topicus.eduarte.web.components.modalwindow.verbintenis.DeelnemerVakResultaatModalWindowPanel;

import org.apache.wicket.model.IModel;

public abstract class DeelnemerVooropleidingVakResultaatEditPanel extends
		AbstractEduArteToevoegenBewerkenPanel<VooropleidingVakResultaat>
{
	private static final long serialVersionUID = 1L;

	public DeelnemerVooropleidingVakResultaatEditPanel(String id,
			IModel<List<VooropleidingVakResultaat>> model, ModelManager manager,
			CustomDataPanelContentDescription<VooropleidingVakResultaat> table)
	{
		super(id, model, manager, table);
	}

	@Override
	public AbstractToevoegenBewerkenModalWindowPanel<VooropleidingVakResultaat> createModalWindowPanel(
			String id, AbstractToevoegenBewerkenModalWindow<VooropleidingVakResultaat> modalWindow)
	{
		modalWindow.setTitle("Vakresultaat bewerken");
		modalWindow.setInitialHeight(260);
		modalWindow.setInitialWidth(500);
		return new DeelnemerVakResultaatModalWindowPanel(id, modalWindow, this);
	}

	@Override
	public String getToevoegenLabel()
	{
		return "Vakresultaat toevoegen";
	}

	@Override
	public String getModalWindowTitle()
	{
		return "Vakresultaat";
	}
}
