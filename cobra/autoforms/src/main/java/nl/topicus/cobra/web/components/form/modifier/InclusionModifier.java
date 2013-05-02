package nl.topicus.cobra.web.components.form.modifier;

import nl.topicus.cobra.reflection.Property;
import nl.topicus.cobra.web.components.form.AutoFieldSet;

/**
 * Deze modifier maakt het mogelijk om velden wel of niet te includen.
 * 
 * @author papegaaij
 */
public class InclusionModifier extends MultiFieldAdapter
{
	private static final long serialVersionUID = 1L;

	private boolean included;

	/**
	 * Maakt een nieuwe InclusionModifier voor 1 veld
	 * 
	 * @param propertyName
	 * @param included
	 */
	public InclusionModifier(String propertyName, boolean included)
	{
		this(included, propertyName);
	}

	/**
	 * Maakt een nieuwe InclusionModifier voor meerdere velden
	 * 
	 * @param propertyNames
	 * @param included
	 */
	public InclusionModifier(boolean included, String... propertyNames)
	{
		super(Action.INCLUSION, propertyNames);
		this.included = included;
	}

	@Override
	public <T> boolean isIncluded(AutoFieldSet<T> fieldSet, Property<T, ? , ? > property)
	{
		return included;
	}
}
