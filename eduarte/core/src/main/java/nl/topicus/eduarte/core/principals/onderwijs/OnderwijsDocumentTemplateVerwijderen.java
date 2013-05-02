package nl.topicus.eduarte.core.principals.onderwijs;

import nl.topicus.cobra.security.Actions;
import nl.topicus.cobra.security.Description;
import nl.topicus.cobra.security.Implies;
import nl.topicus.cobra.security.Write;
import nl.topicus.eduarte.app.EduArteModuleKey;
import nl.topicus.eduarte.app.security.AbstractEduArtePrincipalSource;
import nl.topicus.eduarte.app.security.Display;
import nl.topicus.eduarte.app.security.actions.Instelling;
import nl.topicus.eduarte.app.security.actions.OrganisatieEenheid;

@Actions( {Instelling.class, OrganisatieEenheid.class})
@Description("Onderwijs samenvoegdocumenten bewerken")
@Write
@Display(parent = "Onderwijs", group = "samenvoegdocumenten", label = "Onderwijs samenvoegdocumenten verwijderen", module = EduArteModuleKey.BASISFUNCTIONALITEIT)
@Implies(OnderwijsDocumentTemplateWrite.class)
public class OnderwijsDocumentTemplateVerwijderen extends AbstractEduArtePrincipalSource
{
	private static final long serialVersionUID = 1L;
}
