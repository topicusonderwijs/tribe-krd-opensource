package nl.topicus.cobra.web.security;

import org.apache.wicket.Component;
import org.apache.wicket.security.checks.ISecurityCheck;

public interface ISecurityCheckFactory
{
	public ISecurityCheck createSecurityCheck(Class< ? extends ISecurityCheck> checkClass,
			ISecurityCheck wrapped, Component component, Class< ? > target);
}
