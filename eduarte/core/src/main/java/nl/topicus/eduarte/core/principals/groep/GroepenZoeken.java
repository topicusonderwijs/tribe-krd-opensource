package nl.topicus.eduarte.core.principals.groep;

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
@Implies( {GroepInzien.class, Snelzoeken.class})
@Display(parent = "Groep", label = "Zoeken", module = EduArteModuleKey.BASISFUNCTIONALITEIT)
@Description("Zoeken naar groepen/groepsdeelnames")
public class GroepenZoeken extends AbstractEduArtePrincipalSource
{
	private static final long serialVersionUID = 1L;
}
