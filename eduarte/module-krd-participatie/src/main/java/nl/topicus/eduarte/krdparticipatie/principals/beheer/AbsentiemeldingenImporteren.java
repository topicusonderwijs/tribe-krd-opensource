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

@Actions( {Instelling.class})
@Implies(nl.topicus.eduarte.core.principals.beheer.BeheerHome.class)
@Write()
@Display(parent = "Beheer", label = "Absentiemeldingen importeren", module = {
	EduArteModuleKey.PARTICIPATIE, EduArteModuleKey.KERNREGISTRATIE_DEELNEMERS})
@Description("Absentiemeldingen importeren vanuit een csv-bestand")
@Module(EduArteModuleKey.KRD_PARTICIPATIE)
public class AbsentiemeldingenImporteren extends AbstractEduArtePrincipalSource
{
	private static final long serialVersionUID = 1L;

}
