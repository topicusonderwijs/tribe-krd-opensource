package nl.topicus.cobra.web.components.form.modifier;

import nl.topicus.cobra.web.components.form.AutoFieldSet;
import nl.topicus.cobra.web.components.form.FieldProperties;

import org.apache.wicket.Component;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.validation.IValidator;

public class ValidateModifier extends PostProcessModifier
{
	private static final long serialVersionUID = 1L;

	private IValidator< ? > validator;

	public ValidateModifier(IValidator< ? > validator, String... propertyNames)
	{
		super(propertyNames);
		this.validator = validator;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> void postProcess(AutoFieldSet<T> fieldSet, Component field,
			FieldProperties<T, ? , ? > fieldProperties)
	{
		if (field instanceof FormComponent< ? >)
		{
			((FormComponent) field).add(validator);
		}
	}
}
