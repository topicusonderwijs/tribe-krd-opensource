package nl.topicus.eduarte.core.principals.medewerker;

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
@Implies( {MedewerkerInzien.class, Snelzoeken.class})
@Display(parent = "Medewerker", label = "Zoeken", module = EduArteModuleKey.BASISFUNCTIONALITEIT)
@Description("Medewerkers zoeken")
public class MedewerkersZoeken extends AbstractEduArtePrincipalSource
{
	private static final long serialVersionUID = 1L;
}
