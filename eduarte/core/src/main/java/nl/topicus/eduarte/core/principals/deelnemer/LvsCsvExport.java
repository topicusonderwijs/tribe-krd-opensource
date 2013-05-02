package nl.topicus.eduarte.core.principals.deelnemer;

import nl.topicus.cobra.security.Actions;
import nl.topicus.cobra.security.Description;
import nl.topicus.eduarte.app.EduArteModuleKey;
import nl.topicus.eduarte.app.Module;
import nl.topicus.eduarte.app.security.AbstractEduArtePrincipalSource;
import nl.topicus.eduarte.app.security.Display;
import nl.topicus.eduarte.app.security.actions.Instelling;
import nl.topicus.eduarte.app.security.actions.OrganisatieEenheid;

@Actions( {Instelling.class, OrganisatieEenheid.class})
@Display(parent = "Deelnemer", label = "Lvs csv export", module = EduArteModuleKey.BASISFUNCTIONALITEIT)
@Description("Exporten van gegevens voor lvs")
@Module(EduArteModuleKey.LVS_CSV_EXPORT)
public class LvsCsvExport extends AbstractEduArtePrincipalSource
{
	private static final long serialVersionUID = 1L;
}
