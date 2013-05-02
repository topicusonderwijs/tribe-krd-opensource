package nl.topicus.eduarte.krd.principals.beheer.bron;

import nl.topicus.cobra.security.Actions;
import nl.topicus.cobra.security.Description;
import nl.topicus.cobra.security.ExtraPermission;
import nl.topicus.cobra.security.ExtraPermissions;
import nl.topicus.cobra.security.Write;
import nl.topicus.eduarte.app.EduArteModuleKey;
import nl.topicus.eduarte.app.Module;
import nl.topicus.eduarte.app.security.AbstractEduArtePrincipalSource;
import nl.topicus.eduarte.app.security.actions.Instelling;

import org.apache.wicket.security.actions.Enable;
import org.apache.wicket.security.hive.authorization.permissions.DataPermission;

@Actions(Instelling.class)
@Description("BRON mutaties voor alle deelnemers bewerken")
@Write(read = BronOverzichtInzien.class)
@Module(EduArteModuleKey.KERNREGISTRATIE_DEELNEMERS)
@ExtraPermissions(@ExtraPermission(type = DataPermission.class, name = BronOverzichtWrite.BRON_WRITE, actions = Enable.class))
public class BronOverzichtWrite extends AbstractEduArtePrincipalSource
{
	public static final String BRON_WRITE = "BRON_WRITE";

	private static final long serialVersionUID = 1L;
}