package nl.topicus.cobra.web.components.panels.bottomrow;

import nl.topicus.cobra.web.components.shortcut.CobraKeyAction;

import org.apache.wicket.Page;
import org.apache.wicket.markup.html.link.IPageLink;

public class TerugButton extends AbstractPageBottomRowButton
{
	private static final long serialVersionUID = 1L;

	public TerugButton(BottomRowPanel bottomRow, Class< ? extends Page> pageClass)
	{
		super(bottomRow, "Terug", CobraKeyAction.TERUG, ButtonAlignment.RIGHT, pageClass);
	}

	public TerugButton(BottomRowPanel bottomRow, IPageLink pagelink)
	{
		super(bottomRow, "Terug", CobraKeyAction.TERUG, ButtonAlignment.RIGHT, pagelink);
	}

	public TerugButton(BottomRowPanel bottomRow, Page page)
	{
		super(bottomRow, "Terug", CobraKeyAction.TERUG, ButtonAlignment.RIGHT, page);
	}

	public TerugButton(BottomRowPanel bottomRow, ButtonAlignment alignment, Page page)
	{
		super(bottomRow, "Terug", CobraKeyAction.TERUG, alignment, page);
	}
}
