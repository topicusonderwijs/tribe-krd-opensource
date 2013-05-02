package nl.topicus.eduarte.core.principals.beheer.account;

import nl.topicus.cobra.security.Actions;
import nl.topicus.cobra.security.Description;
import nl.topicus.cobra.security.Implies;
import nl.topicus.cobra.security.Write;
import nl.topicus.eduarte.app.EduArteModuleKey;
import nl.topicus.eduarte.app.security.AbstractEduArtePrincipalSource;
import nl.topicus.eduarte.app.security.Display;
import nl.topicus.eduarte.app.security.actions.Beheer;
import nl.topicus.eduarte.app.security.actions.Instelling;
import nl.topicus.eduarte.app.security.actions.OrganisatieEenheid;

@Actions( {Beheer.class, Instelling.class, OrganisatieEenheid.class})
@Display(parent = "Beheer", label = "Wachtwoorden aanpassen", module = EduArteModuleKey.BASISFUNCTIONALITEIT)
@Implies(AccountsRead.class)
@Description("Wachtwoorden aanpassen")
@Write
public class PasswordWrite extends AbstractEduArtePrincipalSource
{
	private static final long serialVersionUID = 1L;
}
