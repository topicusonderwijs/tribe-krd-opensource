package nl.topicus.cobra.web.components.form.modifier;

import nl.topicus.cobra.web.components.form.AutoFieldSet;
import nl.topicus.cobra.web.components.form.FieldProperties;

import org.apache.wicket.Component;

public abstract class PostProcessModifier extends MultiFieldAdapter
{
	private static final long serialVersionUID = 1L;

	public PostProcessModifier(String... propertyNames)
	{
		super(Action.POST_PROCESS, propertyNames);
	}

	@Override
	public abstract <T> void postProcess(AutoFieldSet<T> fieldSet, Component field,
			FieldProperties<T, ? , ? > fieldProperties);
}
