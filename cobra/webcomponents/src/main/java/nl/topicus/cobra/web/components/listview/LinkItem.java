package nl.topicus.cobra.web.components.listview;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.markup.html.link.ILinkListener;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.security.actions.WaspAction;
import org.apache.wicket.security.checks.ISecurityCheck;
import org.apache.wicket.security.components.ISecureComponent;
import org.apache.wicket.security.components.SecureComponentHelper;

/**
 * ListItem waar op geklikt kan worden.
 * 
 * @author marrink
 */
public class LinkItem<T> extends ListItem<T> implements ILinkListener, ISecureComponent
{
	private static final long serialVersionUID = 1L;

	public LinkItem(int index, IModel<T> model)
	{
		super(index, model);
		add(new AttributeModifier("onclick", true, new Model<String>()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public String getObject()
			{
				CharSequence urlForLink = urlFor(ILinkListener.INTERFACE);
				return "javascript:location.href='" + urlForLink + "';";
			}
		})
		{
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isEnabled(Component component)
			{
				return super.isEnabled(component) && component.isEnableAllowed()
					&& component.isEnabled();
			}
		});
	}

	@Override
	protected boolean getStatelessHint()
	{
		return false;
	}

	public void onLinkClicked()
	{
		getListview().onClick(this);
	}

	@SuppressWarnings("unchecked")
	protected IClickableListView<T> getListview()
	{
		return (IClickableListView<T>) this.getParent();
	}

	@Override
	public ISecurityCheck getSecurityCheck()
	{
		return SecureComponentHelper.getSecurityCheck(this);
	}

	@Override
	public boolean isActionAuthorized(String waspAction)
	{
		return SecureComponentHelper.isActionAuthorized(this, waspAction);
	}

	@Override
	public boolean isActionAuthorized(WaspAction action)
	{
		return SecureComponentHelper.isActionAuthorized(this, action);
	}

	@Override
	public boolean isAuthenticated()
	{
		return SecureComponentHelper.isAuthenticated(this);
	}

	@Override
	public void setSecurityCheck(ISecurityCheck check)
	{
		SecureComponentHelper.setSecurityCheck(this, check);
	}
}
