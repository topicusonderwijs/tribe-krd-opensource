package nl.topicus.eduarte.core.principals.beheer.systeem;

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
@Display(parent = "Beheer", label = "Documenttypes en -categorien", module = EduArteModuleKey.BASISFUNCTIONALITEIT)
@Description("Beheer van documenttypes en -categorien")
@Write
@Module(EduArteModuleKey.BASISFUNCTIONALITEIT)
public class DocumentPrincipal extends AbstractEduArtePrincipalSource
{
	private static final long serialVersionUID = 1L;
}
