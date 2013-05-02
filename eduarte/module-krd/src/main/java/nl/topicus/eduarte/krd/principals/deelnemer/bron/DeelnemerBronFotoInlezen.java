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
@Description("BRON Foto's inlezen")
@Module(EduArteModuleKey.KERNREGISTRATIE_DEELNEMERS)
@Write
@Display(parent = "Deelnemer", label = "BRON foto's inlezen", module = EduArteModuleKey.KERNREGISTRATIE_DEELNEMERS)
@ExtraPermissions(@ExtraPermission(type = DataPermission.class, name = DeelnemerBronFotoInlezen.BRON_FOTO_INLEZEN, actions = Enable.class))
public class DeelnemerBronFotoInlezen extends AbstractEduArtePrincipalSource
{
	public static final String BRON_FOTO_INLEZEN = "BRON_FOTO_INLEZEN";

	private static final long serialVersionUID = 1L;
}