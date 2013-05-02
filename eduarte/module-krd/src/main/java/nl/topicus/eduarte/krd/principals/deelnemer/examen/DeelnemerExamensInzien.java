package nl.topicus.eduarte.krd.principals.deelnemer.examen;

import nl.topicus.cobra.security.Actions;
import nl.topicus.cobra.security.Description;
import nl.topicus.cobra.security.Implies;
import nl.topicus.eduarte.app.EduArteModuleKey;
import nl.topicus.eduarte.app.Module;
import nl.topicus.eduarte.app.security.AbstractEduArtePrincipalSource;
import nl.topicus.eduarte.app.security.Display;
import nl.topicus.eduarte.app.security.actions.Begeleider;
import nl.topicus.eduarte.app.security.actions.Docent;
import nl.topicus.eduarte.app.security.actions.Instelling;
import nl.topicus.eduarte.app.security.actions.OrganisatieEenheid;
import nl.topicus.eduarte.core.principals.deelnemer.DeelnemerInzien;

@Actions( {Instelling.class, OrganisatieEenheid.class, Begeleider.class, Docent.class})
@Implies(DeelnemerInzien.class)
@Display(parent = "Deelnemer", label = "Examens", module = EduArteModuleKey.KERNREGISTRATIE_DEELNEMERS)
@Description("Examendeelnames van een deelnemer bekijken")
@Module(EduArteModuleKey.KERNREGISTRATIE_DEELNEMERS)
public class DeelnemerExamensInzien extends AbstractEduArtePrincipalSource
{
	private static final long serialVersionUID = 1L;

}
