package nl.topicus.cobra.web.validators;

import java.util.Date;

import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.form.validation.AbstractFormValidator;

/**
 * Validator die controleert dat een gegeven begindatum voor een gegeven einddatum ligt.
 * 
 * @author loite
 */
public class BegindatumVoorEinddatumValidator extends AbstractFormValidator
{
	private static final long serialVersionUID = 1L;

	private final TextField< ? extends Date> beginField;

	private final TextField< ? extends Date> eindField;

	private String errorMessageName;

	public BegindatumVoorEinddatumValidator(TextField< ? extends Date> beginField,
			TextField< ? extends Date> eindField)
	{
		this.beginField = beginField;
		this.eindField = eindField;
	}

	public BegindatumVoorEinddatumValidator(TextField< ? extends Date> beginField,
			TextField< ? extends Date> eindField, String errorMessageName)
	{
		this.beginField = beginField;
		this.eindField = eindField;
		this.errorMessageName = errorMessageName;
	}

	@SuppressWarnings("unchecked")
	@Override
	public FormComponent<Date>[] getDependentFormComponents()
	{
		FormComponent< ? >[] formComponents = {beginField, eindField};
		return (FormComponent<Date>[]) formComponents;
	}

	@Override
	public void validate(Form< ? > form)
	{
		if (beginField.getConvertedInput() != null && eindField.getConvertedInput() != null)
		{
			Date begin = beginField.getConvertedInput();
			Date eind = eindField.getConvertedInput();
			if (begin.after(eind))
			{
				if (errorMessageName == null)
				{
					error(beginField, "BegindatumVoorEinddatumValidator.error");
				}
				else
				{
					error(beginField, errorMessageName);
				}
			}
		}
	}

	public TextField< ? extends Date> getBeginField()
	{
		return beginField;
	}

	public TextField< ? extends Date> getEindField()
	{
		return eindField;
	}

}
