package nl.topicus.eduarte.zoekfilters;

import nl.topicus.eduarte.entities.inschrijving.SoortVooropleidingBuitenlands;
import nl.topicus.eduarte.entities.landelijk.Land;

public class SoortVooropleidingBuitenlandsZoekFilter extends
		CodeNaamActiefZoekFilter<SoortVooropleidingBuitenlands>
{
	private static final long serialVersionUID = 1L;

	private Land land;

	public SoortVooropleidingBuitenlandsZoekFilter()
	{
		super(SoortVooropleidingBuitenlands.class);
	}

	public Land getLand()
	{
		return land;
	}

	public void setLand(Land land)
	{
		this.land = land;
	}
}
