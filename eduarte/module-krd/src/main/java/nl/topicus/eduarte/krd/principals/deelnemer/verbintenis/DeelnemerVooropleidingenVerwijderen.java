package nl.topicus.eduarte.krd.principals.deelnemer.verbintenis;

import nl.topicus.cobra.security.Actions;
import nl.topicus.cobra.security.Description;
import nl.topicus.cobra.security.Implies;
import nl.topicus.cobra.security.Write;
import nl.topicus.eduarte.app.EduArteModuleKey;
import nl.topicus.eduarte.app.Module;
import nl.topicus.eduarte.app.security.AbstractEduArtePrincipalSource;
import nl.topicus.eduarte.app.security.Display;
import nl.topicus.eduarte.app.security.actions.Begeleider;
import nl.topicus.eduarte.app.security.actions.Docent;
import nl.topicus.eduarte.app.security.actions.Instelling;
import nl.topicus.eduarte.app.security.actions.OrganisatieEenheid;

@Actions( {Instelling.class, OrganisatieEenheid.class, Begeleider.class, Docent.class})
@Description("Vooropleidingen van een deelnemer verwijderen")
@Write
@Module(EduArteModuleKey.KERNREGISTRATIE_DEELNEMERS)
@Implies(DeelnemerVooropleidingenWrite.class)
@Display(parent = "Deelnemer", label = "Vooropleidingen verwijderen", module = EduArteModuleKey.KERNREGISTRATIE_DEELNEMERS)
public class DeelnemerVooropleidingenVerwijderen extends AbstractEduArtePrincipalSource
{
	private static final long serialVersionUID = 1L;
}
