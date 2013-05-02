package nl.topicus.cobra.web.security;

import nl.topicus.cobra.test.AllowedMethods;

import org.apache.wicket.Page;
import org.apache.wicket.markup.html.link.IPageLink;
import org.apache.wicket.util.string.AppendingStringBuffer;

@AllowedMethods( {
	"public org.apache.wicket.security.components.markup.html.links.SecurePageLink(java.lang.String,java.lang.Class)",
	"public org.apache.wicket.security.components.markup.html.links.SecurePageLink(java.lang.String,org.apache.wicket.markup.html.link.IPageLink)"})
public class TargetBasedSecurePageConfirmationLink<T> extends TargetBasedSecurePageLink<T>
{
	private static final long serialVersionUID = 1L;

	private String confirmation;

	public <C extends Page> TargetBasedSecurePageConfirmationLink(String id, String confirmation,
			Class<C> c)
	{
		super(id, c);
		setConfirmation(confirmation);
	}

	public TargetBasedSecurePageConfirmationLink(String id, String confirmation, IPageLink pageLink)
	{
		super(id, pageLink);
		setConfirmation(confirmation);
	}

	public String getConfirmation()
	{
		return confirmation;
	}

	public void setConfirmation(String confirmation)
	{
		this.confirmation = confirmation;
	}

	@Override
	protected String getOnClickScript(final CharSequence url)
	{
		AppendingStringBuffer buffer = new AppendingStringBuffer();
		buffer.append("if(!confirm('");
		buffer.append(confirmation);
		buffer.append("')) return false; return true;");
		return buffer.toString();
	}
}
