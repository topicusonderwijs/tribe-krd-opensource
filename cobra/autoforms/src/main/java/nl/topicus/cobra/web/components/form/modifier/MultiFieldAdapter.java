package nl.topicus.cobra.web.components.form.modifier;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

import nl.topicus.cobra.reflection.Property;
import nl.topicus.cobra.util.StringUtil;
import nl.topicus.cobra.web.components.form.AutoFieldSet;

/**
 * MultiFieldAdapter is een base class voor FieldModifiers die 1 of meer operaties voor
 * meerdere properties aanpassen. De operaties die dit gedrag implementeert dienen
 * overschreven te worden. De propertynamen mogen wildcards bevatten.
 * 
 * @author papegaaij
 */
public abstract class MultiFieldAdapter extends FieldAdapter
{
	private static final long serialVersionUID = 1L;

	private List<Pattern> propertyNames;

	private Set<Action> triggerActions;

	/**
	 * Maakt een nieuwe MultiFieldAdapter.
	 * 
	 * @param action
	 *            De operatie waar deze adapter op van toepassing is.
	 * @param propertyNames
	 *            De namen van de property waar deze adapter op van toepassing is. Deze
	 *            mogen wildcards bevatten.
	 */
	public MultiFieldAdapter(Action action, String... propertyNames)
	{
		this(EnumSet.of(action), propertyNames);
	}

	/**
	 * Maakt een nieuwe MultiFieldAdapter.
	 * 
	 * @param actions
	 *            De operaties waar deze adapter op van toepassing is.
	 * @param propertyNames
	 *            De namen van de property waar deze adapter op van toepassing is. Deze
	 *            mogen wildcards bevatten.
	 */
	public MultiFieldAdapter(Set<Action> actions, String... propertyNames)
	{
		this.propertyNames = new ArrayList<Pattern>(propertyNames.length);
		for (String curPropertyName : propertyNames)
		{
			if (curPropertyName == null)
				this.propertyNames.add(null);
			else
				this.propertyNames
					.add(Pattern.compile(StringUtil.wildcardToRegex(curPropertyName)));
		}
		this.triggerActions = actions;
	}

	/**
	 * Geeft true als de property en operatie overeenkomen met wat bij de constructor is
	 * meegegeven.
	 * 
	 * @see nl.topicus.cobra.web.components.form.modifier.FieldModifier#isApplicable(nl.topicus.cobra.web.components.form.AutoFieldSet,
	 *      nl.topicus.cobra.reflection.Property,
	 *      nl.topicus.cobra.web.components.form.modifier.FieldModifier.Action)
	 */
	@Override
	public <T> boolean isApplicable(AutoFieldSet<T> fieldSet, Property<T, ? , ? > property,
			Action action)
	{
		if (!this.triggerActions.contains(action))
			return false;

		for (Pattern curPattern : propertyNames)
		{
			if (curPattern == null && property == null)
				return true;
			if (property != null && curPattern != null
				&& curPattern.matcher(property.getPath()).matches())
				return true;
		}
		return false;
	}
}
