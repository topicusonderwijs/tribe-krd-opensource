package nl.topicus.eduarte.core.principals;

import nl.topicus.cobra.security.Actions;
import nl.topicus.cobra.security.Everybody;
import nl.topicus.eduarte.app.security.AbstractEduArtePrincipalSource;
import nl.topicus.eduarte.app.security.actions.Instelling;

@Everybody
@Actions(Instelling.class)
public class Always extends AbstractEduArtePrincipalSource
{
	private static final long serialVersionUID = 1L;
}
