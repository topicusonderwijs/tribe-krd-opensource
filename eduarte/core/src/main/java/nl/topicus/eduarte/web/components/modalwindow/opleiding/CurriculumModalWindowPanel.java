package nl.topicus.eduarte.web.components.modalwindow.opleiding;

import nl.topicus.cobra.web.components.form.AutoFieldSet;
import nl.topicus.cobra.web.components.form.RenderMode;
import nl.topicus.cobra.web.components.modal.toevoegen.AbstractToevoegenBewerkenModalWindow;
import nl.topicus.cobra.web.components.modal.toevoegen.AbstractToevoegenBewerkenModalWindowPanel;
import nl.topicus.cobra.web.validators.UniqueConstraintValidator;
import nl.topicus.eduarte.entities.curriculum.Curriculum;
import nl.topicus.eduarte.entities.opleiding.Opleiding;
import nl.topicus.eduarte.web.components.autoform.OrganisatieEenheidLocatieFieldModifier;

import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

public class CurriculumModalWindowPanel extends
		AbstractToevoegenBewerkenModalWindowPanel<Curriculum>
{
	private static final long serialVersionUID = 1L;

	private AutoFieldSet<Curriculum> gegevensAutoFieldSet;

	public CurriculumModalWindowPanel(String id,
			AbstractToevoegenBewerkenModalWindow<Curriculum> modalWindow,
			CurriculumEditPanel curriculumEditPanel)
	{
		super(id, modalWindow, curriculumEditPanel);
		modalWindow.setInitialHeight(300);

		gegevensAutoFieldSet =
			new AutoFieldSet<Curriculum>("gegevensAutoFieldSet", modalWindow.getModel(),
				"Nieuw Curriculum");
		gegevensAutoFieldSet.setRenderMode(RenderMode.EDIT);
		gegevensAutoFieldSet.setPropertyNames("organisatieEenheid", "locatie", "cohort");
		gegevensAutoFieldSet.addFieldModifier(new OrganisatieEenheidLocatieFieldModifier()
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected IModel< ? > getFilterEntiteitModel(AutoFieldSet< ? > fieldSet)
			{
				return new PropertyModel<Opleiding>(getModalWindow().getModel(), "opleiding");
			}
		});
		gegevensAutoFieldSet.setSortAccordingToPropertyNames(true);
		gegevensAutoFieldSet.addModifier("cohort", new UniqueConstraintValidator<String>(
			gegevensAutoFieldSet, "Curriculum", "cohort", "organisatieEenheid", "locatie",
			"opleiding"));

		getFormContainer().add(gegevensAutoFieldSet);

		createComponents();
	}

}