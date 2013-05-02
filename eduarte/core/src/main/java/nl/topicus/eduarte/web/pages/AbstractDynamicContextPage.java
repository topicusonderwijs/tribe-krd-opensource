package nl.topicus.eduarte.web.pages;

import nl.topicus.cobra.web.components.menu.AbstractMenuBar;

import org.apache.wicket.Component;
import org.apache.wicket.model.IModel;

//@InPrincipal(Always.class)
public abstract class AbstractDynamicContextPage<T> extends SecurePage
{
	private PageContext context;

	public AbstractDynamicContextPage(PageContext context)
	{
		super(context.getCurrentMainMenuItem());
		this.context = context;
	}

	public AbstractDynamicContextPage(IModel<T> model, PageContext context)
	{
		super(model, context.getCurrentMainMenuItem());
		this.context = context;
	}

	@SuppressWarnings("unchecked")
	public IModel<T> getContextModel()
	{
		return (IModel<T>) getDefaultModel();
	}

	@SuppressWarnings("unchecked")
	public T getContextModelObject()
	{
		return (T) getDefaultModelObject();
	}

	protected PageContext getPageContext()
	{
		return context;
	}

	@Override
	public AbstractMenuBar createMenu(String id)
	{
		return context.getMenu(id);
	}

	@Override
	public Component createTitle(String id)
	{
		return context.getTitle(id);
	}

	@Override
	protected void onDetach()
	{
		super.onDetach();
		context.detach();
	}
}
