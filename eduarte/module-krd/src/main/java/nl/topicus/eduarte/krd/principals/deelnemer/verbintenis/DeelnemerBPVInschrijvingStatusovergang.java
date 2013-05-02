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
import nl.topicus.eduarte.app.security.actions.Begeleider;
import nl.topicus.eduarte.app.security.actions.Docent;
import nl.topicus.eduarte.app.security.actions.Instelling;
import nl.topicus.eduarte.app.security.actions.OrganisatieEenheid;
import nl.topicus.eduarte.krd.web.pages.deelnemer.verbintenis.bpv.EditBPVInschrijvingPage;

import org.apache.wicket.security.actions.Enable;
import org.apache.wicket.security.hive.authorization.permissions.DataPermission;

@Actions( {Instelling.class, OrganisatieEenheid.class, Begeleider.class, Docent.class})
@Description("Statusovergang BPV inschrijvingen")
@Write
@Implies(DeelnemerBPVWrite.class)
@Display(parent = "Deelnemer", label = "Statusovergang BPV inschrijvingen", module = EduArteModuleKey.KERNREGISTRATIE_DEELNEMERS)
@Module(EduArteModuleKey.KERNREGISTRATIE_DEELNEMERS)
@ExtraPermissions(@ExtraPermission(type = DataPermission.class, componentPrefix = EditBPVInschrijvingPage.class, name = EditBPVInschrijvingPage.EXTRA_STATUSOVERGANGEN, actions = Enable.class))
public class DeelnemerBPVInschrijvingStatusovergang extends AbstractEduArtePrincipalSource
{
	private static final long serialVersionUID = 1L;
}
