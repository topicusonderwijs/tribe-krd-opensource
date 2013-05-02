package nl.topicus.eduarte.krd.web.components.modalwindow.verbintenis.bekostiging;

import nl.topicus.cobra.web.components.form.AutoFieldSet;
import nl.topicus.cobra.web.components.form.RenderMode;
import nl.topicus.cobra.web.components.form.modifier.EnableModifier;
import nl.topicus.cobra.web.components.modal.toevoegen.AbstractToevoegenBewerkenModalWindow;
import nl.topicus.cobra.web.components.modal.toevoegen.AbstractToevoegenBewerkenModalWindowPanel;
import nl.topicus.eduarte.entities.Entiteit;
import nl.topicus.eduarte.entities.inschrijving.Bekostigingsperiode;

public class BekostigingsperiodeModalWindowPanel extends
		AbstractToevoegenBewerkenModalWindowPanel<Bekostigingsperiode>
{
	private static final long serialVersionUID = 1L;

	private AutoFieldSet<Bekostigingsperiode> gegevensAutoFieldSet;

	public BekostigingsperiodeModalWindowPanel(String id,
			AbstractToevoegenBewerkenModalWindow<Bekostigingsperiode> modalWindow,
			BekostigingsperiodeEditPanel bekostigingsperiodeEditPanel)
	{
		super(id, modalWindow, bekostigingsperiodeEditPanel);

		gegevensAutoFieldSet =
			new AutoFieldSet<Bekostigingsperiode>("gegevensAutoFieldSet", modalWindow.getModel(),
				"Bekostigingsperiode bewerken");
		gegevensAutoFieldSet.setRenderMode(RenderMode.EDIT);
		gegevensAutoFieldSet.setPropertyNames("begindatum", "bekostigd");
		if (((Entiteit) modalWindow.getModelObject()).isSaved())
			gegevensAutoFieldSet.addFieldModifier(new EnableModifier(false, "begindatum"));
		gegevensAutoFieldSet.setSortAccordingToPropertyNames(true);

		getFormContainer().add(gegevensAutoFieldSet);

		createComponents();
	}

}