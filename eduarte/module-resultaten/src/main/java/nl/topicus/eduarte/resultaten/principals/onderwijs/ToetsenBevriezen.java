package nl.topicus.eduarte.resultaten.principals.onderwijs;

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

@Actions( {Instelling.class, OrganisatieEenheid.class})
@Display(parent = "Onderwijs", label = "Toetsen bevriezen", module = EduArteModuleKey.SUMMATIEVE_RESULTATEN)
@Description("Toetsen bevriezen")
@Implies(OnderwijsproductResultaatstructuur.class)
@Module(EduArteModuleKey.SUMMATIEVE_RESULTATEN)
@Write
public class ToetsenBevriezen extends AbstractEduArtePrincipalSource
{
	private static final long serialVersionUID = 1L;
}
