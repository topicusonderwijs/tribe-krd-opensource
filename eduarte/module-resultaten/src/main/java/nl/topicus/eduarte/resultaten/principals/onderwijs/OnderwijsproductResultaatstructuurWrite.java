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
import nl.topicus.eduarte.app.security.actions.Instelling;
import nl.topicus.eduarte.app.security.actions.OrganisatieEenheid;
import nl.topicus.eduarte.entities.resultaatstructuur.Resultaatstructuur;

import org.apache.wicket.security.actions.Enable;
import org.apache.wicket.security.hive.authorization.permissions.DataPermission;

@Actions( {Instelling.class, OrganisatieEenheid.class})
@Description("Resultaatstructuur van een onderwijsproduct bewerken")
@Module(EduArteModuleKey.SUMMATIEVE_RESULTATEN)
@Write(read = OnderwijsproductResultaatstructuur.class)
@Implies(OnderwijsproductHerberekeningen.class)
@ExtraPermissions(@ExtraPermission(type = DataPermission.class, name = Resultaatstructuur.RES_SUMMATIEF, actions = Enable.class))
public class OnderwijsproductResultaatstructuurWrite extends AbstractEduArtePrincipalSource
{
	private static final long serialVersionUID = 1L;
}
