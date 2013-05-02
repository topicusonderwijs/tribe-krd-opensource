package nl.topicus.eduarte.resultaten.principals.onderwijs;

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
import nl.topicus.eduarte.core.principals.onderwijs.OnderwijsproductenZoeken;

@Actions( {Instelling.class, OrganisatieEenheid.class})
@Description("Resultaatstructuur van een onderwijsproduct exporteren")
@Module(EduArteModuleKey.SUMMATIEVE_RESULTATEN)
@Display(parent = "Onderwijs", label = "Resultaatstructuur exporteren", module = EduArteModuleKey.SUMMATIEVE_RESULTATEN)
@Write
@Implies(OnderwijsproductenZoeken.class)
public class OnderwijsproductResultaatstructuurExporteren extends AbstractEduArtePrincipalSource
{
	private static final long serialVersionUID = 1L;
}
