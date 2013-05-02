package nl.topicus.eduarte.core.principals.deelnemer;

import nl.topicus.cobra.security.Actions;
import nl.topicus.cobra.security.Description;
import nl.topicus.cobra.security.Implies;
import nl.topicus.eduarte.app.EduArteModuleKey;
import nl.topicus.eduarte.app.security.AbstractEduArtePrincipalSource;
import nl.topicus.eduarte.app.security.Display;
import nl.topicus.eduarte.app.security.actions.Instelling;
import nl.topicus.eduarte.app.security.actions.OrganisatieEenheid;

@Actions( {Instelling.class, OrganisatieEenheid.class})
@Implies(DeelnemersZoeken.class)
@Display(parent = "Deelnemer", label = "Uitgebreid zoeken", module = EduArteModuleKey.BASISFUNCTIONALITEIT)
@Description("Deelnemers zoeken met veel zoekopties")
public class DeelnemersZoekenUitgebreid extends AbstractEduArtePrincipalSource
{
	private static final long serialVersionUID = 1L;
}
