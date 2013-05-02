package nl.topicus.cobra.web.components;

import org.apache.wicket.Component;
import org.apache.wicket.markup.html.WebMarkupContainer;

public class Enclosure extends WebMarkupContainer
{
	private static final long serialVersionUID = 1L;

	public Enclosure(String id)
	{
		super(id);
		setOutputMarkupPlaceholderTag(true);
	}

	@Override
	public boolean isVisible()
	{
		VisibilityVisitor visitor = new VisibilityVisitor();
		visitChildren(visitor);
		return visitor.visible;
	}

	private final class VisibilityVisitor implements IVisitor<Component>
	{
		private boolean visible = false;

		@Override
		public Object component(Component component)
		{
			visible = visible || component.isVisible();
			return visible ? IVisitor.STOP_TRAVERSAL : IVisitor.CONTINUE_TRAVERSAL;
		}
	}
}
