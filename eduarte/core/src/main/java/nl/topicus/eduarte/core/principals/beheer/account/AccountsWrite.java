package nl.topicus.eduarte.core.principals.beheer.account;

import nl.topicus.cobra.security.Actions;
import nl.topicus.cobra.security.Description;
import nl.topicus.cobra.security.Write;
import nl.topicus.eduarte.app.security.AbstractEduArtePrincipalSource;
import nl.topicus.eduarte.app.security.Niveau;
import nl.topicus.eduarte.app.security.actions.Beheer;
import nl.topicus.eduarte.app.security.actions.Instelling;
import nl.topicus.eduarte.app.security.actions.OrganisatieEenheid;
import nl.topicus.eduarte.entities.security.authorization.AuthorisatieNiveau;

@Actions( {Beheer.class, Instelling.class, OrganisatieEenheid.class})
@Write(read = AccountsRead.class)
@Niveau(AuthorisatieNiveau.APPLICATIE)
@Description("Gebruikersaccounts aanmaken en bewerken")
public class AccountsWrite extends AbstractEduArtePrincipalSource
{
	private static final long serialVersionUID = 1L;
}
