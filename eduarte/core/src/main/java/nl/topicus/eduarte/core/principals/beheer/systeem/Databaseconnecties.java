package nl.topicus.eduarte.core.principals.beheer.systeem;

import nl.topicus.cobra.security.Actions;
import nl.topicus.cobra.security.Description;
import nl.topicus.cobra.security.Implies;
import nl.topicus.cobra.security.Write;
import nl.topicus.eduarte.app.EduArteModuleKey;
import nl.topicus.eduarte.app.security.AbstractEduArtePrincipalSource;
import nl.topicus.eduarte.app.security.Display;
import nl.topicus.eduarte.app.security.actions.Beheer;
import nl.topicus.eduarte.app.security.actions.Instelling;
import nl.topicus.eduarte.core.principals.beheer.BeheerHome;

@Actions( {Beheer.class, Instelling.class})
@Implies(BeheerHome.class)
@Display(parent = "Beheer", label = "Databaseconnecties", module = EduArteModuleKey.BASISFUNCTIONALITEIT)
@Description("Databaseconnecties inzien")
@Write
public class Databaseconnecties extends AbstractEduArtePrincipalSource
{
	private static final long serialVersionUID = 1L;
}
