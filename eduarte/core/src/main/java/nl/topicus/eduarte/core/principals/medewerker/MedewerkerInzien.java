package nl.topicus.eduarte.core.principals.medewerker;

import nl.topicus.cobra.security.Actions;
import nl.topicus.cobra.security.Description;
import nl.topicus.eduarte.app.EduArteModuleKey;
import nl.topicus.eduarte.app.security.AbstractEduArtePrincipalSource;
import nl.topicus.eduarte.app.security.Display;
import nl.topicus.eduarte.app.security.actions.Instelling;
import nl.topicus.eduarte.app.security.actions.OrganisatieEenheid;

@Actions( {Instelling.class, OrganisatieEenheid.class})
@Display(parent = "Medewerker", label = "Inzien", module = EduArteModuleKey.BASISFUNCTIONALITEIT)
@Description("Basisinformatie van een medewerker inzien")
public class MedewerkerInzien extends AbstractEduArtePrincipalSource
{
	private static final long serialVersionUID = 1L;
}
