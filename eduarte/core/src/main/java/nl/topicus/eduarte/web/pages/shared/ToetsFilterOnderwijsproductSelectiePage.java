package nl.topicus.eduarte.web.pages.shared;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.dao.helpers.DatabaseSelection;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.web.pages.IEditPage;
import nl.topicus.eduarte.core.principals.Always;
import nl.topicus.eduarte.entities.onderwijsproduct.Onderwijsproduct;
import nl.topicus.eduarte.web.pages.SecurePage;
import nl.topicus.eduarte.zoekfilters.OnderwijsproductZoekFilter;

@PageInfo(title = "Toetsfilters", menu = "Meerdere paden")
@InPrincipal(Always.class)
public class ToetsFilterOnderwijsproductSelectiePage extends AbstractOnderwijsproductSelectiePage
		implements IEditPage
{
	private static final long serialVersionUID = 1L;

	public ToetsFilterOnderwijsproductSelectiePage(SecurePage returnPage,
			OnderwijsproductZoekFilter filter,
			DatabaseSelection<Onderwijsproduct, Onderwijsproduct> selection,
			SelectieTarget<Onderwijsproduct, Onderwijsproduct> target)
	{
		super(returnPage, filter, selection, target);
	}

	@Override
	public int getMaxResults()
	{
		return Integer.MAX_VALUE;
	}
}
