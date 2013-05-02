package nl.topicus.eduarte.jobs;

import nl.topicus.cobra.app.CobraApplication;
import nl.topicus.eduarte.app.security.authentication.EduArteSubject;

import org.apache.wicket.Component;
import org.apache.wicket.model.IModel;
import org.apache.wicket.security.hive.authentication.Subject;
import org.apache.wicket.security.swarm.strategies.SwarmStrategy;

public class JobAuthorizationStrategy extends SwarmStrategy
{
	private static final long serialVersionUID = 1L;

	private EduArteSubject subject;

	public JobAuthorizationStrategy(EduArteSubject subject)
	{
		super(CobraApplication.get().getHiveKey());
		this.subject = subject;
	}

	@Override
	public Subject getSubject()
	{
		return subject;
	}

	@Override
	@SuppressWarnings("unchecked")
	public boolean isClassAuthenticated(Class clazz)
	{
		return subject.isClassAuthenticated(clazz);
	}

	@Override
	public boolean isComponentAuthenticated(Component component)
	{
		return subject.isComponentAuthenticated(component);
	}

	@Override
	public boolean isModelAuthenticated(IModel< ? > model, Component component)
	{
		return subject.isModelAuthenticated(model, component);
	}

	@Override
	public boolean isUserAuthenticated()
	{
		return subject != null;
	}

	@Override
	public void login(Object context)
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean logoff(Object context)
	{
		throw new UnsupportedOperationException();
	}
}
