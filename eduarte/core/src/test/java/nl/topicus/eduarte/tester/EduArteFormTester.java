package nl.topicus.eduarte.tester;

import nl.topicus.cobra.web.components.wiquery.auto.QuickSearchField;

import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.util.tester.BaseWicketTester;
import org.apache.wicket.util.tester.FormTester;

public class EduArteFormTester extends FormTester
{

	public EduArteFormTester(String path, Form< ? > workingForm,
			final BaseWicketTester wicketTester, boolean fillBlankString)
	{
		super(path, workingForm, wicketTester, fillBlankString);

		// fill blank String for Text Component.
		workingForm.visitFormComponents(new FormComponent.AbstractVisitor()
		{
			@Override
			public void onFormComponent(final FormComponent< ? > formComponent)
			{
				// do nothing for invisible component
				if (!formComponent.isVisibleInHierarchy())
				{
					return;
				}

				// if component is text field and do not have exist value, fill
				// blank String if required
				if (formComponent instanceof QuickSearchField< ? >)
				{
					wicketTester.getServletRequest().setParameter(formComponent.getInputName(),
						formComponent.getValue());
				}
			}
		});
	}

}
