package nl.topicus.cobra.web.components.form.simple;

import nl.topicus.cobra.web.components.form.AutoFieldSet;
import nl.topicus.cobra.web.components.form.FieldProperties;

import org.apache.wicket.Component;
import org.apache.wicket.markup.html.form.CheckBox;

/**
 * De editor container voor een {@code <input type="checkbox">} veld.
 * 
 * @author papegaaij
 */
public class SimpleInputCheckboxEditorContainer<X, Y, Z> extends
		AbstractSimpleFieldContainer<X, Y, Z>
{
	private static final long serialVersionUID = 1L;

	public SimpleInputCheckboxEditorContainer(String id, FieldProperties<X, Y, Z> properties)
	{
		super(id, properties);
	}

	/**
	 * Reset de required property van de checkbox, anders is het verplicht een vinkje te
	 * zetten.
	 * 
	 * @see nl.topicus.cobra.web.components.form.AbstractFieldContainer#postProcessFormField(org.apache.wicket.Component,
	 *      nl.topicus.cobra.web.components.form.AutoFieldSet)
	 */
	@Override
	public void postProcessFormField(Component field, AutoFieldSet<X> fieldSet)
	{
		super.postProcessFormField(field, fieldSet);
		if (field instanceof CheckBox)
		{
			CheckBox cField = (CheckBox) field;
			cField.setRequired(false);
		}
	}
}
