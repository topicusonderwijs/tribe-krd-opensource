package nl.topicus.cobra.web.components.panels.bottomrow;

import nl.topicus.cobra.web.components.shortcut.ActionKey;
import nl.topicus.cobra.web.security.TargetBasedSecureBookmarkablePageLink;
import nl.topicus.cobra.web.security.TargetBasedSecurePageLink;

import org.apache.wicket.Page;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.link.IPageLink;

public class AbstractPageBottomRowButton extends AbstractBottomRowButton
{
	private static final long serialVersionUID = 1L;

	private IPageLink pageLink;

	private Class< ? extends Page> pageClass;

	private Page pageInstance;

	public AbstractPageBottomRowButton(BottomRowPanel bottomRow, String label, ActionKey action,
			ButtonAlignment alignment)
	{
		super(bottomRow, label, action, alignment);
	}

	public AbstractPageBottomRowButton(BottomRowPanel bottomRow, String label, ActionKey action,
			ButtonAlignment alignment, IPageLink pageLink)
	{
		super(bottomRow, label, action, alignment);
		if (pageLink == null)
			throw new NullPointerException("pageLink cannot be null");
		this.pageLink = pageLink;
	}

	public AbstractPageBottomRowButton(BottomRowPanel bottomRow, String label, ActionKey action,
			ButtonAlignment alignment, final Page page)
	{
		super(bottomRow, label, action, alignment);
		if (page == null)
			throw new NullPointerException("page cannot be null");
		this.pageInstance = page;
	}

	public AbstractPageBottomRowButton(BottomRowPanel bottomRow, String label, ActionKey action,
			ButtonAlignment alignment, Class< ? extends Page> pageClass)
	{
		super(bottomRow, label, action, alignment);
		if (pageClass == null)
			throw new NullPointerException("pageClass cannot be null");
		this.pageClass = pageClass;
	}

	public IPageLink getPageLink()
	{
		return pageLink;
	}

	public AbstractBottomRowButton setPageLink(IPageLink pageLink)
	{
		this.pageLink = pageLink;
		return this;
	}

	public Class< ? extends Page> getPageClass()
	{
		return pageClass;
	}

	public AbstractBottomRowButton setPageClass(Class< ? extends Page> pageClass)
	{
		this.pageClass = pageClass;
		return this;
	}

	public Page getPageInstance()
	{
		return pageInstance;
	}

	public void setPageInstance(Page page)
	{
		this.pageInstance = page;
	}

	@Override
	protected WebMarkupContainer getLink(String linkId)
	{
		Page page = getPageInstance();
		if (page != null)
			return new TargetBasedSecurePageLink<Object>(linkId, page);

		IPageLink link = getPageLink();
		if (link != null)
			return new TargetBasedSecurePageLink<Object>(linkId, link);

		if (getPageClass() != null)
			return new TargetBasedSecureBookmarkablePageLink<Object>(linkId, getPageClass());
		throw new IllegalStateException("Er is geen page, geen link en geen class");
	}

	public void followLink()
	{
		if (isVisible())
		{
			Page page = getPageInstance();
			IPageLink link = getPageLink();
			if (page != null)
				setResponsePage(page);
			else if (link != null)
				setResponsePage(link.getPage());
			else
				setResponsePage(getPageClass());
		}
	}
}
