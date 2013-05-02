package nl.topicus.eduarte.web.components.modalwindow.bpvcriteria;

import nl.topicus.cobra.web.components.form.AutoFieldSet;
import nl.topicus.cobra.web.components.form.RenderMode;
import nl.topicus.cobra.web.components.modal.toevoegen.AbstractToevoegenBewerkenModalWindow;
import nl.topicus.cobra.web.components.modal.toevoegen.AbstractToevoegenBewerkenModalWindowPanel;
import nl.topicus.eduarte.entities.bpv.BPVCriteriaOnderwijsproduct;

/**
 * Pagina voor het bewerken van een {@link BPVCriteriaOnderwijsproduct} in een modal
 * window.
 * 
 * @author schimmel
 */
public class BPVCriteriaOnderwijsproductModalWindowPanel extends
		AbstractToevoegenBewerkenModalWindowPanel<BPVCriteriaOnderwijsproduct>
{
	private static final long serialVersionUID = 1L;

	public BPVCriteriaOnderwijsproductModalWindowPanel(String id,
			AbstractToevoegenBewerkenModalWindow<BPVCriteriaOnderwijsproduct> modalWindow,
			BPVCriteriaOnderwijsproductEditPanel bpvCriteriaOnderwijsproductEditPanel)
	{
		super(id, modalWindow, bpvCriteriaOnderwijsproductEditPanel);
		final AutoFieldSet<BPVCriteriaOnderwijsproduct> gegevensAutoFieldSet =
			new AutoFieldSet<BPVCriteriaOnderwijsproduct>("gegevensAutoFieldSet", modalWindow
				.getModel(), "Criteria toevoegen");
		gegevensAutoFieldSet.setRenderMode(RenderMode.EDIT);
		gegevensAutoFieldSet.setSortAccordingToPropertyNames(true);
		gegevensAutoFieldSet.setPropertyNames("bpvCriteria", "status");
		getFormContainer().add(gegevensAutoFieldSet);
		createComponents();
	}
}