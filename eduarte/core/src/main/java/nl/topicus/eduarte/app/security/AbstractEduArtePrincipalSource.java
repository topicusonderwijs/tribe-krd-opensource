package nl.topicus.eduarte.app.security;

import nl.topicus.cobra.security.impl.AbstractPrincipalSource;

import org.apache.wicket.security.actions.WaspAction;

public abstract class AbstractEduArtePrincipalSource extends
		AbstractPrincipalSource<EduArtePrincipal>
{
	private static final long serialVersionUID = 1L;

	public AbstractEduArtePrincipalSource()
	{
	}

	@Override
	public EduArtePrincipal createPrincipal(Class< ? extends WaspAction> action)
	{
		return new EduArtePrincipal(this, action);
	}
}
