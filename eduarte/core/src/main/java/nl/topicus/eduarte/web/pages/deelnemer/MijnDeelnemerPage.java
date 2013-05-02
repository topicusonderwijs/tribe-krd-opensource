package nl.topicus.eduarte.web.pages.deelnemer;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.eduarte.app.security.actions.Instelling;
import nl.topicus.eduarte.app.security.actions.OrganisatieEenheid;
import nl.topicus.eduarte.app.security.actions.SearchImplementsActions;
import nl.topicus.eduarte.core.principals.deelnemer.DeelnemersZoeken;
import nl.topicus.eduarte.zoekfilters.VerbintenisZoekFilter;

@PageInfo(title = "Mijn deelnemers", menu = "Deelnemer")
@InPrincipal(DeelnemersZoeken.class)
@SearchImplementsActions( {Instelling.class, OrganisatieEenheid.class})
public class MijnDeelnemerPage extends DeelnemerZoekenPage
{
	public MijnDeelnemerPage(VerbintenisZoekFilter filter)
	{
		super(filter);
	}

}
