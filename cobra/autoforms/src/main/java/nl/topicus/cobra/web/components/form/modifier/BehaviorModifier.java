package nl.topicus.cobra.web.components.form.modifier;

import nl.topicus.cobra.web.components.form.AutoFieldSet;
import nl.topicus.cobra.web.components.form.FieldProperties;

import org.apache.wicket.Component;
import org.apache.wicket.behavior.IBehavior;

public class BehaviorModifier extends MultiFieldAdapter
{
	private static final long serialVersionUID = 1L;

	private IBehavior behavior;

	public BehaviorModifier(IBehavior behavior, String... propertyNames)
	{
		super(Action.POST_PROCESS, propertyNames);
		this.behavior = behavior;
	}

	@Override
	public <T> void postProcess(AutoFieldSet<T> fieldSet, Component field,
			FieldProperties<T, ? , ? > fieldProperties)
	{
		field.add(behavior);
	}
}
