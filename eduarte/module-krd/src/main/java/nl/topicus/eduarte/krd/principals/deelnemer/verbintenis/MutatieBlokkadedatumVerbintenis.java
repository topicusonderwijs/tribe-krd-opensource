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
import nl.topicus.eduarte.krd.web.pages.deelnemer.verbintenis.BlokkadedatumVerbintenisValidator;

import org.apache.wicket.security.actions.Enable;
import org.apache.wicket.security.hive.authorization.permissions.DataPermission;

@Actions( {Instelling.class, OrganisatieEenheid.class})
@Description("Verbintenissen wijzigen voor blokkadedatum")
@Write
@Implies(DeelnemerVerbintenissenWrite.class)
@Display(parent = "Deelnemer", label = "Verbintenissen wijzigen voor blokkadedatum", module = EduArteModuleKey.KERNREGISTRATIE_DEELNEMERS)
@Module(EduArteModuleKey.KERNREGISTRATIE_DEELNEMERS)
@ExtraPermissions(@ExtraPermission(type = DataPermission.class, name = BlokkadedatumVerbintenisValidator.WIJZIGEN_NA_MUTATIEBLOKKADE, actions = Enable.class))
public class MutatieBlokkadedatumVerbintenis extends AbstractEduArtePrincipalSource
{
	private static final long serialVersionUID = 1L;
}