package nl.topicus.eduarte.krd.principals.groep;

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
@Description("Groepen verwijderen")
@Write
@Module(EduArteModuleKey.KERNREGISTRATIE_DEELNEMERS)
@Display(parent = "Groep", label = "Groepen verwijderen", module = EduArteModuleKey.KERNREGISTRATIE_DEELNEMERS)
@Implies(GroepWrite.class)
public class GroepVerwijderen extends AbstractEduArtePrincipalSource
{
	private static final long serialVersionUID = 1L;
}
