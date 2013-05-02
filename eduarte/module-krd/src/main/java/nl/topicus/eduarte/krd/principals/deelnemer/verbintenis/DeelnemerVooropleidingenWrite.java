package nl.topicus.eduarte.krd.principals.deelnemer.verbintenis;

import nl.topicus.cobra.security.Actions;
import nl.topicus.cobra.security.Description;
import nl.topicus.cobra.security.Write;
import nl.topicus.eduarte.app.EduArteModuleKey;
import nl.topicus.eduarte.app.Module;
import nl.topicus.eduarte.app.security.AbstractEduArtePrincipalSource;
import nl.topicus.eduarte.app.security.actions.Begeleider;
import nl.topicus.eduarte.app.security.actions.Docent;
import nl.topicus.eduarte.app.security.actions.Instelling;
import nl.topicus.eduarte.app.security.actions.OrganisatieEenheid;
import nl.topicus.eduarte.app.security.actions.Uitvoerend;
import nl.topicus.eduarte.app.security.actions.Verantwoordelijk;
import nl.topicus.eduarte.core.principals.deelnemer.DeelnemerVooropleidingen;

@Actions( {Instelling.class, OrganisatieEenheid.class, Verantwoordelijk.class, Uitvoerend.class,
	Begeleider.class, Docent.class})
@Description("Vooropleidingen van een deelnemer bewerken")
@Write(read = DeelnemerVooropleidingen.class)
@Module(EduArteModuleKey.KERNREGISTRATIE_DEELNEMERS)
public class DeelnemerVooropleidingenWrite extends AbstractEduArtePrincipalSource
{
	private static final long serialVersionUID = 1L;
}
