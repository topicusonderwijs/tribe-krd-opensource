package nl.topicus.eduarte.resultaten.principals.deelnemer;

import nl.topicus.cobra.security.Actions;
import nl.topicus.cobra.security.Description;
import nl.topicus.cobra.security.Write;
import nl.topicus.eduarte.app.EduArteModuleKey;
import nl.topicus.eduarte.app.Module;
import nl.topicus.eduarte.app.security.AbstractEduArtePrincipalSource;
import nl.topicus.eduarte.app.security.Display;
import nl.topicus.eduarte.app.security.actions.Instelling;

@Actions(Instelling.class)
@Description("Resultaten importeren")
@Module(EduArteModuleKey.SUMMATIEVE_RESULTATEN)
@Display(parent = "Deelnemer", label = "Resultaten importeren", module = EduArteModuleKey.SUMMATIEVE_RESULTATEN)
@Write
public class ResultatenImporteren extends AbstractEduArtePrincipalSource
{
	private static final long serialVersionUID = 1L;
}
