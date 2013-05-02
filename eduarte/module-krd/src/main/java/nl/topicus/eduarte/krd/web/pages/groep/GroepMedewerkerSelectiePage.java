package nl.topicus.eduarte.krd.web.pages.groep;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.dao.helpers.DatabaseSelection;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.web.pages.IEditPage;
import nl.topicus.eduarte.entities.personen.Medewerker;
import nl.topicus.eduarte.krd.principals.groep.GroepWrite;
import nl.topicus.eduarte.web.pages.SecurePage;
import nl.topicus.eduarte.web.pages.shared.AbstractMedewerkerSelectiePage;
import nl.topicus.eduarte.web.pages.shared.SelectieTarget;
import nl.topicus.eduarte.zoekfilters.MedewerkerZoekFilter;

@PageInfo(title = "Medewerker(s) selecteren", menu = {"Groep > [groep] > Bewerken",
	"Groep > Toevoegen"})
@InPrincipal(GroepWrite.class)
public class GroepMedewerkerSelectiePage extends AbstractMedewerkerSelectiePage<Medewerker>
		implements IEditPage
{
	private static final long serialVersionUID = 1L;

	public GroepMedewerkerSelectiePage(SecurePage returnPage, MedewerkerZoekFilter filter,
			DatabaseSelection<Medewerker, Medewerker> selection,
			SelectieTarget<Medewerker, Medewerker> target)
	{
		super(returnPage, filter, selection, target);
	}

	@Override
	public int getMaxResults()
	{
		return Integer.MAX_VALUE;
	}
}
