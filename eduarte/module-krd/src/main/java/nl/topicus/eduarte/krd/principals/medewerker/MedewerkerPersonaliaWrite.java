package nl.topicus.eduarte.krd.principals.medewerker;

import nl.topicus.cobra.security.Actions;
import nl.topicus.cobra.security.Description;
import nl.topicus.cobra.security.Write;
import nl.topicus.eduarte.app.EduArteModuleKey;
import nl.topicus.eduarte.app.Module;
import nl.topicus.eduarte.app.security.AbstractEduArtePrincipalSource;
import nl.topicus.eduarte.app.security.actions.Instelling;
import nl.topicus.eduarte.app.security.actions.OrganisatieEenheid;
import nl.topicus.eduarte.core.principals.medewerker.MedewerkerPersonaliaRead;

@Actions( {Instelling.class, OrganisatieEenheid.class})
@Description("Personalia van een medewerker bewerken")
@Write(read = MedewerkerPersonaliaRead.class)
@Module(EduArteModuleKey.KERNREGISTRATIE_DEELNEMERS)
public class MedewerkerPersonaliaWrite extends AbstractEduArtePrincipalSource
{
	private static final long serialVersionUID = 1L;
}
