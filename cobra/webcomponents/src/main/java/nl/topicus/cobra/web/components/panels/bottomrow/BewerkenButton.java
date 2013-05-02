package nl.topicus.cobra.web.components.panels.bottomrow;

import nl.topicus.cobra.reflection.InvocationFailedException;
import nl.topicus.cobra.reflection.ReflectionUtil;
import nl.topicus.cobra.web.components.shortcut.CobraKeyAction;

import org.apache.wicket.Page;
import org.apache.wicket.markup.html.link.IPageLink;
import org.apache.wicket.model.IDetachable;
import org.apache.wicket.model.IModel;

/**
 * Button voor het bewerken van het object op het scherm.
 * 
 * @author loite
 */
public class BewerkenButton<T> extends AbstractPageBottomRowButton
{
	private static final long serialVersionUID = 1L;

	public BewerkenButton(BottomRowPanel bottomRow, Class< ? extends Page> pageClass)
	{
		super(bottomRow, "Bewerken", CobraKeyAction.BEWERKEN, ButtonAlignment.RIGHT, pageClass);
	}

	/**
	 * Constructor voor een knop welke een {@link IPageLink} voor ons maken. De Page
	 * target moet een constructor in de vorm van Page(IModel objectModel, Page
	 * returnPage) hebben.
	 * 
	 * @param bottomRow
	 * @param pageClass
	 * @param entityModel
	 * @param returnPage
	 */
	public BewerkenButton(BottomRowPanel bottomRow, Class< ? extends Page> pageClass,
			IModel<T> entityModel, final Page returnPage)
	{
		super(bottomRow, "Bewerken", CobraKeyAction.BEWERKEN, ButtonAlignment.RIGHT);
		setPageLink(new BewerkenButtonPageLink<T>(pageClass, entityModel, returnPage));
	}

	/**
	 * Constructor voor een knop welke een {@link IPageLink} voor ons maken. De Page
	 * target moet een constructor in de vorm van Page(IModel objectModel) hebben.
	 * 
	 * @param bottomRow
	 * @param pageClass
	 * @param entityModel
	 */
	public BewerkenButton(BottomRowPanel bottomRow, Class< ? extends Page> pageClass,
			IModel<T> entityModel)
	{
		super(bottomRow, "Bewerken", CobraKeyAction.BEWERKEN, ButtonAlignment.RIGHT);
		setPageLink(new BewerkenButtonPageLink<T>(pageClass, entityModel));
	}

	public BewerkenButton(BottomRowPanel bottomRow, IPageLink pagelink)
	{
		super(bottomRow, "Bewerken", CobraKeyAction.BEWERKEN, ButtonAlignment.RIGHT, pagelink);
	}

	/**
	 * Constructor
	 * 
	 * @param bottomRow
	 * @param pagelink
	 * @param label
	 */
	public BewerkenButton(BottomRowPanel bottomRow, IPageLink pagelink, String label)
	{
		super(bottomRow, label, CobraKeyAction.BEWERKEN, ButtonAlignment.RIGHT, pagelink);
	}

	/**
	 * Constructor
	 * 
	 * @param bottomRow
	 * @param page
	 * @deprecated evil constructor, making a new page on a rendering page is time
	 *             consuming and can be done via an {@link IPageLink} or one of my other
	 *             constructors.
	 */
	@Deprecated
	public BewerkenButton(BottomRowPanel bottomRow, Page page)
	{
		super(bottomRow, "Bewerken", CobraKeyAction.BEWERKEN, ButtonAlignment.RIGHT, page);
	}

	private class BewerkenButtonPageLink<Y> implements IPageLink, IDetachable
	{
		private static final long serialVersionUID = 1L;

		private Class< ? extends Page> pageClass;

		private IModel<Y> entityModel;

		private Page returnPage;

		public BewerkenButtonPageLink(Class< ? extends Page> pageClass, IModel<Y> entityModel,
				Page returnPage)
		{
			this.pageClass = pageClass;
			this.entityModel = entityModel;
			this.returnPage = returnPage;
		}

		public BewerkenButtonPageLink(Class< ? extends Page> pageClass, IModel<Y> entityModel)
		{
			this.pageClass = pageClass;
			this.entityModel = entityModel;
		}

		@Override
		public Page getPage()
		{
			try
			{
				return ReflectionUtil.invokeConstructor(pageClass, entityModel, returnPage);
			}
			catch (InvocationFailedException e)
			{
				return ReflectionUtil.invokeConstructor(pageClass, entityModel);
			}
		}

		@Override
		public Class< ? extends Page> getPageIdentity()
		{
			return pageClass;
		}

		@Override
		public void detach()
		{
			if (entityModel != null)
				entityModel.detach();

			if (returnPage != null)
				returnPage.detach();
		}
	}
}
