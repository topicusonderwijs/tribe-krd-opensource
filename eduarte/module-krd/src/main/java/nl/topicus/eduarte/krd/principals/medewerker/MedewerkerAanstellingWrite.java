package nl.topicus.eduarte.krd.principals.medewerker;

import nl.topicus.cobra.security.Actions;
import nl.topicus.cobra.security.Description;
import nl.topicus.cobra.security.Write;
import nl.topicus.eduarte.app.EduArteModuleKey;
import nl.topicus.eduarte.app.Module;
import nl.topicus.eduarte.app.security.AbstractEduArtePrincipalSource;
import nl.topicus.eduarte.app.security.actions.Instelling;
import nl.topicus.eduarte.app.security.actions.OrganisatieEenheid;
import nl.topicus.eduarte.core.principals.medewerker.MedewerkerAanstelling;

@Actions( {Instelling.class, OrganisatieEenheid.class})
@Description("Aanstelling van een medewerker bewerken")
@Module(EduArteModuleKey.KERNREGISTRATIE_DEELNEMERS)
@Write(read = MedewerkerAanstelling.class)
public class MedewerkerAanstellingWrite extends AbstractEduArtePrincipalSource
{
	private static final long serialVersionUID = 1L;
}
