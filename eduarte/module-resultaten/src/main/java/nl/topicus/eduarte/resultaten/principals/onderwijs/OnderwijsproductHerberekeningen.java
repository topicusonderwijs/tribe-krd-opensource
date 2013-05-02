package nl.topicus.eduarte.resultaten.principals.onderwijs;

import nl.topicus.cobra.security.Actions;
import nl.topicus.cobra.security.Description;
import nl.topicus.cobra.security.Implies;
import nl.topicus.eduarte.app.EduArteModuleKey;
import nl.topicus.eduarte.app.Module;
import nl.topicus.eduarte.app.security.AbstractEduArtePrincipalSource;
import nl.topicus.eduarte.app.security.Display;
import nl.topicus.eduarte.app.security.actions.Instelling;
import nl.topicus.eduarte.app.security.actions.OrganisatieEenheid;
import nl.topicus.eduarte.core.principals.onderwijs.OnderwijsproductInzien;

@Actions( {Instelling.class, OrganisatieEenheid.class})
@Display(parent = "Onderwijs", label = "Resultaatherberekeningen", module = EduArteModuleKey.SUMMATIEVE_RESULTATEN)
@Description("Herberekeningen van de resultaatstructuur van een onderwijsproduct bekijken")
@Implies( {OnderwijsproductInzien.class, OnderwijsproductResultaatstructuur.class})
@Module(EduArteModuleKey.SUMMATIEVE_RESULTATEN)
public class OnderwijsproductHerberekeningen extends AbstractEduArtePrincipalSource
{
	private static final long serialVersionUID = 1L;
}
