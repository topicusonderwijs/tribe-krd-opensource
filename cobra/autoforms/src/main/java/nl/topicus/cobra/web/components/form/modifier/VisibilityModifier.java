package nl.topicus.cobra.web.components.form.modifier;

import nl.topicus.cobra.reflection.Property;
import nl.topicus.cobra.web.components.form.AutoFieldSet;
import nl.topicus.cobra.web.components.form.FieldProperties;

import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

/**
 * Deze modifier maakt het mogelijk om de zichtbaarheid van velden aan te passen.
 * 
 * @author papegaaij
 */
public class VisibilityModifier extends MultiFieldAdapter
{
	private static final long serialVersionUID = 1L;

	private IModel<Boolean> visibilityModel;

	/**
	 * Maakt een nieuwe VisibilityModifier met waarde die bij elke render opnieuw uit het
	 * model gehaald wordt.
	 * 
	 * @param propertyName
	 * @param visibilityModel
	 */
	public VisibilityModifier(String propertyName, IModel<Boolean> visibilityModel)
	{
		this(visibilityModel, propertyName);
	}

	/**
	 * Maakt een nieuwe VisibilityModifier met de gegeven waarde.
	 * 
	 * @param propertyNames
	 * @param visibility
	 */
	public VisibilityModifier(boolean visibility, String... propertyNames)
	{
		this(new Model<Boolean>(visibility), propertyNames);
	}

	/**
	 * Maakt een nieuwe VisibilityModifier met waarde die bij elke render opnieuw uit het
	 * model gehaald wordt.
	 * 
	 * @param propertyNames
	 * @param visibilityModel
	 */
	public VisibilityModifier(IModel<Boolean> visibilityModel, String... propertyNames)
	{
		super(Action.VISIBILITY, propertyNames);
		this.visibilityModel = visibilityModel;
	}

	@Override
	public <T> IModel<Boolean> getVisibility(AutoFieldSet<T> fieldSet,
			Property<T, ? , ? > property, FieldProperties<T, ? , ? > fieldProperties)
	{
		return visibilityModel;
	}

	@Override
	public void detach()
	{
		visibilityModel.detach();
		super.detach();
	}
}
