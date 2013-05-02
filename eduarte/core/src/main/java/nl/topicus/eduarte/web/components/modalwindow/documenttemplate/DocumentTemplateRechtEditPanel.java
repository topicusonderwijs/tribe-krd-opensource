package nl.topicus.eduarte.web.components.modalwindow.documenttemplate;

import java.util.List;

import nl.topicus.cobra.modelsv2.ModelManager;
import nl.topicus.cobra.web.components.datapanel.CustomDataPanelContentDescription;
import nl.topicus.cobra.web.components.modal.toevoegen.AbstractToevoegenBewerkenModalWindow;
import nl.topicus.cobra.web.components.modal.toevoegen.AbstractToevoegenBewerkenModalWindowPanel;
import nl.topicus.eduarte.entities.rapportage.DocumentTemplateRecht;
import nl.topicus.eduarte.web.components.modalwindow.AbstractEduArteToevoegenBewerkenPanel;

import org.apache.wicket.model.IModel;

public abstract class DocumentTemplateRechtEditPanel extends
		AbstractEduArteToevoegenBewerkenPanel<DocumentTemplateRecht>
{
	private static final long serialVersionUID = 1L;

	public DocumentTemplateRechtEditPanel(String id, IModel<List<DocumentTemplateRecht>> model,
			CustomDataPanelContentDescription<DocumentTemplateRecht> table, ModelManager manager)
	{
		super(id, model, manager, table);
	}

	@Override
	public AbstractToevoegenBewerkenModalWindowPanel<DocumentTemplateRecht> createModalWindowPanel(
			String id, AbstractToevoegenBewerkenModalWindow<DocumentTemplateRecht> modalWindow)
	{
		modalWindow.setInitialHeight(250);
		modalWindow.setInitialWidth(450);
		return new DocumentTemplateRechtModalWindowPanel(id, modalWindow, this);
	}

	@Override
	public String getToevoegenLabel()
	{
		return "Rol koppelen";
	}

	@Override
	public String getModalWindowTitle()
	{
		return "Publiceren voor rol";
	}
}
