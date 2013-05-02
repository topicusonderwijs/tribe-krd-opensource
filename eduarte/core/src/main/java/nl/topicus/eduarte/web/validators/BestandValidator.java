package nl.topicus.eduarte.web.validators;

import nl.topicus.eduarte.entities.bijlage.TypeBijlage;

import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.RadioChoice;
import org.apache.wicket.markup.html.form.upload.FileUploadField;
import org.apache.wicket.markup.html.form.validation.AbstractFormValidator;

/**
 * Validator die controleert afhankelijke van de keuze van de radiochoice dat het
 * bestandField niet leeg is.
 * 
 * @author vandekamp
 */
public class BestandValidator extends AbstractFormValidator
{
	private static final long serialVersionUID = 1L;

	private final FileUploadField bestandField;

	private final RadioChoice<TypeBijlage> radioChoice;

	/**
	 * Constructor
	 */
	public BestandValidator(FileUploadField bestandField, RadioChoice<TypeBijlage> radioChoice)
	{
		this.bestandField = bestandField;
		this.radioChoice = radioChoice;
	}

	@Override
	public void validate(Form< ? > form)
	{
		if (radioChoice.getModelObject().equals(TypeBijlage.Bestand))
		{
			if (bestandField.getFileUpload() == null)
			{
				error(bestandField, "BestandValidator.bestandEmpty");
			}
		}
	}

	@Override
	public FormComponent< ? >[] getDependentFormComponents()
	{
		FormComponent< ? >[] formComponents = {bestandField, radioChoice};
		return formComponents;
	}

}
