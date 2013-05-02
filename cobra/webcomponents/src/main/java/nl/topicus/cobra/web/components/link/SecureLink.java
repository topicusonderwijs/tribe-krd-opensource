package nl.topicus.cobra.web.components.link;

import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.IModel;
import org.apache.wicket.security.actions.WaspAction;
import org.apache.wicket.security.checks.ISecurityCheck;
import org.apache.wicket.security.components.ISecureComponent;
import org.apache.wicket.security.components.SecureComponentHelper;

public abstract class SecureLink<T> extends Link<T> implements ISecureComponent
{
	private static final long serialVersionUID = 1L;

	public SecureLink(String id)
	{
		super(id);
	}

	public SecureLink(String id, IModel<T> model)
	{
		super(id, model);
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
