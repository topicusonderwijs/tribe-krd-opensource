/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.web.pages.deelnemer;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.modelsv2.ModelFactory;
import nl.topicus.cobra.util.StringUtil;
import nl.topicus.cobra.web.components.menu.AbstractMenuBar;
import nl.topicus.cobra.web.components.menu.MenuItemKey;
import nl.topicus.cobra.web.security.RequiredSecurityCheck;
import nl.topicus.eduarte.app.EduArteSession;
import nl.topicus.eduarte.app.security.checks.DeelnemerSecurityCheck;
import nl.topicus.eduarte.dao.helpers.DeelnemerDataAccessHelper;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis;
import nl.topicus.eduarte.entities.personen.Deelnemer;
import nl.topicus.eduarte.entities.sidebar.IContextInfoObject;
import nl.topicus.eduarte.entities.taxonomie.Verbintenisgebied;
import nl.topicus.eduarte.web.components.menu.DeelnemerMenu;
import nl.topicus.eduarte.web.components.menu.main.CoreMainMenuItem;
import nl.topicus.eduarte.web.components.panels.DeelnemerTitel;
import nl.topicus.eduarte.web.pages.SecurePage;
import nl.topicus.eduarte.web.pages.deelnemer.deelnemerkaart.DeelnemerkaartPage;

import org.apache.wicket.Page;
import org.apache.wicket.PageParameters;
import org.apache.wicket.authorization.UnauthorizedInstantiationException;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.protocol.http.servlet.AbortWithWebErrorCodeException;

/**
 * Basispagina voor alle pagina's die voor een individuele deelnemer zijn.
 * 
 * @author loite
 */
@RequiredSecurityCheck(DeelnemerSecurityCheck.class)
public abstract class AbstractDeelnemerPage extends SecurePage
{
	protected final IModel<Deelnemer> deelnemerModel;

	private final IModel<Verbintenis> verbintenisModel;

	private final MenuItemKey selectedMenuItem;

	/**
	 * @param selectedMenuItem
	 * @param deelnemer
	 */
	public AbstractDeelnemerPage(MenuItemKey selectedMenuItem, Deelnemer deelnemer)
	{
		this(selectedMenuItem, deelnemer, null);
	}

	/**
	 * @param selectedMenuItem
	 * @param deelnemer
	 * @param verbintenis
	 */
	public AbstractDeelnemerPage(MenuItemKey selectedMenuItem, Deelnemer deelnemer,
			Verbintenis verbintenis)
	{
		super(CoreMainMenuItem.Deelnemer);
		DeelnemerSecurityCheck.replaceSecurityCheck(this, deelnemer);
		this.deelnemerModel = ModelFactory.getModel(deelnemer);
		this.verbintenisModel = ModelFactory.getModel(verbintenis);
		this.selectedMenuItem = selectedMenuItem;
		setDefaultModel(deelnemerModel);
	}

	public AbstractDeelnemerPage(MenuItemKey selectedMenuItem,
			PropertyModel<Deelnemer> propertyModel)
	{
		this(selectedMenuItem, propertyModel.getObject());
	}

	/**
	 * Haalt de deelnemer op op basis van de parameters in de URL. Als er geen nummer in
	 * de parameters zit, of er kon geen deelnemer gevonden worden bij dit nummer, dan
	 * wordt het request afgebroken met een 404 error.
	 * <p>
	 * Dit gaat er van uit dat de pagina gemount is met een Indexed parameter encoding
	 * strategy.
	 */
	protected static Deelnemer getDeelnemerFromPageParameters(
			Class< ? extends SecurePage> pageClazz, PageParameters parameters)
	{
		if (!EduArteSession.get().isUserAuthenticated())
		{
			throw new UnauthorizedInstantiationException(pageClazz);
		}
		if (parameters.isEmpty() || StringUtil.isEmpty(parameters.getString("0")))
		{
			throw new AbortWithWebErrorCodeException(404,
				"Deelnemerpersonalia heeft een deelnemer nummer nodig");
		}
		Integer deelnemernummer = parameters.getInt("0", -1);
		if (deelnemernummer == -1)
		{
			throw new AbortWithWebErrorCodeException(404,
				"Deelnemerpersonalia heeft een deelnemernummer nodig");
		}
		DeelnemerDataAccessHelper deelnemers =
			DataAccessRegistry.getHelper(DeelnemerDataAccessHelper.class);
		Deelnemer deelnemer = deelnemers.getByDeelnemernummer(deelnemernummer);
		if (deelnemer == null)
		{
			throw new AbortWithWebErrorCodeException(404, "Deelnemer " + deelnemernummer
				+ " is onbekend");
		}
		return deelnemer;
	}

	/**
	 * @return De deelnemer die getoond wordt op deze pagina.
	 */
	public Deelnemer getContextDeelnemer()
	{
		return deelnemerModel.getObject();
	}

	/**
	 * @return De geselecteerde inschrijving.
	 */
	public Verbintenis getContextVerbintenis()
	{
		return verbintenisModel.getObject();
	}

	@Override
	public AbstractMenuBar createMenu(String id)
	{
		return new DeelnemerMenu(id, selectedMenuItem, getContextDeelnemerModel(),
			getContextVerbintenisModel());
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
		deelnemerModel.detach();
		verbintenisModel.detach();
	}

	@Override
	public final Panel createTitle(String id)
	{
		return new DeelnemerTitel(id, getContextDeelnemerModel(), getContextVerbintenisModel())
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void onSelectionChanged(Verbintenis inschrijving)
			{
				AbstractMenuBar menu = getMenu();
				menu.replaceWith(createMenu(menu.getId()));
				AbstractDeelnemerPage.this.onSelectionChanged(inschrijving);
			}

			@Override
			protected boolean showVerbintenis(Verbintenis verbintenis)
			{
				return AbstractDeelnemerPage.this.showVerbintenis(verbintenis);
			}

		};
	}

	/**
	 * Callback om wat te doen op het moment dat de selectie gewijzigd is.
	 * 
	 * @param verbintenis
	 *            de nieuwe selectie
	 */
	protected void onSelectionChanged(Verbintenis verbintenis)
	{

	}

	/**
	 * deze methode haalt een verbintenis uit de lijst van weer te geven verbintenissen
	 * als er false wordt terug gegeven
	 */
	@SuppressWarnings("unused")
	protected boolean showVerbintenis(Verbintenis verbintenis)
	{
		return true;
	}

	public final IModel<Deelnemer> getContextDeelnemerModel()
	{
		return deelnemerModel;
	}

	public final IModel<Verbintenis> getContextVerbintenisModel()
	{
		return verbintenisModel;
	}

	/**
	 * @return Een model dat een set met daarin alle verbinteniselementen van de
	 *         geselecteerde inschrijving teruggeeft.
	 */
	public final IModel<Set<Verbintenisgebied>> getVerbintenisElmentenSetModel()
	{
		return new LoadableDetachableModel<Set<Verbintenisgebied>>()
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected Set<Verbintenisgebied> load()
			{
				if (getContextVerbintenis() == null)
					return null;
				return getContextVerbintenis().getAlleVerbintenisgebieden();
			}

		};
	}

	@Override
	protected Class< ? extends Page> getBasePageForContext()
	{
		return DeelnemerkaartPage.class;
	}

	@Override
	public void getBookmarkConstructorArguments(List<Class< ? >> ctorArgTypes,
			List<Object> ctorArgValues)
	{
		super.getBookmarkConstructorArguments(ctorArgTypes, ctorArgValues);
		ctorArgTypes.add(Deelnemer.class);
		ctorArgTypes.add(Verbintenis.class);
		ctorArgValues.add(getContextDeelnemerModel());
		ctorArgValues.add(getContextVerbintenisModel());
	}

	private static final List<Class< ? extends IContextInfoObject>> CONTEXT_PARAMETER_TYPES =
		new ArrayList<Class< ? extends IContextInfoObject>>(2);
	static
	{
		CONTEXT_PARAMETER_TYPES.add(Deelnemer.class);
		CONTEXT_PARAMETER_TYPES.add(Verbintenis.class);
	}

	@Override
	public List<Class< ? extends IContextInfoObject>> getContextParameterTypes()
	{
		return CONTEXT_PARAMETER_TYPES;
	}

	@Override
	public IContextInfoObject getContextValue(Class< ? extends IContextInfoObject> clazz)
	{
		if (clazz == Deelnemer.class)
		{
			return getContextDeelnemer();
		}
		if (clazz == Verbintenis.class)
		{
			return getContextVerbintenis();
		}
		return null;
	}

	@Override
	public String getContextOmschrijving()
	{
		return getContextValue(Deelnemer.class).getContextInfoOmschrijving();
	}
}
