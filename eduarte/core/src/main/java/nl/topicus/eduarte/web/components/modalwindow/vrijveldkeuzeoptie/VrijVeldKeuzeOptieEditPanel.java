package nl.topicus.eduarte.web.components.modalwindow.vrijveldkeuzeoptie;

import java.util.List;

import nl.topicus.cobra.modelsv2.ModelManager;
import nl.topicus.cobra.web.components.datapanel.CustomDataPanelContentDescription;
import nl.topicus.cobra.web.components.modal.toevoegen.AbstractToevoegenBewerkenModalWindow;
import nl.topicus.cobra.web.components.modal.toevoegen.AbstractToevoegenBewerkenModalWindowPanel;
import nl.topicus.eduarte.entities.vrijevelden.VrijVeldKeuzeOptie;
import nl.topicus.eduarte.web.components.modalwindow.AbstractEduArteToevoegenBewerkenPanel;

import org.apache.wicket.model.IModel;

public abstract class VrijVeldKeuzeOptieEditPanel extends
		AbstractEduArteToevoegenBewerkenPanel<VrijVeldKeuzeOptie>
{
	private static final long serialVersionUID = 1L;

	public VrijVeldKeuzeOptieEditPanel(String id, IModel<List<VrijVeldKeuzeOptie>> model,
			ModelManager manager, CustomDataPanelContentDescription<VrijVeldKeuzeOptie> table)
	{
		super(id, model, manager, table);
	}

	@Override
	public AbstractToevoegenBewerkenModalWindowPanel<VrijVeldKeuzeOptie> createModalWindowPanel(
			String id, AbstractToevoegenBewerkenModalWindow<VrijVeldKeuzeOptie> modalWindow)
	{
		return new VrijVeldKeuzeOptieModalWindowPanel(id, modalWindow, this);
	}

	@Override
	public OpslaanButtonType getOpslaanButtonType()
	{
		return OpslaanButtonType.OpslaanEnToevoegen;
	}

	@Override
	public String getToevoegenLabel()
	{
		return "Keuzeoptie toevoegen";
	}

	@Override
	public String getModalWindowTitle()
	{
		return "Keuzeoptie";
	}
}
