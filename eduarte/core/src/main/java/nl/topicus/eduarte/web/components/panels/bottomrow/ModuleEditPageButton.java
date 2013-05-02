package nl.topicus.eduarte.web.components.panels.bottomrow;

import nl.topicus.cobra.reflection.ReflectionUtil;
import nl.topicus.cobra.web.components.menu.MenuItemKey;
import nl.topicus.cobra.web.components.panels.bottomrow.AbstractPageBottomRowButton;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.cobra.web.components.panels.bottomrow.ButtonAlignment;
import nl.topicus.cobra.web.components.shortcut.CobraKeyAction;
import nl.topicus.eduarte.app.EduArteApp;
import nl.topicus.eduarte.web.pages.IModuleEditPage;
import nl.topicus.eduarte.web.pages.SecurePage;

import org.apache.wicket.Page;
import org.apache.wicket.markup.html.link.IPageLink;
import org.apache.wicket.model.IModel;

/**
 * @author vandekamp
 * @param <T>
 */
public class ModuleEditPageButton<T> extends AbstractPageBottomRowButton
{
	private static final long serialVersionUID = 1L;

	public ModuleEditPageButton(BottomRowPanel bottomRow, Class<T> entityClass,
			SecurePage returnPage)
	{
		this(bottomRow, "Bewerken", CobraKeyAction.BEWERKEN, entityClass, null, returnPage, null);
	}

	public ModuleEditPageButton(BottomRowPanel bottomRow, Class<T> entityClass, MenuItemKey key,
			SecurePage returnPage)
	{
		this(bottomRow, "Bewerken", CobraKeyAction.BEWERKEN, entityClass, key, returnPage, null);
	}

	public ModuleEditPageButton(BottomRowPanel bottomRow, Class<T> entityClass,
			SecurePage returnPage, IModel<T> entityModel)
	{
		this(bottomRow, "Bewerken", CobraKeyAction.BEWERKEN, entityClass, null, returnPage,
			entityModel);
	}

	public ModuleEditPageButton(BottomRowPanel bottomRow, Class<T> entityClass, MenuItemKey key,
			SecurePage returnPage, IModel<T> entityModel)
	{
		this(bottomRow, "Bewerken", CobraKeyAction.BEWERKEN, entityClass, key, returnPage,
			entityModel);
	}

	public ModuleEditPageButton(BottomRowPanel bottomRow, String label, CobraKeyAction action,
			Class<T> entityClass, MenuItemKey key, SecurePage returnPage)
	{
		this(bottomRow, label, action, entityClass, key, returnPage, null);
	}

	public ModuleEditPageButton(BottomRowPanel bottomRow, String label, CobraKeyAction action,
			Class<T> entityClass, SecurePage returnPage)
	{
		this(bottomRow, label, action, entityClass, null, returnPage, null);
	}

	public ModuleEditPageButton(BottomRowPanel bottomRow, String label, CobraKeyAction action,
			Class<T> entityClass, SecurePage returnPage, IModel<T> entityModel)
	{
		this(bottomRow, label, action, entityClass, null, returnPage, entityModel);
	}

	public ModuleEditPageButton(BottomRowPanel bottomRow, String label, CobraKeyAction action,
			Class<T> entityClass, MenuItemKey key, SecurePage returnPage, IModel<T> entityModel)
	{
		super(bottomRow, label, action, ButtonAlignment.RIGHT);

		setDefaultModel(entityModel);

		final Class< ? extends IModuleEditPage<T>> pageClass =
			EduArteApp.get().getModuleEditPage(entityClass, key);
		if (pageClass != null)
			setPageLink(createPageLink(pageClass, returnPage));
		else
			this.setVisible(false);
	}

	private IPageLink createPageLink(final Class< ? extends IModuleEditPage<T>> pageClass,
			final SecurePage returnPage)
	{
		return new IPageLink()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public Page getPage()
			{
				return createTargetPage(pageClass, returnPage);
			}

			@SuppressWarnings("unchecked")
			@Override
			public Class< ? extends Page> getPageIdentity()
			{
				return (Class< ? extends Page>) pageClass;
			}
		};
	}

	protected Page createTargetPage(final Class< ? extends IModuleEditPage<T>> pageClass,
			final SecurePage returnPage)
	{
		return (Page) ReflectionUtil.invokeConstructor(pageClass, getEntity(),
			((returnPage == null) ? getReturnPage() : returnPage));
	}

	/**
	 * @return Het object dat bewerkt wordt op deze pagina.
	 */
	@SuppressWarnings("unchecked")
	protected T getEntity()
	{
		if (getDefaultModel() == null)
		{
			throw new IllegalStateException(
				"Je moet of een model meegeven aan de constructor, of getEntity() overschrijven");
		}
		return (T) getDefaultModelObject();
	}

	protected SecurePage getReturnPage()
	{
		throw new IllegalStateException(
			"Je moet of een returnPage meegeven aan de constructor, of getReturnPage() overschrijven");
	}
}
