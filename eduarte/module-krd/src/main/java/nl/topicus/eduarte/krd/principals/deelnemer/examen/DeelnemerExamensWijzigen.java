package nl.topicus.eduarte.krd.principals.deelnemer.examen;

import nl.topicus.cobra.security.Actions;
import nl.topicus.cobra.security.Description;
import nl.topicus.cobra.security.Write;
import nl.topicus.eduarte.app.EduArteModuleKey;
import nl.topicus.eduarte.app.Module;
import nl.topicus.eduarte.app.security.AbstractEduArtePrincipalSource;
import nl.topicus.eduarte.app.security.actions.Begeleider;
import nl.topicus.eduarte.app.security.actions.Docent;
import nl.topicus.eduarte.app.security.actions.Instelling;
import nl.topicus.eduarte.app.security.actions.OrganisatieEenheid;

@Actions( {Instelling.class, OrganisatieEenheid.class, Begeleider.class, Docent.class})
@Write(read = DeelnemerExamensInzien.class)
@Description("Examendeelnames van een deelnemer wijzigen")
@Module(EduArteModuleKey.KERNREGISTRATIE_DEELNEMERS)
public class DeelnemerExamensWijzigen extends AbstractEduArtePrincipalSource
{
	private static final long serialVersionUID = 1L;

}
