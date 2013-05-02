package nl.topicus.eduarte.onderwijscatalogus.principals.onderwijs;

import nl.topicus.cobra.security.Actions;
import nl.topicus.cobra.security.Description;
import nl.topicus.cobra.security.Write;
import nl.topicus.eduarte.app.EduArteModuleKey;
import nl.topicus.eduarte.app.Module;
import nl.topicus.eduarte.app.security.AbstractEduArtePrincipalSource;
import nl.topicus.eduarte.app.security.actions.Instelling;
import nl.topicus.eduarte.app.security.actions.OrganisatieEenheid;
import nl.topicus.eduarte.core.principals.onderwijs.OnderwijsproductPersoneel;

@Actions( {Instelling.class, OrganisatieEenheid.class})
@Description("Personeel van een onderwijsproduct bewerken")
@Module(EduArteModuleKey.ONDERWIJSCATALOGUS)
@Write(read = OnderwijsproductPersoneel.class)
public class OnderwijsproductPersoneelWrite extends AbstractEduArtePrincipalSource
{
	private static final long serialVersionUID = 1L;
}
