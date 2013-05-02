package nl.topicus.eduarte.zoekfilters.dbs;

import nl.topicus.eduarte.entities.dbs.trajecten.GesprekSamenvattingZin;
import nl.topicus.eduarte.zoekfilters.AbstractZoekFilter;

public class GesprekSamenvattingZinZoekFilter extends AbstractZoekFilter<GesprekSamenvattingZin>
{
	private static final long serialVersionUID = 1L;

	public GesprekSamenvattingZinZoekFilter()
	{
		addOrderByProperty("zin");
	}
}
