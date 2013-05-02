package nl.topicus.eduarte.core.principals.medewerker;

import nl.topicus.cobra.security.Actions;
import nl.topicus.cobra.security.Description;
import nl.topicus.cobra.security.Implies;
import nl.topicus.eduarte.app.EduArteModuleKey;
import nl.topicus.eduarte.app.security.AbstractEduArtePrincipalSource;
import nl.topicus.eduarte.app.security.Display;
import nl.topicus.eduarte.app.security.actions.Instelling;
import nl.topicus.eduarte.app.security.actions.OrganisatieEenheid;

@Actions( {Instelling.class, OrganisatieEenheid.class})
@Display(parent = "Medewerker", label = "Personalia", module = {
	EduArteModuleKey.KERNREGISTRATIE_DEELNEMERS, EduArteModuleKey.BASISFUNCTIONALITEIT})
@Description("Personalia van een medewerker inzien")
@Implies(MedewerkerInzien.class)
public class MedewerkerPersonaliaRead extends AbstractEduArtePrincipalSource
{
	private static final long serialVersionUID = 1L;
}
