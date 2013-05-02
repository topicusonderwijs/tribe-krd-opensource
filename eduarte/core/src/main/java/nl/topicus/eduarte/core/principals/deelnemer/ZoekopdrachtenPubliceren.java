package nl.topicus.eduarte.core.principals.deelnemer;

import nl.topicus.cobra.security.Actions;
import nl.topicus.cobra.security.Description;
import nl.topicus.cobra.security.ExtraPermission;
import nl.topicus.cobra.security.ExtraPermissions;
import nl.topicus.cobra.security.Implies;
import nl.topicus.eduarte.app.EduArteModuleKey;
import nl.topicus.eduarte.app.security.AbstractEduArtePrincipalSource;
import nl.topicus.eduarte.app.security.Display;
import nl.topicus.eduarte.app.security.actions.Instelling;
import nl.topicus.eduarte.web.pages.deelnemer.zoekopdrachten.DeelnemerZoekOpdrachtEditPage;

import org.apache.wicket.security.actions.Render;
import org.apache.wicket.security.hive.authorization.permissions.DataPermission;

@Actions(Instelling.class)
@Implies(DeelnemersZoekenUitgebreid.class)
@Display(parent = "Deelnemer", label = "Zoekopdrachten publiceren", module = EduArteModuleKey.BASISFUNCTIONALITEIT)
@ExtraPermissions(@ExtraPermission(type = DataPermission.class, componentPrefix = DeelnemerZoekOpdrachtEditPage.class, name = DeelnemerZoekOpdrachtEditPage.PUBLICEREN, actions = Render.class))
@Description("Zoekopdrachten publiceren")
public class ZoekopdrachtenPubliceren extends AbstractEduArtePrincipalSource
{
	private static final long serialVersionUID = 1L;
}
