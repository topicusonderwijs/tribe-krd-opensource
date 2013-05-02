package nl.topicus.cobra.web.components.form;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.List;

import nl.topicus.cobra.reflection.Property;
import nl.topicus.cobra.reflection.ReflectionUtil;

import org.apache.wicket.Component;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

/**
 * FieldProperties bevat de instellingen voor een veld dat gerenderd moet worden.
 * 
 * @author papegaaij
 */
public class FieldProperties<X, Y, Z> implements Serializable,
		Comparable<FieldProperties< ? , ? , ? >>
{
	private static final long serialVersionUID = 1L;

	private IModel<String> labelModel;

	private IModel<String> descriptionModel;

	private IModel<Boolean> visibilityModel;

	private Class< ? extends Component> editor;

	private String fieldContainerType;

	private boolean required;

	private List<String> htmlClasses;

	private int order;

	private String propertyName;

	private String modelPath;

	private Class<Y> entityClass;

	private Class<X> baseClass;

	private PseudoProperty<X, Y, Z> pseudoProperty;

	private transient Property<X, Y, Z> cacheProperty;

	private Component component;

	private RenderMode renderMode;

	private IModel<AutoFormValidator[]> validators;

	public FieldProperties()
	{

	}

	public FieldProperties(Property<X, Y, Z> property)
	{
		entityClass = property.getDeclaringClass();
		baseClass = property.getBaseClass();
		propertyName = property.getPath();
		modelPath = propertyName;
		if (property instanceof PseudoProperty< ? , ? , ? >)
			pseudoProperty = (PseudoProperty<X, Y, Z>) property;
		else
			cacheProperty = property;
	}

	public String getLabel()
	{
		return labelModel.getObject().toString();
	}

	public void setLabel(String label)
	{
		labelModel = new Model<String>(label);
	}

	public IModel<String> getLabelModel()
	{
		return labelModel;
	}

	public void setLabelModel(IModel<String> labelModel)
	{
		this.labelModel = labelModel;
	}

	public String getDescription()
	{
		return descriptionModel.getObject().toString();
	}

	public void setDescription(String description)
	{
		descriptionModel = new Model<String>(description);
	}

	public IModel<String> getDescriptionModel()
	{
		return descriptionModel;
	}

	public void setDescriptionModel(IModel<String> descriptionModel)
	{
		this.descriptionModel = descriptionModel;
	}

	public Class< ? extends Component> getEditor()
	{
		return editor;
	}

	public void setEditor(Class< ? extends Component> editor)
	{
		this.editor = editor;
	}

	public String getFieldContainerType()
	{
		return fieldContainerType;
	}

	public void setFieldContainerType(String fieldContainerType)
	{
		this.fieldContainerType = fieldContainerType;
	}

	public boolean isRequired()
	{
		return required;
	}

	public void setRequired(boolean required)
	{
		this.required = required;
	}

	public void setHtmlClasses(List<String> htmlClasses)
	{
		this.htmlClasses = htmlClasses;
	}

	public List<String> getHtmlClasses()
	{
		return htmlClasses;
	}

	public void setOrder(int order)
	{
		this.order = order;
	}

	public int getOrder()
	{
		return order;
	}

	public Field getField()
	{
		try
		{
			return entityClass.getField(propertyName);
		}
		catch (SecurityException e)
		{
			throw new RuntimeException(e);
		}
		catch (NoSuchFieldException e)
		{
			throw new RuntimeException(e);
		}
	}

	public String getPropertyName()
	{
		return propertyName;
	}

	public void setModelPath(String modelPath)
	{
		this.modelPath = modelPath;
	}

	public String getModelPath()
	{
		return modelPath;
	}

	public Class< ? > getEntityClass()
	{
		return entityClass;
	}

	public Class< ? > getBaseClass()
	{
		return baseClass;
	}

	public void setComponent(Component component)
	{
		this.component = component;
	}

	public Component getComponent()
	{
		return component;
	}

	public void setRenderMode(RenderMode renderMode)
	{
		this.renderMode = renderMode;
	}

	public RenderMode getRenderMode()
	{
		return renderMode;
	}

	@SuppressWarnings("unchecked")
	public Property<X, Y, Z> getProperty()
	{
		if (pseudoProperty != null)
			return pseudoProperty;

		if (cacheProperty == null)
			cacheProperty =
				(Property<X, Y, Z>) ReflectionUtil.findProperty(baseClass, propertyName);

		return cacheProperty;
	}

	@Override
	public int hashCode()
	{
		return propertyName.hashCode();
	}

	@Override
	public boolean equals(Object o)
	{
		if (o instanceof FieldProperties< ? , ? , ? >)
		{
			return propertyName.equals(((FieldProperties< ? , ? , ? >) o).getPropertyName());
		}
		return false;
	}

	@Override
	public int compareTo(FieldProperties< ? , ? , ? > o)
	{
		if (equals(o))
			return 0;
		if (getOrder() == o.getOrder())
			return getPropertyName().compareTo(o.getPropertyName());
		return getOrder() - o.getOrder();
	}

	public void setVisibilityModel(IModel<Boolean> visibilityModel)
	{
		this.visibilityModel = visibilityModel;
	}

	public IModel<Boolean> getVisibilityModel()
	{
		return visibilityModel;
	}

	public boolean isVisible()
	{
		return visibilityModel.getObject();
	}

	public void setVisible(boolean visible)
	{
		visibilityModel = new Model<Boolean>(visible);
	}

	public IModel<AutoFormValidator[]> getValidators()
	{
		return validators;
	}

	public void setValidators(IModel<AutoFormValidator[]> validators)
	{
		this.validators = validators;
	}
}
