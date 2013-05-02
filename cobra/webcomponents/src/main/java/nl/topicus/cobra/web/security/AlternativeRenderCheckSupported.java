package nl.topicus.cobra.web.security;

import org.apache.wicket.security.checks.ISecurityCheck;

public interface AlternativeRenderCheckSupported extends ISecurityCheck
{
	public void setUseAlternativeRenderCheck(boolean useAlternativeRenderCheck);

	public boolean getUseAlternativeRenderCheck();
}
