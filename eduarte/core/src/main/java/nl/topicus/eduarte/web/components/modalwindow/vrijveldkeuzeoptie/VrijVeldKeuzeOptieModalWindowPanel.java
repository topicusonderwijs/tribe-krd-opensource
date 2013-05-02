package nl.topicus.eduarte.web.components.modalwindow.vrijveldkeuzeoptie;

import nl.topicus.cobra.web.components.form.AutoFieldSet;
import nl.topicus.cobra.web.components.form.RenderMode;
import nl.topicus.cobra.web.components.modal.toevoegen.AbstractToevoegenBewerkenModalWindow;
import nl.topicus.cobra.web.components.modal.toevoegen.AbstractToevoegenBewerkenModalWindowPanel;
import nl.topicus.eduarte.entities.vrijevelden.VrijVeldKeuzeOptie;

/**
 * Panel voor het toevoegen en bewerken van een {@link VrijVeldKeuzeOptie}.
 * 
 * @author hoeve
 */
public class VrijVeldKeuzeOptieModalWindowPanel extends
		AbstractToevoegenBewerkenModalWindowPanel<VrijVeldKeuzeOptie>
{
	private static final long serialVersionUID = 1L;

	public VrijVeldKeuzeOptieModalWindowPanel(String id,
			AbstractToevoegenBewerkenModalWindow<VrijVeldKeuzeOptie> modalWindow,
			VrijVeldKeuzeOptieEditPanel vrijVeldKeuzeOptieEditPanel)
	{
		super(id, modalWindow, vrijVeldKeuzeOptieEditPanel);

		AutoFieldSet<VrijVeldKeuzeOptie> fieldSet =
			new AutoFieldSet<VrijVeldKeuzeOptie>("fieldset", modalWindow.getModel());
		fieldSet.setPropertyNames("naam", "actief");
		fieldSet.setRenderMode(RenderMode.EDIT);
		fieldSet.setSortAccordingToPropertyNames(true);
		getFormContainer().add(fieldSet);
		modalWindow.setInitialHeight(200);
		modalWindow.setInitialWidth(475);

		createComponents();
	}
}
