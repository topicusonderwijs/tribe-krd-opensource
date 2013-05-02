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
@Display(parent = "Beheer", label = "Mutatielogverwekers starten", module = EduArteModuleKey.WS_AVENTUS)
@Description("Mutatielogverwerkers starten")
@Write
@Module(EduArteModuleKey.WS_AVENTUS)
public class MutatieLogVerwerkenPrincipal extends AbstractEduArtePrincipalSource
{
	private static final long serialVersionUID = 1L;
}
