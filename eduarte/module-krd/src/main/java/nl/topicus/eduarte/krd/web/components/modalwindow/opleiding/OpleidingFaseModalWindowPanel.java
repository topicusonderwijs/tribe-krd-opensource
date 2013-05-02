package nl.topicus.eduarte.krd.web.components.modalwindow.opleiding;

import nl.topicus.cobra.web.components.form.AutoFieldSet;
import nl.topicus.cobra.web.components.form.RenderMode;
import nl.topicus.cobra.web.components.modal.toevoegen.AbstractToevoegenBewerkenModalWindow;
import nl.topicus.cobra.web.components.modal.toevoegen.AbstractToevoegenBewerkenModalWindowPanel;
import nl.topicus.eduarte.entities.hogeronderwijs.OpleidingFase;
import nl.topicus.eduarte.web.components.modalwindow.AbstractEduArteToevoegenBewerkenPanel;

public class OpleidingFaseModalWindowPanel extends
		AbstractToevoegenBewerkenModalWindowPanel<OpleidingFase>
{
	private static final long serialVersionUID = 1L;

	private AutoFieldSet<OpleidingFase> gegevensAutoFieldSet;

	public OpleidingFaseModalWindowPanel(String id,
			AbstractToevoegenBewerkenModalWindow<OpleidingFase> modalWindow,
			AbstractEduArteToevoegenBewerkenPanel<OpleidingFase> editPanel)
	{
		super(id, modalWindow, editPanel);

		gegevensAutoFieldSet =
			new AutoFieldSet<OpleidingFase>("fieldset", modalWindow.getModel(),
				"Opleidingfase bewerken");
		gegevensAutoFieldSet.setRenderMode(RenderMode.EDIT);
		gegevensAutoFieldSet.setPropertyNames("opleidingsvorm", "hoofdfase", "credits");
		gegevensAutoFieldSet.setSortAccordingToPropertyNames(true);

		modalWindow.setInitialWidth(450);
		modalWindow.setInitialHeight(160);

		getFormContainer().add(gegevensAutoFieldSet);

		createComponents();
	}

}