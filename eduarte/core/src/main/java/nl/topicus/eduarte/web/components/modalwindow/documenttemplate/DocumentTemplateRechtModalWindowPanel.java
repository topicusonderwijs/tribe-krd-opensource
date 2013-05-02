package nl.topicus.eduarte.web.components.modalwindow.documenttemplate;

import java.util.Arrays;

import nl.topicus.cobra.web.components.form.AutoFieldSet;
import nl.topicus.cobra.web.components.form.RenderMode;
import nl.topicus.cobra.web.components.modal.toevoegen.AbstractToevoegenBewerkenModalWindow;
import nl.topicus.cobra.web.components.modal.toevoegen.AbstractToevoegenBewerkenModalWindowPanel;
import nl.topicus.eduarte.entities.rapportage.DocumentTemplateRecht;

public class DocumentTemplateRechtModalWindowPanel extends
		AbstractToevoegenBewerkenModalWindowPanel<DocumentTemplateRecht>
{
	private static final long serialVersionUID = 1L;

	private AutoFieldSet<DocumentTemplateRecht> inputfields;

	public DocumentTemplateRechtModalWindowPanel(String id,
			AbstractToevoegenBewerkenModalWindow<DocumentTemplateRecht> modalWindow,
			DocumentTemplateRechtEditPanel editPanel)
	{
		super(id, modalWindow, editPanel);

		inputfields =
			new AutoFieldSet<DocumentTemplateRecht>("inputfields", modalWindow.getModel());
		inputfields.setOutputMarkupId(true);
		inputfields.setPropertyNames(Arrays.asList("rol", "actionClass"));
		inputfields.setRenderMode(RenderMode.EDIT);

		getFormContainer().add(inputfields);

		createComponents();
	}
}
