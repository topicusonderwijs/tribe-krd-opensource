package nl.topicus.cobra.web.components.form.modifier;

import org.apache.wicket.model.IModel;

/**
 * Gebruik EnableModifier. Deze heeft de verkeerde naam.
 * 
 * @author papegaaij
 */
@Deprecated
public class DisableModifier extends EnableModifier
{
	private static final long serialVersionUID = 1L;

	public DisableModifier(String propertyName, IModel<Boolean> enabledModel)
	{
		super(propertyName, enabledModel);
	}

	public DisableModifier(boolean enabled, String... propertyNames)
	{
		super(enabled, propertyNames);
	}

	public DisableModifier(IModel<Boolean> enabledModel, String... propertyNames)
	{
		super(enabledModel, propertyNames);
	}
}
