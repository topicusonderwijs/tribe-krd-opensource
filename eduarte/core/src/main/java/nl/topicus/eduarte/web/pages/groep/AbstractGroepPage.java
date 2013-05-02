/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.web.pages.groep;

import java.util.ArrayList;
import java.util.List;

import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.modelsv2.ModelFactory;
import nl.topicus.cobra.util.StringUtil;
import nl.topicus.cobra.web.components.menu.AbstractMenuBar;
import nl.topicus.cobra.web.components.menu.MenuItemKey;
import nl.topicus.cobra.web.security.RequiredSecurityCheck;
import nl.topicus.eduarte.app.EduArteSession;
import nl.topicus.eduarte.app.security.checks.GroepSecurityCheck;
import nl.topicus.eduarte.dao.helpers.GroepDataAccessHelper;
import nl.topicus.eduarte.entities.groep.Groep;
import nl.topicus.eduarte.entities.sidebar.IContextInfoObject;
import nl.topicus.eduarte.web.components.menu.GroepMenu;
import nl.topicus.eduarte.web.components.menu.main.CoreMainMenuItem;
import nl.topicus.eduarte.web.pages.SecurePage;

import org.apache.wicket.Component;
import org.apache.wicket.Page;
import org.apache.wicket.PageParameters;
import org.apache.wicket.authorization.UnauthorizedInstantiationException;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.protocol.http.servlet.AbortWithWebErrorCodeException;

/**
 * Basispagina voor alle pagina's die voor een individuele groep zijn.
 * 
 * @author hoeve
 */
@RequiredSecurityCheck(GroepSecurityCheck.class)
public abstract class AbstractGroepPage extends SecurePage
{
	private final MenuItemKey selectedMenuItem;

	/**
	 * @param selectedMenuItem
	 * @param groep
	 */
	public AbstractGroepPage(MenuItemKey selectedMenuItem, Groep groep)
	{
		super(ModelFactory.getModel(groep), CoreMainMenuItem.Groep);
		GroepSecurityCheck.replaceSecurityCheck(this, groep);
		this.selectedMenuItem = selectedMenuItem;
	}

	/**
	 * Haalt de groep op op basis van de parameters in de URL. Als er geen nummer in de
	 * parameters zit, of er kon geen groep worden bij dit nummer, dan wordt het request
	 * afgebroken met een 404 error.
	 * <p>
	 * Dit gaat er van uit dat de pagina gemount is met een Indexed parameter encoding
	 * strategy.
	 */
	protected static Groep getGroepFromPageParameters(PageParameters parameters)
	{
		if (!EduArteSession.get().isUserAuthenticated())
		{
			throw new UnauthorizedInstantiationException(null);
		}
		if (parameters.isEmpty() || StringUtil.isEmpty(parameters.getString("0")))
		{
			throw new AbortWithWebErrorCodeException(404, "Groepskaart heeft een groepnummer nodig");
		}
		Long groepnummer = parameters.getLong("0", -1);
		if (groepnummer == -1)
		{
			throw new AbortWithWebErrorCodeException(404, "Groepskaart heeft een groepnummer nodig");
		}
		GroepDataAccessHelper groepen = DataAccessRegistry.getHelper(GroepDataAccessHelper.class);
		Groep groep = groepen.get(groepnummer);
		if (groep == null)
		{
			throw new AbortWithWebErrorCodeException(404, "Groep " + groepnummer + " is onbekend");
		}
		return groep;
	}

	@Override
	protected Class< ? extends Page> getBasePageForContext()
	{
		return GroepKaartPage.class;
	}

	/**
	 * @return De deelnemer die getoond wordt op deze pagina.
	 */
	public Groep getContextGroep()
	{
		return getContextGroepModel().getObject();
	}

	@Override
	public String getContextOmschrijving()
	{
		return getContextValue(Groep.class).getContextInfoOmschrijving();
	}

	@Override
	public AbstractMenuBar createMenu(String id)
	{
		return new GroepMenu(id, selectedMenuItem, getContextGroepModel());
	}

	/**
	 * @return Het geselecteerde menuitem
	 */
	public MenuItemKey getSelectedMenuItem()
	{
		return selectedMenuItem;
	}

	@Override
	public void detachModels()
	{
		super.detachModels();
	}

	@SuppressWarnings("unchecked")
	public final IModel<Groep> getContextGroepModel()
	{
		return (IModel<Groep>) getDefaultModel();
	}

	@Override
	public void getBookmarkConstructorArguments(List<Class< ? >> ctorArgTypes,
			List<Object> ctorArgValues)
	{
		super.getBookmarkConstructorArguments(ctorArgTypes, ctorArgValues);
		ctorArgTypes.add(Groep.class);
		ctorArgValues.add(getContextGroepModel());
	}

	private static final List<Class< ? extends IContextInfoObject>> CONTEXT_PARAMETER_TYPES =
		new ArrayList<Class< ? extends IContextInfoObject>>(2);
	static
	{
		CONTEXT_PARAMETER_TYPES.add(Groep.class);
	}

	@Override
	public List<Class< ? extends IContextInfoObject>> getContextParameterTypes()
	{
		return CONTEXT_PARAMETER_TYPES;
	}

	@Override
	public IContextInfoObject getContextValue(Class< ? extends IContextInfoObject> clazz)
	{
		return getContextGroep();
	}

	@Override
	public Component createTitle(String id)
	{
		return new Label(id, new AbstractReadOnlyModel<String>()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public String getObject()
			{
				Groep contextGroep = getContextGroep();
				if (contextGroep.isSaved())
					return contextGroep.toString();
				return "Nieuwe groep";
			}
		});
	}
}
