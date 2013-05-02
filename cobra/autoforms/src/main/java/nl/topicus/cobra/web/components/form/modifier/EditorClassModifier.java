package nl.topicus.cobra.web.components.form.modifier;

import nl.topicus.cobra.reflection.Property;
import nl.topicus.cobra.web.components.form.AutoFieldSet;
import nl.topicus.cobra.web.components.form.FieldProperties;

import org.apache.wicket.Component;

public class EditorClassModifier extends MultiFieldAdapter
{
	private static final long serialVersionUID = 1L;

	private Class< ? extends Component> editorClass;

	public EditorClassModifier(Class< ? extends Component> editorClass, String... propertyNames)
	{
		super(Action.FIELD_CLASS, propertyNames);
		this.editorClass = editorClass;
	}

	@Override
	public <T> Class< ? extends Component> getFieldType(AutoFieldSet<T> fieldSet,
			Property<T, ? , ? > property, FieldProperties<T, ? , ? > fieldProperties)
	{
		return editorClass;
	}
}
