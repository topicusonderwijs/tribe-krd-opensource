package nl.topicus.eduarte.web.components.modalwindow.bpvcriteria;

import nl.topicus.cobra.web.components.form.AutoFieldSet;
import nl.topicus.cobra.web.components.form.RenderMode;
import nl.topicus.cobra.web.components.modal.toevoegen.AbstractToevoegenBewerkenModalWindow;
import nl.topicus.cobra.web.components.modal.toevoegen.AbstractToevoegenBewerkenModalWindowPanel;
import nl.topicus.eduarte.entities.bpv.BPVCriteriaExterneOrganisatie;

/**
 * Pagina voor het bewerken van een {@link BPVCriteriaExterneOrganisatie} in een modal
 * window.
 * 
 * @author schimmel
 */
public class BPVCriteriaExterneOrganisatieModalWindowPanel extends
		AbstractToevoegenBewerkenModalWindowPanel<BPVCriteriaExterneOrganisatie>
{
	private static final long serialVersionUID = 1L;

	public BPVCriteriaExterneOrganisatieModalWindowPanel(String id,
			AbstractToevoegenBewerkenModalWindow<BPVCriteriaExterneOrganisatie> modalWindow,
			BPVCriteriaExterneOrganisatieEditPanel bpvCriteriaExterneOrganisatieEditPanel)
	{
		super(id, modalWindow, bpvCriteriaExterneOrganisatieEditPanel);
		final AutoFieldSet<BPVCriteriaExterneOrganisatie> gegevensAutoFieldSet =
			new AutoFieldSet<BPVCriteriaExterneOrganisatie>("gegevensAutoFieldSet", modalWindow
				.getModel(), "Criteria toevoegen");
		gegevensAutoFieldSet.setRenderMode(RenderMode.EDIT);
		gegevensAutoFieldSet.setSortAccordingToPropertyNames(true);
		gegevensAutoFieldSet.setPropertyNames("bpvCriteria", "status");
		getFormContainer().add(gegevensAutoFieldSet);
		createComponents();
	}
}