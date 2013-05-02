package nl.topicus.cobra.util;

import org.apache.wicket.model.IDetachable;
import org.apache.wicket.model.IModel;

public class ModelObjectKey implements IDetachable
{
	private static final long serialVersionUID = 1L;

	private IModel< ? > model;

	public ModelObjectKey(IModel< ? > model)
	{
		this.model = model;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (obj instanceof ModelObjectKey)
		{
			ModelObjectKey other = (ModelObjectKey) obj;
			return other.model.getObject().equals(model.getObject());
		}
		return false;
	}

	@Override
	public int hashCode()
	{
		return model.getObject().hashCode();
	}

	@Override
	public void detach()
	{
		ComponentUtil.detachQuietly(model);
	}
}
