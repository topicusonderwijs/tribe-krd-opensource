package nl.topicus.eduarte.web.pages.onderwijs.onderwijscatalogus;

import java.util.ArrayList;
import java.util.List;

import nl.topicus.cobra.util.ComponentUtil;
import nl.topicus.cobra.web.components.menu.AbstractMenuBar;
import nl.topicus.cobra.web.components.menu.MenuItemKey;
import nl.topicus.cobra.web.security.RequiredSecurityCheck;
import nl.topicus.eduarte.app.security.checks.OrganisatieEenheidLocatieKoppelbaarSecurityCheck;
import nl.topicus.eduarte.entities.onderwijsproduct.Onderwijsproduct;
import nl.topicus.eduarte.entities.sidebar.IContextInfoObject;
import nl.topicus.eduarte.web.components.menu.OnderwijsproductMenu;
import nl.topicus.eduarte.web.components.menu.main.CoreMainMenuItem;
import nl.topicus.eduarte.web.pages.SecurePage;

import org.apache.wicket.Component;
import org.apache.wicket.Page;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;

/**
 * Basepagina voor alle onderwijsproductpagina's.
 * 
 * @author loite
 */
@RequiredSecurityCheck(OrganisatieEenheidLocatieKoppelbaarSecurityCheck.class)
public abstract class AbstractOnderwijsproductPage extends SecurePage
{
	private final IModel<Onderwijsproduct> contextOnderwijsproductModel;

	private final MenuItemKey selectedMenuItem;

	public AbstractOnderwijsproductPage(MenuItemKey selectedMenuItem,
			IModel<Onderwijsproduct> onderwijsproductModel)
	{
		super(onderwijsproductModel, CoreMainMenuItem.Onderwijs);
		this.contextOnderwijsproductModel = onderwijsproductModel;
		this.selectedMenuItem = selectedMenuItem;
		setDefaultModel(contextOnderwijsproductModel);
	}

	/**
	 * @return Het onderwijsproduct van deze pagina.
	 */
	public Onderwijsproduct getContextOnderwijsproduct()
	{
		return contextOnderwijsproductModel.getObject();
	}

	@Override
	public AbstractMenuBar createMenu(String id)
	{
		return new OnderwijsproductMenu(id, contextOnderwijsproductModel, selectedMenuItem);
	}

	@Override
	public final Component createTitle(String id)
	{
		return new Label(id, new AbstractReadOnlyModel<String>()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public String getObject()
			{
				Onderwijsproduct contextProduct = getContextOnderwijsproduct();
				if (contextProduct.isSaved())
					return contextProduct.getCodeAndTitle() + getTitlePostfix();
				return "Nieuw onderwijsproduct";
			}
		});
	}

	protected String getTitlePostfix()
	{
		return "";
	}

	@Override
	protected Class< ? extends Page> getBasePageForContext()
	{
		return OnderwijsproductKaartPage.class;
	}

	@Override
	public void getBookmarkConstructorArguments(List<Class< ? >> ctorArgTypes,
			List<Object> ctorArgValues)
	{
		super.getBookmarkConstructorArguments(ctorArgTypes, ctorArgValues);
		ctorArgTypes.add(Onderwijsproduct.class);
		ctorArgValues.add(getContextOnderwijsproductModel());
	}

	private static final List<Class< ? extends IContextInfoObject>> CONTEXT_PARAMETER_TYPES =
		new ArrayList<Class< ? extends IContextInfoObject>>(2);
	static
	{
		CONTEXT_PARAMETER_TYPES.add(Onderwijsproduct.class);
	}

	@Override
	public List<Class< ? extends IContextInfoObject>> getContextParameterTypes()
	{
		return CONTEXT_PARAMETER_TYPES;
	}

	@Override
	public IContextInfoObject getContextValue(Class< ? extends IContextInfoObject> clazz)
	{
		if (clazz == Onderwijsproduct.class)
		{
			return getContextOnderwijsproduct();
		}
		return null;
	}

	@Override
	public String getContextOmschrijving()
	{
		return getContextValue(Onderwijsproduct.class).getContextInfoOmschrijving();
	}

	protected IModel<Onderwijsproduct> getContextOnderwijsproductModel()
	{
		return contextOnderwijsproductModel;
	}

	@Override
	public void detachModels()
	{
		super.detachModels();
		ComponentUtil.detachQuietly(contextOnderwijsproductModel);
	}

	public MenuItemKey getSelectedMenuItem()
	{
		return selectedMenuItem;
	}
}
