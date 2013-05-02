package nl.topicus.eduarte.core.principals.beheer.participatie;

import nl.topicus.cobra.security.Actions;
import nl.topicus.cobra.security.Description;
import nl.topicus.cobra.security.Implies;
import nl.topicus.cobra.security.Write;
import nl.topicus.eduarte.app.EduArteModuleKey;
import nl.topicus.eduarte.app.Module;
import nl.topicus.eduarte.app.security.AbstractEduArtePrincipalSource;
import nl.topicus.eduarte.app.security.Display;
import nl.topicus.eduarte.app.security.actions.Instelling;
import nl.topicus.eduarte.core.principals.beheer.BeheerHome;

@Actions(Instelling.class)
@Implies(BeheerHome.class)
@Write
@Display(parent = "Beheer", label = "Afspraaktype Beheer", module = {EduArteModuleKey.PARTICIPATIE,
	EduArteModuleKey.DEELNEMER_BEGELEIDING})
@Description("Afspraaktypen bewerken/toevoegen")
@Module( {EduArteModuleKey.PARTICIPATIE, EduArteModuleKey.DEELNEMER_BEGELEIDING})
public class AfspraaktypesWrite extends AbstractEduArtePrincipalSource
{
	private static final long serialVersionUID = 1L;
}