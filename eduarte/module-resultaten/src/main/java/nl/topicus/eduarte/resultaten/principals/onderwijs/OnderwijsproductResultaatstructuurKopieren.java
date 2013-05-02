package nl.topicus.eduarte.resultaten.principals.onderwijs;

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
import nl.topicus.eduarte.app.security.actions.Instelling;
import nl.topicus.eduarte.app.security.actions.OrganisatieEenheid;
import nl.topicus.eduarte.core.principals.onderwijs.OnderwijsproductenZoeken;
import nl.topicus.eduarte.entities.resultaatstructuur.Resultaatstructuur;

import org.apache.wicket.security.actions.Enable;
import org.apache.wicket.security.hive.authorization.permissions.DataPermission;

@Actions( {Instelling.class, OrganisatieEenheid.class})
@Description("Resultaatstructuur van een onderwijsproduct kopiëren")
@Module(EduArteModuleKey.SUMMATIEVE_RESULTATEN)
@Display(parent = "Onderwijs", label = "Resultaatstructuur kopiëren", module = EduArteModuleKey.SUMMATIEVE_RESULTATEN)
@Write
@Implies(OnderwijsproductenZoeken.class)
@ExtraPermissions( {@ExtraPermission(type = DataPermission.class, name = Resultaatstructuur.RES_SUMMATIEF
	+ Resultaatstructuur.KOPIEREN, actions = Enable.class)})
public class OnderwijsproductResultaatstructuurKopieren extends AbstractEduArtePrincipalSource
{
	private static final long serialVersionUID = 1L;
}
