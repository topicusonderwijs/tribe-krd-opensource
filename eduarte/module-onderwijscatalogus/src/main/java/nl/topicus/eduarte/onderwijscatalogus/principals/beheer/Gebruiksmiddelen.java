package nl.topicus.eduarte.onderwijscatalogus.principals.beheer;

import nl.topicus.cobra.security.Actions;
import nl.topicus.cobra.security.Description;
import nl.topicus.cobra.security.Implies;
import nl.topicus.cobra.security.Write;
import nl.topicus.eduarte.app.EduArteModuleKey;
import nl.topicus.eduarte.app.Module;
import nl.topicus.eduarte.app.security.AbstractEduArtePrincipalSource;
import nl.topicus.eduarte.app.security.Display;
import nl.topicus.eduarte.app.security.actions.Instelling;
import nl.topicus.eduarte.core.principals.beheer.BeheerHome;

@Actions(Instelling.class)
@Implies(BeheerHome.class)
@Display(parent = "Beheer", label = "Gebruiksmiddelen", module = EduArteModuleKey.ONDERWIJSCATALOGUS)
@Description("Gebruiksmiddelen beheren")
@Write
@Module(EduArteModuleKey.ONDERWIJSCATALOGUS)
public class Gebruiksmiddelen extends AbstractEduArtePrincipalSource
{
	private static final long serialVersionUID = 1L;
}
