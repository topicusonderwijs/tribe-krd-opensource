package nl.topicus.eduarte.krd.principals.organisatie;

import nl.topicus.cobra.security.Actions;
import nl.topicus.cobra.security.Description;
import nl.topicus.cobra.security.Implies;
import nl.topicus.cobra.security.Write;
import nl.topicus.eduarte.app.EduArteModuleKey;
import nl.topicus.eduarte.app.Module;
import nl.topicus.eduarte.app.security.AbstractEduArtePrincipalSource;
import nl.topicus.eduarte.app.security.actions.Instelling;
import nl.topicus.eduarte.core.principals.beheer.organisatie.ExterneOrganisatieKenmerken;
import nl.topicus.eduarte.core.principals.beheer.relatie.RelatieBeheerHome;

@Actions(Instelling.class)
@Implies(RelatieBeheerHome.class)
@Write(read = ExterneOrganisatieKenmerken.class)
@Description("Beheer van de kenmerken van externe organisaties")
@Module(EduArteModuleKey.KERNREGISTRATIE_DEELNEMERS)
public class ExterneOrganisatieKenmerkenWrite extends AbstractEduArtePrincipalSource
{
	private static final long serialVersionUID = 1L;
}
