package nl.topicus.eduarte.resultaten.principals.groep;

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
@Description("Resultatenmatrix van een groep bewerken")
@Write(read = GroepResultatenmatrix.class)
@Module(EduArteModuleKey.SUMMATIEVE_RESULTATEN)
public class GroepResultatenmatrixWrite extends AbstractEduArtePrincipalSource
{
	private static final long serialVersionUID = 1L;
}
