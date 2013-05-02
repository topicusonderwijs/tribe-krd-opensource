package nl.topicus.eduarte.core.principals.deelnemer.verbintenis;

import nl.topicus.cobra.security.Actions;
import nl.topicus.cobra.security.Description;
import nl.topicus.cobra.security.ExtraPermission;
import nl.topicus.cobra.security.ExtraPermissions;
import nl.topicus.cobra.security.Implies;
import nl.topicus.eduarte.app.EduArteModuleKey;
import nl.topicus.eduarte.app.security.AbstractEduArtePrincipalSource;
import nl.topicus.eduarte.app.security.Display;
import nl.topicus.eduarte.app.security.actions.Begeleider;
import nl.topicus.eduarte.app.security.actions.Docent;
import nl.topicus.eduarte.app.security.actions.Instelling;
import nl.topicus.eduarte.app.security.actions.OrganisatieEenheid;
import nl.topicus.eduarte.app.security.actions.Uitvoerend;
import nl.topicus.eduarte.app.security.actions.Verantwoordelijk;

import org.apache.wicket.security.actions.Enable;
import org.apache.wicket.security.actions.Render;
import org.apache.wicket.security.hive.authorization.permissions.DataPermission;

@Actions( {Instelling.class, OrganisatieEenheid.class, Verantwoordelijk.class, Uitvoerend.class,
	Begeleider.class, Docent.class})
@Implies(DeelnemerVerbintenisDocumentenRead.class)
@Display(parent = "Deelnemer", label = "Document inzien", module = EduArteModuleKey.BASISFUNCTIONALITEIT)
@Description("Documenten van een deelnemer inzien")
@ExtraPermissions(@ExtraPermission(type = DataPermission.class, name = DeelnemerVerbintenisDocumentInzien.DEELNEMER_VERBINTENIS_DOCUMENT_INZIEN, actions = {
	Render.class, Enable.class}))
public class DeelnemerVerbintenisDocumentInzien extends AbstractEduArtePrincipalSource
{
	public static final String DEELNEMER_VERBINTENIS_DOCUMENT_INZIEN =
		"DEELNEMER_VERBINTENIS_DOCUMENT_INZIEN";

	private static final long serialVersionUID = 1L;
}
