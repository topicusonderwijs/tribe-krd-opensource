package nl.topicus.eduarte.web.components.panels.bottomrow;

import nl.topicus.cobra.reflection.ReflectionUtil;
import nl.topicus.cobra.web.components.panels.bottomrow.AbstractPageBottomRowButton;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.cobra.web.components.panels.bottomrow.ButtonAlignment;
import nl.topicus.cobra.web.components.shortcut.CobraKeyAction;

import org.apache.wicket.Page;
import org.apache.wicket.markup.html.link.IPageLink;
import org.apache.wicket.model.IDetachable;

/**
 * Button voor het importeren van CSV
 * 
 * @author vandekamp
 */
public class CsvImportButton extends AbstractPageBottomRowButton
{
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor voor een knop welke een {@link IPageLink} voor ons maakt. De Page
	 * target moet een constructor in de vorm van Page(Page returnPage)
	 * 
	 * @param bottomRow
	 * @param pageClass
	 * @param returnPage
	 */
	public CsvImportButton(BottomRowPanel bottomRow, Class< ? extends Page> pageClass,
			final Page returnPage)
	{
		super(bottomRow, "CSV-import", CobraKeyAction.LINKKNOP1, ButtonAlignment.RIGHT);
		setPageLink(new CsvImportButtonPageLink(pageClass, returnPage));
	}

	private class CsvImportButtonPageLink implements IPageLink, IDetachable
	{
		private static final long serialVersionUID = 1L;

		private Class< ? extends Page> pageClass;

		private Page returnPage;

		public CsvImportButtonPageLink(Class< ? extends Page> pageClass, Page returnPage)
		{
			this.pageClass = pageClass;
			this.returnPage = returnPage;
		}

		@Override
		public Page getPage()
		{
			return ReflectionUtil.invokeConstructor(pageClass, returnPage);
		}

		@Override
		public Class< ? extends Page> getPageIdentity()
		{
			return pageClass;
		}

		@Override
		public void detach()
		{
			if (returnPage != null)
				returnPage.detach();
		}
	}
}
