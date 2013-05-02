package nl.topicus.cobra.web.security;

import java.util.ArrayList;
import java.util.List;

import nl.topicus.cobra.reflection.ReflectionUtil;
import nl.topicus.cobra.util.ComponentUtil;

import org.apache.wicket.Component;
import org.apache.wicket.Page;
import org.apache.wicket.security.actions.WaspAction;
import org.apache.wicket.security.checks.AbstractSecurityCheck;
import org.apache.wicket.security.checks.ISecurityCheck;
import org.apache.wicket.security.checks.LinkSecurityCheck;
import org.apache.wicket.security.checks.SecurityChecks;

public class TargetBasedSecurityCheck extends AbstractSecurityCheck
{
	private static final long serialVersionUID = 1L;

	private ISecurityCheck combined = null;

	private ISecurityCheck wrapped = null;

	private boolean useAlternativeRenderCheck = false;

	private Component component;

	private Class< ? > target;

	public TargetBasedSecurityCheck(Component component, Class< ? > target)
	{
		this.component = component;
		this.target = target;
		wrapped = new LinkSecurityCheck(component, target);
	}

	public TargetBasedSecurityCheck(Component component, Page page)
	{
		this(component, page.getClass());
		this.combined = ComponentUtil.getSecurityCheck(page);
	}

	public void setUseAlternativeRenderCheck(boolean useAlternativeRenderCheck)
	{
		this.useAlternativeRenderCheck = useAlternativeRenderCheck;
		tryToSetAlternativeRenderCheck(wrapped);
	}

	private ISecurityCheck getCombined()
	{
		if (combined == null)
		{
			createCombinedCheck();
		}
		return combined;
	}

	private void createCombinedCheck()
	{
		List<ISecurityCheck> checks = createChecks();
		if (checks.isEmpty())
		{
			combined = wrapped;
		}
		else
			combined = SecurityChecks.and(checks.toArray(new ISecurityCheck[checks.size()]));
	}

	private List<ISecurityCheck> createChecks()
	{
		List<ISecurityCheck> ret = new ArrayList<ISecurityCheck>();
		for (RequiredSecurityCheck curCheck : getRequiredSecurityChecks(target))
		{
			ISecurityCheck newCheck = instantiate(curCheck);
			if (newCheck != null)
			{
				if (useAlternativeRenderCheck)
				{
					tryToSetAlternativeRenderCheck(newCheck);
				}
				ret.add(newCheck);
			}
		}
		return ret;
	}

	private void tryToSetAlternativeRenderCheck(ISecurityCheck newCheck)
	{
		if (newCheck instanceof LinkSecurityCheck)
		{
			((LinkSecurityCheck) newCheck).setUseAlternativeRenderCheck(useAlternativeRenderCheck);
		}
		else if (newCheck instanceof AlternativeRenderCheckSupported)
		{
			((AlternativeRenderCheckSupported) newCheck)
				.setUseAlternativeRenderCheck(useAlternativeRenderCheck);
		}
	}

	private ISecurityCheck instantiate(RequiredSecurityCheck check)
	{
		ISecurityCheckFactory factory = ReflectionUtil.invokeConstructor(check.factory());
		return factory.createSecurityCheck(check.value(), wrapped, component, target);
	}

	public static List<RequiredSecurityCheck> getRequiredSecurityChecks(Class< ? > targetClass)
	{
		List<RequiredSecurityCheck> ret;
		if (targetClass.getSuperclass() != null)
			ret = getRequiredSecurityChecks(targetClass.getSuperclass());
		else
			ret = new ArrayList<RequiredSecurityCheck>();
		if (targetClass.isAnnotationPresent(RequiredSecurityCheck.class))
			ret.add(targetClass.getAnnotation(RequiredSecurityCheck.class));
		else if (targetClass.isAnnotationPresent(RequiredSecurityChecks.class))
		{
			for (RequiredSecurityCheck curCheck : targetClass.getAnnotation(
				RequiredSecurityChecks.class).value())
			{
				ret.add(curCheck);
			}
		}
		return ret;
	}

	@Override
	public boolean isActionAuthorized(WaspAction action)
	{
		return getCombined().isActionAuthorized(action);
	}

	@Override
	public boolean isAuthenticated()
	{
		return getCombined().isAuthenticated();
	}
}
