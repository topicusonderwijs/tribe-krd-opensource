package nl.topicus.eduarte.web.validators;

import nl.topicus.eduarte.entities.bijlage.TypeBijlage;

import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.RadioChoice;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.form.validation.AbstractFormValidator;

/**
 * Validator die controleert afhankelijke van de keuze van de radiochoice dat het
 * linkField niet leeg is.
 * 
 * @author vandekamp
 */
public class LinkValidator extends AbstractFormValidator
{
	private static final long serialVersionUID = 1L;

	private final TextField<String> linkField;

	private final RadioChoice<TypeBijlage> radioChoice;

	/**
	 * Constructor
	 */
	public LinkValidator(TextField<String> linkField, RadioChoice<TypeBijlage> radioChoice)
	{
		this.linkField = linkField;
		this.radioChoice = radioChoice;
	}

	@Override
	public void validate(Form< ? > form)
	{
		if (radioChoice.getModelObject().equals(TypeBijlage.Link))
		{
			if (linkField.getValue() == null || linkField.getValue().isEmpty())
				error(linkField, "LinkValidator.linkEmpty");
		}
	}

	@Override
	public FormComponent< ? >[] getDependentFormComponents()
	{
		FormComponent< ? >[] formComponents = {linkField, radioChoice};
		return formComponents;
	}

}
