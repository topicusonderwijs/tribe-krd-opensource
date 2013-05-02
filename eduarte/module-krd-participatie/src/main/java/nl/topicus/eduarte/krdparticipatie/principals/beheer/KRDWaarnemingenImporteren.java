package nl.topicus.eduarte.krdparticipatie.principals.beheer;

import nl.topicus.cobra.security.Actions;
import nl.topicus.cobra.security.Description;
import nl.topicus.cobra.security.Implies;
import nl.topicus.cobra.security.Write;
import nl.topicus.eduarte.app.EduArteModuleKey;
import nl.topicus.eduarte.app.Module;
import nl.topicus.eduarte.app.security.AbstractEduArtePrincipalSource;
import nl.topicus.eduarte.app.security.Display;
import nl.topicus.eduarte.app.security.actions.Instelling;
import nl.topicus.eduarte.core.principals.beheer.BeheerHome;
import nl.topicus.eduarte.core.principals.beheer.systeem.Jobs;

@Actions( {Instelling.class})
@Implies( {BeheerHome.class, Jobs.class})
@Write
@Display(parent = "Beheer", label = "Waarnemingen importeren (KRD)", module = {
	EduArteModuleKey.PARTICIPATIE, EduArteModuleKey.KERNREGISTRATIE_DEELNEMERS})
@Description("Waarnemingen importeren vanuit een csv-bestand")
@Module(EduArteModuleKey.KRD_PARTICIPATIE)
public class KRDWaarnemingenImporteren extends AbstractEduArtePrincipalSource
{
	private static final long serialVersionUID = 1L;

}
