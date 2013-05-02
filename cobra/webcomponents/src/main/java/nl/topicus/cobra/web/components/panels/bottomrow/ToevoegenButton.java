package nl.topicus.cobra.web.components.panels.bottomrow;

import nl.topicus.cobra.reflection.ReflectionUtil;
import nl.topicus.cobra.web.components.shortcut.CobraKeyAction;

import org.apache.wicket.Page;
import org.apache.wicket.markup.html.link.IPageLink;
import org.apache.wicket.model.IDetachable;

/**
 * Button voor het toevoegen van elementen.
 * 
 * @author loite
 */
public class ToevoegenButton extends AbstractPageBottomRowButton
{
	private static final long serialVersionUID = 1L;

	public ToevoegenButton(BottomRowPanel bottomRow, Class< ? extends Page> pageClass)
	{
		super(bottomRow, "Toevoegen", CobraKeyAction.TOEVOEGEN, ButtonAlignment.RIGHT, pageClass);
	}

	public ToevoegenButton(BottomRowPanel bottomRow, String label,
			Class< ? extends Page> pageClass, final Page returnPage)
	{
		super(bottomRow, label, CobraKeyAction.TOEVOEGEN, ButtonAlignment.RIGHT);
		setPageLink(new ToevoegenButtonPageLink(pageClass, returnPage));
	}

	public ToevoegenButton(BottomRowPanel bottomRow, IPageLink pagelink)
	{
		super(bottomRow, "Toevoegen", CobraKeyAction.TOEVOEGEN, ButtonAlignment.RIGHT, pagelink);
	}

	public ToevoegenButton(BottomRowPanel bottomRow, IPageLink pagelink, String label)
	{
		super(bottomRow, label, CobraKeyAction.TOEVOEGEN, ButtonAlignment.RIGHT, pagelink);
	}

	private class ToevoegenButtonPageLink implements IPageLink, IDetachable
	{
		private static final long serialVersionUID = 1L;

		private Class< ? extends Page> pageClass;

		private Page returnPage;

		public ToevoegenButtonPageLink(Class< ? extends Page> pageClass, Page returnPage)
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
