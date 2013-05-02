package nl.topicus.eduarte.core.principals.deelnemer.verbintenis;

import nl.topicus.cobra.security.Actions;
import nl.topicus.cobra.security.Description;
import nl.topicus.cobra.security.Write;
import nl.topicus.eduarte.app.security.AbstractEduArtePrincipalSource;
import nl.topicus.eduarte.app.security.actions.Begeleider;
import nl.topicus.eduarte.app.security.actions.Docent;
import nl.topicus.eduarte.app.security.actions.Instelling;
import nl.topicus.eduarte.app.security.actions.OrganisatieEenheid;
import nl.topicus.eduarte.app.security.actions.Uitvoerend;
import nl.topicus.eduarte.app.security.actions.Verantwoordelijk;

@Actions( {Instelling.class, OrganisatieEenheid.class, Verantwoordelijk.class, Uitvoerend.class,
	Begeleider.class, Docent.class})
@Description("Documenten van een deelnemer bewerken")
@Write(read = DeelnemerVerbintenisDocumentenRead.class)
public class DeelnemerVerbintenisDocumenten extends AbstractEduArtePrincipalSource
{
	private static final long serialVersionUID = 1L;
}
