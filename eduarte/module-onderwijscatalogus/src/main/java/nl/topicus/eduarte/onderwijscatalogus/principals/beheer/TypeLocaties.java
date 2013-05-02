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
@Description("Beheer van locatietypen")
@Display(parent = "Beheer", group = "Organisatiemodel", label = "Locatietypen", module = EduArteModuleKey.ONDERWIJSCATALOGUS)
@Write
@Module(EduArteModuleKey.ONDERWIJSCATALOGUS)
public class TypeLocaties extends AbstractEduArtePrincipalSource
{
	private static final long serialVersionUID = 1L;
}
