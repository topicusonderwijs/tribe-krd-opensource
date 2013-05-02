package nl.topicus.cobra.web.components.panels;

import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

public class TypedPanel<T> extends Panel
{
	private static final long serialVersionUID = 1L;

	public TypedPanel(String id)
	{
		super(id);
	}

	public TypedPanel(String id, IModel<T> model)
	{
		super(id, model);
	}

	@SuppressWarnings("unchecked")
	public final IModel<T> getModel()
	{
		return (IModel<T>) getDefaultModel();
	}

	public void setModel(IModel<T> model)
	{
		setDefaultModel(model);
	}

	@SuppressWarnings("unchecked")
	public final T getModelObject()
	{
		return (T) getDefaultModelObject();
	}

	public final void setModelObject(T object)
	{
		setDefaultModelObject(object);
	}

}
