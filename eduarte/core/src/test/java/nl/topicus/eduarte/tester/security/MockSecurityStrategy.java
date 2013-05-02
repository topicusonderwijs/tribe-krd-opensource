/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.tester.security;

import org.apache.wicket.Component;
import org.apache.wicket.model.IModel;
import org.apache.wicket.security.authentication.LoginException;
import org.apache.wicket.security.hive.authentication.LoginContext;
import org.apache.wicket.security.hive.authentication.Subject;
import org.apache.wicket.security.hive.authorization.Permission;
import org.apache.wicket.security.swarm.strategies.SwarmStrategy;

public class MockSecurityStrategy extends SwarmStrategy
{
	private final class MockLoginContext extends LoginContext
	{
		private final Object context;

		private MockLoginContext(Object context)
		{
			this.context = context;
		}

		@Override
		public Subject login()
		{
			return (Subject) context;
		}
	}

	private MockLoginContext previousContext = null;

	public MockSecurityStrategy(Object hiveQueen)
	{
		super(hiveQueen);
	}

	private static final long serialVersionUID = 1L;

	@Override
	public void destroy()
	{
	}

	@Override
	public boolean isUserAuthenticated()
	{
		return previousContext != null;
	}

	@Override
	public void login(Object context)
	{
		logoff(this.previousContext);
		if (context instanceof Subject)
		{
			try
			{
				MockLoginContext nextContext = new MockLoginContext(context);
				super.login(nextContext);
				this.previousContext = nextContext;
			}
			catch (LoginException e)
			{
				// yeah right... komt echt wel voor!
				throw new RuntimeException(e);
			}
		}
	}

	@Override
	public boolean logoff(Object context)
	{
		if (this.previousContext != null)
		{
			super.logoff(this.previousContext);
			this.previousContext = null;
		}
		return true;
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean isClassAuthenticated(Class clazz)
	{
		return isUserAuthenticated();
	}

	@Override
	public boolean isComponentAuthenticated(Component component)
	{
		return isUserAuthenticated();
	}

	@Override
	public boolean hasPermission(Permission permission, Subject subject)
	{
		if (subject != null)
		{
			return super.hasPermission(permission, subject);
		}
		return isUserAuthenticated();
	}

	@Override
	public boolean isModelAuthenticated(IModel< ? > model, Component component)
	{
		return isUserAuthenticated();
	}
}
