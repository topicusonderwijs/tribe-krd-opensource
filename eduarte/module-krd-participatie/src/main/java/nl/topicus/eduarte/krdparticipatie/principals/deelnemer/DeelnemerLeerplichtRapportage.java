package nl.topicus.eduarte.krdparticipatie.principals.deelnemer;

import nl.topicus.cobra.security.Actions;
import nl.topicus.cobra.security.Description;
import nl.topicus.cobra.security.Implies;
import nl.topicus.eduarte.app.EduArteModuleKey;
import nl.topicus.eduarte.app.Module;
import nl.topicus.eduarte.app.security.AbstractEduArtePrincipalSource;
import nl.topicus.eduarte.app.security.Display;
import nl.topicus.eduarte.app.security.actions.Instelling;
import nl.topicus.eduarte.app.security.actions.OrganisatieEenheid;
import nl.topicus.eduarte.core.principals.deelnemer.DeelnemersZoeken;

@Actions( {Instelling.class, OrganisatieEenheid.class})
@Implies(DeelnemersZoeken.class)
@Display(parent = "Deelnemer", label = "Deelnemer leerplichtrapportage", module = {
	EduArteModuleKey.PARTICIPATIE, EduArteModuleKey.KERNREGISTRATIE_DEELNEMERS})
@Description("Deelnemer leerplichtrapportage inzien")
@Module(EduArteModuleKey.KRD_PARTICIPATIE)
public class DeelnemerLeerplichtRapportage extends AbstractEduArtePrincipalSource
{
	private static final long serialVersionUID = 1L;
}
