package nl.topicus.eduarte.core.principals.beheer.onderwijs;

import nl.topicus.cobra.security.Actions;
import nl.topicus.cobra.security.Description;
import nl.topicus.cobra.security.Implies;
import nl.topicus.cobra.security.Write;
import nl.topicus.eduarte.app.EduArteModuleKey;
import nl.topicus.eduarte.app.security.AbstractEduArtePrincipalSource;
import nl.topicus.eduarte.app.security.Display;
import nl.topicus.eduarte.app.security.actions.Instelling;
import nl.topicus.eduarte.core.principals.beheer.BeheerHome;

@Actions(Instelling.class)
@Implies(BeheerHome.class)
@Display(parent = "Beheer", label = "Schalen", module = EduArteModuleKey.BASISFUNCTIONALITEIT)
@Description("Schalen beheren")
@Write
public class Schalen extends AbstractEduArtePrincipalSource
{
	private static final long serialVersionUID = 1L;
}
