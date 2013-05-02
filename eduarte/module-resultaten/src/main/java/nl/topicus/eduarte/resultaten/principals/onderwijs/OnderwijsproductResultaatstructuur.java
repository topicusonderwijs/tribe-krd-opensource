package nl.topicus.eduarte.resultaten.principals.onderwijs;

import nl.topicus.cobra.security.Actions;
import nl.topicus.cobra.security.Description;
import nl.topicus.cobra.security.ExtraPermission;
import nl.topicus.cobra.security.ExtraPermissions;
import nl.topicus.cobra.security.Implies;
import nl.topicus.eduarte.app.EduArteModuleKey;
import nl.topicus.eduarte.app.Module;
import nl.topicus.eduarte.app.security.AbstractEduArtePrincipalSource;
import nl.topicus.eduarte.app.security.Display;
import nl.topicus.eduarte.app.security.actions.Instelling;
import nl.topicus.eduarte.app.security.actions.OrganisatieEenheid;
import nl.topicus.eduarte.core.principals.onderwijs.OnderwijsproductInzien;
import nl.topicus.eduarte.entities.resultaatstructuur.Resultaatstructuur;

import org.apache.wicket.security.actions.Render;
import org.apache.wicket.security.hive.authorization.permissions.DataPermission;

@Actions( {Instelling.class, OrganisatieEenheid.class})
@Display(parent = "Onderwijs", group = "onderwijsproduct", label = "Resultaatstructuur", module = EduArteModuleKey.SUMMATIEVE_RESULTATEN)
@Description("Resultaatstructuur van een onderwijsproduct bekijken")
@Implies(OnderwijsproductInzien.class)
@Module(EduArteModuleKey.SUMMATIEVE_RESULTATEN)
@ExtraPermissions(@ExtraPermission(type = DataPermission.class, name = Resultaatstructuur.RES_SUMMATIEF, actions = Render.class))
public class OnderwijsproductResultaatstructuur extends AbstractEduArtePrincipalSource
{
	private static final long serialVersionUID = 1L;
}
