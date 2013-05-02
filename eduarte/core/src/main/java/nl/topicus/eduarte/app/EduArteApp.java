/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.app;

import java.util.*;

import nl.topicus.cobra.app.CobraHibernateApp;
import nl.topicus.cobra.app.ServerAndClientTimeFilter;
import nl.topicus.cobra.entities.AdministratiePakket;
import nl.topicus.cobra.modules.CobraAnnotationAuthorizationModule;
import nl.topicus.cobra.modules.IModuleKey;
import nl.topicus.cobra.modules.ModuleComponentFactory;
import nl.topicus.cobra.security.IPrincipalSource;
import nl.topicus.cobra.security.IPrincipalSourceResolver;
import nl.topicus.cobra.security.RechtenSoort;
import nl.topicus.cobra.security.RechtenSoorten;
import nl.topicus.cobra.security.impl.AnnotationHiveFactory;
import nl.topicus.cobra.util.ComponentScanner;
import nl.topicus.cobra.util.StringUtil;
import nl.topicus.cobra.util.TimeUtil;
import nl.topicus.cobra.web.components.form.AutoFormRegistry;
import nl.topicus.cobra.web.components.form.table.EduArteTableMarkupRenderer;
import nl.topicus.cobra.web.components.menu.MenuItemKey;
import nl.topicus.eduarte.app.resultaat.ResultaatMutex;
import nl.topicus.eduarte.app.resultaat.ResultaatMutexImpl;
import nl.topicus.eduarte.app.security.Display;
import nl.topicus.eduarte.app.security.EduArtePrincipal;
import nl.topicus.eduarte.app.security.PrincipalGroup;
import nl.topicus.eduarte.app.security.actions.EduArteActionFactory;
import nl.topicus.eduarte.app.signalering.EventDescription;
import nl.topicus.eduarte.app.signalering.EventTransport;
import nl.topicus.eduarte.app.signalering.transport.DatabaseTransport;
import nl.topicus.eduarte.app.signalering.transport.EmailTransport;
import nl.topicus.eduarte.converters.SchooljaarConverter;
import nl.topicus.eduarte.entities.landelijk.Schooljaar;
import nl.topicus.eduarte.entities.organisatie.Organisatie;
import nl.topicus.eduarte.entities.security.authentication.Account;
import nl.topicus.eduarte.entities.signalering.Event;
import nl.topicus.eduarte.web.pages.IModuleEditPage;
import nl.topicus.eduarte.web.pages.SecurePage;
import nl.topicus.eduarte.web.pages.deelnemerportaal.home.DeelnemerportaalHomePage;
import nl.topicus.eduarte.web.pages.home.HomePage;
import nl.topicus.eduarte.web.pages.login.LoginPage;

import org.apache.wicket.Application;
import org.apache.wicket.IConverterLocator;
import org.apache.wicket.Request;
import org.apache.wicket.Response;
import org.apache.wicket.protocol.http.IRequestLogger;
import org.apache.wicket.protocol.http.WebRequest;
import org.apache.wicket.protocol.http.WebResponse;
import org.apache.wicket.request.target.coding.IndexedHybridUrlCodingStrategy;
import org.apache.wicket.resource.loader.ClassStringResourceLoader;
import org.apache.wicket.resource.loader.IStringResourceLoader;
import org.apache.wicket.security.actions.WaspAction;
import org.apache.wicket.security.hive.HiveMind;
import org.apache.wicket.util.convert.ConverterLocator;
import org.wicketstuff.htmlvalidator.HtmlValidationResponseFilter;

/**
 * @author marrink
 */
public class EduArteApp extends CobraHibernateApp
{
	private EduArteConfig configuration = null;

	private ResultaatMutex resultaatMutex;

	// wordt geset via spring
	private String OOServletURL;

	private List<EduArtePrincipal> principals;

	private List<Class< ? extends Event>> eventClasses;

	private List<EventTransport> transports = new ArrayList<EventTransport>();
	{
		transports.add(new DatabaseTransport());
		transports.add(new EmailTransport());
	}

	/**
	 * De property naam welke gelijk is aan de scheduler context bean def in de
	 * spring.xml.
	 */
	public static String APPLICATION_CONTEXT_SCHEDULER_CONTEXT_KEY = "applicationContext";

	/**
	 * De property naam welke gelijk is aan de applicatie bean def in de spring-web.xml
	 */
	public static String APPLICATION_KEY = "wicketApplication";

	/**
	 * Minimale datum waaraan voldoen moet worden om de datum te printen (???)
	 */
	public static final Date MIN_BEGINDATUM_VOOR_TOSTRING =
		TimeUtil.getInstance().isoStringAsDate("19000101");

	public static final String VERSION = "2.00.11-SNAPSHOT";

	/**
	 * @see nl.topicus.cobra.app.CobraHibernateApp#getVersion()
	 */
	@Override
	public String getVersion()
	{
		return VERSION;
	}

	/**
	 * 
	 * @return De titel van de applicatie op het inlogscherm (de werkelijke titel van de
	 *         applicatie kan per instelling verschillen, maar op het inlogscherm gaat dat
	 *         niet).
	 */
	public String getLoginTitle()
	{
		// TODO: Een geschikte titel vinden + dit in een configuratiebestand plaatsen.
		return "Educus";
	}

	public EduArteConfig getConfiguration()
	{
		if (configuration == null)
		{
			initEduArteConfiguration();
		}
		return configuration;
	}

	private void initEduArteConfiguration()
	{
		String configString = null;
		try
		{
			configString = System.getProperty("eduarte.config");
		}
		catch (SecurityException e)
		{
			// Ignore - we're not allowed to read system properties.
		}

		// If no system parameter check filter/servlet <init-param> and <context-param>
		if (configString == null)
		{
			configString = getInitParameter("eduarte.config");
		}
		if (configString == null)
		{
			configString = getServletContext().getInitParameter("eduarte.config");
		}

		// Return result if we have found it, else fall back to DEVELOPMENT mode
		// as the default.
		if (StringUtil.isNotEmpty(configString))
		{
			configuration = EduArteConfig.parse(configString);
		}
		if (configuration == null)
		{
			System.err
				.print("*************************************************************************\n"
					+ "*** WAARSCHUWING: EDUARTE DRAAIT IN ONTWIKKELMODE.                    ***\n"
					+ "***                                 ^^^^^^^^^^^^^                     ***\n"
					+ "*** Deze mode is automatisch geselecteerd omdat er geen configuratie  ***\n"
					+ "*** gevonden kon worden tijdens het opstarten. Je kan de configuratie ***\n"
					+ "*** aanpassen door één van de volgende opties te gebruiken:           ***\n"
					+ "***                                                                   ***\n"
					+ "*** 1. Systeem property in het startup script van de servletcontainer ***\n"
					+ "***    bijv. -Deduarte.config=productie                               ***\n"
					+ "*** 2. Init parameter voor Wicket filter/servlet                      ***\n"
					+ "*** 3. Init parameter voor de webapp context                          ***\n"
					+ "***                                                                   ***\n"
					+ "*** Geldige waarden zijn:                                             ***\n"
					+ "***    ontwikkel, test, acceptatie, productie                         ***\n"
					+ "***                                                                   ***\n"
					+ "*************************************************************************\n");
			System.err.flush();
			configuration = EduArteConfig.Ontwikkel;
		}
	}

	/**
	 * @see org.apache.wicket.security.swarm.SwarmWebApplication#setupActionFactory()
	 */
	@Override
	protected void setupActionFactory()
	{
		setActionFactory(new EduArteActionFactory(getClass().getName() + ":" + getHiveKey()));
	}

	@Override
	public EduArteActionFactory getActionFactory()
	{
		return (EduArteActionFactory) super.getActionFactory();
	}

	public List<EduArtePrincipal> getPrincipals()
	{
		return principals;
	}

	/**
	 * @see org.apache.wicket.security.swarm.SwarmWebApplication#setUpHive()
	 */
	@Override
	@SuppressWarnings("unchecked")
	protected void setUpHive()
	{
		List<IPrincipalSourceResolver<EduArtePrincipal>> resolvers =
			new ArrayList<IPrincipalSourceResolver<EduArtePrincipal>>();
		for (CobraAnnotationAuthorizationModule module : getModules(CobraAnnotationAuthorizationModule.class))
		{
			resolvers.add((IPrincipalSourceResolver<EduArtePrincipal>) module
				.getPrincipalSourceResolver());
		}
		AnnotationHiveFactory<EduArtePrincipal> factory =
			new AnnotationHiveFactory<EduArtePrincipal>(getActionFactory(), resolvers);
		HiveMind.registerHive(getHiveKey(), factory);
		principals = factory.getAllPrincipals();
	}

	/**
	 * @see org.apache.wicket.protocol.http.WebApplication#newRequestCycle(org.apache.wicket.Request,
	 *      org.apache.wicket.Response)
	 */
	@Override
	public EduArteRequestCycle newRequestCycle(Request request, Response response)
	{
		return new EduArteRequestCycle(this, (WebRequest) request, (WebResponse) response,
			getHibernateSessionFactory());
	}

	/**
	 * @see nl.topicus.cobra.app.CobraHibernateApp#newSession(org.apache.wicket.Request,
	 *      org.apache.wicket.Response)
	 */
	@Override
	public EduArteSession newSession(Request request, Response response)
	{
		return new EduArteSession(this, request);
	}

	/**
	 * @see org.apache.wicket.Application#getHomePage()
	 */
	@Override
	public Class< ? extends SecurePage> getHomePage()
	{
		Account account = EduArteContext.get().getAccount();
		if (account != null && RechtenSoort.DEELNEMER.equals(account.getRechtenSoort()))
		{
			return DeelnemerportaalHomePage.class;
		}
		return HomePage.class;
	}

	/**
	 * @see org.apache.wicket.security.WaspApplication#getLoginPage()
	 */
	@Override
	public Class<LoginPage> getLoginPage()
	{
		return LoginPage.class;
	}

	/**
	 * @see org.apache.wicket.security.swarm.SwarmWebApplication#init()
	 */
	@Override
	protected void init()
	{
		try
		{
			super.init();
			getMarkupSettings().setStripComments(false);

			getDebugSettings().setOutputMarkupContainerClassName(isDevelopment());
			getDebugSettings().setOutputComponentPath(false);

			removeClassStringResourceLoader(getResourceSettings().getStringResourceLoaders());
			getResourceSettings().addStringResourceLoader(new EduArteStringResourceLoader());
			getResourceSettings()
				.addStringResourceLoader(new ClassStringResourceLoader(getClass()));
			getResourceSettings().setLocalizer(new EduArteLocalizer());

			AutoFormRegistry.getInstance().registerMarkupRenderer(
				AutoFormRegistry.DEFAULT_MARKUP_RENDERER, new EduArteTableMarkupRenderer());

			getRequestCycleSettings().addResponseFilter(new ServerAndClientTimeFilter()
			{
				@Override
				protected String getUser()
				{
					Account account = EduArteContext.get().getAccount();
					if (account == null)
						return super.getUser();
					return account.getGebruikersnaam();
				}

				@Override
				protected String getDomain()
				{
					Organisatie organisatie = EduArteRequestCycle.get().getOrganisatie();
					if (organisatie == null)
						return super.getDomain();
					return organisatie.getNaam();
				}
			});
			if (isDevelopment())
			{
				HtmlValidationResponseFilter validatingFilter = new HtmlValidationResponseFilter();
				validatingFilter.setIgnoreKnownWicketBugs(false);
				validatingFilter.setIgnoreAutocomplete(true);
				getRequestCycleSettings().addResponseFilter(validatingFilter);
			}

			AdministratiePakket.getPakket().setNaam("EduArte");
			AdministratiePakket.getPakket().setVersie(getVersion());

			getRequestLoggerSettings().setRequestLoggerEnabled(true);
			getRequestLoggerSettings().setRecordSessionSize(false);
			getRequestLoggerSettings().setRequestsWindowSize(0);

			getEduarteScheduler().init();
			setResultaatMutex(new ResultaatMutexImpl());

			eventClasses =
				ComponentScanner.scanForClasses(Event.class, EventDescription.class,
					"nl.topicus.eduarte.**.entities");
		}
		catch (RuntimeException e)
		{
			log.error("Initialisation failed: " + e.getMessage(), e);
			throw e;
		}
		mount(new IndexedHybridUrlCodingStrategy("/login", getLoginPage()));

		// initialiseer de configuratie
		getConfiguration();
	}

	private void removeClassStringResourceLoader(List<IStringResourceLoader> stringResourceLoaders)
	{
		Iterator<IStringResourceLoader> it = stringResourceLoaders.iterator();
		while (it.hasNext())
		{
			if (it.next() instanceof ClassStringResourceLoader)
				it.remove();
		}
	}

	@Override
	protected IRequestLogger newRequestLogger()
	{
		return super.newRequestLogger();
	}

	/**
	 * @return De EduArte scheduler van deze applicatie.
	 */
	public EduArteScheduler getEduarteScheduler()
	{
		return (EduArteScheduler) super.getQuartzScheduler();
	}

	public void setEduArteScheduler(EduArteScheduler scheduler)
	{
		super.setQuartzScheduler(scheduler);
	}

	/**
	 * @return de url string van de OO Servlet. Deze Servlet wordt gebruikt om documenten
	 *         (rtf, docx etc) te converteren naar pdf.
	 */
	public String getOOServletURL()
	{
		return OOServletURL;
	}

	public void setOOServletURL(String servletURL)
	{
		OOServletURL = servletURL;
	}

	public ResultaatMutex getResultaatMutex()
	{
		return resultaatMutex;
	}

	public void setResultaatMutex(ResultaatMutex resultaatMutex)
	{
		this.resultaatMutex = resultaatMutex;
	}

	/**
	 * @return De EduArte app van de aanroepende thread
	 */
	public static EduArteApp get()
	{
		Set<String> keys = Application.getApplicationKeys();
		if (keys.size() > 1)
		{
			log.error("Meer dan 1 Wicket applicaties gevonden: {}, eerst gevonden wordt gebruikt",
				keys.size());
			for (String key : keys)
			{
				log.error("Gevonden Wicket application key: {}", key);
			}
		}
		return (EduArteApp) Application.get(keys.iterator().next());
	}

	@Override
	protected IConverterLocator newConverterLocator()
	{
		IConverterLocator locator = super.newConverterLocator();
		((ConverterLocator) locator).set(Schooljaar.class, new SchooljaarConverter());
		return locator;
	}

	/**
	 * @param <T>
	 * @param entityClass
	 * @param key
	 * @return {@link IModuleEditPage}
	 */
	public <T> Class< ? extends IModuleEditPage<T>> getModuleEditPage(Class<T> entityClass,
			MenuItemKey key)
	{
		List<Class< ? extends IModuleEditPage<T>>> ret =
			new ArrayList<Class< ? extends IModuleEditPage<T>>>();
		for (EduArteEditPageModule curModule : getActiveModules(EduArteEditPageModule.class,
			EduArteContext.get().getOrganisatie()))
		{
			Class< ? extends IModuleEditPage<T>> page =
				curModule.getModuleEditPage(entityClass, key);
			if (page != null)
				ret.add(page);
		}
		if (ret.size() == 1)
			return ret.get(0);
		else if (ret.size() < 1)
			return null;
		else
			throw new RuntimeException("Too many ModuleEditPages registred for " + entityClass);
	}

	/**
	 * Bepaalt of de modules aangegeven door de <tt>keys</tt> actief zijn voor de actieve
	 * organisatie. Is het zelfde als
	 * <tt>isModuleActive(EduArteContext.get().getOrganisatie(), keys)</tt>
	 */
	public boolean isModuleActive(IModuleKey... keys)
	{
		return super.isModuleActive(EduArteContext.get().getOrganisatie(), keys);
	}

	public List<IModuleKey> getActiveModules()
	{
		List<IModuleKey> ret = new ArrayList<IModuleKey>();
		for (EduArteModuleKey curKey : EduArteModuleKey.values())
		{
			if (isModuleActive(curKey))
				ret.add(curKey);
		}
		return ret;
	}

	public <T extends ModuleComponentFactory> List<T> getPanelFactories(Class<T> factoryClass)
	{
		return super.getPanelFactories(factoryClass, EduArteContext.get().getOrganisatie());
	}

	public List<Class< ? extends Event>> getEventClasses()
	{
		return eventClasses;
	}

	public List<EventTransport> getTransports()
	{
		return transports;
	}

	public List<Class< ? extends WaspAction>> getActions(RechtenSoort rechtenSoort)
	{
		List<Class< ? extends WaspAction>> ret = new ArrayList<Class< ? extends WaspAction>>();
		for (Class< ? extends WaspAction> curAction : getActionFactory().getEduArteActions())
		{
			RechtenSoorten soorten = curAction.getAnnotation(RechtenSoorten.class);
			if (soorten != null && Arrays.asList(soorten.value()).contains(rechtenSoort))
			{
				Module module = curAction.getAnnotation(Module.class);
				if (module == null || isModuleActive(module.value()))
					ret.add(curAction);
			}
		}
		return ret;
	}

	public Map<EduArtePrincipal, Set<EduArtePrincipal>> getImpliesRelation()
	{
		Map<EduArtePrincipal, Set<EduArtePrincipal>> ret =
			new HashMap<EduArtePrincipal, Set<EduArtePrincipal>>();
		List<EduArtePrincipal> allPrincipals = getPrincipals();
		for (EduArtePrincipal curPrincipal : allPrincipals)
		{
			Set<EduArtePrincipal> implied = new HashSet<EduArtePrincipal>();
			for (EduArtePrincipal curPrincipal2 : allPrincipals)
			{
				if (curPrincipal.implies(curPrincipal2))
					implied.add(curPrincipal2);
			}
			implied.remove(curPrincipal);
			ret.put(curPrincipal, implied);
		}
		return ret;
	}

	public List<PrincipalGroup> getPrincipalGroups()
	{
		List<IModuleKey> activeModules = getActiveModules();
		Map<Class< ? extends IPrincipalSource<EduArtePrincipal>>, PrincipalGroup> groups =
			new HashMap<Class< ? extends IPrincipalSource<EduArtePrincipal>>, PrincipalGroup>();
		for (EduArtePrincipal curPrincipal : getPrincipals())
		{
			boolean active = false;
			for (EduArteModuleKey curKey : curPrincipal.getModules())
				if (activeModules.contains(curKey))
				{
					active = true;
					break;
				}
			if (active)
			{
				Class< ? extends IPrincipalSource<EduArtePrincipal>> groupClass =
					curPrincipal.getGroupClass();
				if (groupClass.isAnnotationPresent(Display.class))
				{
					PrincipalGroup curGroup = groups.get(groupClass);
					if (curGroup == null)
					{
						curGroup = new PrincipalGroup(groupClass);
						groups.put(groupClass, curGroup);
					}
					curGroup.addPrincipal(curPrincipal);
				}
			}
		}
		return new ArrayList<PrincipalGroup>(groups.values());
	}

	public boolean isHogerOnderwijs()
	{
		return EduArteApp.get().isModuleActive(EduArteModuleKey.HOGER_ONDERWIJS);
	}

	/**
	 * @return "Deelnemer" of "Student", afhankelijk van module Hoger Onderwijs
	 */
	public String getDeelnemerTerm()
	{
		return isHogerOnderwijs() ? "Student" : "Deelnemer";
	}

	/**
	 * @return "Deelnemer" of "Student", afhankelijk van module Hoger Onderwijs
	 */
	public String getDeelnemerTermUpperCase()
	{
		return isHogerOnderwijs() ? "Studenten" : "Deelnemers";
	}

	/**
	 * @return "deelnemers" of "studenten" (lower case), afhankelijk van module Hoger
	 *         Onderwijs
	 */
	public String getDeelnemerTermMeervoud()
	{
		return isHogerOnderwijs() ? "studenten" : "deelnemers";
	}

	/**
	 * @return "Deelnemers" of "Studenten" (Upper case), afhankelijk van module Hoger
	 *         Onderwijs
	 */
	public String getDeelnemerTermMeervoudUpperCase()
	{
		return isHogerOnderwijs() ? "Studenten" : "Deelnemers";
	}

	/**
	 * @return "BPV" of "Stage" (lower case), afhankelijk van module Hoger Onderwijs
	 */
	public String getBPVTerm()
	{
		return isHogerOnderwijs() ? "Stage" : "BPV-";
	}
}
