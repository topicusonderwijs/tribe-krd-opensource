package nl.topicus.eduarte.krd.principals.deelnemer.onderwijsproduct;

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
import nl.topicus.eduarte.core.principals.deelnemer.onderwijsproducten.DeelnemerOnderwijsproductenInzien;

@Actions( {Instelling.class, OrganisatieEenheid.class, Verantwoordelijk.class, Uitvoerend.class,
	Begeleider.class, Docent.class})
@Description("Afgenomen onderwijsproducten van een deelnemer bewerken")
@Write(read = DeelnemerOnderwijsproductenInzien.class)
@Module(EduArteModuleKey.KERNREGISTRATIE_DEELNEMERS)
public class DeelnemerOnderwijsproductenWrite extends AbstractEduArtePrincipalSource
{
	private static final long serialVersionUID = 1L;

}
