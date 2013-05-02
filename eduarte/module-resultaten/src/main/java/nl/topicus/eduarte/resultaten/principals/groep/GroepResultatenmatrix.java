package nl.topicus.eduarte.resultaten.principals.groep;

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
import nl.topicus.eduarte.core.principals.groep.GroepInzien;

@Actions( {Instelling.class, OrganisatieEenheid.class, Begeleider.class, Docent.class})
@Display(parent = "Groep", label = "Resultatenmatrix", module = EduArteModuleKey.SUMMATIEVE_RESULTATEN)
@Description("Resultatenmatrix van een groep bekijken")
@Implies(GroepInzien.class)
@Module(EduArteModuleKey.SUMMATIEVE_RESULTATEN)
public class GroepResultatenmatrix extends AbstractEduArtePrincipalSource
{
	private static final long serialVersionUID = 1L;
}
