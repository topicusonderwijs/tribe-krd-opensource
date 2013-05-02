package nl.topicus.cobra.security;

import org.apache.wicket.security.hive.authorization.Principal;

public abstract class NoReadPrincipalSource implements IPrincipalSource<Principal>
{
	private static final long serialVersionUID = 1L;

	private NoReadPrincipalSource()
	{
		throw new UnsupportedOperationException(
			"Cannot be instantiated, only for default values in annotations");
	}
}
