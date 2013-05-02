package nl.topicus.eduarte.krd.principals.onderwijs;

import nl.topicus.cobra.security.Actions;
import nl.topicus.cobra.security.Description;
import nl.topicus.cobra.security.Write;
import nl.topicus.eduarte.app.EduArteModuleKey;
import nl.topicus.eduarte.app.Module;
import nl.topicus.eduarte.app.security.AbstractEduArtePrincipalSource;
import nl.topicus.eduarte.app.security.actions.Instelling;
import nl.topicus.eduarte.app.security.actions.OrganisatieEenheid;
import nl.topicus.eduarte.core.principals.onderwijs.OpleidingInzien;

@Actions( {Instelling.class, OrganisatieEenheid.class})
@Description("Opleiding bewerken")
@Write(read = OpleidingInzien.class)
@Module(EduArteModuleKey.KERNREGISTRATIE_DEELNEMERS)
public class OpleidingWrite extends AbstractEduArtePrincipalSource
{
	private static final long serialVersionUID = 1L;

}
