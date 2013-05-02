package nl.topicus.eduarte.krd.principals.deelnemer;

import nl.topicus.cobra.security.Actions;
import nl.topicus.cobra.security.Description;
import nl.topicus.cobra.security.Implies;
import nl.topicus.cobra.security.Write;
import nl.topicus.eduarte.app.EduArteModuleKey;
import nl.topicus.eduarte.app.Module;
import nl.topicus.eduarte.app.security.AbstractEduArtePrincipalSource;
import nl.topicus.eduarte.app.security.Display;
import nl.topicus.eduarte.app.security.actions.Instelling;
import nl.topicus.eduarte.app.security.actions.OrganisatieEenheid;
import nl.topicus.eduarte.core.principals.deelnemer.DeelnemerInzien;

@Actions( {Instelling.class, OrganisatieEenheid.class})
@Display(parent = "Deelnemer", label = "Intakewizard", module = EduArteModuleKey.KERNREGISTRATIE_DEELNEMERS)
@Description("Intakewizard uitvoeren")
@Implies( {DeelnemerIntakeWizardStap0.class, DeelnemerInzien.class})
@Module(EduArteModuleKey.KERNREGISTRATIE_DEELNEMERS)
@Write
public class DeelnemerIntakeWizard extends AbstractEduArtePrincipalSource
{
	private static final long serialVersionUID = 1L;
}
