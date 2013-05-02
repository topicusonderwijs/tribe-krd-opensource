package nl.topicus.eduarte.core.principals.beheer.contract;

import nl.topicus.cobra.security.Actions;
import nl.topicus.cobra.security.Description;
import nl.topicus.cobra.security.Implies;
import nl.topicus.eduarte.app.EduArteModuleKey;
import nl.topicus.eduarte.app.Module;
import nl.topicus.eduarte.app.security.AbstractEduArtePrincipalSource;
import nl.topicus.eduarte.app.security.Display;
import nl.topicus.eduarte.app.security.actions.Instelling;
import nl.topicus.eduarte.core.principals.beheer.relatie.RelatieBeheerHome;

@Actions(Instelling.class)
@Implies(RelatieBeheerHome.class)
@Display(parent = "Relatiebeheer", label = "Contracten", module = EduArteModuleKey.KERNREGISTRATIE_DEELNEMERS)
@Description("Beheer van contracten")
@Module(EduArteModuleKey.KERNREGISTRATIE_DEELNEMERS)
public class ContractenRead extends AbstractEduArtePrincipalSource
{
	private static final long serialVersionUID = 1L;
}
