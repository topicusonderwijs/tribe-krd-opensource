package nl.topicus.cobra.web.components.form.modifier;

import nl.topicus.cobra.reflection.Property;
import nl.topicus.cobra.web.components.form.AutoFieldSet;

/**
 * SingeFieldAdapter is een base class voor FieldModifiers die 1 operatie voor 1 property
 * aanpassen. De operatie die dit gedrag implementeerd dient overschreven te worden.
 * 
 * @author papegaaij
 */
public abstract class SingleFieldAdapter extends FieldAdapter
{
	private static final long serialVersionUID = 1L;

	private String propertyName;

	private Action triggerAction;

	/**
	 * Maakt een nieuwe SingleFieldAdapter.
	 * 
	 * @param propertyName
	 *            De naam van de property waar deze adapter op van toepassing is.
	 * @param action
	 *            De operatie waar deze adapter op van toepassing is.
	 */
	public SingleFieldAdapter(String propertyName, Action action)
	{
		this.propertyName = propertyName;
		this.triggerAction = action;
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
		if (property == null)
			return false;

		return this.triggerAction.equals(action) && propertyName.equals(property.getPath());
	}
}
