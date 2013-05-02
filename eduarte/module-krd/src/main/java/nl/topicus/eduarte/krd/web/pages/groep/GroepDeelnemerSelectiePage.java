package nl.topicus.eduarte.krd.web.pages.groep;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.dao.helpers.DatabaseSelection;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.web.pages.IEditPage;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis;
import nl.topicus.eduarte.entities.personen.Deelnemer;
import nl.topicus.eduarte.krd.principals.groep.GroepsdeelnameWrite;
import nl.topicus.eduarte.web.pages.SecurePage;
import nl.topicus.eduarte.web.pages.shared.AbstractDeelnemerSelectiePage;
import nl.topicus.eduarte.web.pages.shared.SelectieTarget;
import nl.topicus.eduarte.zoekfilters.VerbintenisZoekFilter;

@PageInfo(title = "Deelnemer(s) selecteren", menu = {"Groep > [groep] > Bewerken",
	"Groep > Toevoegen"})
@InPrincipal(GroepsdeelnameWrite.class)
public class GroepDeelnemerSelectiePage extends AbstractDeelnemerSelectiePage<Deelnemer> implements
		IEditPage
{
	private static final long serialVersionUID = 1L;

	public GroepDeelnemerSelectiePage(SecurePage returnPage, VerbintenisZoekFilter filter,
			DatabaseSelection<Deelnemer, Verbintenis> selection,
			SelectieTarget<Deelnemer, Verbintenis> target)
	{
		super(returnPage, filter, selection, target);
	}

	@Override
	public int getMaxResults()
	{
		return Integer.MAX_VALUE;
	}
}
