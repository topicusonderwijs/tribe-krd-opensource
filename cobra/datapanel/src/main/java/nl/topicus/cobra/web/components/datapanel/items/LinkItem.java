package nl.topicus.cobra.web.components.datapanel.items;

import nl.topicus.cobra.web.behaviors.AppendingAttributeModifier;

import org.apache.wicket.Application;
import org.apache.wicket.ResourceReference;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.internal.HtmlHeaderContainer;
import org.apache.wicket.markup.html.link.ILinkListener;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.apache.wicket.security.WaspApplication;
import org.apache.wicket.security.actions.ActionFactory;
import org.apache.wicket.security.actions.Enable;
import org.apache.wicket.security.actions.WaspAction;
import org.apache.wicket.security.checks.ISecurityCheck;
import org.apache.wicket.security.components.ISecureComponent;
import org.apache.wicket.security.components.SecureComponentHelper;

/**
 * ListItem waar op geklikt kan worden.
 * 
 * @author marrink
 */
public abstract class LinkItem<T> extends Item<T> implements ILinkListener, ISecureComponent
{
	private static final long serialVersionUID = 1L;

	private static final ResourceReference JS_LIB =
		new ResourceReference(LinkItem.class, "LinkItem.js");

	private boolean linkEnabled = true;

	public LinkItem(String id, int index, IModel<T> model)
	{
		super(id, index, model);
	}

	public void setLinkEnabled(boolean linkEnabled)
	{
		this.linkEnabled = linkEnabled;
	}

	public boolean isLinkEnabled()
	{
		return linkEnabled;
	}

	@Override
	protected boolean getStatelessHint()
	{
		return false;
	}

	@Override
	public void renderHead(HtmlHeaderContainer container)
	{
		IHeaderResponse response = container.getHeaderResponse();
		response.renderJavascriptReference(JS_LIB);
		super.renderHead(container);
	}

	public abstract void onLinkClicked();

	protected final ActionFactory getActionFactory()
	{
		return ((WaspApplication) Application.get()).getActionFactory();
	}

	@Override
	protected void onBeforeRender()
	{
		super.onBeforeRender();
		if (!hasBeenRendered() && isActionAuthorized(getActionFactory().getAction(Enable.class))
			&& isEnabled() && isLinkEnabled())
		{
			CharSequence urlForLink = urlFor(ILinkListener.INTERFACE);
			add(new AppendingAttributeModifier("onclick", "linkItemDoClick(arguments[0],'"
				+ urlForLink + "',this);", ";"));
		}
	}

	public ISecurityCheck getSecurityCheck()
	{
		return SecureComponentHelper.getSecurityCheck(this);
	}

	public boolean isActionAuthorized(String jaasAction)
	{
		return SecureComponentHelper.isActionAuthorized(this, jaasAction);
	}

	public boolean isActionAuthorized(WaspAction jaasAction)
	{
		return SecureComponentHelper.isActionAuthorized(this, jaasAction);
	}

	@Override
	public boolean isAuthenticated()
	{
		return SecureComponentHelper.isAuthenticated(this);
	}

	public void setSecurityCheck(ISecurityCheck check)
	{
		SecureComponentHelper.setSecurityCheck(this, check);

	}
}
