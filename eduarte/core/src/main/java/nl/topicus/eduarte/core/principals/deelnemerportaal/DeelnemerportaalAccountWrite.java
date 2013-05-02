package nl.topicus.eduarte.core.principals.deelnemerportaal;

import nl.topicus.cobra.security.Actions;
import nl.topicus.cobra.security.Description;
import nl.topicus.cobra.security.Write;
import nl.topicus.eduarte.app.EduArteModuleKey;
import nl.topicus.eduarte.app.Module;
import nl.topicus.eduarte.app.security.AbstractEduArtePrincipalSource;
import nl.topicus.eduarte.app.security.actions.Deelnemer;

@Actions( {Deelnemer.class})
@Write(read = DeelnemerportaalAccount.class)
@Description("Account bewerken")
@Module(EduArteModuleKey.BASISFUNCTIONALITEIT)
public class DeelnemerportaalAccountWrite extends AbstractEduArtePrincipalSource
{
	private static final long serialVersionUID = 1L;
}
