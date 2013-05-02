package nl.topicus.eduarte.web.components.modalwindow.opleiding;

import java.util.List;

import nl.topicus.cobra.modelsv2.ModelManager;
import nl.topicus.cobra.web.components.datapanel.CustomDataPanelContentDescription;
import nl.topicus.cobra.web.components.modal.toevoegen.AbstractToevoegenBewerkenModalWindow;
import nl.topicus.cobra.web.components.modal.toevoegen.AbstractToevoegenBewerkenModalWindowPanel;
import nl.topicus.eduarte.entities.curriculum.CurriculumOnderwijsproduct;
import nl.topicus.eduarte.web.components.modalwindow.AbstractEduArteToevoegenBewerkenPanel;

import org.apache.wicket.model.IModel;

public abstract class CurriculumOnderwijsproductEditPanel extends
		AbstractEduArteToevoegenBewerkenPanel<CurriculumOnderwijsproduct>
{
	private static final long serialVersionUID = 1L;

	public CurriculumOnderwijsproductEditPanel(String id,
			IModel<List<CurriculumOnderwijsproduct>> model, ModelManager manager,
			CustomDataPanelContentDescription<CurriculumOnderwijsproduct> table)
	{
		super(id, model, manager, table);
	}

	@Override
	public AbstractToevoegenBewerkenModalWindowPanel<CurriculumOnderwijsproduct> createModalWindowPanel(
			String id, AbstractToevoegenBewerkenModalWindow<CurriculumOnderwijsproduct> modalWindow)
	{
		modalWindow.setTitle("Curriculum bewerken");
		modalWindow.setInitialHeight(260);
		modalWindow.setInitialWidth(500);

		return new CurriculumOnderwijsproductModalWindowPanel(id, modalWindow, this);
	}

	@Override
	public String getToevoegenLabel()
	{
		return "Onderwijsproduct toevoegen";
	}

	@Override
	public String getModalWindowTitle()
	{
		return "Curriculum";
	}
}
