package nl.topicus.eduarte.core.principals.deelnemer;

import nl.topicus.cobra.security.Actions;
import nl.topicus.cobra.security.Description;
import nl.topicus.cobra.security.Implies;
import nl.topicus.eduarte.app.EduArteModuleKey;
import nl.topicus.eduarte.app.security.AbstractEduArtePrincipalSource;
import nl.topicus.eduarte.app.security.Display;
import nl.topicus.eduarte.app.security.actions.Instelling;
import nl.topicus.eduarte.app.security.actions.OrganisatieEenheid;
import nl.topicus.eduarte.core.principals.medewerker.MedewerkerInzien;

@Actions( {Instelling.class, OrganisatieEenheid.class})
@Implies(MedewerkerInzien.class)
@Display(parent = "Medewerker", label = "Kenmerken", module = {
	EduArteModuleKey.KERNREGISTRATIE_DEELNEMERS, EduArteModuleKey.BASISFUNCTIONALITEIT})
@Description("Kenmerken van een medewerker bekijken")
public class MedewerkerKenmerken extends AbstractEduArtePrincipalSource
{
	private static final long serialVersionUID = 1L;
}
