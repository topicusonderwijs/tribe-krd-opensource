package nl.topicus.eduarte.krdparticipatie.principals.deelnemer;

import nl.topicus.cobra.security.Actions;
import nl.topicus.cobra.security.Description;
import nl.topicus.cobra.security.Write;
import nl.topicus.eduarte.app.EduArteModuleKey;
import nl.topicus.eduarte.app.Module;
import nl.topicus.eduarte.app.security.AbstractEduArtePrincipalSource;
import nl.topicus.eduarte.app.security.actions.Instelling;
import nl.topicus.eduarte.app.security.actions.OrganisatieEenheid;

@Actions( {Instelling.class, OrganisatieEenheid.class})
@Description("Bewerken van Budgetten van deelnemers")
@Write(read = DeelnemerBudgettenRead.class)
@Module(EduArteModuleKey.KRD_PARTICIPATIE)
public class DeelnemerBudgettenWrite extends AbstractEduArtePrincipalSource
{
	private static final long serialVersionUID = 1L;
}
