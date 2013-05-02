package nl.topicus.eduarte.web.pages.medewerker;

import java.util.ArrayList;
import java.util.List;

import nl.topicus.cobra.modelsv2.ModelFactory;
import nl.topicus.cobra.web.components.menu.AbstractMenuBar;
import nl.topicus.cobra.web.components.menu.MenuItemKey;
import nl.topicus.cobra.web.security.RequiredSecurityCheck;
import nl.topicus.eduarte.app.security.checks.OrganisatieEenheidLocatieKoppelbaarSecurityCheck;
import nl.topicus.eduarte.entities.personen.Medewerker;
import nl.topicus.eduarte.entities.sidebar.IContextInfoObject;
import nl.topicus.eduarte.web.components.menu.MedewerkerMenu;
import nl.topicus.eduarte.web.components.menu.main.CoreMainMenuItem;
import nl.topicus.eduarte.web.pages.SecurePage;

import org.apache.wicket.Component;
import org.apache.wicket.Page;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

/**
 * Baseclass voor medewerkerpagina's.
 * 
 * @author loite
 */
@RequiredSecurityCheck(OrganisatieEenheidLocatieKoppelbaarSecurityCheck.class)
public abstract class AbstractMedewerkerPage extends SecurePage
{
	private final MenuItemKey selectedMenuItem;

	public AbstractMedewerkerPage(MenuItemKey selectedMenuItem, Medewerker medewerker)
	{
		super(CoreMainMenuItem.Medewerker);
		setDefaultModel(ModelFactory.getCompoundModel(medewerker));
		this.selectedMenuItem = selectedMenuItem;
		OrganisatieEenheidLocatieKoppelbaarSecurityCheck.replaceSecurityCheck(this, medewerker);
	}

	/**
	 * @return De medewerker van deze pagina.
	 */
	public Medewerker getContextMedewerker()
	{
		return getContextMedewerkerModel().getObject();
	}

	@Override
	public AbstractMenuBar createMenu(String id)
	{
		return new MedewerkerMenu(id, getContextMedewerkerModel(), selectedMenuItem);
	}

	public MenuItemKey getSelectedMenuItem()
	{
		return selectedMenuItem;
	}

	@Override
	public final Component createTitle(String id)
	{
		return new Label(id, new PropertyModel<String>(getContextMedewerkerModel(),
			"persoon.volledigeNaam"));
	}

	@Override
	protected Class< ? extends Page> getBasePageForContext()
	{
		return MedewerkerkaartPage.class;
	}

	@Override
	public void getBookmarkConstructorArguments(List<Class< ? >> ctorArgTypes,
			List<Object> ctorArgValues)
	{
		super.getBookmarkConstructorArguments(ctorArgTypes, ctorArgValues);
		ctorArgTypes.add(Medewerker.class);
		ctorArgValues.add(getContextMedewerkerModel());
	}

	private static final List<Class< ? extends IContextInfoObject>> CONTEXT_PARAMETER_TYPES =
		new ArrayList<Class< ? extends IContextInfoObject>>(2);
	static
	{
		CONTEXT_PARAMETER_TYPES.add(Medewerker.class);
	}

	@Override
	public List<Class< ? extends IContextInfoObject>> getContextParameterTypes()
	{
		return CONTEXT_PARAMETER_TYPES;
	}

	@Override
	public IContextInfoObject getContextValue(Class< ? extends IContextInfoObject> clazz)
	{
		if (clazz == Medewerker.class)
		{
			return getContextMedewerker();
		}
		return null;
	}

	@Override
	public String getContextOmschrijving()
	{
		return getContextValue(Medewerker.class).getContextInfoOmschrijving();
	}

	@SuppressWarnings("unchecked")
	public IModel<Medewerker> getContextMedewerkerModel()
	{
		return (IModel<Medewerker>) getDefaultModel();
	}
}
