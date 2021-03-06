package nl.topicus.eduarte.core.principals.deelnemerportaal;

import nl.topicus.cobra.security.Actions;
import nl.topicus.cobra.security.Description;
import nl.topicus.eduarte.app.EduArteModuleKey;
import nl.topicus.eduarte.app.security.AbstractEduArtePrincipalSource;
import nl.topicus.eduarte.app.security.Display;
import nl.topicus.eduarte.app.security.actions.Deelnemer;

@Actions( {Deelnemer.class})
@Display(parent = "Selfservice", label = "Groepen", module = {EduArteModuleKey.BASISFUNCTIONALITEIT})
@Description("Groepen van een deelnemer bekijken")
public class DeelnemerportaalGroepen extends AbstractEduArtePrincipalSource
{
	private static final long serialVersionUID = 1L;
}
