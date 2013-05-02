package nl.topicus.eduarte.core.principals.groep;

import nl.topicus.cobra.security.Actions;
import nl.topicus.cobra.security.Description;
import nl.topicus.cobra.security.Implies;
import nl.topicus.eduarte.app.EduArteModuleKey;
import nl.topicus.eduarte.app.security.AbstractEduArtePrincipalSource;
import nl.topicus.eduarte.app.security.Display;
import nl.topicus.eduarte.app.security.EduArtePrincipal;
import nl.topicus.eduarte.app.security.actions.Begeleider;
import nl.topicus.eduarte.app.security.actions.Docent;
import nl.topicus.eduarte.app.security.actions.Instelling;

@Actions(Instelling.class)
@Implies(GroepInzien.class)
@Display(parent = "Groep", label = "Mijn groepen", module = EduArteModuleKey.BASISFUNCTIONALITEIT)
@Description("Mijn groepen")
public class GroepenMijn extends AbstractEduArtePrincipalSource
{
	private static final long serialVersionUID = 1L;

	@Override
	public boolean isImplied(EduArtePrincipal base, EduArtePrincipal possibleImplied)
	{
		if (possibleImplied.getActionClass().equals(Begeleider.class)
			|| possibleImplied.getActionClass().equals(Docent.class))
			return super.isImplied(base, possibleImplied);
		return false;
	}
}
