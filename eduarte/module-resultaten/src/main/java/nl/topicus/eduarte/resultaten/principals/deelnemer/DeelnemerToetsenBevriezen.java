package nl.topicus.eduarte.resultaten.principals.deelnemer;

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
@Display(parent = "Deelnemer", label = "Toetsen bevriezen", module = EduArteModuleKey.SUMMATIEVE_RESULTATEN)
@Description("Toetsen bevriezen")
@Module(EduArteModuleKey.SUMMATIEVE_RESULTATEN)
@Write
public class DeelnemerToetsenBevriezen extends AbstractEduArtePrincipalSource
{
	private static final long serialVersionUID = 1L;
}
