package nl.topicus.eduarte.web.components.modalwindow.schaalwaarde;

import java.util.List;

import nl.topicus.cobra.modelsv2.ModelManager;
import nl.topicus.cobra.web.components.datapanel.CustomDataPanelContentDescription;
import nl.topicus.cobra.web.components.modal.toevoegen.AbstractToevoegenBewerkenModalWindow;
import nl.topicus.cobra.web.components.modal.toevoegen.AbstractToevoegenBewerkenModalWindowPanel;
import nl.topicus.eduarte.entities.resultaatstructuur.Schaalwaarde;
import nl.topicus.eduarte.web.components.modalwindow.AbstractEduArteToevoegenBewerkenPanel;

import org.apache.wicket.model.IModel;

public abstract class SchaalwaardeEditPanel extends
		AbstractEduArteToevoegenBewerkenPanel<Schaalwaarde>
{
	private static final long serialVersionUID = 1L;

	public SchaalwaardeEditPanel(String id, IModel<List<Schaalwaarde>> model, ModelManager manager,
			CustomDataPanelContentDescription<Schaalwaarde> table)
	{
		super(id, model, manager, table);
	}

	@Override
	public AbstractToevoegenBewerkenModalWindowPanel<Schaalwaarde> createModalWindowPanel(
			String id, AbstractToevoegenBewerkenModalWindow<Schaalwaarde> modalWindow)
	{
		return new SchaalwaardeModalWindowPanel(id, modalWindow, this);
	}

	@Override
	public boolean isDeletable(Schaalwaarde object)
	{
		return super.isDeletable(object) && !object.isInGebruik();
	}

	@Override
	public OpslaanButtonType getOpslaanButtonType()
	{
		return OpslaanButtonType.OpslaanEnToevoegen;
	}

	@Override
	public String getToevoegenLabel()
	{
		return "Schaalwaarde toevoegen";
	}

	@Override
	public String getModalWindowTitle()
	{
		return "Schaalwaarde";
	}
}
