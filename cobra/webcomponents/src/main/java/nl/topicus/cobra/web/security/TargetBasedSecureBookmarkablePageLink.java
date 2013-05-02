package nl.topicus.cobra.web.security;

import org.apache.wicket.Page;
import org.apache.wicket.PageParameters;
import org.apache.wicket.security.components.markup.html.links.SecureBookmarkablePageLink;

public class TargetBasedSecureBookmarkablePageLink<T> extends SecureBookmarkablePageLink<T>
{
	private static final long serialVersionUID = 1L;

	public TargetBasedSecureBookmarkablePageLink(String id, Class< ? extends Page> c)
	{
		super(id, c);
		setSecurityCheck(new TargetBasedSecurityCheck(this, c));
	}

	public TargetBasedSecureBookmarkablePageLink(String id, Class< ? extends Page> c,
			PageParameters parameters)
	{
		super(id, c, parameters);
		setSecurityCheck(new TargetBasedSecurityCheck(this, c));
	}
}
