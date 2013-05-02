package nl.topicus.eduarte.core.principals.onderwijs;

import nl.topicus.cobra.security.Actions;
import nl.topicus.cobra.security.Description;
import nl.topicus.cobra.security.Implies;
import nl.topicus.eduarte.app.EduArteModuleKey;
import nl.topicus.eduarte.app.Module;
import nl.topicus.eduarte.app.security.AbstractEduArtePrincipalSource;
import nl.topicus.eduarte.app.security.Display;
import nl.topicus.eduarte.app.security.actions.Instelling;
import nl.topicus.eduarte.app.security.actions.OrganisatieEenheid;

@Actions( {Instelling.class, OrganisatieEenheid.class})
@Display(parent = "Amarantis", label = "Amarantis Curriculum", module = EduArteModuleKey.ONDERWIJSCATALOGUS)
@Description("Amarantis Curriculum inzien")
@Module(EduArteModuleKey.ONDERWIJSCATALOGUS_AMARANTIS)
@Implies(OpleidingInzien.class)
public class CurriculumInzien extends AbstractEduArtePrincipalSource
{
	private static final long serialVersionUID = 1L;
}
