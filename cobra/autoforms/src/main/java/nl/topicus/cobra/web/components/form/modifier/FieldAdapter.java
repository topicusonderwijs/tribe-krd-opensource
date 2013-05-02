package nl.topicus.cobra.web.components.form.modifier;

import java.util.List;

import nl.topicus.cobra.reflection.Property;
import nl.topicus.cobra.web.components.form.AutoFieldSet;
import nl.topicus.cobra.web.components.form.AutoFormValidator;
import nl.topicus.cobra.web.components.form.FieldProperties;
import nl.topicus.cobra.web.components.form.RenderMode;

import org.apache.wicket.Component;
import org.apache.wicket.model.IModel;

/**
 * FieldAdapter is een lege implementatie van {@link FieldModifier}. Alle operaties gooien
 * een exception. Subclasses dienen daarom in ieder geval 1 van de operatie methoden te
 * overschrijven.
 * 
 * @author papegaaij
 */
public abstract class FieldAdapter implements FieldModifier
{
	private static final long serialVersionUID = 1L;

	public <T> void bind(AutoFieldSet<T> fieldSet)
	{
	}

	@Override
	public <T> List<Property<T, ? , ? >> collectProperties(AutoFieldSet<T> fieldSet,
			List<Property<T, ? , ? >> properties)
	{
		throw new UnsupportedOperationException("collectProperties on "
			+ getClass().getSimpleName());
	}

	@Override
	public <T> Component createField(AutoFieldSet<T> fieldSet, String id, IModel< ? > model,
			Property<T, ? , ? > property, FieldProperties<T, ? , ? > fieldProperties)
	{
		throw new UnsupportedOperationException("createField on " + getClass().getSimpleName());
	}

	@Override
	public <T> IModel< ? > createModel(AutoFieldSet<T> fieldSet, Property<T, ? , ? > property,
			FieldProperties<T, ? , ? > fieldProperties)
	{
		throw new UnsupportedOperationException("createModel on " + getClass().getSimpleName());
	}

	@Override
	public <T> String getFieldContainer(AutoFieldSet<T> fieldSet, Property<T, ? , ? > property,
			FieldProperties<T, ? , ? > fieldProperties)
	{
		throw new UnsupportedOperationException("getFieldContainer on "
			+ getClass().getSimpleName());
	}

	@Override
	public <T> Class< ? extends Component> getFieldType(AutoFieldSet<T> fieldSet,
			Property<T, ? , ? > property, FieldProperties<T, ? , ? > fieldProperties)
	{
		throw new UnsupportedOperationException("getFieldType on " + getClass().getSimpleName());
	}

	@Override
	public <T> List<String> getHtmlClasses(AutoFieldSet<T> fieldSet, Property<T, ? , ? > property,
			FieldProperties<T, ? , ? > fieldProperties)
	{
		throw new UnsupportedOperationException("getHtmlClasses on " + getClass().getSimpleName());
	}

	@Override
	public <T> IModel<String> getLabel(AutoFieldSet<T> fieldSet, Property<T, ? , ? > property,
			FieldProperties<T, ? , ? > fieldProperties)
	{
		throw new UnsupportedOperationException("getLabel on " + getClass().getSimpleName());
	}

	@Override
	public <T> IModel<String> getDescription(AutoFieldSet<T> fieldSet,
			Property<T, ? , ? > property, FieldProperties<T, ? , ? > fieldProperties)
	{
		throw new UnsupportedOperationException("getDescription on " + getClass().getSimpleName());
	}

	@Override
	public <T> int getOrder(AutoFieldSet<T> fieldSet, Property<T, ? , ? > property, int index,
			FieldProperties<T, ? , ? > fieldProperties)
	{
		throw new UnsupportedOperationException("getOrder on " + getClass().getSimpleName());
	}

	@Override
	public <T> RenderMode getRenderMode(AutoFieldSet<T> fieldSet, Property<T, ? , ? > property,
			FieldProperties<T, ? , ? > fieldProperties)
	{
		throw new UnsupportedOperationException("getRenderMode on " + getClass().getSimpleName());
	}

	@Override
	public <T> boolean isRequired(AutoFieldSet<T> fieldSet, Property<T, ? , ? > property,
			FieldProperties<T, ? , ? > fieldProperties)
	{
		throw new UnsupportedOperationException("isRequired on " + getClass().getSimpleName());
	}

	@Override
	public <T> boolean isIncluded(AutoFieldSet<T> fieldSet, Property<T, ? , ? > property)
	{
		throw new UnsupportedOperationException("isIncluded on " + getClass().getSimpleName());
	}

	@Override
	public <T> IModel<Boolean> getVisibility(AutoFieldSet<T> fieldSet,
			Property<T, ? , ? > property, FieldProperties<T, ? , ? > fieldProperties)
	{
		throw new UnsupportedOperationException("isIncluded on " + getClass().getSimpleName());
	}

	@Override
	public <T> void postProcess(AutoFieldSet<T> fieldSet, Component field,
			FieldProperties<T, ? , ? > fieldProperties)
	{
		throw new UnsupportedOperationException("postProcess on " + getClass().getSimpleName());
	}

	@Override
	public <T> IModel<AutoFormValidator[]> getValidators(AutoFieldSet<T> fieldSet,
			Property<T, ? , ? > property, FieldProperties<T, ? , ? > fieldProperties)
	{
		throw new UnsupportedOperationException("postProcess on " + getClass().getSimpleName());
	}

	@Override
	public void detach()
	{
	}
}
