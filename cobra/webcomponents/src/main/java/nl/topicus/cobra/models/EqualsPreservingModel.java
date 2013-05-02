package nl.topicus.cobra.models;

import java.io.Serializable;

import nl.topicus.cobra.util.JavaUtil;

import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

public class EqualsPreservingModel<T extends Serializable> extends Model<T>
{
	private static final long serialVersionUID = 1L;

	public EqualsPreservingModel()
	{
	}

	public EqualsPreservingModel(T object)
	{
		super(object);
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean equals(Object obj)
	{
		if (obj instanceof IModel< ? >)
		{
			return JavaUtil.equalsOrBothNull(((IModel<T>) obj).getObject(), getObject());
		}
		return false;
	}

	@Override
	public int hashCode()
	{
		Object obj = getObject();
		return obj == null ? 0 : obj.hashCode();
	}
}
