/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.web.pages;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.Cookie;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.entities.IdObject;
import nl.topicus.cobra.reflection.MethodNotFoundException;
import nl.topicus.cobra.reflection.ReflectionUtil;
import nl.topicus.cobra.util.Asserts;
import nl.topicus.cobra.util.StringUtil;
import nl.topicus.cobra.web.components.menu.AbstractMenuBar;
import nl.topicus.cobra.web.components.menu.main.MainMenuItem;
import nl.topicus.cobra.web.components.modal.ModalWindowContainer;
import nl.topicus.cobra.web.components.panels.InfoButton;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.cobra.web.components.search.browser.SearchResultsPanel;
import nl.topicus.cobra.web.components.wiquery.SlidingFeedbackPanel;
import nl.topicus.cobra.web.components.wiquery.indicator.AjaxIndicator;
import nl.topicus.cobra.web.components.wiquery.resources.ResourceRefUtil;
import nl.topicus.cobra.web.pages.CobraSecurePage;
import nl.topicus.cobra.web.pages.IEditPage;
import nl.topicus.eduarte.app.EduArteApp;
import nl.topicus.eduarte.app.EduArteContext;
import nl.topicus.eduarte.app.EduArteModuleKey;
import nl.topicus.eduarte.app.EduArteRequestCycle;
import nl.topicus.eduarte.app.EduArteSession;
import nl.topicus.eduarte.app.sidebar.datastores.AbstractSideBarDataStore;
import nl.topicus.eduarte.dao.helpers.InstellingDataAccessHelper;
import nl.topicus.eduarte.entities.organisatie.OrganisatieEenheid;
import nl.topicus.eduarte.entities.personen.Deelnemer;
import nl.topicus.eduarte.entities.personen.Medewerker;
import nl.topicus.eduarte.entities.personen.Persoon;
import nl.topicus.eduarte.entities.security.authentication.Account;
import nl.topicus.eduarte.entities.sidebar.IContextInfoObject;
import nl.topicus.eduarte.web.components.menu.main.EduArteMainMenu;
import nl.topicus.eduarte.web.components.screensaver.ScreenSaver;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.Page;
import org.apache.wicket.WicketRuntimeException;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.feedback.FeedbackMessage;
import org.apache.wicket.feedback.IFeedbackMessageFilter;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.EmptyPanel;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.security.actions.Render;
import org.odlabs.wiquery.core.commons.WiQueryResourceManager;
import org.odlabs.wiquery.ui.commons.WiQueryUIPlugin;

/**
 * Basis pagina voor alle beveiligde pagina's in de applicatie. In feite dus alles behalve
 * de login / loguit pagina's. Al deze pagina's hebben een naam / titel, deze dient via de
 * constructor gezet te worden of via een {@link PageInfo} annotatie
 * 
 * @author marrink
 */
@WiQueryUIPlugin
public abstract class SecurePage extends CobraSecurePage
{
	private static final long serialVersionUID = 1L;

	/**
	 * Id voor de rapportage sectie van de sidebar
	 */
	public static final String ID_RAPPORTAGESIDEBAR = "rapportage";

	/**
	 * Id voor de sidebar
	 */
	public static final String ID_LAYSIDE = "laySide";

	/**
	 * status vlag om te controleren of een pagina wel createComponents heeft aangeroepen.
	 */
	private boolean createComponentsAangeroepen = false;

	private SearchResultsPanel< ? > searchResultsPanel;

	private WebMarkupContainer layHeader;

	private WebMarkupContainer layLeft;

	private BottomRowPanel bottomRow;

	private SlidingFeedbackPanel feedbackPanel;

	private Component title;

	/**
	 * @param selectedItem
	 */
	public SecurePage(MainMenuItem selectedItem)
	{
		this(null, null, selectedItem);
	}

	/**
	 * @param name
	 *            naam voor navigatie
	 * @param selectedItem
	 */
	protected SecurePage(String name, MainMenuItem selectedItem)
	{
		this(name, null, selectedItem);
	}

	/**
	 * @param model
	 * @param selectedItem
	 */
	public SecurePage(IModel< ? > model, MainMenuItem selectedItem)
	{
		this(null, model, selectedItem);
	}

	/**
	 * @param name
	 *            naam voor navigatie
	 * @param model
	 * @param selectedItem
	 */
	protected SecurePage(String name, IModel< ? > model, MainMenuItem selectedItem)
	{
		super(model, selectedItem, name);
		String applicationTitle =
			DataAccessRegistry.getHelper(InstellingDataAccessHelper.class).getApplicationTitle();
		add(new Label("pageTitle", applicationTitle));

		CenterContainer centerContainer = new CenterContainer("centerContainer", selectedItem);
		add(centerContainer);

		WebMarkupContainer favicon = new WebMarkupContainer("favicon");
		favicon.add(new AttributeModifier("href", true, new AbstractReadOnlyModel<String>()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public String getObject()
			{
				String cp = EduArteApp.get().getServletContext().getContextPath();
				if (EduArteApp.get().isModuleActive(EduArteModuleKey.HOGER_ONDERWIJS))
					return cp + "/assets/img/icons/alluris1.ico";

				return cp + "/assets/img/icons/eduarte.ico";
			}
		}));
		add(favicon);

		layHeader = new WebMarkupContainer("layHeader");
		layHeader.setOutputMarkupId(true);
		add(layHeader);
		layLeft = new WebMarkupContainer("layLeft")
		{
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isTransparentResolver()
			{
				return true;
			}

		};
		add(new Slidebar(ID_LAYSIDE, this));
		add(layLeft);

		EduArteMainMenu mainMenu = new EduArteMainMenu("mainmenu", selectedItem, this);
		layHeader.add(mainMenu);
		feedbackPanel = createFeedbackPanel();
		ContBox contBox = new ContBox("content", feedbackPanel, getPageStyle());
		layLeft.add(feedbackPanel);
		layLeft.add(contBox);
		layLeft.add(new ScreenSaver("screensaver"));

		String extraLayClass = getExtraLayClass();
		if (extraLayClass != null)
		{
			centerContainer.add(new AttributeAppender("class", new Model<String>(extraLayClass),
				" "));
		}
		add(new InfoButton("infoButton", this, applicationTitle));
	}

	/**
	 * @return The height of the screen of the client.
	 */
	protected int getScreenHeight()
	{
		Cookie cookie = EduArteRequestCycle.get().getWebRequest().getCookie("displayheight");
		if (cookie != null && StringUtil.isNumeric(cookie.getValue()))
		{
			return Integer.valueOf(cookie.getValue()).intValue();
		}
		return 768;
	}

	protected SlidingFeedbackPanel createFeedbackPanel()
	{
		SlidingFeedbackPanel feedback = new SlidingFeedbackPanel("feedback");
		feedback.setFilter(new IFeedbackMessageFilter()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public boolean accept(FeedbackMessage message)
			{
				return message.getReporter() == null || message.getReporter() == SecurePage.this
					|| message.getReporter().findParent(SecurePage.class) == SecurePage.this;
			}
		});
		feedback.setOutputMarkupId(true);
		return feedback;
	}

	/**
	 * @return The height of the screen of the client.
	 */
	protected int getScreenWidth()
	{
		Cookie cookie = EduArteRequestCycle.get().getWebRequest().getCookie("displaywidth");
		if (cookie != null && StringUtil.isNumeric(cookie.getValue()))
		{
			return Integer.valueOf(cookie.getValue()).intValue();
		}
		return 1024;
	}

	/**
	 * Kan overschreven worden om een extra class te geven die toegevoegd wordt aan de
	 * layHeader en layLeft divs in de HTML.
	 * 
	 * @return De class of null.
	 */
	protected String getExtraLayClass()
	{
		return null;
	}

	/**
	 * @param id
	 * @return Een extra header voor de pagina. Default geeft deze methode een onzichtare,
	 *         lege container terug.
	 */
	protected Component createExtraHeader(String id)
	{
		return new WebMarkupContainer(id).setVisible(false);
	}

	protected Component createLayDetails(String id)
	{
		return new WebMarkupContainer(id).setVisible(false);
	}

	/**
	 * @see nl.topicus.cobra.web.pages.BasePage#getNavigationStackName()
	 */
	@Override
	public final String getNavigationStackName()
	{
		return name;
	}

	/**
	 * Maakt oa de menubalk en de knoppen onderin. Dit dient handmatig door de subclasses
	 * aangeroepen te worden omdat de components die hier gemaakt worden nog niet genoeg
	 * informatie hebben als de via de constructor van de deze class gemaakt worden.
	 */
	protected void createComponents()
	{

		if (!isActionAuthorized(EduArteApp.get().getActionFactory().getAction(Render.class)))
			throw new nl.topicus.cobra.web.components.search.browser.UnAuthorizedInstantiationException(
				getClass(), getBasePageForContext());
		if (createComponentsAangeroepen)
			throw new IllegalStateException("createComponents() is al aangeroepen");
		createComponentsAangeroepen = true;
		layHeader.add(new AjaxIndicator("ajaxIndicator"));
		if (isEditable())
		{
			layHeader.add(new WebMarkupContainer("navigationStack").setVisible(false));
		}
		else
		{
			searchResultsPanel = new SearchResultsPanel<IdObject>("navigationStack")
			{
				private static final long serialVersionUID = 1L;

				@Override
				protected Object[] getAdditionalParameters()
				{
					return getAdditionalParametersForSearchResultsPanel();
				}

			};
			layHeader.add(searchResultsPanel);
		}
		AbstractMenuBar menu = createMenu("menu");
		// Diable het menu voor editpagina's.
		if (isEditable())
		{
			menu.setEnabled(false);
		}
		layHeader.add(menu);
		recreateBottomRow();
		layHeader.add(title = createTitle("title"));
		layHeader.add(createExtraHeader("layExtraHeader"));
		add(getPlaceholderComponent("placeholder"));
		add(new ModalWindowContainer("modalWindowContainer", this));
		layLeft.add(createLayDetails("layDetails"));
	}

	/**
	 * @return De search results panel van deze pagina, of null indien de pagina geen
	 *         heeft.
	 */
	protected SearchResultsPanel< ? > getSearchResultsPanel()
	{
		return searchResultsPanel;
	}

	/**
	 * Het menu voor deze pagina. Factory methode.
	 * 
	 * @param id
	 * @return menu, nooit null
	 */
	public abstract AbstractMenuBar createMenu(String id);

	public final AbstractMenuBar getMenu()
	{
		return (AbstractMenuBar) layHeader.get("menu");
	}

	/**
	 * Vult de bottom row met de knoppen.
	 * 
	 * @param panel
	 *            het panel
	 */
	protected void fillBottomRow(BottomRowPanel panel)
	{
	}

	public BottomRowPanel recreateBottomRow()
	{
		bottomRow = new BottomRowPanel("bottom");
		bottomRow.setOutputMarkupId(true);
		replaceBottomRow(bottomRow);
		fillBottomRow(bottomRow);
		return bottomRow;
	}

	public BottomRowPanel getBottomRow()
	{
		return bottomRow;
	}

	public void replaceBottomRow(BottomRowPanel bottomPanel)
	{
		layLeft.addOrReplace(bottomPanel);
	}

	/**
	 * Een label of panel met daarop de context-gevoelige header van de pagina.
	 * 
	 * @param id
	 * @return label of panel, nooit null
	 */
	public Component createTitle(String id)
	{
		return new Label(id, getName());
	}

	public final Component getTitle()
	{
		return title;
	}

	/**
	 * @return De default organisatie-eenheid van de ingelogde gebruiker
	 */
	protected static final OrganisatieEenheid getDefaultOrganisatieEenheid()
	{
		return EduArteContext.get().getDefaultOrganisatieEenheid();
	}

	/**
	 * De account van de ingelogde gebruiker.
	 * 
	 * @return account of null als er niemand is ingelogd
	 */
	protected static final Account getIngelogdeAccount()
	{
		return EduArteContext.get().getAccount();
	}

	/**
	 * De medewerker die ingelogd is.
	 * 
	 * @return ingelogde medewerker of een exception als niemand ingelogd is, of het geen
	 *         medewerker is. Er wordt wel null terug gegeven voor Account (dus niet een
	 *         subclass van).
	 */
	protected static final Medewerker getIngelogdeMedewerker()
	{
		return EduArteContext.get().getMedewerker();
	}

	/**
	 * De deelnemer die ingelogd is.
	 * 
	 * @return ingelogde deelnemer of null als het geen medewerker is.
	 */
	protected static final Deelnemer getIngelogdeDeelnemer()
	{
		return EduArteContext.get().getDeelnemer();
	}

	/**
	 * De medewerker of deelnemer die ingelogd is.
	 * 
	 * @return ingelogde persoon of null als niemand ingelogd is
	 */
	protected static final Persoon getIngelogdeGebruiker()
	{
		return EduArteContext.get().getGebruiker();
	}

	/**
	 * De style van de pagina. Standaard is dit {@link PageStyle#Default}.
	 * 
	 * @return style van de pagina
	 */
	public PageStyle getPageStyle()
	{
		return PageStyle.Default;
	}

	/**
	 * Trigger een ajax refresh op de feedback component.
	 * 
	 * @param target
	 */
	public final void refreshFeedback(AjaxRequestTarget target)
	{
		target.addComponent(feedbackPanel);
	}

	/**
	 * Trigger een ajax refresh op de title component.
	 * 
	 * @param target
	 */
	public final void refreshTitle(AjaxRequestTarget target)
	{
		target.addComponent(layHeader.get("title"));
	}

	/**
	 * Trigger een ajax refresh op de bottom row.
	 * 
	 * @param target
	 */
	public final void refreshBottomRow(AjaxRequestTarget target)
	{
		target.addComponent(bottomRow);
	}

	/**
	 * Flag om aan te geven of deze pagina gebruikt wordt om gegevens te wijzigen. Default
	 * is false.
	 * 
	 * @return true als gegevens gewijzigd kunnen worden middels deze pagina, anders
	 *         false.
	 */
	public final boolean isEditable()
	{
		return this instanceof IEditPage;
	}

	/**
	 * @param id
	 *            id van het component
	 * @return Component op de placeholder plek
	 */
	protected Component getPlaceholderComponent(String id)
	{
		return new EmptyPanel(id).setRenderBodyOnly(true);
	}

	@Override
	public String getIngelogdeGebruikersNaam()
	{
		if (getIngelogdeAccount().getEigenaar() != null)
			return getIngelogdeAccount().getEigenaar().getVolledigeNaam();
		return getIngelogdeAccount().getGebruikersnaam();
	}

	@Override
	public String getOrganisatieNaam()
	{
		return getIngelogdeAccount().getOrganisatie().getNaam();
	}

	/**
	 * @return Aanvullende parameterwaarden voor zoekresultaten panel. Geeft default null
	 *         terug.
	 */
	protected Object[] getAdditionalParametersForSearchResultsPanel()
	{
		return null;
	}

	protected Class< ? extends Page> getBasePageForContext()
	{
		return EduArteApp.get().getHomePage();
	}

	/**
	 * @return true als deze pagina bookmarks ondersteunt. Als de pagina bookmarks
	 *         ondersteunt, moet ook de methode
	 *         {@link #getBookmarkConstructorArguments(List, List)} overschreven worden
	 *         als de pagina geen default constructor (constructor zonder argumenten)
	 *         heeft.
	 */
	public boolean supportsBookmarks()
	{
		return !isEditable();
	}

	/**
	 * @return true als deze pagina contextinformatie voor bookmarks bevat. Dit kan
	 *         bijvoorbeeld de geselecteerde deelnemer of inschrijving zijn. Bij het
	 *         aanmaken van een bookmark kan de gebruiker kiezen of de contextinformatie
	 *         in de bookmark opgeslagen moeten worden, of dat deze elke keer opnieuw uit
	 *         de context gehaald moeten worden.
	 */
	public final boolean containsBookmarkContextInformation()
	{
		return !getContextParameterTypes().isEmpty();
	}

	/**
	 * @return Een lijst met alle parameter types die uit de context gehaald kunnen
	 *         worden.
	 */
	public List<Class< ? extends IContextInfoObject>> getContextParameterTypes()
	{
		return Collections.emptyList();
	}

	/**
	 * @param clazz
	 *            Het type contextinfo dat gezocht moet worden.
	 * @return De contextwaarde van deze pagina voor de gegeven class
	 */
	public IContextInfoObject getContextValue(Class< ? extends IContextInfoObject> clazz)
	{
		return null;
	}

	@Override
	public void contribute(WiQueryResourceManager resourceManager)
	{
		super.contribute(resourceManager);
		ResourceRefUtil.addMediaQueries(resourceManager);
	}

	/**
	 * Vult de gegeven lijsten met gegevens waarmee de huidige pagina (dus inclusief alle
	 * selecties van de gebruiker) opnieuw geconstrueerd kan worden. De twee lijsten
	 * moeten gevuld worden met respectievelijk de constructor argument types en de
	 * constructor argument waarden voor de constructor die aangeroepen moet worden om de
	 * pagina opnieuw te construeren.
	 * 
	 * @param ctorArgTypes
	 * @param ctorArgValues
	 */
	public void getBookmarkConstructorArguments(List<Class< ? >> ctorArgTypes,
			List<Object> ctorArgValues)
	{
		if (supportsBookmarks())
		{
			Asserts.assertEmptyCollection("ctorArgTypes", ctorArgTypes);
			Asserts.assertEmptyCollection("ctorArgValues", ctorArgValues);
		}
		else
		{
			throw new UnsupportedOperationException("Deze pagina ondersteunt geen bookmarks");
		}
	}

	/**
	 * @see org.apache.wicket.Page#onBeforeRender()
	 */
	@Override
	protected void onBeforeRender()
	{
		if (!createComponentsAangeroepen)
		{
			throw new IllegalStateException(
				"createComponents() is niet aangeroepen in de constructor van "
					+ getClass().getName());
		}
		// Verwerk sidebars
		for (AbstractSideBarDataStore datastore : EduArteSession.get().getSideBarDataStores())
		{
			datastore.onBeforeRender(this);
		}
		if (EduArteApp.get().isDevelopment())
		{
			if (supportsBookmarks())
			{
				List<Class< ? >> ctorArgTypes = new ArrayList<Class< ? >>();
				getBookmarkConstructorArguments(ctorArgTypes, new ArrayList<Object>());
				try
				{
					ReflectionUtil.findConstructor(getClass(), ctorArgTypes
						.toArray(new Class< ? >[ctorArgTypes.size()]));
				}
				catch (MethodNotFoundException e)
				{
					throw new WicketRuntimeException(
						"De pagina heeft geen constructor die voldoet aan de bookmark types", e);
				}
			}
		}
		super.onBeforeRender();
	}

	/**
	 * Probeert de default context page te construeren, en geeft <code>null</code> terug
	 * als dit niet werkt.
	 */
	public Page createDefaultContextPage()
	{
		Page responsePage = null;
		try
		{
			Class< ? extends Page> pageClass = getBasePageForContext();
			Object[] parameters = getAdditionalParametersForSearchResultsPanel();
			Object[] args = getConstructorArguments(getDefaultModelObject(), parameters);
			responsePage = ReflectionUtil.invokeConstructor(pageClass, args);
		}
		catch (Exception exception)
		{
			// negeer de exceptie en geef dan maar null terug...
			responsePage = null;
		}
		return responsePage;
	}

	private static Object[] getConstructorArguments(Object object, Object[] additionalParametersIn)
	{
		Object[] additionalParameters =
			additionalParametersIn == null ? new Object[0] : additionalParametersIn;

		Object[] constructorParameters = new Object[1 + additionalParameters.length];
		constructorParameters[0] = object;
		System.arraycopy(additionalParameters, 0, constructorParameters, 1,
			additionalParameters.length);
		return constructorParameters;
	}

	public SecurePage getSecurePage()
	{
		return (SecurePage) getPage();
	}
}
