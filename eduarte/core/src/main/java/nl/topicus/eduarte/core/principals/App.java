package nl.topicus.eduarte.core.principals;

import nl.topicus.cobra.security.Actions;
import nl.topicus.cobra.security.Description;
import nl.topicus.cobra.security.Write;
import nl.topicus.eduarte.app.EduArteModuleKey;
import nl.topicus.eduarte.app.security.AbstractEduArtePrincipalSource;
import nl.topicus.eduarte.app.security.Display;
import nl.topicus.eduarte.app.security.EduArtePrincipal;
import nl.topicus.eduarte.app.security.Niveau;
import nl.topicus.eduarte.app.security.actions.Beheer;
import nl.topicus.eduarte.app.security.actions.Instelling;
import nl.topicus.eduarte.app.security.actions.OrganisatieEenheid;
import nl.topicus.eduarte.entities.security.authorization.AuthorisatieNiveau;

@Actions( {Beheer.class, Instelling.class, OrganisatieEenheid.class})
@Niveau(AuthorisatieNiveau.APPLICATIE)
@Write
@Display(parent = "Beheer", label = "Applicatiebeheerder", module = EduArteModuleKey.BASISFUNCTIONALITEIT)
@Description("Alle applicatiebeheerder rechten")
public class App extends AbstractEduArtePrincipalSource
{
	private static final long serialVersionUID = 1L;

	@Override
	public boolean isImplied(EduArtePrincipal base, EduArtePrincipal possibleImplied)
	{
		if (possibleImplied.getAuthorisatieNiveau().implies(base.getAuthorisatieNiveau()))
			return false;
		return base.getAction().implies(possibleImplied.getAction());
	}
}
