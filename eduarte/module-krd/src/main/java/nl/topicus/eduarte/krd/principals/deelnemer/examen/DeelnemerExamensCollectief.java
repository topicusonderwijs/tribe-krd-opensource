package nl.topicus.eduarte.krd.principals.deelnemer.examen;

import nl.topicus.cobra.security.Actions;
import nl.topicus.cobra.security.Description;
import nl.topicus.cobra.security.Write;
import nl.topicus.eduarte.app.EduArteModuleKey;
import nl.topicus.eduarte.app.Module;
import nl.topicus.eduarte.app.security.AbstractEduArtePrincipalSource;
import nl.topicus.eduarte.app.security.Display;
import nl.topicus.eduarte.app.security.actions.Instelling;
import nl.topicus.eduarte.app.security.actions.OrganisatieEenheid;

@Actions( {Instelling.class, OrganisatieEenheid.class})
@Write
@Description("Collectief examendeelnames van een deelnemer bewerken")
@Display(parent = "Deelnemer", label = "Collectief examens", module = EduArteModuleKey.KERNREGISTRATIE_DEELNEMERS)
@Module(EduArteModuleKey.KERNREGISTRATIE_DEELNEMERS)
public class DeelnemerExamensCollectief extends AbstractEduArtePrincipalSource
{
	private static final long serialVersionUID = 1L;

}
