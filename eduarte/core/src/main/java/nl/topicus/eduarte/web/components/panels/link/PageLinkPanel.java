package nl.topicus.eduarte.web.components.panels.link;

import nl.topicus.cobra.web.security.TargetBasedSecurePageLink;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.IPageLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IDetachable;

public class PageLinkPanel extends Panel
{
	private static final long serialVersionUID = 1L;

	private final IPageLink pageLink;

	public PageLinkPanel(String id, final IPageLink pageLink, String label)
	{
		super(id);
		this.pageLink = pageLink;
		Link<Void> link = new TargetBasedSecurePageLink<Void>("link", pageLink);
		link.add(new Label("linkLabel", label));
		add(link);
	}

	@Override
	protected void onDetach()
	{
		super.onDetach();
		if (pageLink instanceof IDetachable)
		{
			((IDetachable) pageLink).detach();
		}
	}

}