package nl.topicus.eduarte.core.principals.deelnemer.verbintenis;

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
import nl.topicus.eduarte.core.principals.deelnemer.DeelnemerInzien;

@Actions( {Instelling.class, OrganisatieEenheid.class, Verantwoordelijk.class, Uitvoerend.class,
	Begeleider.class, Docent.class})
@Implies(DeelnemerInzien.class)
@Display(parent = "Deelnemer", label = "Documenten", module = EduArteModuleKey.BASISFUNCTIONALITEIT)
@Description("Dossier van een deelnemer inzien")
public class DeelnemerVerbintenisDocumentenRead extends AbstractEduArtePrincipalSource
{
	private static final long serialVersionUID = 1L;
}
