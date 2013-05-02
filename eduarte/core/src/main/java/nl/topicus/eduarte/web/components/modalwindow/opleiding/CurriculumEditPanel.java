package nl.topicus.eduarte.web.components.modalwindow.opleiding;

import java.util.List;

import nl.topicus.cobra.modelsv2.ModelManager;
import nl.topicus.cobra.web.components.datapanel.CustomDataPanelContentDescription;
import nl.topicus.cobra.web.components.modal.toevoegen.AbstractToevoegenBewerkenModalWindow;
import nl.topicus.cobra.web.components.modal.toevoegen.AbstractToevoegenBewerkenModalWindowPanel;
import nl.topicus.eduarte.entities.curriculum.Curriculum;
import nl.topicus.eduarte.web.components.modalwindow.AbstractEduArteToevoegenBewerkenPanel;

import org.apache.wicket.model.IModel;

public abstract class CurriculumEditPanel extends AbstractEduArteToevoegenBewerkenPanel<Curriculum>
{
	private static final long serialVersionUID = 1L;

	public CurriculumEditPanel(String id, IModel<List<Curriculum>> model, ModelManager manager,
			CustomDataPanelContentDescription<Curriculum> table)
	{
		super(id, model, manager, table);
	}

	@Override
	public AbstractToevoegenBewerkenModalWindowPanel<Curriculum> createModalWindowPanel(String id,
			AbstractToevoegenBewerkenModalWindow<Curriculum> modalWindow)
	{
		modalWindow.setTitle("Curriculum bewerken");
		modalWindow.setInitialHeight(260);
		modalWindow.setInitialWidth(500);

		return new CurriculumModalWindowPanel(id, modalWindow, this);
	}

	@Override
	public String getToevoegenLabel()
	{
		return "Cohort toevoegen";
	}

	@Override
	public String getModalWindowTitle()
	{
		return "Curriculum";
	}
}
