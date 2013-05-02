package nl.topicus.eduarte.core.principals.deelnemerportaal;

import nl.topicus.cobra.security.Actions;
import nl.topicus.cobra.security.Description;
import nl.topicus.eduarte.app.EduArteModuleKey;
import nl.topicus.eduarte.app.security.AbstractEduArtePrincipalSource;
import nl.topicus.eduarte.app.security.Display;
import nl.topicus.eduarte.app.security.actions.Deelnemer;

@Actions( {Deelnemer.class})
// @Implies(DeelnemerInzien.class)
@Display(parent = "Selfservice", label = "Account", module = {EduArteModuleKey.BASISFUNCTIONALITEIT})
@Description("Account van een deelnemer bekijken")
public class DeelnemerportaalAccount extends AbstractEduArtePrincipalSource
{
	private static final long serialVersionUID = 1L;
}
