package nl.topicus.eduarte.krd.principals.onderwijs;

import nl.topicus.cobra.security.Actions;
import nl.topicus.cobra.security.Description;
import nl.topicus.cobra.security.Implies;
import nl.topicus.cobra.security.Write;
import nl.topicus.eduarte.app.EduArteModuleKey;
import nl.topicus.eduarte.app.Module;
import nl.topicus.eduarte.app.security.AbstractEduArtePrincipalSource;
import nl.topicus.eduarte.app.security.Display;
import nl.topicus.eduarte.app.security.actions.Instelling;

@Actions(Instelling.class)
@Description("Taxonomie verwijderen")
@Write
@Implies(TaxonomieElementWrite.class)
@Display(parent = "Onderwijs", label = "Taxonomie verwijderen", module = EduArteModuleKey.KERNREGISTRATIE_DEELNEMERS)
@Module(EduArteModuleKey.KERNREGISTRATIE_DEELNEMERS)
public class TaxonomieElementVerwijderen extends AbstractEduArtePrincipalSource
{
	private static final long serialVersionUID = 1L;
}
