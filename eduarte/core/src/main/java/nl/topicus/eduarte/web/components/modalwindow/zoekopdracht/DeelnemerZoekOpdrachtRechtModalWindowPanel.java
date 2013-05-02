package nl.topicus.eduarte.web.components.modalwindow.zoekopdracht;

import java.util.Arrays;

import nl.topicus.cobra.web.components.form.AutoFieldSet;
import nl.topicus.cobra.web.components.form.RenderMode;
import nl.topicus.cobra.web.components.modal.toevoegen.AbstractToevoegenBewerkenModalWindow;
import nl.topicus.cobra.web.components.modal.toevoegen.AbstractToevoegenBewerkenModalWindowPanel;
import nl.topicus.eduarte.entities.rapportage.DeelnemerZoekOpdrachtRecht;

public class DeelnemerZoekOpdrachtRechtModalWindowPanel extends
		AbstractToevoegenBewerkenModalWindowPanel<DeelnemerZoekOpdrachtRecht>
{
	private static final long serialVersionUID = 1L;

	private AutoFieldSet<DeelnemerZoekOpdrachtRecht> inputfields;

	public DeelnemerZoekOpdrachtRechtModalWindowPanel(String id,
			AbstractToevoegenBewerkenModalWindow<DeelnemerZoekOpdrachtRecht> modalWindow,
			DeelnemerZoekOpdrachtRechtEditPanel editPanel)
	{
		super(id, modalWindow, editPanel);

		inputfields =
			new AutoFieldSet<DeelnemerZoekOpdrachtRecht>("inputfields", modalWindow.getModel());
		inputfields.setOutputMarkupId(true);
		inputfields.setPropertyNames(Arrays.asList("rol"));
		inputfields.setRenderMode(RenderMode.EDIT);

		getFormContainer().add(inputfields);

		createComponents();
	}
}
