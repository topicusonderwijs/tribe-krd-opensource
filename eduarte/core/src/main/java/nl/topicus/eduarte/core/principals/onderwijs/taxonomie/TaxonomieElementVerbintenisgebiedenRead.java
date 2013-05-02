package nl.topicus.eduarte.core.principals.onderwijs.taxonomie;

import nl.topicus.cobra.security.Actions;
import nl.topicus.cobra.security.Description;
import nl.topicus.cobra.security.Implies;
import nl.topicus.eduarte.app.EduArteModuleKey;
import nl.topicus.eduarte.app.security.AbstractEduArtePrincipalSource;
import nl.topicus.eduarte.app.security.Display;
import nl.topicus.eduarte.app.security.actions.Instelling;
import nl.topicus.eduarte.core.principals.onderwijs.OpleidingenZoeken;

@Actions(Instelling.class)
@Implies(OpleidingenZoeken.class)
@Display(parent = "Onderwijs", label = "Verbintenis gebieden inzien", module = EduArteModuleKey.BASISFUNCTIONALITEIT)
@Description("Verbintenis gebieden inzien")
public class TaxonomieElementVerbintenisgebiedenRead extends AbstractEduArtePrincipalSource
{
	private static final long serialVersionUID = 1L;
}
