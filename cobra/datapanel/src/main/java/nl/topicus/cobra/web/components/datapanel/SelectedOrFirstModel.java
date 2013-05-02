package nl.topicus.cobra.web.components.datapanel;

import java.util.List;

import org.apache.wicket.model.IModel;

public class SelectedOrFirstModel<T> implements IModel<T>
{
	private static final long serialVersionUID = 1L;

	private IModel<T> wrapped;

	private IModel< ? extends List<T>> listModel;

	public SelectedOrFirstModel(IModel<T> wrapped, IModel< ? extends List<T>> listModel)
	{
		this.wrapped = wrapped;
		this.listModel = listModel;
	}

	@Override
	public T getObject()
	{
		if (wrapped.getObject() == null)
		{
			List<T> list = listModel.getObject();
			return list.isEmpty() ? null : list.get(0);
		}

		return wrapped.getObject();
	}

	@Override
	public void setObject(T object)
	{
		wrapped.setObject(object);
	}

	@Override
	public void detach()
	{
		wrapped.detach();
		listModel.detach();
	}
}
