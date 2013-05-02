package nl.topicus.eduarte.krd.principals.onderwijs;

import nl.topicus.cobra.security.Actions;
import nl.topicus.cobra.security.Description;
import nl.topicus.cobra.security.Implies;
import nl.topicus.cobra.security.Write;
import nl.topicus.eduarte.app.EduArteModuleKey;
import nl.topicus.eduarte.app.Module;
import nl.topicus.eduarte.app.security.AbstractEduArtePrincipalSource;
import nl.topicus.eduarte.app.security.Display;
import nl.topicus.eduarte.app.security.actions.Instelling;
import nl.topicus.eduarte.app.security.actions.OrganisatieEenheid;
import nl.topicus.eduarte.core.principals.onderwijs.OpleidingenZoeken;

@Actions( {Instelling.class, OrganisatieEenheid.class})
@Description("Inrichting van een opleiding exporteren")
@Module(EduArteModuleKey.KERNREGISTRATIE_DEELNEMERS)
@Display(parent = "Onderwijs", label = "Inrichting exporteren", module = EduArteModuleKey.KERNREGISTRATIE_DEELNEMERS)
@Write
@Implies(OpleidingenZoeken.class)
public class OpleidingInrichtingExporteren extends AbstractEduArtePrincipalSource
{
	private static final long serialVersionUID = 1L;
}
