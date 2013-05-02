package nl.topicus.cobra.web.components.form.modifier;

import nl.topicus.cobra.reflection.Property;
import nl.topicus.cobra.web.components.form.AutoFieldSet;
import nl.topicus.cobra.web.components.form.FieldProperties;
import nl.topicus.cobra.web.components.form.RenderMode;

public class RenderModeModifier extends MultiFieldAdapter
{
	private static final long serialVersionUID = 1L;

	private RenderMode newMode;

	public RenderModeModifier(RenderMode newMode, String... propertyNames)
	{
		super(Action.RENDER_MODE, propertyNames);
		this.newMode = newMode;
	}

	@Override
	public <T> RenderMode getRenderMode(AutoFieldSet<T> fieldSet, Property<T, ? , ? > property,
			FieldProperties<T, ? , ? > fieldProperties)
	{
		return newMode;
	}
}
