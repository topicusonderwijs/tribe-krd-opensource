package nl.topicus.eduarte.krd.web.components.modalwindow.bpvopmerking;

import nl.topicus.cobra.web.components.form.AutoFieldSet;
import nl.topicus.cobra.web.components.form.RenderMode;
import nl.topicus.cobra.web.components.modal.toevoegen.AbstractToevoegenBewerkenModalWindow;
import nl.topicus.cobra.web.components.modal.toevoegen.AbstractToevoegenBewerkenModalWindowPanel;
import nl.topicus.eduarte.entities.organisatie.ExterneOrganisatieOpmerking;

/**
 * Pagina voor het bewerken van een {@link ExterneOrganisatieOpmerking} in een modal
 * window.
 * 
 * @author vandekamp
 */
public class BPVOpmerkingModalWindowPanel extends
		AbstractToevoegenBewerkenModalWindowPanel<ExterneOrganisatieOpmerking>
{
	private static final long serialVersionUID = 1L;

	public BPVOpmerkingModalWindowPanel(String id,
			AbstractToevoegenBewerkenModalWindow<ExterneOrganisatieOpmerking> modalWindow,
			BPVOpmerkingEditPanel bpvOpmerkingEditPanel)
	{
		super(id, modalWindow, bpvOpmerkingEditPanel);
		final AutoFieldSet<ExterneOrganisatieOpmerking> gegevensAutoFieldSet =
			new AutoFieldSet<ExterneOrganisatieOpmerking>("gegevensAutoFieldSet", modalWindow
				.getModel(), "Nieuwe BPV-opmerking");
		gegevensAutoFieldSet.setRenderMode(RenderMode.EDIT);
		gegevensAutoFieldSet.setSortAccordingToPropertyNames(false);
		gegevensAutoFieldSet.setPropertyNames("datum", "opmerking", "auteur", "tonenBijMatching");
		getFormContainer().add(gegevensAutoFieldSet);
		createComponents();
	}
}