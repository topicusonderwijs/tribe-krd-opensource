package nl.topicus.eduarte.core.principals.beheer.relatie;

import nl.topicus.cobra.security.Actions;
import nl.topicus.cobra.security.Description;
import nl.topicus.eduarte.app.security.AbstractEduArtePrincipalSource;
import nl.topicus.eduarte.app.security.actions.Beheer;
import nl.topicus.eduarte.app.security.actions.Instelling;
import nl.topicus.eduarte.app.security.actions.OrganisatieEenheid;

@Actions( {Beheer.class, Instelling.class, OrganisatieEenheid.class})
@Description("Beheer sectie")
public class RelatieBeheerHome extends AbstractEduArtePrincipalSource
{

	private static final long serialVersionUID = 1L;

}
