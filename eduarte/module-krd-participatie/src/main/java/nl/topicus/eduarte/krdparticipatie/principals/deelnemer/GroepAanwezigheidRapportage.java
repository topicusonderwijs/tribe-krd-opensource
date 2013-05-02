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
import nl.topicus.eduarte.core.principals.groep.GroepInzien;

@Actions( {Instelling.class, OrganisatieEenheid.class})
@Implies(GroepInzien.class)
@Display(parent = "Groep", label = "Groep aanwezigheidrapportage", module = {
	EduArteModuleKey.PARTICIPATIE, EduArteModuleKey.KERNREGISTRATIE_DEELNEMERS})
@Description("Groep aanwezigheidrapportage inzien")
@Module(EduArteModuleKey.KRD_PARTICIPATIE)
public class GroepAanwezigheidRapportage extends AbstractEduArtePrincipalSource
{
	private static final long serialVersionUID = 1L;

}
