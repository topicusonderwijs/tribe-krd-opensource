package nl.topicus.eduarte.tester;

import nl.topicus.eduarte.app.EduArteContext;
import nl.topicus.eduarte.app.EduArteRequestCycle;
import nl.topicus.eduarte.entities.organisatie.Organisatie;

import org.apache.wicket.Response;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.protocol.http.WebRequest;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

public class MockEduArteRequestCycle extends EduArteRequestCycle
{
	private final MockEduArteApp mockApplication;

	public MockEduArteRequestCycle(WebApplication application, WebRequest request,
			Response response, SessionFactory sessionFactory)
	{
		super(application, request, response, sessionFactory);
		this.mockApplication = (MockEduArteApp) application;
	}

	@Override
	protected void onBeginRequest()
	{
		super.onBeginRequest();
		EduArteContext.get().setAccount(mockApplication.getAccount());
	}

	@Override
	public Organisatie getOrganisatie()
	{
		return mockApplication.getInstelling();
	}

	@Override
	public Session openHibernateSession()
	{
		return mockApplication.getHibernateSessionFactory().openSession();
	}
}
