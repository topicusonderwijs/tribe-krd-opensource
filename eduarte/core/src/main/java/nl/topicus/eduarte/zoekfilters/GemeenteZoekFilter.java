package nl.topicus.eduarte.zoekfilters;

import nl.topicus.eduarte.entities.landelijk.Gemeente;

/**
 * @author loite
 */
public class GemeenteZoekFilter extends LandelijkCodeNaamZoekFilter<Gemeente>
{
	private static final long serialVersionUID = 1L;

	public GemeenteZoekFilter()
	{
		super(Gemeente.class);
	}
}
