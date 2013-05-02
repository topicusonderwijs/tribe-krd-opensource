package nl.topicus.eduarte.core.principals.rapportage;

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
@Description("Samenvoegdocumenten bewerken")
@Write
@Display(parent = "Beheer", group = "samenvoegdocumenten", label = "Samenvoegdocumenten verwijderen", module = EduArteModuleKey.BASISFUNCTIONALITEIT)
@Implies(DocumentTemplateWrite.class)
public class DocumentTemplateVerwijderen extends AbstractEduArtePrincipalSource
{
	private static final long serialVersionUID = 1L;
}
