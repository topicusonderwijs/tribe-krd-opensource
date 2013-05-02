package nl.topicus.eduarte.krd.principals.beheer.systeemtabellen;

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
@Display(parent = "Beheer", label = "Relatie soort", module = EduArteModuleKey.KERNREGISTRATIE_DEELNEMERS)
@Description("Beheer van relatie soorten")
@Write
@Module(EduArteModuleKey.KERNREGISTRATIE_DEELNEMERS)
public class RelatieSoortPrincipal extends AbstractEduArtePrincipalSource
{

	private static final long serialVersionUID = 1L;

}
