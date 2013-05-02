package nl.topicus.eduarte.core.principals.deelnemer.resultaten;

import nl.topicus.cobra.security.Actions;
import nl.topicus.cobra.security.Description;
import nl.topicus.cobra.security.Implies;
import nl.topicus.eduarte.app.EduArteModuleKey;
import nl.topicus.eduarte.app.security.AbstractEduArtePrincipalSource;
import nl.topicus.eduarte.app.security.Display;
import nl.topicus.eduarte.app.security.actions.Begeleider;
import nl.topicus.eduarte.app.security.actions.Docent;
import nl.topicus.eduarte.app.security.actions.Instelling;
import nl.topicus.eduarte.app.security.actions.OrganisatieEenheid;
import nl.topicus.eduarte.core.principals.deelnemer.DeelnemerInzien;

@Actions( {Instelling.class, OrganisatieEenheid.class, Begeleider.class, Docent.class})
@Display(parent = "Deelnemer", label = "Resultatenmatrix", module = {
	EduArteModuleKey.SUMMATIEVE_RESULTATEN, EduArteModuleKey.BASISFUNCTIONALITEIT})
@Description("Resultatenmatrix van een deelnemer bekijken")
@Implies(DeelnemerInzien.class)
public class DeelnemerResultatenmatrix extends AbstractEduArtePrincipalSource
{
	private static final long serialVersionUID = 1L;
}
