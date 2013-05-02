package nl.topicus.cobra.web.components.panels.bottomrow;

import nl.topicus.cobra.web.components.shortcut.CobraKeyAction;

import org.apache.wicket.Page;
import org.apache.wicket.markup.html.link.IPageLink;

public class AnnulerenButton extends AbstractPageBottomRowButton
{
	private static final long serialVersionUID = 1L;

	public AnnulerenButton(BottomRowPanel bottomRow, Class< ? extends Page> pageClass)
	{
		super(bottomRow, "Annuleren", CobraKeyAction.ANNULEREN, ButtonAlignment.RIGHT, pageClass);
	}

	public AnnulerenButton(BottomRowPanel bottomRow, IPageLink pagelink)
	{
		super(bottomRow, "Annuleren", CobraKeyAction.ANNULEREN, ButtonAlignment.RIGHT, pagelink);
	}

	public AnnulerenButton(BottomRowPanel bottomRow, Page page)
	{
		super(bottomRow, "Annuleren", CobraKeyAction.ANNULEREN, ButtonAlignment.RIGHT, page);
	}
}
