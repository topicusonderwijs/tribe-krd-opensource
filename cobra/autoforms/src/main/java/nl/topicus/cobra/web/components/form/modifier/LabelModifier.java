package nl.topicus.cobra.web.components.form.modifier;

import nl.topicus.cobra.reflection.Property;
import nl.topicus.cobra.web.components.form.AutoFieldSet;
import nl.topicus.cobra.web.components.form.FieldProperties;

import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

/**
 * Deze modifier maakt het mogelijk om het label van velden aan te passen.
 * 
 * @author papegaaij
 */
public class LabelModifier extends SingleFieldAdapter
{
	private static final long serialVersionUID = 1L;

	private IModel<String> labelModel;

	/**
	 * Maakt een nieuwe LabelModifier met een vaste waarde
	 * 
	 * @param propertyName
	 * @param label
	 */
	public LabelModifier(String propertyName, String label)
	{
		this(propertyName, new Model<String>(label));
	}

	/**
	 * Maakt een nieuwe LabelModifier met waarde die bij elke render opnieuw uit het model
	 * gehaald wordt.
	 * 
	 * @param propertyName
	 * @param labelModel
	 */
	public LabelModifier(String propertyName, IModel<String> labelModel)
	{
		super(propertyName, Action.LABEL);
		this.labelModel = labelModel;
	}

	@Override
	public <T> IModel<String> getLabel(AutoFieldSet<T> fieldSet, Property<T, ? , ? > property,
			FieldProperties<T, ? , ? > fieldProperties)
	{
		return labelModel;
	}

	@Override
	public void detach()
	{
		labelModel.detach();
		super.detach();
	}
}
