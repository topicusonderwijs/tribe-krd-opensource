package nl.topicus.eduarte.web.components.modalwindow.verbintenis;

import nl.topicus.cobra.web.components.form.AutoFieldSet;
import nl.topicus.cobra.web.components.form.RenderMode;
import nl.topicus.cobra.web.components.modal.toevoegen.AbstractToevoegenBewerkenModalWindow;
import nl.topicus.cobra.web.components.modal.toevoegen.AbstractToevoegenBewerkenModalWindowPanel;
import nl.topicus.eduarte.entities.hogeronderwijs.VooropleidingVakResultaat;
import nl.topicus.eduarte.web.components.panels.verbintenis.DeelnemerVooropleidingVakResultaatEditPanel;

public class DeelnemerVakResultaatModalWindowPanel extends
		AbstractToevoegenBewerkenModalWindowPanel<VooropleidingVakResultaat>
{
	private static final long serialVersionUID = 1L;

	private AutoFieldSet<VooropleidingVakResultaat> fieldset;

	public DeelnemerVakResultaatModalWindowPanel(String id,
			AbstractToevoegenBewerkenModalWindow<VooropleidingVakResultaat> modalWindow,
			DeelnemerVooropleidingVakResultaatEditPanel deelnemerVooropleidingVakResultaatEditPanel)
	{
		super(id, modalWindow, deelnemerVooropleidingVakResultaatEditPanel);
		modalWindow.setInitialHeight(300);

		fieldset =
			new AutoFieldSet<VooropleidingVakResultaat>("gegevensAutoFieldSet", modalWindow
				.getModel(), "Nieuw vakresultaat");

		fieldset.setRenderMode(RenderMode.EDIT);
		fieldset.setPropertyNames("vak.naam", "score", "status", "vak.code");
		fieldset.setSortAccordingToPropertyNames(true);

		getFormContainer().add(fieldset);

		createComponents();
	}
}