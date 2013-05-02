package nl.topicus.eduarte.krd.principals.deelnemer.verbintenis;

import nl.topicus.cobra.security.Actions;
import nl.topicus.cobra.security.Description;
import nl.topicus.cobra.security.ExtraPermission;
import nl.topicus.cobra.security.ExtraPermissions;
import nl.topicus.cobra.security.Implies;
import nl.topicus.cobra.security.Write;
import nl.topicus.eduarte.app.EduArteModuleKey;
import nl.topicus.eduarte.app.Module;
import nl.topicus.eduarte.app.security.AbstractEduArtePrincipalSource;
import nl.topicus.eduarte.app.security.Display;
import nl.topicus.eduarte.app.security.actions.Instelling;
import nl.topicus.eduarte.app.security.actions.OrganisatieEenheid;
import nl.topicus.eduarte.krd.web.pages.deelnemer.verbintenis.EditVerbintenisPage;

import org.apache.wicket.security.actions.Enable;
import org.apache.wicket.security.hive.authorization.permissions.DataPermission;

@Actions( {Instelling.class, OrganisatieEenheid.class})
@Description("Statusovergang verbintenissen met speciale autorisatie")
@Write
@Implies(DeelnemerVerbintenissenWrite.class)
@Display(parent = "Deelnemer", label = "Statusovergang verbintenissen", module = EduArteModuleKey.KERNREGISTRATIE_DEELNEMERS)
@Module(EduArteModuleKey.KERNREGISTRATIE_DEELNEMERS)
@ExtraPermissions(@ExtraPermission(type = DataPermission.class, componentPrefix = EditVerbintenisPage.class, name = EditVerbintenisPage.EXTRA_STATUSOVERGANGEN, actions = Enable.class))
public class DeelnemerVerbintenisStatusovergang extends AbstractEduArtePrincipalSource
{
	private static final long serialVersionUID = 1L;
}
