package nl.topicus.eduarte.tester;

import static org.easymock.EasyMock.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import nl.topicus.cobra.dao.BatchDataAccessHelper;
import nl.topicus.cobra.dao.DataAccessHelper;
import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.dao.EncryptionProvider;
import nl.topicus.cobra.dao.helpers.BatchExtendableDataViewDataAccessHelper;
import nl.topicus.cobra.dao.helpers.BatchGroupPropertySettingDataAccessHelper;
import nl.topicus.cobra.dao.hibernate.EmptyCriteriaInterceptor;
import nl.topicus.cobra.dao.hibernate.UniqueCheckDataAccessHelper;
import nl.topicus.cobra.dao.hibernate.UnproxyingHelper;
import nl.topicus.cobra.entities.IdObject;
import nl.topicus.cobra.entities.dataview.IExtendableDataViewComponentSetting;
import nl.topicus.cobra.util.TimeUtil;
import nl.topicus.cobra.web.behaviors.ServerCallAjaxBehaviour;
import nl.topicus.cobra.web.components.datapanel.CustomDataPanelId;
import nl.topicus.cobra.web.components.quicksearch.AbstractBaseSearchEditor;
import nl.topicus.cobra.web.components.wiquery.auto.QuickSearchField;
import nl.topicus.eduarte.app.EduArteContext;
import nl.topicus.eduarte.app.EduArteModuleKey;
import nl.topicus.eduarte.app.security.AbstractEduArtePrincipalSource;
import nl.topicus.eduarte.core.principals.App;
import nl.topicus.eduarte.dao.helpers.*;
import nl.topicus.eduarte.dao.webservices.MockPostcodeDataAccessHelperImpl;
import nl.topicus.eduarte.dao.webservices.PostcodeDataAccessHelper;
import nl.topicus.eduarte.entities.adres.SoortContactgegeven;
import nl.topicus.eduarte.entities.adres.StandaardContactgegeven;
import nl.topicus.eduarte.entities.bijlage.DocumentCategorie;
import nl.topicus.eduarte.entities.codenaamactief.ICodeNaamActiefEntiteit;
import nl.topicus.eduarte.entities.contract.SoortContract;
import nl.topicus.eduarte.entities.contract.TypeFinanciering;
import nl.topicus.eduarte.entities.examen.ExamenWorkflow;
import nl.topicus.eduarte.entities.groep.Groep;
import nl.topicus.eduarte.entities.groep.Groepstype;
import nl.topicus.eduarte.entities.inschrijving.RedenUitschrijving;
import nl.topicus.eduarte.entities.inschrijving.SoortVooropleiding;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis;
import nl.topicus.eduarte.entities.inschrijving.SoortVooropleiding.SoortOnderwijs;
import nl.topicus.eduarte.entities.jobs.logging.JobRun;
import nl.topicus.eduarte.entities.kenmerk.Kenmerk;
import nl.topicus.eduarte.entities.landelijk.*;
import nl.topicus.eduarte.entities.opleiding.Opleiding;
import nl.topicus.eduarte.entities.organisatie.Brin;
import nl.topicus.eduarte.entities.organisatie.Instelling;
import nl.topicus.eduarte.entities.organisatie.Locatie;
import nl.topicus.eduarte.entities.organisatie.OrganisatieEenheid;
import nl.topicus.eduarte.entities.organisatie.SoortOrganisatieEenheid;
import nl.topicus.eduarte.entities.personen.Deelnemer;
import nl.topicus.eduarte.entities.personen.Functie;
import nl.topicus.eduarte.entities.personen.Persoon;
import nl.topicus.eduarte.entities.personen.RedenUitDienst;
import nl.topicus.eduarte.entities.personen.Relatie;
import nl.topicus.eduarte.entities.personen.RelatieSoort;
import nl.topicus.eduarte.entities.productregel.Productregel;
import nl.topicus.eduarte.entities.security.authentication.Account;
import nl.topicus.eduarte.entities.security.authentication.BeheerderAccount;
import nl.topicus.eduarte.entities.security.authorization.AuthorisatieNiveau;
import nl.topicus.eduarte.entities.settings.DebiteurNummerSetting;
import nl.topicus.eduarte.entities.settings.GebruikLandelijkeExterneOrganisatiesSetting;
import nl.topicus.eduarte.entities.settings.RadiusServerConfiguration;
import nl.topicus.eduarte.entities.settings.RadiusServerSetting;
import nl.topicus.eduarte.entities.settings.ScreenSaverConfiguration;
import nl.topicus.eduarte.entities.settings.ScreenSaverSetting;
import nl.topicus.eduarte.entities.sidebar.Bookmark;
import nl.topicus.eduarte.entities.sidebar.BookmarkFolder;
import nl.topicus.eduarte.entities.sidebar.Bookmark.SoortBookmark;
import nl.topicus.eduarte.entities.taxonomie.Taxonomie;
import nl.topicus.eduarte.entities.vrijevelden.VrijVeld;
import nl.topicus.eduarte.tester.hibernate.DatabaseAction;
import nl.topicus.eduarte.tester.hibernate.MockHibernateDataAccessHelper;
import nl.topicus.eduarte.tester.matchers.ComponentMatcher;
import nl.topicus.eduarte.tester.matchers.ComponentTypeMatcher;
import nl.topicus.eduarte.tester.security.MockSecurityStrategy;
import nl.topicus.eduarte.zoekfilters.*;
import nl.topicus.onderwijs.duo.bron.bve.waardelijsten.NT2Niveau;
import nl.topicus.onderwijs.duo.bron.bve.waardelijsten.NT2Vaardigheid;

import org.apache.wicket.Component;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.Page;
import org.apache.wicket.WicketRuntimeException;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.FormComponentPanel;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.protocol.http.MockHttpServletRequest;
import org.apache.wicket.protocol.http.WebRequestCycle;
import org.apache.wicket.security.hive.authentication.DefaultSubject;
import org.apache.wicket.util.tester.DummyPanelPage;
import org.apache.wicket.util.tester.FormTester;
import org.apache.wicket.util.tester.ITestPageSource;
import org.apache.wicket.util.tester.TestPanelSource;
import org.apache.wicket.util.tester.WicketTester;
import org.easymock.EasyMock;
import org.easymock.IAnswer;
import org.springframework.beans.factory.InitializingBean;

public class EduArteTester extends WicketTester implements InitializingBean
{
	private final MockEduArteApp application;

	// private final List<AbstractEduArteModule> modules = new
	// ArrayList<AbstractEduArteModule>();

	private final MockSecurityStrategy securityStrategy;

	private List<Relatie> relaties = new ArrayList<Relatie>();

	private Map<NT2Vaardigheid, List<NT2Niveau>> nt2data;

	private TimeUtil timeUtil = TimeUtil.getInstance();

	private MockHibernateDataAccessHelper mockHibernateHelper;

	public EduArteTester(MockEduArteApp app)
	{
		super(app);

		application = app;

		securityStrategy = application.getSecurityStrategy();
	}

	@Override
	public void afterPropertiesSet()
	{
		securityStrategy.login(null);
		setupInstelling();
		setupRegistry();
		voerTestUitMetMedewerker();
	}

	/**
	 * Gets the database actions that happened for the current transaction. See
	 * clearTransaction() if you want to start with a clean slate during your tests.
	 */
	public List<DatabaseAction> getTransactionLog()
	{
		return mockHibernateHelper.getTransactionLog();
	}

	/**
	 * Clears the current transaction for use between requests (commit will persist the
	 * changes permanently for this test session-as long as the JVM runs).
	 */
	public void rollback()
	{
		mockHibernateHelper.batchRollback();
	}

	/**
	 * Clears the current transaction *log* for use between requests, but leaves persisted
	 * objects in the database in the uncommitted list.
	 */
	public void clearTransactionLog()
	{
		mockHibernateHelper.clearTransactionLog();
	}

	/**
	 * Gets the objects that were persisted (updated, inserted minus those that were
	 * deleted) from the current transaction. This excludes the objects that were deleted.
	 */
	public List<IdObject> getObjectsFromTransaction()
	{
		return mockHibernateHelper.getObjectsFromTransaction();
	}

	@Override
	public FormTester newFormTester(String path, boolean fillBlankString)
	{
		return new EduArteFormTester(path, (Form< ? >) getComponentFromLastRenderedPage(path),
			this, fillBlankString);
	}

	public Panel startEduArtePanel(final TestPanelSource testPanelSource)
	{
		return (Panel) startPage(new ITestPageSource()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public Page getTestPage()
			{
				return new EduArteDummyPanelPage(testPanelSource);
			}
		}).get(DummyPanelPage.TEST_PANEL_ID);
	}

	public void executeQuickSearchSelect(AbstractBaseSearchEditor< ? , ? > field, String value)
	{
		QuickSearchField< ? > input = field.getSearchField();
		for (Object curBehavior : input.getBehaviors())
		{
			if (curBehavior instanceof ServerCallAjaxBehaviour)
			{
				ServerCallAjaxBehaviour behavior = (ServerCallAjaxBehaviour) curBehavior;
				WebRequestCycle requestCycle = resolveRequestCycle();

				// when the requestcycle is not created via setupRequestAndResponse(true),
				// it can happen
				// that the request is not an ajax request -> we have to set the header
				// manually
				if (!requestCycle.getWebRequest().isAjax())
				{
					HttpServletRequest req = requestCycle.getWebRequest().getHttpServletRequest();
					if (req instanceof MockHttpServletRequest)
					{
						((MockHttpServletRequest) req).addHeader("Wicket-Ajax", "Yes");
					}
				}
				getServletRequest().setParameter("methodName", "onCallBack");
				getServletRequest().setParameter("argCount", "2");
				getServletRequest().setParameter("arg1", value);
				getServletRequest().setParameter("arg2", "");
				behavior.onRequest();

				// process the request target
				processRequestCycle(requestCycle);
				return;
			}
		}
		throw new WicketRuntimeException("No ServerCallAjaxBehaviour found on component " + field);
	}

	public <X extends Component> X first(final MarkupContainer root, ComponentMatcher matcher)
	{
		CollectingVisitor<X> visitor = new CollectingVisitor<X>(matcher, true);
		root.visitChildren(visitor);
		return visitor.matchedComponents.isEmpty() ? null : visitor.matchedComponents.get(0);
	}

	public <X extends Component> List<X> all(final MarkupContainer root, ComponentMatcher matcher)
	{
		CollectingVisitor<X> visitor = new CollectingVisitor<X>(matcher);
		root.visitChildren(visitor);
		return visitor.matchedComponents;
	}

	public <X extends Component> X first(final MarkupContainer root, Class<X> componentType)
	{
		CollectingVisitor<X> visitor =
			new CollectingVisitor<X>(new ComponentTypeMatcher(componentType), true);
		root.visitChildren(visitor);
		return visitor.matchedComponents.get(0);
	}

	public <X extends Component> List<X> all(MarkupContainer root, Class<X> componentType)
	{
		ComponentMatcher matcher = new ComponentTypeMatcher(componentType);
		CollectingVisitor<X> visitor = new CollectingVisitor<X>(matcher);
		root.visitChildren(componentType, visitor);
		return visitor.matchedComponents;
	}

	// public void addModule(String name, AbstractEduArteModule module)
	// {
	// application.getApplicationContext().putBean(name, module);
	// modules.add(module);
	// }

	public void logoff()
	{
		securityStrategy.logoff(null);
	}

	/**
	 * Sets the value on the input control.
	 */
	public void setValue(FormComponent< ? > input, String value)
	{
		getServletRequest().setParameter(input.getInputName(), value);
	}

	/**
	 * Renders a <code>Panel</code> defined in <code>TestPanelSource</code> inside a
	 * {@link Form}. The usage is similar to {@link #startPage(ITestPageSource)}. Please
	 * note that testing <code>Panel</code> must use the supplied
	 * <code>panelId<code> as a <code>Component</code> id.
	 * 
	 * <pre>
	 * tester.startFormPanel(new TestPanelSource()
	 * {
	 * 	public Panel getTestPanel(String panelId)
	 * 	{
	 * 		MyData mockMyData = new MyData();
	 * 		return new MyPanel(panelId, mockMyData);
	 * 	}
	 * });
	 * </pre>
	 * 
	 * @param factory
	 *            a <code>Panel</code> factory that creates test <code>Panel</code>
	 *            instances
	 * @return a rendered <code>Panel</code>
	 */
	public Panel startFormPanel(final TestPanelSource factory)
	{
		FormTestPage page = (FormTestPage) startPage(new ITestPageSource()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public Page getTestPage()
			{
				return new FormTestPage(factory);
			}
		});
		return (Panel) page.get(page.getPanelComponentPath());
	}

	/**
	 * Renders a <code>FormComponentPanel</code> defined in
	 * <code>TestFormComponentPanelSource</code> inside a {@link Form}. The usage is
	 * similar to {@link #startPage(ITestPageSource)}. Please note that testing
	 * <code>Panel</code> must use the supplied
	 * <code>panelId<code> as a <code>Component</code> id.
	 * 
	 * <pre>
	 * tester.startFormPanel(new TestPanelSource()
	 * {
	 * 	public FormComponentPanel getTestPanel(String panelId)
	 * 	{
	 * 		MyData mockMyData = new MyData();
	 * 		return new MyPanel(panelId, mockMyData);
	 * 	}
	 * });
	 * </pre>
	 * 
	 * @param factory
	 *            a <code>Panel</code> factory that creates test <code>Panel</code>
	 *            instances
	 * @return a rendered <code>Panel</code>
	 */
	public FormComponentPanel< ? > startFormPanel(final TestFormComponentPanelSource factory)
	{
		FormTestPage page = (FormTestPage) startPage(new ITestPageSource()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public Page getTestPage()
			{
				return new FormTestPage(factory);
			}
		});
		return (FormComponentPanel< ? >) page.get(page.getPanelComponentPath());
	}

	public void voerTestUitMetApplicatieBeheerder(AbstractEduArtePrincipalSource... principals)
	{
		BeheerderAccount beheerder = new BeheerderAccount();
		beheerder.setActief(true);
		beheerder.setAuthorisatieNiveau(AuthorisatieNiveau.APPLICATIE);
		beheerder.setGebruikersnaam("mockApplicatieBeheerder");

		DefaultSubject subject = new DefaultSubject();
		subject.addPrincipal(new App()
			.createPrincipal(nl.topicus.eduarte.app.security.actions.Instelling.class));
		for (AbstractEduArtePrincipalSource principal : principals)
		{
			subject.addPrincipal(principal
				.createPrincipal(nl.topicus.eduarte.app.security.actions.Instelling.class));
		}
		EduArteContext.get().setAccount(beheerder);
		application.setAccount(beheerder);
		securityStrategy.login(subject);
	}

	public void voerTestUitMetMedewerker(AbstractEduArtePrincipalSource... principals)
	{
		BeheerderAccount medewerker = new BeheerderAccount();
		medewerker.setActief(true);
		medewerker.setAuthorisatieNiveau(AuthorisatieNiveau.REST);
		medewerker.setGebruikersnaam("mockMedewerker");

		DefaultSubject subject = new DefaultSubject();
		for (AbstractEduArtePrincipalSource principal : principals)
		{
			subject.addPrincipal(principal
				.createPrincipal(nl.topicus.eduarte.app.security.actions.Instelling.class));
		}
		EduArteContext.get().setAccount(medewerker);
		application.setAccount(medewerker);
		securityStrategy.login(subject);
	}

	@Override
	public WebRequestCycle setupRequestAndResponse()
	{

		WebRequestCycle requestCycle = super.setupRequestAndResponse();

		clearStubReturnLists();
		return requestCycle;
	}

	@Override
	public WebRequestCycle setupRequestAndResponse(boolean isAjax)
	{
		WebRequestCycle requestCycle = super.setupRequestAndResponse(isAjax);
		return requestCycle;
	}

	private void setupInstelling()
	{
		Instelling instelling = new Instelling();
		instelling.setId(1L);
		instelling.setNaam("Mock Instelling");
		Brin brin = new Brin("00AA");
		brin.setId(0L);
		instelling.setBrincode(brin);
		application.setInstelling(instelling);
	}

	public Instelling getInstelling()
	{
		return application.getInstelling();
	}

	public Account getAccount()
	{
		return application.getAccount();
	}

	public void setRelaties(List<Relatie> relaties)
	{
		this.relaties.addAll(relaties);
	}

	private void clearStubReturnLists()
	{
		relaties.clear();
	}

	private void setupRegistry()
	{
		mockRegistry();
		mockGenericDataAccessHelper(); // must come first
		mockAccounts();
		mockBookmarks();
		mockBookmarkFolders();
		mockCustomDataPanel();
		mockDeelnemer();
		mockRelaties();
		mockCohort();
		mockGroep();
		mockFuncties();
		mockOrganisaties();
		mockInstellingen();
		mockSettings();
		mockSoortContactGegevens();
		mockRelatieSoort();
		mockTypeFinanciering();
		mockVerbintenis();
		mockRedenUitschrijving();
		mockExamenWorkflow();
		mockOrganisatieEenheid();
		mockLocatie();
		mockRedenUitDienst();
		mockLanden();
		mockNationaliteiten();
		mockSoortVooropleidingen();
		mockOpleidingen();
		mockVoorvoegsels();
		mockJobs();
		mockVrijVeld();
		mockTaxonomie();
		mockCodeNaamActief();
		mockKenmerk();
		mockBrin();
		mockPostcode();
		mockProductregels();
		mockResultaten();
		mockSoortContracten();
		mockToetsen();
		mockDocumenten();

		BatchDataAccessHelper< ? > helper =
			DataAccessRegistry.getHelper(BatchDataAccessHelper.class);
		helper.batchExecute();
	}

	private void mockAccounts()
	{
		AccountDataAccessHelper mock = createMock(AccountDataAccessHelper.class);
		replay(mock);
		register(AccountDataAccessHelper.class, mock);
	}

	private void mockRegistry()
	{
		// DataAccessRegistry registry = new MockDataAccessRegistry();
		// application.getApplicationContext().putBean(registry);
		// registry.setApplicationContext(application.getApplicationContext());
	}

	@SuppressWarnings("unchecked")
	private void addToMockDatabase(IdObject object)
	{
		BatchDataAccessHelper<IdObject> helper =
			DataAccessRegistry.getHelper(BatchDataAccessHelper.class);
		helper.saveOrUpdate(object);
	}

	private void mockOpleidingen()
	{
		OpleidingDataAccessHelper mock = createMock(OpleidingDataAccessHelper.class);
		List<Opleiding> opleidingen = new ArrayList<Opleiding>();
		Opleiding opl1 = new Opleiding();
		opl1.setNaam("1");
		opl1.setCode("1");
		opleidingen.add(opl1);

		expect(mock.listCount((OpleidingZoekFilter) anyObject())).andStubReturn(opleidingen.size());
		expect(mock.list((OpleidingZoekFilter) anyObject())).andStubReturn(opleidingen);
		expect(mock.list((OpleidingZoekFilter) anyObject(), anyInt(), anyInt())).andStubReturn(
			opleidingen);

		replay(mock);
		register(OpleidingDataAccessHelper.class, mock);
	}

	private void mockBookmarks()
	{
		BookmarkDataAccessHelper mock = createNiceMock(BookmarkDataAccessHelper.class);

		expect(mock.list((Account) anyObject(), (String) anyObject(), (SoortBookmark) anyObject()))
			.andStubReturn(new ArrayList<Bookmark>());

		expect(mock.list((BookmarkZoekFilter) anyObject(), anyInt(), anyInt())).andStubReturn(
			new ArrayList<Bookmark>());

		replay(mock);
		register(BookmarkDataAccessHelper.class, mock);
	}

	private void mockBookmarkFolders()
	{
		BookmarkFolderDataAccessHelper mock = createNiceMock(BookmarkFolderDataAccessHelper.class);

		expect(mock.list((Account) anyObject())).andStubReturn(new ArrayList<BookmarkFolder>());
		expect(mock.list((BookmarkFolderZoekFilter) anyObject(), anyInt(), anyInt()))
			.andStubReturn(new ArrayList<BookmarkFolder>());
		replay(mock);
		register(BookmarkFolderDataAccessHelper.class, mock);
	}

	@SuppressWarnings("unchecked")
	private void mockCustomDataPanel()
	{
		BatchExtendableDataViewDataAccessHelper mock =
			createNiceMock(BatchExtendableDataViewDataAccessHelper.class);

		expect(mock.getSettings((CustomDataPanelId) anyObject(), (Serializable) anyObject()))
			.andStubReturn(new ArrayList<IExtendableDataViewComponentSetting>());
		replay(mock);
		register(BatchExtendableDataViewDataAccessHelper.class, mock);

		BatchGroupPropertySettingDataAccessHelper mock2 =
			createNiceMock(BatchGroupPropertySettingDataAccessHelper.class);

		replay(mock2);
		register(BatchGroupPropertySettingDataAccessHelper.class, mock2);
	}

	@SuppressWarnings("unchecked")
	private void mockDeelnemer()
	{
		NummerGeneratorDataAccessHelper mockNumbers =
			createMock(NummerGeneratorDataAccessHelper.class);
		expect(mockNumbers.newDeelnemernummer()).andStubReturn(12345);
		expect(mockNumbers.newDebiteurnummer()).andStubReturn(56789L);

		replay(mockNumbers);
		register(NummerGeneratorDataAccessHelper.class, mockNumbers);

		setupDeelnemer();

		BatchDataAccessHelper<IdObject> helper =
			DataAccessRegistry.getHelper(BatchDataAccessHelper.class);

		DeelnemerDataAccessHelper mockDeelnemers = createMock(DeelnemerDataAccessHelper.class);
		expect(mockDeelnemers.get((Long) anyObject())).andStubReturn(
			helper.get(Deelnemer.class, 102446L));
		expect(mockDeelnemers.list((DeelnemerZoekFilter) anyObject(), anyInt(), anyInt()))
			.andStubReturn(new ArrayList<Deelnemer>());
		expect(mockDeelnemers.list((DeelnemerZoekFilter) anyObject())).andStubReturn(
			new ArrayList<Deelnemer>());

		replay(mockDeelnemers);
		register(DeelnemerDataAccessHelper.class, mockDeelnemers);

		PersoonDataAccessHelper mockPersoon = createMock(PersoonDataAccessHelper.class);
		expect(mockPersoon.get((Long) anyObject())).andStubReturn(
			helper.get(Persoon.class, 102447L));

		replay(mockPersoon);
		register(PersoonDataAccessHelper.class, mockPersoon);

	}

	private void setupDeelnemer()
	{
		Deelnemer deelnemer = new Deelnemer();
		deelnemer.setId(102446L);
		deelnemer.setDeelnemernummer(102446);

		Persoon persoon = new Persoon();
		persoon.setId(102447L);
		persoon.setAchternaam("Aajoud");
		persoon.setVoorletters("A");
		persoon.setVoornamen("Abdelkarim");
		persoon.setGeboortedatum(timeUtil.asDate(1980, 1, 1));
		deelnemer.setPersoon(persoon);

		Verbintenis verbintenis = new Verbintenis(deelnemer);
		verbintenis.setId(102448L);
		List<Verbintenis> verbintenissen = new ArrayList<Verbintenis>();
		verbintenissen.add(verbintenis);
		deelnemer.setVerbintenissen(verbintenissen);

		addToMockDatabase(deelnemer);
		addToMockDatabase(persoon);
		addToMockDatabase(verbintenis);
	}

	private void mockRelaties()
	{
		RelatieDataAccessHelper helper = EasyMock.createNiceMock(RelatieDataAccessHelper.class);

		EasyMock.expect(helper.list((RelatieZoekFilter) anyObject())).andStubReturn(relaties);
		EasyMock.expect(helper.list((RelatieZoekFilter) anyObject(), anyInt(), anyInt()))
			.andStubReturn(relaties);
		EasyMock.replay(helper);
		register(RelatieDataAccessHelper.class, helper);
	}

	private void mockSoortContracten()
	{
		SoortContractDataAccessHelper helper =
			EasyMock.createNiceMock(SoortContractDataAccessHelper.class);

		List<SoortContract> soortContracten = Collections.emptyList();
		EasyMock.expect(helper.list((SoortContractZoekFilter) anyObject())).andStubReturn(
			soortContracten);
		EasyMock.expect(helper.list((SoortContractZoekFilter) anyObject(), anyInt(), anyInt()))
			.andStubReturn(soortContracten);
		EasyMock.replay(helper);
		register(SoortContractDataAccessHelper.class, helper);
	}

	private void mockTypeFinanciering()
	{
		TypeFinancieringDataAccessHelper helper =
			EasyMock.createNiceMock(TypeFinancieringDataAccessHelper.class);

		List<TypeFinanciering> types = Collections.emptyList();
		EasyMock.expect(helper.list((TypeFinancieringZoekFilter) anyObject())).andStubReturn(types);
		EasyMock.expect(helper.list((TypeFinancieringZoekFilter) anyObject(), anyInt(), anyInt()))
			.andStubReturn(types);
		EasyMock.replay(helper);
		register(TypeFinancieringDataAccessHelper.class, helper);
	}

	private void mockCohort()
	{
		CohortDataAccessHelper helper = createNiceMock(CohortDataAccessHelper.class);
		expect(helper.list()).andStubReturn(new ArrayList<Cohort>());
		expect(helper.getCohortOpDatum((Date) anyObject())).andStubAnswer(new IAnswer<Cohort>()
		{
			@Override
			public Cohort answer()
			{
				Date arg = (Date) EasyMock.getCurrentArguments()[0];
				int beginjaar = TimeUtil.getInstance().getYear(arg);
				if (TimeUtil.getInstance().getMonth(arg) < Cohort.BEGIN_MONTH)
					beginjaar--;
				return Cohort.createCohort(beginjaar);
			}
		});
		replay(helper);
		register(CohortDataAccessHelper.class, helper);
	}

	private void mockFuncties()
	{
		FunctieDataAccessHelper mock = createMock(FunctieDataAccessHelper.class);

		List<Functie> functies = Arrays.asList(new Functie());
		expect(mock.listCount((FunctieZoekFilter) anyObject())).andStubReturn(functies.size());
		expect(mock.list((FunctieZoekFilter) anyObject(), anyInt(), anyInt())).andStubReturn(
			functies);
		replay(mock);
		register(FunctieDataAccessHelper.class, mock);
	}

	private void mockGenericDataAccessHelper()
	{
		mockHibernateHelper =
			new MockHibernateDataAccessHelper(application.getProvider(),
				new EmptyCriteriaInterceptor());

		register(UnproxyingHelper.class, mockHibernateHelper);
		register(DataAccessHelper.class, mockHibernateHelper);
		register(BatchDataAccessHelper.class, mockHibernateHelper);
		register(nl.topicus.cobra.dao.helpers.UniqueCheckDataAccessHelper.class,
			new UniqueCheckDataAccessHelper<IdObject>(application.getProvider(),
				new EmptyCriteriaInterceptor()));
		// register(HibernateDataAccessHelper.class, mockHibernateHelper);
	}

	private void mockGroep()
	{
		GroepDataAccessHelper mockGroepen = createMock(GroepDataAccessHelper.class);
		GroepstypeDataAccessHelper mockGroepstypen = createMock(GroepstypeDataAccessHelper.class);
		List<Groepstype> resultTypen = new ArrayList<Groepstype>();
		Groepstype type1 = new Groepstype();
		type1.setCode("1");
		type1.setNaam("1");
		type1.setActief(true);
		resultTypen.add(type1);

		expect(mockGroepen.listCount((GroepZoekFilter) anyObject())).andStubReturn(0);
		expect(mockGroepen.list((GroepZoekFilter) anyObject())).andStubReturn(
			new ArrayList<Groep>());
		expect(mockGroepen.list((GroepZoekFilter) anyObject(), anyInt(), anyInt())).andStubReturn(
			new ArrayList<Groep>());
		replay(mockGroepen);

		expect(mockGroepstypen.listCount((GroepstypeZoekFilter) anyObject())).andStubReturn(
			resultTypen.size());
		expect(mockGroepstypen.list((GroepstypeZoekFilter) anyObject())).andStubReturn(resultTypen);
		expect(mockGroepstypen.list((GroepstypeZoekFilter) anyObject(), anyInt(), anyInt()))
			.andStubReturn(resultTypen);
		replay(mockGroepstypen);

		register(GroepDataAccessHelper.class, mockGroepen);
		register(GroepstypeDataAccessHelper.class, mockGroepstypen);
	}

	private void mockInstellingen()
	{
		InstellingDataAccessHelper mock = createMock(InstellingDataAccessHelper.class);
		expect(mock.getApplicationTitle()).andStubReturn("Tribe KRD");
		replay(mock);
		register(InstellingDataAccessHelper.class, mock);
	}

	@SuppressWarnings("unchecked")
	private void mockOrganisaties()
	{
		OrganisatieDataAccessHelper mock = createMock(OrganisatieDataAccessHelper.class);

		List organisaties = Arrays.asList(application.getInstelling());
		expect(mock.getInlogDomeinen()).andStubReturn(organisaties);
		expect(
			mock
				.isModuleAfgenomen((EduArteModuleKey) anyObject(), (EncryptionProvider) anyObject()))
			.andStubReturn(true);
		addToMockDatabase(getInstelling());
		replay(mock);
		register(OrganisatieDataAccessHelper.class, mock);
	}

	private void mockSettings()
	{
		SettingsDataAccessHelper mockSettings = createMock(SettingsDataAccessHelper.class);

		ScreenSaverSetting sssetting = new ScreenSaverSetting();
		sssetting.setValue(new ScreenSaverConfiguration());
		expect(mockSettings.getSetting(ScreenSaverSetting.class)).andStubReturn(sssetting);

		RadiusServerSetting rssetting = new RadiusServerSetting();
		rssetting.setValue(new RadiusServerConfiguration());
		expect(mockSettings.getSetting(RadiusServerSetting.class)).andStubReturn(rssetting);

		GebruikLandelijkeExterneOrganisatiesSetting gleoSetting =
			new GebruikLandelijkeExterneOrganisatiesSetting();
		expect(mockSettings.getSetting(GebruikLandelijkeExterneOrganisatiesSetting.class))
			.andStubReturn(gleoSetting);

		DebiteurNummerSetting debNumSetting = new DebiteurNummerSetting();
		expect(mockSettings.getSetting(DebiteurNummerSetting.class)).andStubReturn(debNumSetting);

		replay(mockSettings);

		register(SettingsDataAccessHelper.class, mockSettings);
	}

	@SuppressWarnings("unchecked")
	private void mockSoortContactGegevens()
	{
		SoortContactgegevenDataAccessHelper mock =
			createMock(SoortContactgegevenDataAccessHelper.class);

		expect(mock.list((List<StandaardContactgegeven>) anyObject(), anyBoolean())).andStubReturn(
			new ArrayList<SoortContactgegeven>());
		expect(mock.list((SoortContactgegevenZoekFilter) anyObject())).andStubReturn(
			new ArrayList<SoortContactgegeven>());
		replay(mock);

		register(SoortContactgegevenDataAccessHelper.class, mock);
	}

	private void mockRelatieSoort()
	{
		RelatieSoortDataAccesHelper mock = createMock(RelatieSoortDataAccesHelper.class);

		expect(mock.list(true, false)).andStubReturn(new ArrayList<RelatieSoort>());
		expect(mock.list((RelatieSoortZoekFilter) anyObject())).andStubReturn(
			new ArrayList<RelatieSoort>());
		replay(mock);
		register(RelatieSoortDataAccesHelper.class, mock);
	}

	private void mockVerbintenis()
	{
		MockVerbintenisDataAccessHelper mock = new MockVerbintenisDataAccessHelper(this);
		register(VerbintenisDataAccessHelper.class, mock);
	}

	private void mockRedenUitschrijving()
	{
		RedenUitschrijvingDataAccessHelper mock =
			createMock(RedenUitschrijvingDataAccessHelper.class);
		expect(mock.list((RedenUitschrijvingZoekFilter) anyObject())).andStubReturn(
			new ArrayList<RedenUitschrijving>());
		replay(mock);
		register(RedenUitschrijvingDataAccessHelper.class, mock);
	}

	private void mockExamenWorkflow()
	{
		ExamenWorkflowDataAccessHelper mock = createMock(ExamenWorkflowDataAccessHelper.class);
		expect(mock.list()).andStubReturn(new ArrayList<ExamenWorkflow>());
		replay(mock);
		register(ExamenWorkflowDataAccessHelper.class, mock);
	}

	private void mockOrganisatieEenheid()
	{
		OrganisatieEenheidDataAccessHelper mock =
			createNiceMock(OrganisatieEenheidDataAccessHelper.class);
		SoortOrganisatieEenheidDataAccessHelper mock2 =
			createNiceMock(SoortOrganisatieEenheidDataAccessHelper.class);

		List<SoortOrganisatieEenheid> soortEenheden = new ArrayList<SoortOrganisatieEenheid>();
		SoortOrganisatieEenheid soortEenheid = new SoortOrganisatieEenheid();
		soortEenheid.setNaam("Standaard");
		soortEenheid.setCode("Std");
		soortEenheid.setActief(true);
		soortEenheid.setId(-1L);
		soortEenheden.add(soortEenheid);

		List<OrganisatieEenheid> eenheden = new ArrayList<OrganisatieEenheid>();
		OrganisatieEenheid eenheid = new OrganisatieEenheid();
		eenheid.setNaam("Standaard");
		eenheid.setAfkorting("Std");
		eenheid.setId(-1L);
		eenheid.setSoortOrganisatieEenheid(soortEenheid);
		eenheden.add(eenheid);

		expect(mock.list((OrganisatieEenheidZoekFilter) anyObject(), anyInt(), anyInt()))
			.andStubReturn(eenheden);
		expect(mock.list()).andStubReturn(eenheden);

		expect(mock2.list((SoortOrganisatieEenheidZoekFilter) anyObject(), anyInt(), anyInt()))
			.andStubReturn(soortEenheden);
		expect(mock2.list(anyBoolean())).andStubReturn(soortEenheden);

		replay(mock);
		replay(mock2);
		register(OrganisatieEenheidDataAccessHelper.class, mock);
		register(SoortOrganisatieEenheidDataAccessHelper.class, mock2);
	}

	private void mockLocatie()
	{
		LocatieDataAccessHelper mock = createNiceMock(LocatieDataAccessHelper.class);

		List<Locatie> locaties = new ArrayList<Locatie>();

		expect(mock.list((LocatieZoekFilter) anyObject(), anyInt(), anyInt())).andStubReturn(
			locaties);

		replay(mock);
		register(LocatieDataAccessHelper.class, mock);
	}

	@SuppressWarnings("unchecked")
	private void mockLanden()
	{
		LandDataAccessHelper mock = createNiceMock(LandDataAccessHelper.class);

		Land land = new Land();
		land.setId(6030L);
		land.setCode("6030");
		land.setNaam("Nederland");
		expect(mock.get("6030")).andStubReturn(land);
		expect(mock.list((LandelijkCodeNaamZoekFilter<Land>) anyObject(), anyInt(), anyInt()))
			.andStubReturn(Arrays.asList(land));
		expect(mock.list((LandelijkCodeNaamZoekFilter<Land>) anyObject())).andStubReturn(
			Arrays.asList(land));
		addToMockDatabase(land);

		Land onbekend = new Land();
		onbekend.setId(0L);
		onbekend.setCode("0000");
		onbekend.setNaam("Onbekend");
		expect(mock.get("0000")).andStubReturn(onbekend);
		addToMockDatabase(onbekend);

		Land landGB = new Land();
		landGB.setId(6039L);
		landGB.setCode("6039");
		landGB.setNaam("Grootbrittannië");
		expect(mock.get("6039")).andStubReturn(landGB);
		addToMockDatabase(landGB);

		Land landBE = new Land();
		landBE.setId(5010L);
		landBE.setCode("5010");
		landBE.setNaam("België");
		expect(mock.get("5010")).andStubReturn(landBE);
		addToMockDatabase(landBE);

		replay(mock);
		register(LandDataAccessHelper.class, mock);
	}

	private void mockNationaliteiten()
	{
		NationaliteitDataAccessHelper mock = createNiceMock(NationaliteitDataAccessHelper.class);

		Nationaliteit nationaliteit = new Nationaliteit();
		nationaliteit.setId(1L);
		nationaliteit.setCode("0001");
		nationaliteit.setNaam("Nederlandse");
		expect(mock.get("0001")).andStubReturn(nationaliteit);
		addToMockDatabase(nationaliteit);
		LandelijkCodeNaamZoekFilter<Nationaliteit> anyObject = anyObject();
		expect(mock.list(anyObject, anyInt(), anyInt()))
			.andStubReturn(Arrays.asList(nationaliteit));
		expect(mock.list(anyObject)).andStubReturn(Arrays.asList(nationaliteit));
		replay(mock);
		register(NationaliteitDataAccessHelper.class, mock);
	}

	private void mockSoortVooropleidingen()
	{
		SoortVooropleidingDataAccessHelper mock =
			createNiceMock(SoortVooropleidingDataAccessHelper.class);

		for (SoortOnderwijs soortOnderwijs : SoortOnderwijs.values())
		{
			SoortVooropleiding soortVooropleiding = new SoortVooropleiding();
			soortVooropleiding.setId(Long.valueOf(soortOnderwijs.ordinal()));
			soortVooropleiding.setCode(soortOnderwijs.getCode());
			soortVooropleiding.setNaam(soortOnderwijs.getNaam());
			soortVooropleiding.setSoortOnderwijsMetDiploma(soortOnderwijs);
			soortVooropleiding.setSoortOnderwijsZonderDiploma(SoortOnderwijs.Basisvorming);
			expect(mock.get(soortOnderwijs)).andStubReturn(soortVooropleiding);
			SoortVooropleidingZoekFilter filter = new SoortVooropleidingZoekFilter();
			expect(mock.list(filter)).andStubReturn(Arrays.asList(soortVooropleiding));
			addToMockDatabase(soortVooropleiding);
		}
		replay(mock);
		register(SoortVooropleidingDataAccessHelper.class, mock);
	}

	private void mockResultaten()
	{
		ResultaatDataAccessHelper mock = new ResultaatMockDataAccessHelper();
		register(ResultaatDataAccessHelper.class, mock);
	}

	private void mockToetsen()
	{
		ToetsDataAccessHelper mockDataAccessHelper = new ToetsMockDataAccessHelper();
		register(ToetsDataAccessHelper.class, mockDataAccessHelper);
	}

	private void mockDocumenten()
	{
		DocumentCategorieDataAccessHelper mock =
			createNiceMock(DocumentCategorieDataAccessHelper.class);

		List<DocumentCategorie> categories = new ArrayList<DocumentCategorie>();
		expect(mock.list((DocumentCategorieZoekFilter) anyObject())).andStubReturn(categories);

		replay(mock);
		register(DocumentCategorieDataAccessHelper.class, mock);
	}

	private void mockRedenUitDienst()
	{
		RedenUitDienstDataAccessHelper mock = createNiceMock(RedenUitDienstDataAccessHelper.class);
		List<RedenUitDienst> redenen = new ArrayList<RedenUitDienst>();

		expect(mock.list((RedenUitDienstZoekFilter) anyObject(), anyInt(), anyInt()))
			.andStubReturn(redenen);

		replay(mock);
		register(RedenUitDienstDataAccessHelper.class, mock);
	}

	private void mockVoorvoegsels()
	{
		VoorvoegselDataAccessHelper mock = createNiceMock(VoorvoegselDataAccessHelper.class);

		Voorvoegsel de = new Voorvoegsel();
		de.setNaam("de");

		Voorvoegsel den = new Voorvoegsel();
		den.setNaam("den");

		Voorvoegsel der = new Voorvoegsel();
		der.setNaam("der");

		Voorvoegsel van = new Voorvoegsel();
		van.setNaam("van");

		Voorvoegsel vande = new Voorvoegsel();
		vande.setNaam("van de");

		Voorvoegsel vanden = new Voorvoegsel();
		vanden.setNaam("van den");

		Voorvoegsel vander = new Voorvoegsel();
		vander.setNaam("van der");

		List<Voorvoegsel> voorvoegsels = Arrays.asList(de);
		expect(mock.list((VoorvoegselZoekFilter) anyObject(), anyInt(), anyInt())).andStubReturn(
			voorvoegsels);
		for (Voorvoegsel curVoorvoegsel : voorvoegsels)
		{
			expect(mock.get(curVoorvoegsel.getNaam())).andStubReturn(curVoorvoegsel);
		}
		replay(mock);
		register(VoorvoegselDataAccessHelper.class, mock);
	}

	private void mockVrijVeld()
	{
		VrijVeldDataAccessHelper mock = createNiceMock(VrijVeldDataAccessHelper.class);

		List<VrijVeld> vrijvelden = new ArrayList<VrijVeld>();

		expect(mock.list((VrijVeldZoekFilter) anyObject(), anyInt(), anyInt())).andStubReturn(
			vrijvelden);
		expect(mock.list((VrijVeldZoekFilter) anyObject())).andStubReturn(vrijvelden);

		replay(mock);
		register(VrijVeldDataAccessHelper.class, mock);
	}

	private void mockTaxonomie()
	{
		TaxonomieElementDataAccessHelper mock =
			createNiceMock(TaxonomieElementDataAccessHelper.class);

		expect(mock.listTaxonomien()).andStubReturn(new ArrayList<Taxonomie>());

		replay(mock);
		register(TaxonomieElementDataAccessHelper.class, mock);
	}

	@SuppressWarnings("unchecked")
	private void mockCodeNaamActief()
	{
		CodeNaamActiefLandelijkOfInstellingEntiteitDataAccessHelper<ICodeNaamActiefEntiteit, ICodeNaamActiefZoekFilter<ICodeNaamActiefEntiteit>> mock =
			createNiceMock(CodeNaamActiefLandelijkOfInstellingEntiteitDataAccessHelper.class);

		expect(mock.list((ICodeNaamActiefZoekFilter<ICodeNaamActiefEntiteit>) anyObject()))
			.andStubReturn(new ArrayList<ICodeNaamActiefEntiteit>());

		replay(mock);
		register(CodeNaamActiefLandelijkOfInstellingEntiteitDataAccessHelper.class, mock);
	}

	private void mockKenmerk()
	{
		KenmerkDataAccessHelper mock = createNiceMock(KenmerkDataAccessHelper.class);

		expect(mock.list((KenmerkZoekFilter) anyObject())).andStubReturn(new ArrayList<Kenmerk>());

		replay(mock);
		register(KenmerkDataAccessHelper.class, mock);
	}

	@SuppressWarnings("unchecked")
	private void mockJobs()
	{
		JobRunDataAccessHelper<JobRun, JobRunZoekFilter<JobRun>> mock =
			createNiceMock(JobRunDataAccessHelper.class);

		expect(mock.list((JobRunZoekFilter<JobRun>) anyObject(), anyInt(), anyInt())).andReturn(
			new ArrayList<JobRun>());
		expect(mock.listCount((JobRunZoekFilter<JobRun>) anyObject()))
			.andStubReturn(new Integer(0));

		replay(mock);
		register(JobRunDataAccessHelper.class, mock);
	}

	private void mockBrin()
	{
		BrinDataAccessHelper mock = createNiceMock(BrinDataAccessHelper.class);
		List<Brin> brins = new ArrayList<Brin>();
		brins.add(EduArteContext.get().getInstelling().getBrincode());

		expect(mock.list((BrinZoekFilter) anyObject())).andStubReturn(brins);

		replay(mock);
		register(BrinDataAccessHelper.class, mock);
	}

	private void mockPostcode()
	{
		// gebruik onze eigen mock.
		PostcodeDataAccessHelper mock = new MockPostcodeDataAccessHelperImpl();
		register(PostcodeDataAccessHelper.class, mock);

		Gemeente gemeente = new Gemeente();
		gemeente.setId(936L);
		gemeente.setCode("0150");
		gemeente.setBegindatum(timeUtil.asDate(1980, 1, 1));
		gemeente.setNaam("Deventer");
		Provincie provincie = new Provincie();
		provincie.setId(807L);
		provincie.setCode("23");
		provincie.setBegindatum(timeUtil.asDate(1980, 1, 1));
		provincie.setNaam("Overijssel");
		Regio regio = new Regio();
		regio.setId(824L);
		regio.setCode("9");
		regio.setBegindatum(timeUtil.asDate(1980, 1, 1));
		regio.setNaam("Stedendriehoek");
		Plaats plaats = new Plaats("Deventer");
		plaats.setGemeente(gemeente);
		plaats.setProvincie(provincie);
		plaats.setSorteerNaam("Deventer");
		plaats.setId(821L);
		plaats.setUniek(true);

		addToMockDatabase(gemeente);
		addToMockDatabase(provincie);
		addToMockDatabase(regio);
		addToMockDatabase(plaats);

		GemeenteDataAccessHelper gemeentes = createNiceMock(GemeenteDataAccessHelper.class);
		expect(gemeentes.get((String) anyObject())).andStubReturn(gemeente);
		ProvincieDataAccessHelper provincies = createNiceMock(ProvincieDataAccessHelper.class);
		expect(provincies.get((String) anyObject())).andStubReturn(provincie);
		PlaatsDataAccessHelper plaatsen = createNiceMock(PlaatsDataAccessHelper.class);
		expect(plaatsen.get((String) anyObject())).andStubReturn(plaats);

		replay(gemeentes);
		replay(provincies);
		replay(plaatsen);
		register(GemeenteDataAccessHelper.class, gemeentes);
		register(ProvincieDataAccessHelper.class, provincies);
		register(PlaatsDataAccessHelper.class, plaatsen);
	}

	private void mockProductregels()
	{
		ProductregelDataAccessHelper mock = createNiceMock(ProductregelDataAccessHelper.class);
		expect(mock.list((ProductregelZoekFilter) anyObject())).andStubReturn(
			new ArrayList<Productregel>());

		replay(mock);
		register(ProductregelDataAccessHelper.class, mock);
	}

	public <T extends DataAccessHelper< ? >> void register(Class<T> interfaceClass, T helper)
	{
		// application.getApplicationContext().putBean(interfaceClass.getName(), helper);
		DataAccessRegistry registry = DataAccessRegistry.getInstance();
		registry.register(interfaceClass, helper);
	}

	public RepeatingView getRepeatingView(String path)
	{
		Page renderedPage = getLastRenderedPage();
		assertComponent(path, RepeatingView.class);
		RepeatingView rv = (RepeatingView) renderedPage.get(path);
		return rv;
	}

	public ListView< ? > getListView(String path)
	{
		Page renderedPage = getLastRenderedPage();
		assertComponent(path, ListView.class);
		ListView< ? > rv = (ListView< ? >) renderedPage.get(path);
		return rv;
	}

	public void setNt2data(Map<NT2Vaardigheid, List<NT2Niveau>> nt2data)
	{
		this.nt2data = nt2data;
	}

	public Map<NT2Vaardigheid, List<NT2Niveau>> getNt2data()
	{
		return nt2data;
	}
}