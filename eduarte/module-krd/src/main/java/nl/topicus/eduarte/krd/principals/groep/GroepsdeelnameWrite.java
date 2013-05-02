package nl.topicus.eduarte.krd.principals.groep;

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
import nl.topicus.eduarte.core.principals.deelnemer.groep.DeelnemerGroepen;
import nl.topicus.eduarte.core.principals.groep.GroepInzien;
import nl.topicus.eduarte.entities.groep.Groep;

import org.apache.wicket.security.actions.Enable;
import org.apache.wicket.security.hive.authorization.permissions.DataPermission;

@Actions( {Instelling.class, OrganisatieEenheid.class})
@Description("Groepsdeelnames bewerken")
@Write
@Module(EduArteModuleKey.KERNREGISTRATIE_DEELNEMERS)
@ExtraPermissions(@ExtraPermission(type = DataPermission.class, name = Groep.EDIT_DEELNAME, actions = Enable.class))
@Display(parent = "Groep", label = "Groepsdeelnames", module = EduArteModuleKey.KERNREGISTRATIE_DEELNEMERS)
@Implies( {GroepInzien.class, DeelnemerGroepen.class})
public class GroepsdeelnameWrite extends AbstractEduArtePrincipalSource
{
	private static final long serialVersionUID = 1L;
}
