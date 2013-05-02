package nl.topicus.eduarte.core.principals.beheer.organisatie;

import nl.topicus.cobra.security.Actions;
import nl.topicus.cobra.security.Description;
import nl.topicus.cobra.security.Implies;
import nl.topicus.cobra.security.Write;
import nl.topicus.eduarte.app.EduArteModuleKey;
import nl.topicus.eduarte.app.security.AbstractEduArtePrincipalSource;
import nl.topicus.eduarte.app.security.Display;
import nl.topicus.eduarte.app.security.actions.Instelling;
import nl.topicus.eduarte.core.principals.beheer.BeheerHome;

@Actions(Instelling.class)
@Implies(BeheerHome.class)
@Display(parent = "Beheer", label = "Organisatiemodel", module = EduArteModuleKey.BASISFUNCTIONALITEIT)
@Description("Beheer van organisatie eenheden en locaties")
@Write
public class OrganisatiemodelPrincipal extends AbstractEduArtePrincipalSource
{
	private static final long serialVersionUID = 1L;
}
