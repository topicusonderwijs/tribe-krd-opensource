package nl.topicus.cobra.modelsv2;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

import org.apache.wicket.model.AbstractPropertyModel;

/**
 * Property Model implementatie voor het refereren naar een item uit een lijst. Deze model
 * past nooit de data aan maar verschuift de index op de lijst.
 * 
 * @author hoeve
 */
public class ReadOnlyListPropertyModel<T> extends AbstractPropertyModel<T>
{
	private static final long serialVersionUID = 1L;

	private int index;

	public ReadOnlyListPropertyModel(List<T> modelObject)
	{
		this(modelObject, 0);
	}

	public ReadOnlyListPropertyModel(List<T> modelObject, int index)
	{
		this((Object) modelObject, index);
	}

	public ReadOnlyListPropertyModel(Object modelObject)
	{
		this(modelObject, 0);
	}

	public ReadOnlyListPropertyModel(Object modelObject, int index)
	{
		super(modelObject);
		this.index = index;
	}

	@SuppressWarnings("unchecked")
	@Override
	public T getObject()
	{
		if (index < 0)
			return null;

		List<T> list = (List<T>) getTarget();
		if (list != null && list.size() > 0)
		{
			return list.get(index);
		}
		return null;
	}

	/**
	 * Shifts the index according to the given object. If the object is not present in the
	 * list an Exception is thrown.
	 * 
	 * @param object
	 *            The object that will be used to shift the index.
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void setObject(T object)
	{
		if (object == null)
		{
			index = -1;
			return;
		}

		List<T> list = (List<T>) getTarget();
		index = list.indexOf(object);
	}

	@Override
	public Field getPropertyField()
	{
		return null;
	}

	@Override
	public Method getPropertyGetter()
	{
		return null;
	}

	@Override
	public Method getPropertySetter()
	{
		return null;
	}

	@Override
	protected String propertyExpression()
	{
		return "[" + index + "]";
	}

	@Override
	public String toString()
	{
		StringBuffer sb = new StringBuffer(super.toString());
		sb.append(":expression=[").append(propertyExpression()).append("]");
		return sb.toString();
	}
}
