package nl.topicus.cobra.web.security;

import org.apache.wicket.security.checks.ISecurityCheck;

public interface EditTargetAwareSecurityCheck extends ISecurityCheck
{
	public void setEditTarget(boolean editTarget);
}
