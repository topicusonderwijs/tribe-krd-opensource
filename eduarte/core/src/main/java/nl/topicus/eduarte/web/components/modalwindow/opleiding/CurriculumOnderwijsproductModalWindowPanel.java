package nl.topicus.eduarte.web.components.modalwindow.opleiding;

import nl.topicus.cobra.web.components.form.AutoFieldSet;
import nl.topicus.cobra.web.components.form.FieldProperties;
import nl.topicus.cobra.web.components.form.RenderMode;
import nl.topicus.cobra.web.components.form.modifier.PostProcessModifier;
import nl.topicus.cobra.web.components.modal.toevoegen.AbstractToevoegenBewerkenModalWindow;
import nl.topicus.cobra.web.components.modal.toevoegen.AbstractToevoegenBewerkenModalWindowPanel;
import nl.topicus.cobra.web.components.quicksearch.ISelectListener;
import nl.topicus.eduarte.entities.curriculum.CurriculumOnderwijsproduct;
import nl.topicus.eduarte.web.components.quicksearch.onderwijsproduct.OnderwijsproductSearchEditor;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;

public class CurriculumOnderwijsproductModalWindowPanel extends
		AbstractToevoegenBewerkenModalWindowPanel<CurriculumOnderwijsproduct>
{
	private static final long serialVersionUID = 1L;

	private AutoFieldSet<CurriculumOnderwijsproduct> gegevensAutoFieldSet;

	public CurriculumOnderwijsproductModalWindowPanel(String id,
			AbstractToevoegenBewerkenModalWindow<CurriculumOnderwijsproduct> modalWindow,
			CurriculumOnderwijsproductEditPanel curriculumOnderwijsproductEditPanel)
	{
		super(id, modalWindow, curriculumOnderwijsproductEditPanel);
		modalWindow.setInitialHeight(300);

		gegevensAutoFieldSet =
			new AutoFieldSet<CurriculumOnderwijsproduct>("gegevensAutoFieldSet", modalWindow
				.getModel(), "Nieuw onderwijsproduct");
		gegevensAutoFieldSet.setRenderMode(RenderMode.EDIT);
		gegevensAutoFieldSet.setPropertyNames("onderwijsproduct", "leerjaar", "periode",
			"onderwijstijd", "totaleOnderwijstijdOnderwijsproduct", "belastingOnderwijsproduct");
		gegevensAutoFieldSet.addFieldModifier(new PostProcessModifier("onderwijsproduct")
		{
			private static final long serialVersionUID = 1L;

			@Override
			public <T> void postProcess(AutoFieldSet<T> fieldSet, Component field,
					FieldProperties<T, ? , ? > fieldProperties)
			{
				OnderwijsproductSearchEditor editor = (OnderwijsproductSearchEditor) field;
				editor.addListener(getListener());
			}
		});

		gegevensAutoFieldSet.setSortAccordingToPropertyNames(true);

		getFormContainer().add(gegevensAutoFieldSet);

		createComponents();
	}

	private ISelectListener getListener()
	{
		return new ISelectListener()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void onUpdate(AjaxRequestTarget target)
			{
				target.addComponent(gegevensAutoFieldSet
					.findFieldComponent("belastingOnderwijsproduct"));
				target.addComponent(gegevensAutoFieldSet
					.findFieldComponent("totaleOnderwijstijdOnderwijsproduct"));
			}
		};
	}
}