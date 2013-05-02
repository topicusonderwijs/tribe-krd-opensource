package nl.topicus.eduarte.tester;

import nl.topicus.eduarte.app.EduArteApp;
import nl.topicus.eduarte.app.EduArteContext;
import nl.topicus.eduarte.app.EduArteRequestCycle;
import nl.topicus.eduarte.entities.organisatie.Instelling;
import nl.topicus.eduarte.entities.security.authentication.Account;
import nl.topicus.eduarte.tester.hibernate.MockHibernateSessionFactory;
import nl.topicus.eduarte.tester.security.MockSecurityStrategy;
import nl.topicus.eduarte.tester.security.MockSecurityStrategyFactory;
import nl.topicus.eduarte.tester.security.MockWaspActionFactory;

import org.apache.wicket.Application;
import org.apache.wicket.Request;
import org.apache.wicket.Response;
import org.apache.wicket.protocol.http.WebRequest;
import org.apache.wicket.protocol.http.WebResponse;
import org.hibernate.SessionFactory;

class MockEduArteApp extends EduArteApp
{
	// private ServletContext servletContext;

	private Instelling instelling;

	private Account account;

	private MockSecurityStrategy strategy;

	public MockEduArteApp()
	{
		setApplicationKey("eduarte");

		// applicationContext = new MockEduarteApplicationContext();
		// setEduArteScheduler(new MockEduArteScheduler());
		// HibernateSessionProvider provider = new
		// GeneralPurposeHibernateSessionProvider();
		// applicationContext.putBean(provider);
		// applicationContext.putBean(new DefaultCustomDataPanelService());
		// getApplicationContext().putBean(new DataAccessHelperObjectAccess());
		// applicationContext.putBean(new UniqueCheckDataAccessHelper<IdObject>(provider,
		// new EmptyCriteriaInterceptor()));
		// setApplicationContext(applicationContext);
	}

	// @Override
	// public MockEduarteApplicationContext getApplicationContext()
	// {
	// return applicationContext;
	// }
	@Override
	public boolean isDevelopment()
	{
		return false;
	}

	public Instelling getInstelling()
	{
		return instelling;
	}

	public void setInstelling(Instelling instelling)
	{
		this.instelling = instelling;
		EduArteContext.get().setOrganisatie(instelling);
	}

	public Account getAccount()
	{
		return account;
	}

	public void setAccount(Account account)
	{
		this.account = account;
	}

	@Override
	protected void init()
	{
		super.init();

		getDebugSettings().setComponentUseCheck(true);
	}

	//
	// @Override
	// public ServletContext getServletContext()
	// {
	// if (servletContext == null)
	// {
	// servletContext = new MockServletContext(this, getApplicationKey());
	// servletContext.setAttribute(
	// WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE, applicationContext);
	// }
	// return servletContext;
	// }

	@Override
	public EduArteRequestCycle newRequestCycle(Request request, Response response)
	{
		return new MockEduArteRequestCycle(this, (WebRequest) request, (WebResponse) response, null);
	}

	@Override
	protected void setupActionFactory()
	{
		setActionFactory(new MockWaspActionFactory(getClass().getName() + ":" + getHiveKey()));
	}

	@Override
	protected void setUpHive()
	{
		super.setUpHive();
	}

	public void setSecurityStrategy(MockSecurityStrategy strategy)
	{
		this.strategy = strategy;
	}

	public MockSecurityStrategy getSecurityStrategy()
	{
		if (strategy == null)
		{
			strategy = new MockSecurityStrategy(getHiveKey());
		}
		return strategy;
	}

	@Override
	protected void setupStrategyFactory()
	{
		setStrategyFactory(new MockSecurityStrategyFactory(getHiveKey(), getSecurityStrategy()));
	}

	@Override
	public String getConfigurationType()
	{
		return Application.DEVELOPMENT;
	}

	@Override
	public String getTemplateDir()
	{
		return "";
	}

	@Override
	public SessionFactory getHibernateSessionFactory()
	{
		return new MockHibernateSessionFactory();
	}
}
