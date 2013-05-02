package nl.topicus.cobra.web.components.form.modifier;

import nl.topicus.cobra.reflection.Property;
import nl.topicus.cobra.web.components.form.AutoFieldSet;
import nl.topicus.cobra.web.components.form.FieldProperties;

/**
 * Deze modifier maakt het mogelijk om de verplichtheid van velden aan te passen.
 * 
 * @author papegaaij
 */
public class RequiredModifier extends MultiFieldAdapter
{
	private static final long serialVersionUID = 1L;

	private boolean required;

	public RequiredModifier(boolean required, String... propertyNames)
	{
		super(Action.REQUIRED, propertyNames);
		this.required = required;
	}

	@Override
	public <T> boolean isRequired(AutoFieldSet<T> fieldSet, Property<T, ? , ? > property,
			FieldProperties<T, ? , ? > fieldProperties)
	{
		return required;
	}
}
