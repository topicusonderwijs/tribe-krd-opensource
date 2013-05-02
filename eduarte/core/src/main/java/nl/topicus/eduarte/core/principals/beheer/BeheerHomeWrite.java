package nl.topicus.eduarte.core.principals.beheer;

import nl.topicus.cobra.security.Actions;
import nl.topicus.cobra.security.Description;
import nl.topicus.cobra.security.Write;
import nl.topicus.eduarte.app.security.AbstractEduArtePrincipalSource;
import nl.topicus.eduarte.app.security.actions.Beheer;
import nl.topicus.eduarte.app.security.actions.Instelling;

@Actions( {Beheer.class, Instelling.class})
@Description("Organisatie wijzigen")
@Write(read = BeheerHome.class)
public class BeheerHomeWrite extends AbstractEduArtePrincipalSource
{
	private static final long serialVersionUID = 1L;
}
