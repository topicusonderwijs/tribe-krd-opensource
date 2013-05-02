package nl.topicus.eduarte.krd.principals.deelnemer.bron;

import nl.topicus.cobra.security.Actions;
import nl.topicus.cobra.security.Description;
import nl.topicus.cobra.security.ExtraPermission;
import nl.topicus.cobra.security.ExtraPermissions;
import nl.topicus.cobra.security.Write;
import nl.topicus.eduarte.app.EduArteModuleKey;
import nl.topicus.eduarte.app.Module;
import nl.topicus.eduarte.app.security.AbstractEduArtePrincipalSource;
import nl.topicus.eduarte.app.security.Display;
import nl.topicus.eduarte.app.security.actions.Instelling;

import org.apache.wicket.security.actions.Enable;
import org.apache.wicket.security.hive.authorization.permissions.DataPermission;

@Actions(Instelling.class)
@Description("BRON CFI-terugmelding inlezen")
@Module(EduArteModuleKey.KERNREGISTRATIE_DEELNEMERS)
@Write
@Display(parent = "Deelnemer", label = "BRON CFI-terugmelding inlezen", module = EduArteModuleKey.KERNREGISTRATIE_DEELNEMERS)
@ExtraPermissions(@ExtraPermission(type = DataPermission.class, name = DeelnemerBronCfiTerugmeldingInlezen.BRON_CFI_INLEZEN, actions = Enable.class))
public class DeelnemerBronCfiTerugmeldingInlezen extends AbstractEduArtePrincipalSource
{
	public static final String BRON_CFI_INLEZEN = "BRON_CFI_INLEZEN";

	private static final long serialVersionUID = 1L;
}