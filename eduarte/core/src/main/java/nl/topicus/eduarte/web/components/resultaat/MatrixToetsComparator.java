package nl.topicus.eduarte.web.components.resultaat;

import java.io.Serializable;
import java.util.Comparator;
import java.util.List;

import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.eduarte.dao.helpers.ResultaatstructuurDataAccessHelper;
import nl.topicus.eduarte.entities.resultaatstructuur.Resultaatstructuur;
import nl.topicus.eduarte.entities.resultaatstructuur.Toets;
import nl.topicus.eduarte.entities.resultaatstructuur.Toets.ToetsCodePathMode;
import nl.topicus.eduarte.zoekfilters.ResultaatstructuurZoekFilter;

public class MatrixToetsComparator implements Comparator<Toets>, Serializable
{
	private static final long serialVersionUID = 1L;

	private ToetsCodePathMode toetsCodePathMode;

	private List<String> sortedToetscodepaths;

	public MatrixToetsComparator(ResultaatstructuurZoekFilter filter,
			ToetsCodePathMode toetsCodePathMode)
	{
		List<Resultaatstructuur> structuren =
			DataAccessRegistry.getHelper(ResultaatstructuurDataAccessHelper.class).list(filter);
		sortedToetscodepaths =
			ToetsDirectedWeightedGraph.sortToetsen(structuren, toetsCodePathMode);
		this.toetsCodePathMode = toetsCodePathMode;
	}

	@Override
	public int compare(Toets o1, Toets o2)
	{
		return sortedToetscodepaths.indexOf(o1.getToetscodePath(toetsCodePathMode))
			- sortedToetscodepaths.indexOf(o2.getToetscodePath(toetsCodePathMode));
	}
}
