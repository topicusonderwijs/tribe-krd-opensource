package nl.topicus.eduarte.core.principals.onderwijs.taxonomie;

import nl.topicus.cobra.security.Actions;
import nl.topicus.cobra.security.Description;
import nl.topicus.cobra.security.Implies;
import nl.topicus.eduarte.app.EduArteModuleKey;
import nl.topicus.eduarte.app.security.AbstractEduArtePrincipalSource;
import nl.topicus.eduarte.app.security.Display;
import nl.topicus.eduarte.app.security.actions.Beheer;
import nl.topicus.eduarte.app.security.actions.Instelling;
import nl.topicus.eduarte.core.principals.onderwijs.OpleidingenZoeken;

@Actions( {Beheer.class, Instelling.class})
@Implies( {OpleidingenZoeken.class, TaxonomieElementVerbintenisgebiedenRead.class})
@Display(parent = "Onderwijs", label = "Verbintenis gebieden zoeken", module = EduArteModuleKey.BASISFUNCTIONALITEIT)
@Description("Verbintenis gebieden zoeken")
public class TaxonomieElementVerbintenisgebiedenZoeken extends AbstractEduArtePrincipalSource
{
	private static final long serialVersionUID = 1L;
}
