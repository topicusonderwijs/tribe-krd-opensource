package nl.topicus.cobra.web.components;

import java.util.Collection;

import org.apache.wicket.model.IModel;

public class SimpleModelSelection<T> extends ModelSelection<T, T>
{
	private static final long serialVersionUID = 1L;

	public SimpleModelSelection(IModel< ? extends Collection<T>> model)
	{
		super(model);
	}

	@Override
	protected T convertStoR(T object)
	{
		return object;
	}
}
