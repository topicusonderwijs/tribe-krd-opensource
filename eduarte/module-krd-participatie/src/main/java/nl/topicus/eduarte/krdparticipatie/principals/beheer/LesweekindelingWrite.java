package nl.topicus.eduarte.krdparticipatie.principals.beheer;

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
@Implies(nl.topicus.eduarte.core.principals.beheer.BeheerHome.class)
@Write()
@Display(parent = "Rooster", label = "lesweekindeling", module = EduArteModuleKey.PARTICIPATIE)
@Description("lesweekindeling bewerken/toevoegen")
@Module(EduArteModuleKey.PARTICIPATIE)
public class LesweekindelingWrite extends AbstractEduArtePrincipalSource
{
	private static final long serialVersionUID = 1L;

}
