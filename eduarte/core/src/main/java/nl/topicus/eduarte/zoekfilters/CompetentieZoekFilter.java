package nl.topicus.eduarte.zoekfilters;

import nl.topicus.eduarte.entities.taxonomie.mbo.cgo.Competentie;

public class CompetentieZoekFilter extends AbstractZoekFilter<Competentie>
{
	private static final long serialVersionUID = 1L;

	public CompetentieZoekFilter()
	{
		addOrderByProperty("code");
	}
}
