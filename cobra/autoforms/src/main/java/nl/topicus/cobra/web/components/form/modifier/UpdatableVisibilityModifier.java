package nl.topicus.cobra.web.components.form.modifier;

import nl.topicus.cobra.web.components.form.AutoFieldSet;
import nl.topicus.cobra.web.components.form.FieldProperties;

import org.apache.wicket.Component;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

/**
 * Deze modifier maakt het mogelijk om de zichtbaarheid van velden aan te passen en werkt
 * ook met AjaxRefreshModifier.
 * 
 * @author papegaaij
 */
public class UpdatableVisibilityModifier extends EnableModifier
{
	private static final long serialVersionUID = 1L;

	/**
	 * Maakt een nieuwe VisibilityModifier met waarde die bij elke render opnieuw uit het
	 * model gehaald wordt.
	 * 
	 * @param propertyName
	 * @param visibilityModel
	 */
	public UpdatableVisibilityModifier(String propertyName, IModel<Boolean> visibilityModel)
	{
		this(visibilityModel, propertyName);
	}

	/**
	 * Maakt een nieuwe VisibilityModifier met de gegeven waarde.
	 * 
	 * @param propertyNames
	 * @param visibility
	 */
	public UpdatableVisibilityModifier(boolean visibility, String... propertyNames)
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
	public UpdatableVisibilityModifier(IModel<Boolean> visibilityModel, String... propertyNames)
	{
		super(visibilityModel, propertyNames);
	}

	@Override
	public <T> void postProcess(AutoFieldSet<T> fieldSet, Component field,
			FieldProperties<T, ? , ? > fieldProperties)
	{
		super.postProcess(fieldSet, field, fieldProperties);
		field.getParent().add(new AttributeAppender("class", new AbstractReadOnlyModel<String>()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public String getObject()
			{
				return getBehavior().calcEnabled() ? null : "hide";
			}
		}, " "));
	}
}
