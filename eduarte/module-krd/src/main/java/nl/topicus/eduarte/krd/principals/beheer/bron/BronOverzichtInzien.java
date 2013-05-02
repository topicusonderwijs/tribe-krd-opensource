package nl.topicus.eduarte.krd.principals.beheer.bron;

import nl.topicus.cobra.security.Actions;
import nl.topicus.cobra.security.Description;
import nl.topicus.cobra.security.Implies;
import nl.topicus.eduarte.app.EduArteModuleKey;
import nl.topicus.eduarte.app.Module;
import nl.topicus.eduarte.app.security.AbstractEduArtePrincipalSource;
import nl.topicus.eduarte.app.security.Display;
import nl.topicus.eduarte.app.security.actions.Instelling;
import nl.topicus.eduarte.app.security.actions.OrganisatieEenheid;
import nl.topicus.eduarte.core.principals.beheer.BeheerHome;
import nl.topicus.eduarte.core.principals.deelnemer.DeelnemerInzien;

@Actions( {Instelling.class, OrganisatieEenheid.class})
@Description("Overzicht van BRON mutaties voor alle deelnemers inzien")
@Module(EduArteModuleKey.KERNREGISTRATIE_DEELNEMERS)
@Display(parent = "Beheer", label = "BRON mutaties collectief", module = EduArteModuleKey.KERNREGISTRATIE_DEELNEMERS)
@Implies( {BeheerHome.class, DeelnemerInzien.class})
public class BronOverzichtInzien extends AbstractEduArtePrincipalSource
{
	private static final long serialVersionUID = 1L;
}