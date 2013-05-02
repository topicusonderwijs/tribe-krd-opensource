package nl.topicus.eduarte.resultaten.web.components.modalwindow;

import nl.topicus.cobra.modelsv2.ModelFactory;
import nl.topicus.cobra.web.components.form.AutoFieldSet;
import nl.topicus.cobra.web.components.form.RenderMode;
import nl.topicus.cobra.web.components.form.modifier.ConstructorArgModifier;
import nl.topicus.cobra.web.components.modal.toevoegen.AbstractToevoegenBewerkenModalWindow;
import nl.topicus.cobra.web.components.modal.toevoegen.AbstractToevoegenBewerkenModalWindowPanel;
import nl.topicus.eduarte.entities.resultaatstructuur.Scoreschaalwaarde;

public class ScoreschaalwaardeModalWindowPanel extends
		AbstractToevoegenBewerkenModalWindowPanel<Scoreschaalwaarde>
{
	private static final long serialVersionUID = 1L;

	private AutoFieldSet<Scoreschaalwaarde> inputfields;

	public ScoreschaalwaardeModalWindowPanel(String id,
			AbstractToevoegenBewerkenModalWindow<Scoreschaalwaarde> modalWindow,
			ScoreschaalwaardeEditPanel editPanel)
	{
		super(id, modalWindow, editPanel);

		inputfields =
			new AutoFieldSet<Scoreschaalwaarde>("inputfields", modalWindow.getModel(),
				"Scoreschaalwaarde");
		inputfields.setRenderMode(RenderMode.EDIT);
		inputfields.addFieldModifier(new ConstructorArgModifier("waarde", ModelFactory
			.getModel(getWaarde().getToets().getSchaal())));

		getFormContainer().add(inputfields);

		createComponents();
	}

	private Scoreschaalwaarde getWaarde()
	{
		return getModalWindow().getModelObject();
	}
}
