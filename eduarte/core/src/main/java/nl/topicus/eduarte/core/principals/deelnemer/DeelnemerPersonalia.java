package nl.topicus.eduarte.core.principals.deelnemer;

import nl.topicus.cobra.security.Actions;
import nl.topicus.cobra.security.Description;
import nl.topicus.cobra.security.Implies;
import nl.topicus.eduarte.app.EduArteModuleKey;
import nl.topicus.eduarte.app.security.AbstractEduArtePrincipalSource;
import nl.topicus.eduarte.app.security.Display;
import nl.topicus.eduarte.app.security.actions.Begeleider;
import nl.topicus.eduarte.app.security.actions.Docent;
import nl.topicus.eduarte.app.security.actions.Instelling;
import nl.topicus.eduarte.app.security.actions.OrganisatieEenheid;
import nl.topicus.eduarte.app.security.actions.Uitvoerend;
import nl.topicus.eduarte.app.security.actions.Verantwoordelijk;

@Actions( {Instelling.class, OrganisatieEenheid.class, Verantwoordelijk.class, Uitvoerend.class,
	Begeleider.class, Docent.class})
@Implies(DeelnemerInzien.class)
@Display(parent = "Deelnemer", label = "Personalia", module = {
	EduArteModuleKey.KERNREGISTRATIE_DEELNEMERS, EduArteModuleKey.BASISFUNCTIONALITEIT})
@Description("Personalia van een deelnemer bekijken")
public class DeelnemerPersonalia extends AbstractEduArtePrincipalSource
{
	private static final long serialVersionUID = 1L;
}
