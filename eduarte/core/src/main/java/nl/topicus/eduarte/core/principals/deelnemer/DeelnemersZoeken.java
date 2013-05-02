package nl.topicus.eduarte.core.principals.deelnemer;

import nl.topicus.cobra.security.Actions;
import nl.topicus.cobra.security.Description;
import nl.topicus.cobra.security.Implies;
import nl.topicus.eduarte.app.EduArteModuleKey;
import nl.topicus.eduarte.app.security.AbstractEduArtePrincipalSource;
import nl.topicus.eduarte.app.security.Display;
import nl.topicus.eduarte.app.security.actions.Instelling;
import nl.topicus.eduarte.app.security.actions.OrganisatieEenheid;
import nl.topicus.eduarte.core.principals.Snelzoeken;

@Actions( {Instelling.class, OrganisatieEenheid.class})
@Implies( {DeelnemerInzien.class, Snelzoeken.class})
@Display(parent = "Deelnemer", label = "Zoeken", module = EduArteModuleKey.BASISFUNCTIONALITEIT)
@Description("Deelnemers zoeken")
public class DeelnemersZoeken extends AbstractEduArtePrincipalSource
{
	private static final long serialVersionUID = 1L;
}
