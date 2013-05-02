package nl.topicus.cobra.web.components.panels.bottomrow;

import org.apache.wicket.Page;
import org.apache.wicket.markup.html.link.IPageLink;

public class PageLinkButton extends AbstractPageBottomRowButton
{
	private static final long serialVersionUID = 1L;

	public PageLinkButton(BottomRowPanel bottomRow, String titel, Class< ? extends Page> pageClass)
	{
		super(bottomRow, titel, null, ButtonAlignment.RIGHT, pageClass);
	}

	public PageLinkButton(BottomRowPanel bottomRow, String titel, ButtonAlignment alignment,
			Class< ? extends Page> pageClass)
	{
		super(bottomRow, titel, null, alignment, pageClass);
	}

	public PageLinkButton(BottomRowPanel bottomRow, String titel, ButtonAlignment alignment,
			IPageLink pagelink)
	{
		super(bottomRow, titel, null, alignment, pagelink);
	}

	public PageLinkButton(BottomRowPanel bottomRow, String titel, IPageLink pagelink)
	{
		super(bottomRow, titel, null, ButtonAlignment.RIGHT, pagelink);
	}

	public PageLinkButton(BottomRowPanel bottomRow, String titel, Page page)
	{
		super(bottomRow, titel, null, ButtonAlignment.RIGHT, page);
	}
}
