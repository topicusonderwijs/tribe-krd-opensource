package nl.topicus.eduarte.onderwijscatalogus.principals.onderwijs;

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
@Description("Toegestane onderwijsproducten importeren")
@Module(EduArteModuleKey.ONDERWIJSCATALOGUS)
@Display(parent = "Onderwijs", label = "Toegestane onderwijsproducten importeren", module = EduArteModuleKey.ONDERWIJSCATALOGUS)
@Write()
@Implies(OnderwijsproductenZoeken.class)
public class ToegestaneOnderwijsproductenImporteren extends AbstractEduArtePrincipalSource
{
	private static final long serialVersionUID = 1L;
}
