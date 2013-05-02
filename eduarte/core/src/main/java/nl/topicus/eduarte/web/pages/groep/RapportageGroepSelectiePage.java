package nl.topicus.eduarte.web.pages.groep;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.dao.helpers.DatabaseSelection;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.eduarte.core.principals.groep.GroepRapportages;
import nl.topicus.eduarte.entities.groep.Groep;
import nl.topicus.eduarte.web.pages.SecurePage;
import nl.topicus.eduarte.web.pages.shared.AbstractGroepSelectiePage;
import nl.topicus.eduarte.web.pages.shared.SelectieTarget;
import nl.topicus.eduarte.zoekfilters.GroepZoekFilter;

@PageInfo(title = "Groep(en) selecteren", menu = "Groep > Rapportages")
@InPrincipal(GroepRapportages.class)
public class RapportageGroepSelectiePage extends AbstractGroepSelectiePage<Groep>
{

	private static final long serialVersionUID = 1L;

	public RapportageGroepSelectiePage(SecurePage returnPage, GroepZoekFilter filter,
			DatabaseSelection<Groep, Groep> selection, SelectieTarget<Groep, Groep> target)
	{
		super(returnPage, filter, selection, target);
	}

	@Override
	public int getMaxResults()
	{
		return 5000;
	}

	@Override
	public boolean supportsBookmarks()
	{
		return false;
	}
}
