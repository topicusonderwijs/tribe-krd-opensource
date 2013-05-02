package nl.topicus.eduarte.krd.principals.deelnemer.verbintenis;

import nl.topicus.cobra.security.Actions;
import nl.topicus.cobra.security.Description;
import nl.topicus.cobra.security.ExtraPermission;
import nl.topicus.cobra.security.ExtraPermissions;
import nl.topicus.cobra.security.Write;
import nl.topicus.eduarte.app.EduArteModuleKey;
import nl.topicus.eduarte.app.Module;
import nl.topicus.eduarte.app.security.AbstractEduArtePrincipalSource;
import nl.topicus.eduarte.app.security.actions.Begeleider;
import nl.topicus.eduarte.app.security.actions.Docent;
import nl.topicus.eduarte.app.security.actions.Instelling;
import nl.topicus.eduarte.app.security.actions.OrganisatieEenheid;
import nl.topicus.eduarte.app.security.actions.Uitvoerend;
import nl.topicus.eduarte.app.security.actions.Verantwoordelijk;
import nl.topicus.eduarte.core.principals.deelnemer.verbintenis.DeelnemerBPV;
import nl.topicus.eduarte.web.pages.deelnemer.verbintenis.bpv.DeelnemerBPVPage;

import org.apache.wicket.security.actions.Enable;
import org.apache.wicket.security.hive.authorization.permissions.DataPermission;

@Actions( {Instelling.class, OrganisatieEenheid.class, Verantwoordelijk.class, Uitvoerend.class,
	Begeleider.class, Docent.class})
@Description("BPV van een deelnemer bewerken")
@Write(read = DeelnemerBPV.class)
@Module(EduArteModuleKey.KERNREGISTRATIE_DEELNEMERS)
@ExtraPermissions(@ExtraPermission(type = DataPermission.class, componentPrefix = DeelnemerBPVPage.class, name = "AfdrukkenButtonTonen", actions = Enable.class))
public class DeelnemerBPVWrite extends AbstractEduArtePrincipalSource
{
	private static final long serialVersionUID = 1L;
}
