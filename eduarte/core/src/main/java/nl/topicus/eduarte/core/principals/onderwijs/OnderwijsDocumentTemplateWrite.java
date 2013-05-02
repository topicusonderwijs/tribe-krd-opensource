package nl.topicus.eduarte.core.principals.onderwijs;

import nl.topicus.cobra.security.Actions;
import nl.topicus.cobra.security.Description;
import nl.topicus.cobra.security.Write;
import nl.topicus.eduarte.app.security.AbstractEduArtePrincipalSource;
import nl.topicus.eduarte.app.security.actions.Instelling;
import nl.topicus.eduarte.app.security.actions.OrganisatieEenheid;

@Actions( {Instelling.class, OrganisatieEenheid.class})
@Description("Onderwijs samenvoegdocumenten bewerken")
@Write(read = OnderwijsDocumentTemplateRead.class)
public class OnderwijsDocumentTemplateWrite extends AbstractEduArtePrincipalSource
{
	private static final long serialVersionUID = 1L;
}
