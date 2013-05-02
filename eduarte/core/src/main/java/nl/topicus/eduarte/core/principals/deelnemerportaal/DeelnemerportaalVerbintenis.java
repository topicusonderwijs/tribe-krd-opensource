package nl.topicus.eduarte.core.principals.deelnemerportaal;

import nl.topicus.cobra.security.Actions;
import nl.topicus.cobra.security.Description;
import nl.topicus.eduarte.app.EduArteModuleKey;
import nl.topicus.eduarte.app.security.AbstractEduArtePrincipalSource;
import nl.topicus.eduarte.app.security.Display;
import nl.topicus.eduarte.app.security.actions.Deelnemer;

@Actions( {Deelnemer.class})
@Display(parent = "Selfservice", label = "Verbintenis", module = {EduArteModuleKey.BASISFUNCTIONALITEIT})
@Description("Verbintenis van een deelnemer bekijken")
public class DeelnemerportaalVerbintenis extends AbstractEduArtePrincipalSource
{
	private static final long serialVersionUID = 1L;
}