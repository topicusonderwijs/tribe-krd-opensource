package nl.topicus.cobra.web.components.form.modifier;

import java.util.EnumSet;
import java.util.List;

import nl.topicus.cobra.reflection.Property;
import nl.topicus.cobra.reflection.ReflectionUtil;
import nl.topicus.cobra.util.StringUtil;
import nl.topicus.cobra.web.components.form.AutoFieldSet;
import nl.topicus.cobra.web.components.form.FieldContainerType;
import nl.topicus.cobra.web.components.form.FieldProperties;
import nl.topicus.cobra.web.components.form.PseudoProperty;
import nl.topicus.cobra.web.components.form.RenderMode;

import org.apache.wicket.Component;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

public class WachtwoordModifier extends FieldAdapter
{
	private static final long serialVersionUID = 1L;

	private String wachtwoordField;

	private boolean added = false;

	public WachtwoordModifier(String wachtwoordField)
	{
		this.wachtwoordField = wachtwoordField;
	}

	@Override
	public <T> boolean isApplicable(AutoFieldSet<T> fieldSet, Property<T, ? , ? > property,
			Action action)
	{
		if (property == null)
			return Action.PROPERTY_COLLECTION.equals(action);
		if (property.getPath().equals(wachtwoordField))
			return EnumSet.of(Action.FIELD_CLASS, Action.MODEL, Action.FIELD_CONTAINER).contains(
				action);
		if (property.getPath().equals(wachtwoordField + "Check"))
			return EnumSet.of(Action.FIELD_CLASS, Action.HTML_CLASSES, Action.LABEL, Action.MODEL,
				Action.FIELD_CONTAINER).contains(action);
		return false;
	}

	@Override
	public <T> List<Property<T, ? , ? >> collectProperties(AutoFieldSet<T> fieldSet,
			List<Property<T, ? , ? >> properties)
	{
		Property<T, ? , ? > wachtwoordFieldProperty = null;
		for (Property<T, ? , ? > curProperty : properties)
		{
			if (curProperty.getPath().equals(wachtwoordField))
			{
				wachtwoordFieldProperty = curProperty;
				break;
			}
		}
		if (wachtwoordFieldProperty != null)
		{
			properties.add(new PseudoProperty<T, T, String>(wachtwoordFieldProperty.getBaseClass(),
				wachtwoordField + "Check", String.class));
			added = true;
		}
		return properties;
	}

	@Override
	public <T> IModel< ? > createModel(final AutoFieldSet<T> fieldSet,
			Property<T, ? , ? > property, FieldProperties<T, ? , ? > fieldProperties)
	{
		if (property.getPath().equals(wachtwoordField))
			return new IModel<String>()
			{
				private static final long serialVersionUID = 1L;

				@Override
				public void setObject(String object)
				{
					if (StringUtil.isNotEmpty(object))
					{
						T target = fieldSet.getModelObject();
						Property< ? , ? , ? > wachtWoordProp =
							ReflectionUtil.findProperty(target.getClass(), wachtwoordField);
						wachtWoordProp.setValue(target, object);
					}
				}

				@Override
				public String getObject()
				{
					return null;
				}

				@Override
				public void detach()
				{
				}
			};
		else
			return new Model<String>();
	}

	@Override
	public <T> String getFieldContainer(AutoFieldSet<T> fieldSet, Property<T, ? , ? > property,
			FieldProperties<T, ? , ? > fieldProperties)
	{
		return FieldContainerType.INPUT_PASSWORD;
	}

	@Override
	public <T> Class< ? extends Component> getFieldType(AutoFieldSet<T> fieldSet,
			Property<T, ? , ? > property, FieldProperties<T, ? , ? > fieldProperties)
	{
		return PasswordTextField.class;
	}

	@Override
	public <T> List<String> getHtmlClasses(AutoFieldSet<T> fieldSet, Property<T, ? , ? > property,
			FieldProperties<T, ? , ? > fieldProperties)
	{
		return fieldSet.getFieldProperties(wachtwoordField).getHtmlClasses();
	}

	@Override
	public <T> IModel<String> getLabel(AutoFieldSet<T> fieldSet, Property<T, ? , ? > property,
			FieldProperties<T, ? , ? > fieldProperties)
	{
		return new Model<String>("Herhaal wachtwoord");
	}

	@Override
	public <T> int getOrder(AutoFieldSet<T> fieldSet, Property<T, ? , ? > property, int index,
			FieldProperties<T, ? , ? > fieldProperties)
	{
		return index;
	}

	@Override
	public <T> RenderMode getRenderMode(AutoFieldSet<T> fieldSet, Property<T, ? , ? > property,
			FieldProperties<T, ? , ? > fieldProperties)
	{
		return RenderMode.EDIT;
	}

	@Override
	public <T> IModel<Boolean> getVisibility(AutoFieldSet<T> fieldSet,
			Property<T, ? , ? > property, FieldProperties<T, ? , ? > fieldProperties)
	{
		return new Model<Boolean>(added);
	}

	@Override
	public <T> boolean isRequired(AutoFieldSet<T> fieldSet, Property<T, ? , ? > property,
			FieldProperties<T, ? , ? > fieldProperties)
	{
		return false;
	}
}
