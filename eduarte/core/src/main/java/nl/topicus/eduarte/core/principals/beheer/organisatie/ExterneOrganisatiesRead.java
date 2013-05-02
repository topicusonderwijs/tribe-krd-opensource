package nl.topicus.eduarte.core.principals.beheer.organisatie;

import nl.topicus.cobra.security.Actions;
import nl.topicus.cobra.security.Description;
import nl.topicus.cobra.security.Implies;
import nl.topicus.eduarte.app.EduArteModuleKey;
import nl.topicus.eduarte.app.security.AbstractEduArtePrincipalSource;
import nl.topicus.eduarte.app.security.Display;
import nl.topicus.eduarte.app.security.actions.Instelling;
import nl.topicus.eduarte.core.principals.beheer.relatie.RelatieBeheerHome;

@Actions(Instelling.class)
@Implies(RelatieBeheerHome.class)
@Display(parent = "Relatiebeheer", label = "Externe organisaties", module = {
	EduArteModuleKey.KERNREGISTRATIE_DEELNEMERS, EduArteModuleKey.BASISFUNCTIONALITEIT})
@Description("Externe organisaties inzien")
public class ExterneOrganisatiesRead extends AbstractEduArtePrincipalSource
{
	private static final long serialVersionUID = 1L;
}
