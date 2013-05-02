package nl.topicus.eduarte.krd.principals.deelnemer.verbintenis;

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
import nl.topicus.eduarte.app.security.actions.OrganisatieEenheid;
import nl.topicus.eduarte.krd.web.pages.deelnemer.verbintenis.BlokkadedatumBPVValidator;

import org.apache.wicket.security.actions.Enable;
import org.apache.wicket.security.hive.authorization.permissions.DataPermission;

@Actions( {Instelling.class, OrganisatieEenheid.class})
@Description("BPV-inschrijvingen wijzigen voor blokkadedatum")
@Module(EduArteModuleKey.KERNREGISTRATIE_DEELNEMERS)
@ExtraPermissions(@ExtraPermission(type = DataPermission.class, name = BlokkadedatumBPVValidator.WIJZIGEN_NA_MUTATIEBLOKKADE, actions = Enable.class))
@Write
@Display(parent = "Deelnemer", label = "BPV-inschrijvingen wijzigen voor blokkadedatum", module = EduArteModuleKey.KERNREGISTRATIE_DEELNEMERS)
public class MutatieBlokkadedatumBPVInschrijving extends AbstractEduArtePrincipalSource
{
	private static final long serialVersionUID = 1L;
}