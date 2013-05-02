package nl.topicus.eduarte.core.principals;

import nl.topicus.cobra.security.Actions;
import nl.topicus.cobra.security.Description;
import nl.topicus.cobra.security.Implies;
import nl.topicus.cobra.security.Write;
import nl.topicus.eduarte.app.EduArteModuleKey;
import nl.topicus.eduarte.app.security.AbstractEduArtePrincipalSource;
import nl.topicus.eduarte.app.security.Display;
import nl.topicus.eduarte.app.security.Niveau;
import nl.topicus.eduarte.app.security.actions.Beheer;
import nl.topicus.eduarte.app.security.actions.Instelling;
import nl.topicus.eduarte.core.principals.beheer.account.AccountsWrite;
import nl.topicus.eduarte.core.principals.beheer.account.RightsWrite;
import nl.topicus.eduarte.entities.security.authorization.AuthorisatieNiveau;

@Actions( {Beheer.class, Instelling.class})
@Write
@Niveau(AuthorisatieNiveau.SUPER)
@Implies( {RightsWrite.class, AccountsWrite.class})
@Display(parent = "Beheer", label = "Root account", module = EduArteModuleKey.BASISFUNCTIONALITEIT)
@Description("Alle root rechten")
public class Root extends AbstractEduArtePrincipalSource
{
	private static final long serialVersionUID = 1L;
}
