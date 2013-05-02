package nl.topicus.eduarte.web.pages.onderwijs.opleiding;

import java.util.ArrayList;
import java.util.List;

import nl.topicus.cobra.modelsv2.ModelFactory;
import nl.topicus.cobra.util.ComponentUtil;
import nl.topicus.cobra.web.components.menu.AbstractMenuBar;
import nl.topicus.cobra.web.components.menu.MenuItemKey;
import nl.topicus.cobra.web.security.RequiredSecurityCheck;
import nl.topicus.eduarte.app.security.checks.OrganisatieEenheidLocatieKoppelbaarSecurityCheck;
import nl.topicus.eduarte.entities.opleiding.Opleiding;
import nl.topicus.eduarte.entities.sidebar.IContextInfoObject;
import nl.topicus.eduarte.web.components.menu.OpleidingMenu;
import nl.topicus.eduarte.web.components.menu.main.CoreMainMenuItem;
import nl.topicus.eduarte.web.pages.SecurePage;

import org.apache.wicket.Component;
import org.apache.wicket.Page;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

/**
 * @author loite
 */
@RequiredSecurityCheck(OrganisatieEenheidLocatieKoppelbaarSecurityCheck.class)
public abstract class AbstractOpleidingPage extends SecurePage
{
	private final IModel<Opleiding> contextOpleidingModel;

	private final MenuItemKey selectedMenuItem;

	public AbstractOpleidingPage(MenuItemKey selectedMenuItem, Opleiding opleiding)
	{
		super(CoreMainMenuItem.Onderwijs);
		this.contextOpleidingModel = ModelFactory.getModel(opleiding);
		this.selectedMenuItem = selectedMenuItem;
		setDefaultModel(contextOpleidingModel);
	}

	public AbstractOpleidingPage(MenuItemKey selectedMenuItem, IModel<Opleiding> opleidingModel)
	{
		super(CoreMainMenuItem.Onderwijs);
		this.contextOpleidingModel = opleidingModel;
		this.selectedMenuItem = selectedMenuItem;
		setDefaultModel(contextOpleidingModel);
	}

	/**
	 * @return De opleiding van deze pagina.
	 */
	public Opleiding getContextOpleiding()
	{
		return contextOpleidingModel.getObject();
	}

	@Override
	public AbstractMenuBar createMenu(String id)
	{
		return new OpleidingMenu(id, contextOpleidingModel, selectedMenuItem);
	}

	@Override
	public final Component createTitle(String id)
	{
		return new Label(id, new PropertyModel<String>(getContextOpleidingModel(), "naam"));
	}

	@Override
	protected Class< ? extends Page> getBasePageForContext()
	{
		return OpleidingkaartPage.class;
	}

	@Override
	public void getBookmarkConstructorArguments(List<Class< ? >> ctorArgTypes,
			List<Object> ctorArgValues)
	{
		super.getBookmarkConstructorArguments(ctorArgTypes, ctorArgValues);
		ctorArgTypes.add(Opleiding.class);
		ctorArgValues.add(getContextOpleidingModel());
	}

	private static final List<Class< ? extends IContextInfoObject>> CONTEXT_PARAMETER_TYPES =
		new ArrayList<Class< ? extends IContextInfoObject>>(2);
	static
	{
		CONTEXT_PARAMETER_TYPES.add(Opleiding.class);
	}

	@Override
	public List<Class< ? extends IContextInfoObject>> getContextParameterTypes()
	{
		return CONTEXT_PARAMETER_TYPES;
	}

	@Override
	public IContextInfoObject getContextValue(Class< ? extends IContextInfoObject> clazz)
	{
		if (clazz == Opleiding.class)
		{
			return getContextOpleiding();
		}
		return null;
	}

	protected IModel<Opleiding> getContextOpleidingModel()
	{
		return contextOpleidingModel;
	}

	@Override
	public void detachModels()
	{
		super.detachModels();
		ComponentUtil.detachQuietly(contextOpleidingModel);
	}

	public MenuItemKey getSelectedMenuItem()
	{
		return selectedMenuItem;
	}
}
