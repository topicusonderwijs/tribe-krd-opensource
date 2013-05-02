package nl.topicus.eduarte.web.components.modalwindow.externeorganisatie;

import nl.topicus.cobra.web.components.form.AutoFieldSet;
import nl.topicus.cobra.web.components.form.RenderMode;
import nl.topicus.cobra.web.components.modal.toevoegen.AbstractToevoegenBewerkenModalWindow;
import nl.topicus.cobra.web.components.modal.toevoegen.AbstractToevoegenBewerkenModalWindowPanel;
import nl.topicus.eduarte.entities.bpv.BPVCriteriaExterneOrganisatie;
import nl.topicus.eduarte.entities.organisatie.ExterneOrganisatiePraktijkbegeleider;

/**
 * Pagina voor het bewerken van een {@link BPVCriteriaExterneOrganisatie} in een modal
 * window.
 * 
 * @author schimmel
 */
public class ExterneOrganisatiePraktijkbegeleiderModalWindowPanel extends
		AbstractToevoegenBewerkenModalWindowPanel<ExterneOrganisatiePraktijkbegeleider>
{
	private static final long serialVersionUID = 1L;

	public ExterneOrganisatiePraktijkbegeleiderModalWindowPanel(
			String id,
			AbstractToevoegenBewerkenModalWindow<ExterneOrganisatiePraktijkbegeleider> modalWindow,
			ExterneOrganisatiePraktijkbegeleiderEditPanel externeOrganisatiePraktijkbegeleiderEditPanel)
	{
		super(id, modalWindow, externeOrganisatiePraktijkbegeleiderEditPanel);
		final AutoFieldSet<ExterneOrganisatiePraktijkbegeleider> gegevensAutoFieldSet =
			new AutoFieldSet<ExterneOrganisatiePraktijkbegeleider>("gegevensAutoFieldSet",
				modalWindow.getModel(), "Begeleider toevoegen");
		gegevensAutoFieldSet.setRenderMode(RenderMode.EDIT);
		gegevensAutoFieldSet.setSortAccordingToPropertyNames(true);
		gegevensAutoFieldSet.setPropertyNames("medewerker");
		getFormContainer().add(gegevensAutoFieldSet);
		createComponents();
	}
}