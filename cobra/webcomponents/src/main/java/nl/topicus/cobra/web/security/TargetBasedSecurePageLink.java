package nl.topicus.cobra.web.security;

import nl.topicus.cobra.test.AllowedMethods;

import org.apache.wicket.Page;
import org.apache.wicket.markup.html.link.IPageLink;
import org.apache.wicket.security.components.markup.html.links.SecurePageLink;

@AllowedMethods( {
	"public org.apache.wicket.security.components.markup.html.links.SecurePageLink(java.lang.String,java.lang.Class)",
	"public org.apache.wicket.security.components.markup.html.links.SecurePageLink(java.lang.String,org.apache.wicket.markup.html.link.IPageLink)"})
public class TargetBasedSecurePageLink<T> extends SecurePageLink<T>
{
	private static final long serialVersionUID = 1L;

	public <C extends Page> TargetBasedSecurePageLink(String id, Class<C> c)
	{
		super(id, c);
		setSecurityCheck(new TargetBasedSecurityCheck(this, c));
	}

	public TargetBasedSecurePageLink(String id, IPageLink pageLink)
	{
		super(id, pageLink);
		setSecurityCheck(new TargetBasedSecurityCheck(this, pageLink.getPageIdentity()));
	}

	public TargetBasedSecurePageLink(String id, final Page page)
	{
		super(id, new IPageLink()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public Page getPage()
			{
				return page;
			}

			@Override
			public Class< ? extends Page> getPageIdentity()
			{
				return page.getClass();
			}
		});
		setSecurityCheck(new TargetBasedSecurityCheck(this, page));
	}
}
