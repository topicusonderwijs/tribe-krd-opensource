package nl.topicus.eduarte.krd.principals.deelnemer.bron;

import nl.topicus.cobra.security.Actions;
import nl.topicus.cobra.security.Description;
import nl.topicus.cobra.security.Write;
import nl.topicus.eduarte.app.EduArteModuleKey;
import nl.topicus.eduarte.app.Module;
import nl.topicus.eduarte.app.security.AbstractEduArtePrincipalSource;
import nl.topicus.eduarte.app.security.Display;
import nl.topicus.eduarte.app.security.actions.Instelling;

@Actions(Instelling.class)
@Description("BRON terugkoppelbestanden inlezen")
@Module(EduArteModuleKey.KERNREGISTRATIE_DEELNEMERS)
@Write
@Display(parent = "Deelnemer", label = "BRON terugkoppelbestanden inlezen", module = EduArteModuleKey.KERNREGISTRATIE_DEELNEMERS)
public class DeelnemerBronTerugkoppelbestandInlezen extends AbstractEduArtePrincipalSource
{
	private static final long serialVersionUID = 1L;
}