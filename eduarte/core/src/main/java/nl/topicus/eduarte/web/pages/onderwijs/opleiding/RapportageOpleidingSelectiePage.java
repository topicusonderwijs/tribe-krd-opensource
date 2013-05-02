package nl.topicus.eduarte.web.pages.onderwijs.opleiding;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.dao.helpers.DatabaseSelection;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.eduarte.core.principals.onderwijs.OpleidingRapportages;
import nl.topicus.eduarte.entities.opleiding.Opleiding;
import nl.topicus.eduarte.web.pages.SecurePage;
import nl.topicus.eduarte.web.pages.shared.AbstractOpleidingSelectiePage;
import nl.topicus.eduarte.web.pages.shared.SelectieTarget;
import nl.topicus.eduarte.zoekfilters.OpleidingZoekFilter;

@PageInfo(title = "Opleiding(en) selecteren", menu = "Onderwijs > Opleiding rapportages")
@InPrincipal(OpleidingRapportages.class)
public class RapportageOpleidingSelectiePage extends AbstractOpleidingSelectiePage
{
	private static final long serialVersionUID = 1L;

	public RapportageOpleidingSelectiePage(SecurePage returnPage, OpleidingZoekFilter filter,
			DatabaseSelection<Opleiding, Opleiding> selection,
			SelectieTarget<Opleiding, Opleiding> target)
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
