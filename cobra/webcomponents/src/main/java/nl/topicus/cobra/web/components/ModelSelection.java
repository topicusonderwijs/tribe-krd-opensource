package nl.topicus.cobra.web.components;

import java.util.Collection;

import org.apache.wicket.model.IModel;

public abstract class ModelSelection<R, S> implements Selection<R, S>
{
	private static final long serialVersionUID = 1L;

	private IModel< ? extends Collection<R>> model;

	public ModelSelection(IModel< ? extends Collection<R>> model)
	{
		this.model = model;
	}

	protected abstract R convertStoR(S object);

	protected Collection<R> getSelected()
	{
		return model.getObject();
	}

	public IModel< ? extends Collection<R>> getSelectedModel()
	{
		return model;
	}

	@Override
	public void add(S object)
	{
		getSelected().add(convertStoR(object));
	}

	@Override
	public void clear()
	{
		getSelected().clear();
	}

	@Override
	public boolean isSelected(S object)
	{
		return getSelected().contains(convertStoR(object));
	}

	@Override
	public void remove(S object)
	{
		getSelected().remove(convertStoR(object));
	}

	@Override
	public int size()
	{
		return getSelected().size();
	}

	@Override
	public void detach()
	{
		model.detach();
	}
}
