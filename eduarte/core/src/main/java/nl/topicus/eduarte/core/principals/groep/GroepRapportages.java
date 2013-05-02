package nl.topicus.eduarte.core.principals.groep;

import nl.topicus.cobra.security.Actions;
import nl.topicus.cobra.security.Description;
import nl.topicus.eduarte.app.EduArteModuleKey;
import nl.topicus.eduarte.app.security.AbstractEduArtePrincipalSource;
import nl.topicus.eduarte.app.security.Display;
import nl.topicus.eduarte.app.security.actions.Begeleider;
import nl.topicus.eduarte.app.security.actions.Docent;
import nl.topicus.eduarte.app.security.actions.Instelling;
import nl.topicus.eduarte.app.security.actions.OrganisatieEenheid;

@Actions( {Instelling.class, OrganisatieEenheid.class, Begeleider.class, Docent.class})
@Display(parent = "Groep", label = "Rapportages", module = EduArteModuleKey.BASISFUNCTIONALITEIT)
@Description("Rapportages over groepen maken")
public class GroepRapportages extends AbstractEduArtePrincipalSource
{
	private static final long serialVersionUID = 1L;
}
