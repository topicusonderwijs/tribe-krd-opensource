package nl.topicus.eduarte.krdparticipatie.principals.deelnemer;

import nl.topicus.cobra.security.Actions;
import nl.topicus.cobra.security.Description;
import nl.topicus.cobra.security.Implies;
import nl.topicus.eduarte.app.EduArteModuleKey;
import nl.topicus.eduarte.app.Module;
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
@Display(parent = "Deelnemer", label = "Waarnemingen per onderwijsproduct", module = {
	EduArteModuleKey.PARTICIPATIE, EduArteModuleKey.KERNREGISTRATIE_DEELNEMERS})
@Description("Raportage waarnemingen totalen per onderwijsproduct")
@Module(EduArteModuleKey.KRD_PARTICIPATIE)
public class DeelnemerTotalenPerOnderwijsproductRapportage extends AbstractEduArtePrincipalSource
{
	private static final long serialVersionUID = 1L;

}
