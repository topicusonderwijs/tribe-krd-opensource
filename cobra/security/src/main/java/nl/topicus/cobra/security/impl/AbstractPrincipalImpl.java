package nl.topicus.cobra.security.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.WeakHashMap;

import nl.topicus.cobra.security.Description;
import nl.topicus.cobra.security.IPrincipalSource;
import nl.topicus.cobra.security.NoReadPrincipalSource;
import nl.topicus.cobra.security.Write;

import org.apache.wicket.security.actions.WaspAction;
import org.apache.wicket.security.actions.WaspActionFactory;
import org.apache.wicket.security.hive.authentication.Subject;
import org.apache.wicket.security.hive.authorization.Principal;
import org.apache.wicket.security.swarm.actions.SwarmAction;

public abstract class AbstractPrincipalImpl<T extends AbstractPrincipalImpl<T>> implements
		Principal
{
	/**
	 * Static cache for implies lookup.
	 * 
	 * @author marrink
	 */
	private static final class StaticCache
	{
		private Map<Subject, Map<String, Boolean>> cache =
			new WeakHashMap<Subject, Map<String, Boolean>>(100);

		private Boolean implies(Subject subject, String principalName)
		{
			Map<String, Boolean> innerCache = null;
			innerCache = cache.get(subject);
			if (innerCache != null)
				return innerCache.get(principalName);
			return null;
		}

		private void cache(Subject subject, String principalName, boolean result)
		{
			Map<String, Boolean> innerCache = null;
			innerCache = cache.get(subject);
			if (innerCache == null)
			{
				innerCache = new HashMap<String, Boolean>(100);
				cache.put(subject, innerCache);
			}
			innerCache.put(principalName, result);
		}
	}

	private static final StaticCache CACHE = new StaticCache();

	private static final long serialVersionUID = 1L;

	private IPrincipalSource<T> source;

	private Class< ? extends WaspAction> actionClass;

	public AbstractPrincipalImpl(IPrincipalSource<T> source,
			Class< ? extends WaspAction> actionClass)
	{
		this.source = source;
		this.actionClass = actionClass;
	}

	@Override
	public String getName()
	{
		return getSourceClass().getName() + "." + getAction().getName();
	}

	@Override
	@SuppressWarnings("unchecked")
	public boolean implies(Subject subject)
	{
		if (subject == null)
			return false;
		// we can synch on subject because it is unlikely a subject will access this
		// multithreaded.
		synchronized (subject)
		{
			String name = getName();
			Boolean b = CACHE.implies(subject, name);
			if (b != null)
			{
				return b;
			}
			for (Principal principal : subject.getPrincipals())
			{
				if (equals(principal)
					|| (principal instanceof AbstractPrincipalImpl && ((AbstractPrincipalImpl) principal)
						.implies(this)))
				{
					CACHE.cache(subject, name, true);
					return true;
				}
			}
			CACHE.cache(subject, name, false);
			return false;
		}
	}

	@SuppressWarnings("unchecked")
	public Class< ? extends IPrincipalSource<T>> getSourceClass()
	{
		return (Class< ? extends IPrincipalSource<T>>) source.getClass();
	}

	public Class< ? extends WaspAction> getActionClass()
	{
		return actionClass;
	}

	public SwarmAction getAction()
	{
		return (SwarmAction) getActionFactory().getAction(getActionClass());
	}

	abstract protected WaspActionFactory getActionFactory();

	@SuppressWarnings("unchecked")
	public boolean implies(T principal)
	{
		return getSourceClass().equals(principal.getSourceClass())
			&& getAction().implies(principal.getAction()) || source.isImplied((T) this, principal);
	}

	public String getDescription()
	{
		if (getSourceClass().isAnnotationPresent(Description.class))
			return getSourceClass().getAnnotation(Description.class).value();
		return "";
	}

	public Class< ? extends IPrincipalSource<T>> getGroupClass()
	{
		return getGroupClass(getSourceClass());
	}

	@SuppressWarnings("unchecked")
	private Class< ? extends IPrincipalSource<T>> getGroupClass(
			Class< ? extends IPrincipalSource<T>> checkClass)
	{
		if (checkClass.isAnnotationPresent(Write.class))
		{
			Write write = checkClass.getAnnotation(Write.class);
			if (!write.read().equals(NoReadPrincipalSource.class))
				return getGroupClass((Class< ? extends IPrincipalSource<T>>) write.read());
		}
		return checkClass;
	}

	public boolean isWrite()
	{
		return getSourceClass().isAnnotationPresent(Write.class);
	}

	@Override
	public String toString()
	{
		return getName();
	}

	@Override
	public int hashCode()
	{
		return getSourceClass().hashCode() ^ getActionClass().hashCode();
	}

	@Override
	@SuppressWarnings("unchecked")
	public boolean equals(Object obj)
	{
		if (obj instanceof AbstractPrincipalImpl)
		{
			AbstractPrincipalImpl<T> other = (AbstractPrincipalImpl<T>) obj;
			return getSourceClass().equals(other.getSourceClass())
				&& getActionClass().equals(other.getActionClass());
		}
		return false;
	}
}
