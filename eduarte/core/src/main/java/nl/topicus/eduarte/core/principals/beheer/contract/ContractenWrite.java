package nl.topicus.eduarte.core.principals.beheer.contract;

import nl.topicus.cobra.security.Actions;
import nl.topicus.cobra.security.Description;
import nl.topicus.cobra.security.Implies;
import nl.topicus.cobra.security.Write;
import nl.topicus.eduarte.app.EduArteModuleKey;
import nl.topicus.eduarte.app.Module;
import nl.topicus.eduarte.app.security.AbstractEduArtePrincipalSource;
import nl.topicus.eduarte.app.security.actions.Instelling;
import nl.topicus.eduarte.core.principals.beheer.relatie.RelatieBeheerHome;

@Actions(Instelling.class)
@Implies(RelatieBeheerHome.class)
@Write(read = ContractenRead.class)
@Description("Beheer van contracten")
@Module(EduArteModuleKey.KERNREGISTRATIE_DEELNEMERS)
public class ContractenWrite extends AbstractEduArtePrincipalSource
{
	private static final long serialVersionUID = 1L;
}
