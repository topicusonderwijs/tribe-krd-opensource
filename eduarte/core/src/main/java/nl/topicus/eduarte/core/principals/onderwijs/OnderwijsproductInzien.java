package nl.topicus.eduarte.core.principals.onderwijs;

import nl.topicus.cobra.security.Actions;
import nl.topicus.cobra.security.Description;
import nl.topicus.eduarte.app.EduArteModuleKey;
import nl.topicus.eduarte.app.security.AbstractEduArtePrincipalSource;
import nl.topicus.eduarte.app.security.Display;
import nl.topicus.eduarte.app.security.actions.Instelling;
import nl.topicus.eduarte.app.security.actions.OrganisatieEenheid;

@Actions( {Instelling.class, OrganisatieEenheid.class})
@Display(parent = "Onderwijs", group = "onderwijsproduct", label = "Onderwijsproduct algemeen", module = {
	EduArteModuleKey.ONDERWIJSCATALOGUS, EduArteModuleKey.BASISFUNCTIONALITEIT})
@Description("Basisgegevens van een onderwijsproduct bekijken")
public class OnderwijsproductInzien extends AbstractEduArtePrincipalSource
{
	private static final long serialVersionUID = 1L;
}
