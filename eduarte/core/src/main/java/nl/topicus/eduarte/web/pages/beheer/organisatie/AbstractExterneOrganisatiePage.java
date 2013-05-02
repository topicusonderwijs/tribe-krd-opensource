package nl.topicus.eduarte.web.pages.beheer.organisatie;

import java.util.ArrayList;
import java.util.List;

import nl.topicus.cobra.modelsv2.ModelFactory;
import nl.topicus.cobra.web.components.menu.AbstractMenuBar;
import nl.topicus.cobra.web.components.menu.MenuItemKey;
import nl.topicus.eduarte.entities.organisatie.ExterneOrganisatie;
import nl.topicus.eduarte.entities.sidebar.IContextInfoObject;
import nl.topicus.eduarte.web.components.menu.ExterneOrganisatieMenu;
import nl.topicus.eduarte.web.components.menu.main.CoreMainMenuItem;
import nl.topicus.eduarte.web.pages.SecurePage;

import org.apache.wicket.Component;
import org.apache.wicket.Page;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

/**
 * Baseclass voor externe organisatiepagina's.
 * 
 * @author elferink
 */
public abstract class AbstractExterneOrganisatiePage extends SecurePage
{
	private final MenuItemKey selectedMenuItem;

	public AbstractExterneOrganisatiePage(MenuItemKey selectedMenuItem,
			ExterneOrganisatie externeOrganisatie)
	{
		super(CoreMainMenuItem.Relatie);
		setDefaultModel(ModelFactory.getCompoundModel(externeOrganisatie));
		this.selectedMenuItem = selectedMenuItem;
	}

	/**
	 * @return De externeOrganisatie van deze pagina.
	 */
	public ExterneOrganisatie getContextExterneOrganisatie()
	{
		return getContextExterneOrganisatieModel().getObject();
	}

	@Override
	public AbstractMenuBar createMenu(String id)
	{
		return new ExterneOrganisatieMenu(id, getContextExterneOrganisatieModel(), selectedMenuItem);
	}

	public MenuItemKey getSelectedMenuItem()
	{
		return selectedMenuItem;
	}

	@Override
	public Component createTitle(String id)
	{
		return new Label(id, new PropertyModel<String>(getContextExterneOrganisatieModel(), "naam"));
	}

	@Override
	protected Class< ? extends Page> getBasePageForContext()
	{
		return ExterneOrganisatieOverzichtPage.class;
	}

	@Override
	public void getBookmarkConstructorArguments(List<Class< ? >> ctorArgTypes,
			List<Object> ctorArgValues)
	{
		super.getBookmarkConstructorArguments(ctorArgTypes, ctorArgValues);
		ctorArgTypes.add(ExterneOrganisatie.class);
		ctorArgValues.add(getContextExterneOrganisatieModel());
	}

	private static final List<Class< ? extends IContextInfoObject>> CONTEXT_PARAMETER_TYPES =
		new ArrayList<Class< ? extends IContextInfoObject>>(2);
	static
	{
		CONTEXT_PARAMETER_TYPES.add(ExterneOrganisatie.class);
	}

	@Override
	public List<Class< ? extends IContextInfoObject>> getContextParameterTypes()
	{
		return CONTEXT_PARAMETER_TYPES;
	}

	@Override
	public IContextInfoObject getContextValue(Class< ? extends IContextInfoObject> clazz)
	{
		if (clazz == ExterneOrganisatie.class)
		{
			return getContextExterneOrganisatie();
		}
		return null;
	}

	@Override
	public String getContextOmschrijving()
	{
		return getContextValue(ExterneOrganisatie.class).getContextInfoOmschrijving();
	}

	@SuppressWarnings("unchecked")
	public IModel<ExterneOrganisatie> getContextExterneOrganisatieModel()
	{
		return (IModel<ExterneOrganisatie>) getDefaultModel();
	}
}
