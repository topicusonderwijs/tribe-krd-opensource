package nl.topicus.cobra.hibernate;

import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

import java.sql.Connection;

import org.hibernate.EmptyInterceptor;
import org.hibernate.Interceptor;
import org.hibernate.SessionFactory;
import org.hibernate.classic.Session;
import org.junit.Assert;
import org.junit.Test;

public class InterceptingSessionFactoryTest
{
	@Test(expected = UnsupportedOperationException.class)
	public void openSessionWithInterceptorFails()
	{
		SessionFactory mockSessionFactory = mock(SessionFactory.class);
		Interceptor mockInterceptor = mock(Interceptor.class);
		InterceptingSessionFactory factory = new InterceptingSessionFactory(mockSessionFactory);
		factory.openSession(mockInterceptor);
	}

	@Test(expected = UnsupportedOperationException.class)
	public void openSessionWithConnectionAndInterceptorFails()
	{
		SessionFactory mockSessionFactory = mock(SessionFactory.class);
		Interceptor mockInterceptor = mock(Interceptor.class);
		Connection mockConnection = mock(Connection.class);
		InterceptingSessionFactory factory = new InterceptingSessionFactory(mockSessionFactory);
		factory.openSession(mockConnection, mockInterceptor);
	}

	@Test
	public void singleSessionAwareInterceptorReceivesSession()
	{
		final SessionFactory mockSessionFactory = mock(SessionFactory.class);
		final Session currentSession = mock(Session.class);

		when(mockSessionFactory.openSession()).thenReturn(currentSession);
		when(mockSessionFactory.openSession((Connection) anyObject())).thenReturn(currentSession);
		when(mockSessionFactory.openSession((Interceptor) anyObject())).thenReturn(currentSession);
		when(mockSessionFactory.openSession((Connection) anyObject(), (Interceptor) anyObject()))
			.thenReturn(currentSession);

		InterceptorFactory interceptorFactory = mock(InterceptorFactory.class);

		InterceptingSessionFactory factory = new InterceptingSessionFactory(mockSessionFactory);

		MockSessionAwareInterceptor interceptor = new MockSessionAwareInterceptor();
		when(interceptorFactory.createInterceptor(factory)).thenReturn(interceptor);
		factory.addInterceptorFactory(interceptorFactory);

		factory.openSession();
		Assert.assertTrue(interceptor.isSessionSet());
	}

	@Test
	public void multipleSessionAwareInterceptorsReceiveSession()
	{
		final SessionFactory mockSessionFactory = mock(SessionFactory.class);
		final Session currentSession = mock(Session.class);

		when(mockSessionFactory.openSession()).thenReturn(currentSession);
		when(mockSessionFactory.openSession((Connection) anyObject())).thenReturn(currentSession);
		when(mockSessionFactory.openSession((Interceptor) anyObject())).thenReturn(currentSession);
		when(mockSessionFactory.openSession((Connection) anyObject(), (Interceptor) anyObject()))
			.thenReturn(currentSession);

		InterceptingSessionFactory factory = new InterceptingSessionFactory(mockSessionFactory);

		InterceptorFactory interceptorFactory1 = mock(InterceptorFactory.class);
		MockSessionAwareInterceptor interceptor1 = new MockSessionAwareInterceptor();
		when(interceptorFactory1.createInterceptor(factory)).thenReturn(interceptor1);
		factory.addInterceptorFactory(interceptorFactory1);

		InterceptorFactory interceptorFactory2 = mock(InterceptorFactory.class);
		MockInterceptor interceptor2 = new MockInterceptor();
		when(interceptorFactory2.createInterceptor(factory)).thenReturn(interceptor2);
		factory.addInterceptorFactory(interceptorFactory2);

		InterceptorFactory interceptorFactory3 = mock(InterceptorFactory.class);
		MockSessionAwareInterceptor interceptor3 = new MockSessionAwareInterceptor();
		when(interceptorFactory3.createInterceptor(factory)).thenReturn(interceptor3);
		factory.addInterceptorFactory(interceptorFactory3);

		factory.openSession();

		Assert.assertTrue(interceptor1.isSessionSet());
		Assert.assertTrue(interceptor3.isSessionSet());
	}

	class MockInterceptor extends EmptyInterceptor
	{
		private static final long serialVersionUID = 1L;
	}

	class MockSessionAwareInterceptor extends EmptyInterceptor implements SessionAware
	{
		private static final long serialVersionUID = 1L;

		private Session session;

		@Override
		public void setSession(org.hibernate.Session session)
		{
			this.session = (Session) session;
		}

		public boolean isSessionSet()
		{
			return session != null;
		}
	}
}
