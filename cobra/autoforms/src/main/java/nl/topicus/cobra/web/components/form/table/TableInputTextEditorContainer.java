package nl.topicus.cobra.web.components.form.table;

import javax.persistence.Column;

import nl.topicus.cobra.web.components.form.AutoFieldSet;
import nl.topicus.cobra.web.components.form.FieldProperties;

import org.apache.wicket.Component;
import org.apache.wicket.behavior.SimpleAttributeModifier;
import org.apache.wicket.markup.html.form.FormComponent;

/**
 * De editor container voor een {@code <input type="text">} veld.
 * 
 * @author papegaaij
 */
public class TableInputTextEditorContainer<X, Y, Z> extends AbstractTableFieldContainer<X, Y, Z>
{
	private static final long serialVersionUID = 1L;

	/**
	 * Maakt een nieuw TableInputTextEditorContainer.
	 * 
	 * @param id
	 *            Het id.
	 * @param properties
	 *            De properties.
	 */
	public TableInputTextEditorContainer(String id, FieldProperties<X, Y, Z> properties)
	{
		super(id, properties);
	}

	/**
	 * Voegt een maxlength attribuut toe.
	 * 
	 * @see nl.topicus.cobra.web.components.form.AbstractFieldContainer#postProcessFormField(org.apache.wicket.Component,
	 *      nl.topicus.cobra.web.components.form.AutoFieldSet)
	 */
	@Override
	public void postProcessFormField(Component field, AutoFieldSet<X> fieldSet)
	{
		super.postProcessFormField(field, fieldSet);
		Column column = getFieldProperties().getProperty().getAnnotation(Column.class);
		int length = column == null ? 255 : column.length();
		field.add(new SimpleAttributeModifier("maxlength", Integer.toString(length)));
		if (field instanceof FormComponent< ? >)
		{
			((FormComponent< ? >) field).setType(getFieldProperties().getProperty().getType());
		}
	}
}
