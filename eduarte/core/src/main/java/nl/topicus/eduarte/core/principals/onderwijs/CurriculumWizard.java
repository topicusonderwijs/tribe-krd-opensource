package nl.topicus.eduarte.core.principals.onderwijs;

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

@Actions( {Instelling.class, OrganisatieEenheid.class})
@Display(parent = "Amarantis", label = "Amarantis Curriculum Wizard", module = EduArteModuleKey.ONDERWIJSCATALOGUS)
@Description("Amarantis Curriculum Wizard")
@Module(EduArteModuleKey.ONDERWIJSCATALOGUS_AMARANTIS)
@Write
@Implies(CurriculumInzien.class)
public class CurriculumWizard extends AbstractEduArtePrincipalSource
{
	private static final long serialVersionUID = 1L;
}