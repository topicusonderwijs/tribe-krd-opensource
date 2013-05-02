package nl.topicus.eduarte.core.principals.onderwijs.taxonomie;

import nl.topicus.cobra.security.Actions;
import nl.topicus.cobra.security.Description;
import nl.topicus.eduarte.app.EduArteModuleKey;
import nl.topicus.eduarte.app.security.AbstractEduArtePrincipalSource;
import nl.topicus.eduarte.app.security.Display;
import nl.topicus.eduarte.app.security.actions.Beheer;
import nl.topicus.eduarte.app.security.actions.Instelling;

@Actions( {Beheer.class, Instelling.class})
@Display(parent = "Onderwijs", label = "Taxonomie inzien", module = {
	EduArteModuleKey.KERNREGISTRATIE_DEELNEMERS, EduArteModuleKey.BASISFUNCTIONALITEIT})
@Description("Taxonomie inzien")
public class TaxonomieElementRead extends AbstractEduArtePrincipalSource
{
	private static final long serialVersionUID = 1L;
}
