package nl.topicus.eduarte.core.principals.beheer.organisatie;

import nl.topicus.cobra.security.Actions;
import nl.topicus.cobra.security.Description;
import nl.topicus.cobra.security.Implies;
import nl.topicus.eduarte.app.EduArteModuleKey;
import nl.topicus.eduarte.app.security.AbstractEduArtePrincipalSource;
import nl.topicus.eduarte.app.security.Display;
import nl.topicus.eduarte.app.security.actions.Instelling;

@Actions(Instelling.class)
@Implies(ExterneOrganisatiesRead.class)
@Display(parent = "Beheer", label = "BPV-deelnemers", module = EduArteModuleKey.BASISFUNCTIONALITEIT)
@Description("BPV-deelnemers inzien")
public class BPVDeelnemers extends AbstractEduArtePrincipalSource
{
	private static final long serialVersionUID = 1L;
}
