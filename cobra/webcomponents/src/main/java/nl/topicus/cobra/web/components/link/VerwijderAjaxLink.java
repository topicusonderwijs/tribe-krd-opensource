package nl.topicus.cobra.web.components.link;

import org.apache.wicket.model.IModel;

public abstract class VerwijderAjaxLink<T> extends CssAjaxLink<T>
{
	private static final long serialVersionUID = 1L;

	public VerwijderAjaxLink(String id)
	{
		this(id, null);
	}

	public VerwijderAjaxLink(String id, IModel<T> model)
	{
		super(id, model, "deleteItem", "deleteItem_grey");
	}
}
