package nl.topicus.eduarte.core.principals.beheer.account;

import nl.topicus.cobra.security.Actions;
import nl.topicus.cobra.security.Description;
import nl.topicus.cobra.security.Implies;
import nl.topicus.eduarte.app.EduArteModuleKey;
import nl.topicus.eduarte.app.security.AbstractEduArtePrincipalSource;
import nl.topicus.eduarte.app.security.Display;
import nl.topicus.eduarte.app.security.actions.Beheer;
import nl.topicus.eduarte.app.security.actions.Instelling;
import nl.topicus.eduarte.app.security.actions.OrganisatieEenheid;

@Actions( {Beheer.class, Instelling.class, OrganisatieEenheid.class})
@Implies(nl.topicus.eduarte.core.principals.beheer.BeheerHome.class)
@Display(parent = "Beheer", label = "Gebruikersaccounts beheren", module = EduArteModuleKey.BASISFUNCTIONALITEIT)
@Description("Gebruikersaccounts bekijken")
public class AccountsRead extends AbstractEduArtePrincipalSource
{
	private static final long serialVersionUID = 1L;
}
