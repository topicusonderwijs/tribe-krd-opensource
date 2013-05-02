package nl.topicus.eduarte.web.pages.shared;

import nl.topicus.cobra.web.components.datapanel.selection.ISelectionComponent;

import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.IDetachable;
import org.apache.wicket.security.checks.ISecurityCheck;

public interface SelectieTarget<R, S> extends IDetachable
{
	public ISecurityCheck getSecurityCheck();

	public Link< ? > createLink(String linkId, ISelectionComponent<R, S> base);

	public String getLinkLabel();
}
