package nl.topicus.eduarte.resultaten.principals.deelnemer;

import nl.topicus.cobra.security.Actions;
import nl.topicus.cobra.security.Description;
import nl.topicus.eduarte.app.EduArteModuleKey;
import nl.topicus.eduarte.app.Module;
import nl.topicus.eduarte.app.security.AbstractEduArtePrincipalSource;
import nl.topicus.eduarte.app.security.Display;
import nl.topicus.eduarte.app.security.actions.Begeleider;
import nl.topicus.eduarte.app.security.actions.Docent;
import nl.topicus.eduarte.app.security.actions.Instelling;
import nl.topicus.eduarte.app.security.actions.OrganisatieEenheid;

@Actions( {Instelling.class, OrganisatieEenheid.class, Begeleider.class, Docent.class})
@Display(parent = "Deelnemer", label = "Resultatenmatrix (collectief)", module = EduArteModuleKey.SUMMATIEVE_RESULTATEN)
@Description("Resultatenmatrix van geselecteerde deelnemers bekijken")
@Module(EduArteModuleKey.SUMMATIEVE_RESULTATEN)
public class DeelnemerCollectiefResultatenmatrix extends AbstractEduArtePrincipalSource
{
	private static final long serialVersionUID = 1L;
}
