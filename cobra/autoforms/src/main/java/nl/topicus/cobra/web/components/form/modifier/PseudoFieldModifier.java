package nl.topicus.cobra.web.components.form.modifier;

import java.util.List;

import nl.topicus.cobra.reflection.Property;
import nl.topicus.cobra.web.components.form.AutoFieldSet;
import nl.topicus.cobra.web.components.form.FieldProperties;
import nl.topicus.cobra.web.components.form.PseudoProperty;

import org.apache.wicket.model.IModel;

public class PseudoFieldModifier<B, T> extends FieldAdapter
{
	private static final long serialVersionUID = 1L;

	private Class<B> baseClass;

	private String name;

	private Class<T> type;

	private IModel<T> model;

	public PseudoFieldModifier(Class<B> baseClass, String name, Class<T> type, IModel<T> model)
	{
		this.baseClass = baseClass;
		this.name = name;
		this.type = type;
		this.model = model;
	}

	@Override
	public <F> boolean isApplicable(AutoFieldSet<F> fieldSet, Property<F, ? , ? > property,
			Action action)
	{
		return Action.PROPERTY_COLLECTION.equals(action)
			|| (Action.MODEL.equals(action) && name.equals(property.getPath()));
	}

	@Override
	public <F> List<Property<F, ? , ? >> collectProperties(AutoFieldSet<F> fieldSet,
			List<Property<F, ? , ? >> properties)
	{
		properties.add(new PseudoProperty<F, B, T>(baseClass, name, type));
		return properties;
	}

	@Override
	public <F> IModel< ? > createModel(AutoFieldSet<F> fieldSet, Property<F, ? , ? > property,
			FieldProperties<F, ? , ? > fieldProperties)
	{
		return model;
	}

	@Override
	public void detach()
	{
		super.detach();
		model.detach();
	}
}
