package nl.topicus.cobra.web.components.datapanel.items;

import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.IAjaxCallDecorator;
import org.apache.wicket.ajax.calldecorator.CancelEventIfNoAjaxDecorator;
import org.apache.wicket.ajax.markup.html.IAjaxLink;
import org.apache.wicket.behavior.IBehavior;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.OddEvenItem;
import org.apache.wicket.model.IModel;

/**
 * Data table item waarop geklikt kan worden, werkt als AjaxLink.
 * 
 * @author Martijn Dashorst
 */
public abstract class AjaxClickableItem<T> extends Item<T> implements IAjaxLink
{
	/** Voor serializatie. */
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor.
	 * 
	 * @see OddEvenItem#OddEvenItem(java.lang.String, int, org.apache.wicket.model.IModel)
	 */
	public AjaxClickableItem(String id, int index, IModel<T> model)
	{
		super(id, index, model);
		add(newOnClickBehavior());
	}

	/**
	 * Item moet statefull zijn, omdat anders bookmarkable urls worden gegenereerd (een
	 * combinatie van stateless en statefull, die niet goed werkt).
	 */
	@Override
	protected boolean getStatelessHint()
	{
		return false;
	}

	/**
	 * Genereert een ajax onclick javascript.
	 */
	protected IBehavior newOnClickBehavior()
	{
		AjaxEventBehavior ajaxEventBehavior = new AjaxEventBehavior("onclick")
		{
			/** Voor serializatie. */
			private static final long serialVersionUID = 1L;

			@Override
			protected void onEvent(AjaxRequestTarget target)
			{
				onClick(target);
			}

			@Override
			protected IAjaxCallDecorator getAjaxCallDecorator()
			{
				return new CancelEventIfNoAjaxDecorator(null);
			}
		};
		return ajaxEventBehavior;
	}

	public abstract void onClick(AjaxRequestTarget target);
}
